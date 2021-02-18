package com.guillot.go4lunch.list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.User;
import com.guillot.go4lunch.model.distance.ApiDistanceResponse;
import com.guillot.go4lunch.model.distance.Elements;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.distance.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantListViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private UserRepository userRepository;
    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();
    private MutableLiveData<List<String>> usersIds = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestaurantsList() {
        return restaurantsList;
    }
    public LiveData<List<String>> getUsersIds(){ return usersIds; }

    List<Restaurant> restaurants = new ArrayList<>();
    List<User> usersList = new ArrayList<>();

    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }
    public void executeNetworkRequest(LatLng location) {
        Disposable disposableRestaurant = mRestaurantRepository.streamFetchRestaurantsDetailsLst(Utils.convertLocationForApi(location)).subscribeWith(new DisposableObserver<List<ApiDetailsRestaurantResponse>>() {
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

    private DisposableObserver<ApiDistanceResponse> executeDistanceRequest(Restaurant restaurant) {

        return new DisposableObserver<ApiDistanceResponse>() {
            @Override
            public void onNext(ApiDistanceResponse distanceApi) {
                List<Row> row = distanceApi.getRows();
                if (row.size() > 0) {
                    List<Elements> elements = row.get(0).getElements();
                    if (elements.size() > 0) {
                        Integer distance = elements.get(0).getDistance().getValue();
                        restaurant.setDistance(distance);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void createRestaurantList(List<ApiDetailsRestaurantResponse> results) {
        for (ApiDetailsRestaurantResponse detailsResult : results) {
            if (detailsResult.getResult() != null) {
                Restaurant restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
                restaurants.add(restaurant);
                getAllUsersRestaurantsIds();
                getUsersEatingHere(restaurant.getRestaurantID());
            }
        }
        restaurantsList.setValue(restaurants);
    }

    public void getAllUsersRestaurantsIds() {
        List<String> usersList = new ArrayList<>();
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!Objects.requireNonNull(userFetched).getId().equals(userRepository.getCurrentUserId())) {
                            usersList.add(userFetched.getRestaurantId());
                        }
                    }
                    usersIds.setValue(usersList);
                });
    }

    public void getUsersEatingHere(String restaurantId) {
        UserHelper.getUserByRestaurantId(restaurantId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!Objects.requireNonNull(userFetched).getId().equals(userRepository.getCurrentUserId())) {
                            usersList.add(userFetched);
                        }
                    }
                    checkUserRestaurant();
                });
    }

    private void checkUserRestaurant() {
        for (Restaurant restaurant : restaurants) {
            List<User> userToAdd = new ArrayList<>();
            String restaurantId = restaurant.getRestaurantID();
            for (User user : usersList) {
                String chosenRestaurant = user.getRestaurantId();
                if (chosenRestaurant != null) {
                    if (restaurantId.equals(chosenRestaurant)) {
                        userToAdd.add(user);
                    }
                }
            }
            restaurant.setUserGoingEating(userToAdd);
        }
    }
}
