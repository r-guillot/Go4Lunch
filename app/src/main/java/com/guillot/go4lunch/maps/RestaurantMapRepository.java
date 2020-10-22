package com.guillot.go4lunch.maps;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.api.ApiDetails;
import com.guillot.go4lunch.api.ApiInterface;
import com.guillot.go4lunch.api.RetrofitService;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.ApiRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.RestaurantApi;
import com.guillot.go4lunch.model.details.OpeningHours;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantMapRepository {

    private final String TAG = RestaurantMapRepository.class.getSimpleName();

    private ApiInterface apiInterface;
    private ApiDetails apiDetails;

    private static RestaurantMapRepository newsRepository;

    public static RestaurantMapRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new RestaurantMapRepository();
        }
        return newsRepository;
    }

    public RestaurantMapRepository() {
        apiInterface = RetrofitService.getInterface();
        apiDetails = RetrofitService.getDetails();
    }

//    public MutableLiveData<List<Restaurant>> getRestaurantList(String location) {
//        MutableLiveData<List<Restaurant>> restaurantList = new MutableLiveData<>();
//        apiInterface
//                .getRestaurants(location)
//                .enqueue(new Callback<ApiResponse>() {
//                    @Override
//                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                        Log.i(TAG, "onResponse");
//
//                        List<Restaurant> restaurants = new ArrayList<>();
//                        if (response.isSuccessful()) {
//                            for (RestaurantApi restaurantApi :
//                                    response.body().getResults()) {
//                                restaurants.add(createRestaurant(restaurantApi));
//                            }
//                            restaurantList.setValue(restaurants);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ApiResponse> call, Throwable t) {
//                        Log.i(TAG, "onFailure: " + t.getLocalizedMessage());
//                        restaurantList.setValue(null);
//                    }
//                });
//        return restaurantList;
//    }

    public Observable<ApiRestaurantResponse> streamFetchRestaurantsCloseToMe(String location){
        return apiInterface.getRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS);
    }

    public Observable<ApiDetailsRestaurantResponse> streamFetchRestaurantDetails(String placeId) {
        return apiDetails.getRestaurantDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS);
    }

    public Observable<List<ApiDetailsRestaurantResponse>> streamFetchRestaurantsDetailsLst(String location) {
        return streamFetchRestaurantsCloseToMe(location)
                .map(new Function<ApiRestaurantResponse, List<RestaurantApi>>() {
                    @Override
                    public List<RestaurantApi> apply(ApiRestaurantResponse apiRestaurantResponse) throws Throwable {
                        return apiRestaurantResponse.getResults();
                    }
                })
                .concatMap(new Function<List<RestaurantApi>, Observable<List<ApiDetailsRestaurantResponse>>>() {
                    @Override
                    public Observable<List<ApiDetailsRestaurantResponse>> apply(List<RestaurantApi> results) throws Throwable {
                        if (results != null && !results.isEmpty()) {
                            return Observable.fromIterable(results)
                                    .concatMap((Function<RestaurantApi, Observable<ApiDetailsRestaurantResponse>>) (RestaurantApi result) ->{
                                        return RestaurantMapRepository.this.streamFetchRestaurantDetails(result.getPlaceId());
                                    })
                                    .toList()
                                    .toObservable();
                        } else {
                            return null;
                        }
                    }
                });
    }


    public String getPhotoRestaurant(String photoReference) {
        return String.format("%splace/photo?maxwidth=400&photoreference=%s&key=%s",
                BuildConfig.ApiPlaceBase, photoReference, BuildConfig.ApiPlaceKey);
    }

    public Restaurant createRestaurant(RestaurantApi result) {
        String uid = result.getPlaceId();
        String name = result.getName();
        Double latitude = result.getGeometry().getLocation().getLat();
        Double longitude = result.getGeometry().getLocation().getLng();
        String photo = (result.getPhotos() != null) ? getPhotoRestaurant(result.getPhotos().get(0).getPhotoReference()) : null;
        String address = result.getVicinity();
        String webSite = result.getWebsite();
        String phoneNumber = result.getPhoneNumber();
        float rating = result.getRating();
        return new Restaurant(uid, name, latitude, longitude, address, photo, rating, phoneNumber, webSite);
    }
}
