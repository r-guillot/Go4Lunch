package com.guillot.go4lunch.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.base.BaseFragment;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.ApiDistanceResponse;
import com.guillot.go4lunch.model.Distance;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantListViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private UserRepository userRepository;
    private Disposable disposable;
    private List<Restaurant> mRestaurants;
    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();
    private MutableLiveData<String> distance = new MutableLiveData<>();

    public LiveData<String> getDistanceLiveData() {return distance;}


    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public LiveData<List<Restaurant>> getRestaurantsList() {
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
        for (ApiDetailsRestaurantResponse detailsResult : results) {
            if (detailsResult.getResult() != null) {
                Restaurant restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
                mRestaurants.add(restaurant);
            }
        }
        restaurantsList.setValue(mRestaurants);
    }

    public void executeDistanceRequest(String restaurantLocation) {
        this.disposable = mRestaurantRepository.streamFetchDistanceFromRestaurant(restaurantLocation).subscribeWith(new DisposableObserver<ApiDistanceResponse>() {
            @Override
            public void onNext(@NonNull ApiDistanceResponse apiDistanceResponse) {
                createDistanceValue(apiDistanceResponse);
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

    public void createDistanceValue(ApiDistanceResponse results){
        if (results != null){
            Distance distanceToCreate = mRestaurantRepository.createDistance(results);
            String test = distanceToCreate.toString();
            distance.setValue(test);
        }
    }
}
