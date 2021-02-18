package com.guillot.go4lunch.maps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantMapViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private List<String> restaurantIdList;
    private String userId;

    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();
    public MutableLiveData<List<String>> userList = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestaurantsList(){
        return restaurantsList;
    }
    public LiveData<List<String>> getUserIdList(){
        return userList;
    }

    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        UserRepository userRepository = UserRepository.getInstance();
        userId = userRepository.getCurrentUserId();
        getAllOccupiedRestaurant();
    }

    public void executeNetworkRequest(LatLng location) {
        Disposable disposable = mRestaurantRepository.streamFetchRestaurantsDetailsLst(Utils.convertLocationForApi(location)).subscribeWith(new DisposableObserver<List<ApiDetailsRestaurantResponse>>() {
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
        List<Restaurant> restaurants = new ArrayList<>();
        for (ApiDetailsRestaurantResponse detailsResult : results){
            if(detailsResult.getResult() != null) {
                Restaurant restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
                restaurants.add(restaurant);
            }
        }
        restaurantsList.setValue(restaurants);
    }

    public void getAllOccupiedRestaurant() {
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantIdList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (Objects.requireNonNull(userFetched).getRestaurantId() != null) {
                            if (!userFetched.getId().equals(userId)) {
                                restaurantIdList.add(userFetched.getRestaurantId());
                            }
                        }
                    }
                    userList.setValue(restaurantIdList);
                });
    }

    public void updateUserLocation(String locationUser){
        UserHelper.updateUserLocation(userId, locationUser);

    }
}
