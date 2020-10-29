package com.guillot.go4lunch.restaurantDetails;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.api.ApiDetails;
import com.guillot.go4lunch.api.RetrofitService;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.RestaurantApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

class RestaurantDetailRepository {
    private ApiDetails apiDetails;

    private static RestaurantDetailRepository newsRepository;

    public static RestaurantDetailRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new RestaurantDetailRepository();
        }
        return newsRepository;
    }

    public RestaurantDetailRepository() {
        apiDetails = RetrofitService.getDetails();
    }

    public Observable<ApiDetailsRestaurantResponse> streamFetchRestaurantDetails(String placeId) {
        return apiDetails.getRestaurantDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS);
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
        String photo = result.getPhotos().get(0).getPhotoReference();
        String address = result.getVicinity();
        String webSite = result.getWebsite();
        String phoneNumber = result.getPhoneNumber();
        float rating = result.getRating();
        return new Restaurant(uid, name, latitude, longitude, address, photo, rating, phoneNumber, webSite);
    }


}
