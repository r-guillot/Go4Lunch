package com.guillot.go4lunch.restaurantDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;


public class RestaurantDetailsViewModel extends ViewModel {

    private RestaurantDetailRepository mRestaurantRepository;
    private Disposable disposable;
    private Restaurant restaurant;
    private MutableLiveData<Restaurant> restaurantDetail = new MutableLiveData<>();

    public void init() {
        mRestaurantRepository = RestaurantDetailRepository.getInstance();
    }

    public LiveData<Restaurant> getRestaurants(){
        return restaurantDetail;
    }

    public void executeNetworkRequest(String placeId) {
        this.disposable = mRestaurantRepository.streamFetchRestaurantDetails(placeId).subscribeWith(new DisposableObserver<ApiDetailsRestaurantResponse>() {
            @Override
            public void onNext(@NonNull ApiDetailsRestaurantResponse apiDetailsRestaurantResponse) {
                createRestaurant(apiDetailsRestaurantResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("error executeNetworkRequest" + e);

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public String getPhoto(String photoReference) {
        return mRestaurantRepository.getPhotoRestaurant(photoReference);
    }

    private void createRestaurant(ApiDetailsRestaurantResponse results) {
    ApiDetailsRestaurantResponse detailsResult = results;
        if(detailsResult.getResult() != null) {
            restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
        }
        restaurantDetail.setValue(restaurant);
    }


}
