package com.guillot.go4lunch.details;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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

    private MutableLiveData<Restaurant> restaurantDetail = new MutableLiveData<>();
    private MutableLiveData<List<User>> usersEatingHere = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MediatorLiveData<Pair<Restaurant, User>> mediatorLiveData = new MediatorLiveData<>();

    public LiveData<Restaurant> getRestaurantDetails(){ return restaurantDetail; }
    public LiveData<List<User>> getListUsersEatingHere() {return usersEatingHere;}
    public LiveData<User> getCurrentUserLiveData() {return currentUser;}
    public LiveData<Pair<Restaurant, User>> getMediatorLiveData() {return mediatorLiveData;}

    public void init() {
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
        userId = userRepository.getCurrentUserId();
        Log.d(TAG, "init: ");
    }


    public void executeNetworkRequest(String placeId) {
        this.disposable = mRestaurantRepository.streamFetchRestaurantDetails(placeId).subscribeWith(new DisposableObserver<ApiDetailsRestaurantResponse>() {
            @Override
            public void onNext(@NonNull ApiDetailsRestaurantResponse apiDetailsRestaurantResponse) {
                createRestaurant(apiDetailsRestaurantResponse);
                getCurrentUser();
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
        mediatorLiveData.addSource(getRestaurantDetails(), new Observer<Restaurant>() {
            private int count = 1;
            @Override
            public void onChanged(Restaurant restaurant) {
                    count++;
                    mediatorLiveData.setValue(Pair.create(restaurant, getCurrentUserLiveData().getValue()));
                    if (count > 10) {
                        mediatorLiveData.removeSource(getRestaurantDetails());
                    }
            }
        });
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

    public void getCurrentUser() {
        Log.d(TAG, "getCurrentUser: start");
        UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
            fetchedUser = documentSnapshot.toObject(User.class);
            currentUser.setValue(fetchedUser);
            Log.d(TAG, "getCurrentUser: " + currentUser);
            Log.d(TAG, "fetchedUser: " +fetchedUser);
        });
        mediatorLiveData.addSource(getCurrentUserLiveData(), new Observer<User>() {
            private int count = 1;
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "mediatorLiveDataUser: start 2 ");
                count++;
                mediatorLiveData.setValue(Pair.create(getRestaurantDetails().getValue(), user));
                Log.d(TAG, "mediatorLiveData: " + mediatorLiveData);
                if (count > 10) {
                    mediatorLiveData.removeSource(getCurrentUserLiveData());
                }
            }
        });
    }

    public void updateSelectedRestaurant(String restaurantId, String restaurantName) {
        UserHelper.updateRestaurantId(userId, restaurantId, restaurantName);
    }


}
