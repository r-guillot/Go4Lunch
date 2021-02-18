package com.guillot.go4lunch.details;

import androidx.lifecycle.LiveData;
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
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;


public class RestaurantDetailsViewModel extends ViewModel {

    private RestaurantRepository mRestaurantRepository;
    private UserRepository userRepository;
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
        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public void getUserId() {
        userId = userRepository.getCurrentUserId();
    }

    public void getCurrentUser(String placeId) {
        if (userRepository.getCurrentUserId() == null) throw new AssertionError();
        UserHelper.getUser(userRepository.getCurrentUserId()).addOnSuccessListener(documentSnapshot -> {
                fetchedUser = documentSnapshot.toObject(User.class);
                currentUser.setValue(fetchedUser);
        });
        executeNetworkRequest(placeId);
    }

    public void executeNetworkRequest(String placeId) {
        Disposable disposable = mRestaurantRepository.streamFetchRestaurantDetails(placeId).subscribeWith(new DisposableObserver<ApiDetailsRestaurantResponse>() {
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
        if(results.getResult() != null) {
            restaurant = mRestaurantRepository.createRestaurant(results.getResult());
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
                        assert userFetched != null;
                        if (!userFetched.getId().equals(userId)) {
                            usersList.add(userFetched);
                        }
                    }
                    usersEatingHere.setValue(usersList);
                });
    }

    public boolean checkIfRestaurantIsChosen() {
        state = Objects.requireNonNull(getCurrentUserLiveData().getValue()).getRestaurantId() != null && getCurrentUserLiveData().getValue().getRestaurantId().equals(Objects.requireNonNull(getRestaurantDetails().getValue()).getRestaurantID());
        return state;
    }

    public boolean checkIfRestaurantIsLiked() {
        state = Objects.requireNonNull(getCurrentUserLiveData().getValue()).getRestaurantLiked() != null && getCurrentUserLiveData().getValue().getRestaurantLiked().contains(Objects.requireNonNull(getRestaurantDetails().getValue()).getRestaurantID());
        return state;
    }

    public void setRestaurantSelected() {
        UserHelper.updateRestaurantInfo(Objects.requireNonNull(getCurrentUserLiveData().getValue()).getId(), Objects.requireNonNull(getRestaurantDetails().getValue()).getRestaurantID(), getRestaurantDetails().getValue().getName(), getRestaurantDetails().getValue().getAddress());
}

    public void setRestaurantUnselected() {
        UserHelper.updateRestaurantInfo(Objects.requireNonNull(getCurrentUserLiveData().getValue()).getId(), "", "", "");
    }

    public void setRestaurantLiked(List<String> restaurantLiked){
        restaurantLiked.add(Objects.requireNonNull(getRestaurantDetails().getValue()).getRestaurantID());
        UserHelper.updateRestaurantLiked(Objects.requireNonNull(getCurrentUserLiveData().getValue()).getId(), restaurantLiked);
    }

    public void setRestaurantUnliked(List<String> restaurantLiked){
        restaurantLiked.remove(Objects.requireNonNull(getRestaurantDetails().getValue()).getRestaurantID());
        UserHelper.updateRestaurantLiked(Objects.requireNonNull(getCurrentUserLiveData().getValue()).getId(), restaurantLiked);
    }


}
