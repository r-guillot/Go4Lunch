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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantListViewModel extends ViewModel {
    private final String TAG = RestaurantListViewModel.class.getSimpleName();

    private RestaurantRepository mRestaurantRepository;
    private UserRepository userRepository;
    private Disposable disposableRestaurant;
    private Disposable disposableDistance;
    private List<Restaurant> mRestaurants;
    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();
    private MutableLiveData<Integer> distanceRU = new MutableLiveData<Integer>();
    private MutableLiveData<List<User>> usersEatingHere = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestaurantsList() {
        return restaurantsList;
    }

    public LiveData<Integer> getDistanceLiveData() {
        return distanceRU;
    }

    public LiveData<List<User>> getListUsersEatingHere() {
        return usersEatingHere;
    }


    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
        Log.d(TAG, "start");
    }

    public void destroyDisposable() {
        if (this.disposableRestaurant != null && !this.disposableRestaurant.isDisposed())
            this.disposableRestaurant.dispose();
        if (this.disposableDistance != null && !this.disposableDistance.isDisposed())
            this.disposableDistance.dispose();
    }

    public void executeNetworkRequest(LatLng location) {
        this.disposableRestaurant = mRestaurantRepository.streamFetchRestaurantsDetailsLst(Utils.convertLocationForApi(location)).subscribeWith(new DisposableObserver<List<ApiDetailsRestaurantResponse>>() {
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
        Log.d(TAG, "executeDistanceRequest: ");

        return new DisposableObserver<ApiDistanceResponse>() {
            @Override
            public void onNext(ApiDistanceResponse distanceApi) {
                Log.d(TAG, "onNext: " + distanceApi);
                List<Row> row = distanceApi.getRows();
                Log.d(TAG, "row: " + distanceApi.getRows().size());
                if (row.size() > 0) {
                    List<Elements> elements = row.get(0).getElements();
                    if (elements.size() > 0) {
                        Log.d(TAG, "elements: " + row.get(0).getElements());
                        Integer distance = elements.get(0).getDistance().getValue();
                        Log.d(TAG, "onNext:" + distance);
                        restaurant.setDistance(distance);
                        Log.d(TAG, "onNext: " + restaurant.getDistance());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void createRestaurantList(List<ApiDetailsRestaurantResponse> results) {
        mRestaurants = new ArrayList<>();
        for (ApiDetailsRestaurantResponse detailsResult : results) {
            if (detailsResult.getResult() != null) {
                Restaurant restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
                LatLng positionRestaurant = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                this.disposableDistance = mRestaurantRepository.streamFetchDistanceFromRestaurant(Utils.convertLocationForApi(positionRestaurant)).subscribeWith(executeDistanceRequest(restaurant));
                Log.d(TAG, "createRestaurantList: " + disposableDistance);
                mRestaurants.add(restaurant);
                getUsersEatingHere(restaurant.getRestaurantID());
            }
        }
        restaurantsList.setValue(mRestaurants);
    }

    public void getUsersEatingHere(String restaurantId) {
        UserHelper.getUserByRestaurantId(restaurantId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> usersList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!userFetched.getId().equals(userRepository.getCurrentUserId())) {
                            usersList.add(userFetched);
                        }
                    }
                    usersEatingHere.setValue(usersList);
                });
    }
}
