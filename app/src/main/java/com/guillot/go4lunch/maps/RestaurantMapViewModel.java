package com.guillot.go4lunch.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantMapViewModel extends ViewModel {

    private  RestaurantMapRepository mRestaurantRepository;
    private Disposable disposable;
    private List<Restaurant> mRestaurants;
    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();

    public void init() {
        mRestaurantRepository = RestaurantMapRepository.getInstance();
    }

    public LiveData<List<Restaurant>> getRestaurantsList(){
        return restaurantsList;
    }

    public void executeNetworkRequest(LatLng location) {
        this.disposable = mRestaurantRepository.streamFetchRestaurantsDetailsLst(Utils.convertLocationForApi(location)).subscribeWith(new DisposableObserver<List<ApiDetailsRestaurantResponse>>() {
            @Override
            public void onNext(@NonNull List<ApiDetailsRestaurantResponse> apiDetailsRestaurantResponses) {
                createRestaurantList(apiDetailsRestaurantResponses);

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

    private void createRestaurantList(List<ApiDetailsRestaurantResponse> results) {
        mRestaurants = new ArrayList<>();
        for (ApiDetailsRestaurantResponse detailsResult : results){
            if(detailsResult.getResult() != null) {
                Restaurant restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
                mRestaurants.add(restaurant);
            }
        }
        restaurantsList.setValue(mRestaurants);
    }

}
