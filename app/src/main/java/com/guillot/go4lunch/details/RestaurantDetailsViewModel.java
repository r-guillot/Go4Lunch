package com.guillot.go4lunch.details;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;


public class RestaurantDetailsViewModel extends ViewModel {
    private final String TAG = RestaurantDetailsViewModel.class.getSimpleName();

    private RestaurantRepository mRestaurantRepository;
    private UserRepository userRepository;
    private Disposable disposable;
    private Restaurant restaurant;
    private String userId;
    private User fetchedUser;
    private boolean state;

    private MutableLiveData<Restaurant> restaurantDetail = new MutableLiveData<>();
    private MutableLiveData<List<User>> usersEatingHere = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<Restaurant> getRestaurantDetails(){ return restaurantDetail; }
    public LiveData<List<User>> getListUsersEatingHere() {return usersEatingHere;}
    public LiveData<User> getCurrentUserLiveData() {return currentUser;}

    public void init() {
        Log.d(TAG, "init: 1");
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
        Log.d(TAG, "init: 2");
    }

    public void getUserId() {
        userId = userRepository.getCurrentUserId();
        Log.d(TAG, "userId: " + userId);
    }

//    public void getCurrentUser() {
//        Log.d(TAG, "getCurrentUser: start");
//        if (userId != null) {
//            Log.d(TAG, "getCurrentUserId: " + userId);
//            UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
//                fetchedUser = documentSnapshot.toObject(User.class);
//                Log.d(TAG, "fetchedUser: " + fetchedUser);
//                currentUser.setValue(fetchedUser);
//                Log.d(TAG, "getCurrentUser: " + currentUser);
//            })
//                    .addOnFailureListener(e -> {
//                        Log.d(TAG, "getCurrentUser: " + e);
//                    });
//        }
//    }

    public void getCurrentUser(String placeId) {
        Log.d(TAG, "getCurrentUser 1: " + userRepository.getCurrentUserId());
        assert userRepository.getCurrentUserId() != null;
        UserHelper.getUser(userRepository.getCurrentUserId()).addOnSuccessListener(documentSnapshot -> {
            fetchedUser = documentSnapshot.toObject(User.class);
            currentUser.setValue(fetchedUser);
            Log.d(TAG, "getCurrentUser 2: " + fetchedUser + currentUser);
        });
        executeNetworkRequest(placeId);
    }

    public void executeNetworkRequest(String placeId) {
        this.disposable = mRestaurantRepository.streamFetchRestaurantDetails(placeId).subscribeWith(new DisposableObserver<ApiDetailsRestaurantResponse>() {
            @Override
            public void onNext(@NonNull ApiDetailsRestaurantResponse apiDetailsRestaurantResponse) {
                createRestaurant(apiDetailsRestaurantResponse, placeId);
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

    private void createRestaurant(ApiDetailsRestaurantResponse results, String placeId) {
    ApiDetailsRestaurantResponse detailsResult = results;
        if(detailsResult.getResult() != null) {
            restaurant = mRestaurantRepository.createRestaurant(detailsResult.getResult());
        }
        restaurantDetail.setValue(restaurant);
        getUsersEatingHere(placeId);
    }

    public void getUsersEatingHere(String restaurantId) {
        UserHelper.getUserByRestaurantId(restaurantId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> usersList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!userFetched.getId().equals(userId)) {
                            usersList.add(userFetched);
                        }
                    }
                    usersEatingHere.setValue(usersList);
                });
    }

    public boolean checkIfRestaurantIsChosen() {
        if (getCurrentUserLiveData().getValue().getRestaurantId() != null && getCurrentUserLiveData().getValue().getRestaurantId().equals(getRestaurantDetails().getValue().getRestaurantID())) {
            Log.d(TAG, "checkIfRestaurantIsChosen: userRestoId " + getCurrentUserLiveData().getValue().getRestaurantId() + "restoId" + getRestaurantDetails().getValue().getRestaurantID());
            state = true;
        } else{ state = false;}
        Log.d(TAG, "checkIfRestaurantIsChosen: " + state);
        return state;
    }

    public void setRestaurantSelected() {
        UserHelper.updateRestaurantInfo(getCurrentUserLiveData().getValue().getId(), getRestaurantDetails().getValue().getRestaurantID(), getRestaurantDetails().getValue().getName(), getRestaurantDetails().getValue().getAddress());
    }

    public void setRestaurantUnselected() {
        UserHelper.updateRestaurantInfo(getCurrentUserLiveData().getValue().getId(), "", "", "");
    }


}
