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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RestaurantMapViewModel extends ViewModel {
    private final String TAG = RestaurantMapViewModel.class.getSimpleName();

    private RestaurantRepository mRestaurantRepository;
    private UserRepository mUserRepository;
    private Disposable disposable;
    private List<Restaurant> mRestaurants;
    private List<String> restaurantIdList;
    private String userId;

    private MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<>();
    private MutableLiveData<List<String>> userList = new MutableLiveData<>();

    public LiveData<List<Restaurant>> getRestaurantsList(){
        return restaurantsList;
    }
    public LiveData<List<String>> getUserIdList(){
        return userList;
    }

    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        mUserRepository = UserRepository.getInstance();
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

    public void getAllOccupiedRestaurant() {
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    restaurantIdList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        Log.d(TAG, "userFetched: " +userFetched);
                        assert userFetched != null;
                        if (userFetched.getRestaurantId() != null) {
                            Log.d(TAG, "restaurantId " + userFetched.getRestaurantId());
                            restaurantIdList.add(userFetched.getRestaurantId());
                            Log.d(TAG, "getAllOccupiedRestaurantList1: " + restaurantIdList);
                        }
                    }
                    userList.setValue(restaurantIdList);
                });
    }

    public List<String> getIdList(){
        List<String> idList = new ArrayList<>();
        idList = getUserIdList().getValue();

        return idList;
    }



    public void getUserId() {
        userId = mUserRepository.getCurrentUserId();
    }

    public void updateUserLocation(String locationUser){
        getUserId();
        UserHelper.updateUserLocation(userId, locationUser);

    }
}
