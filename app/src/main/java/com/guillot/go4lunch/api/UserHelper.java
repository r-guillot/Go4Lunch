package com.guillot.go4lunch.api;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.guillot.go4lunch.model.User;

import java.util.List;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";


    // COLLECTION REFERENCE

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE

    public static Task<Void> createUser(String id, String username, String urlProfilePicture, String userLocation, String userMail, List<String> restaurantLiked,
                                        String restaurantId, String restaurantName, String restaurantAddress, boolean notification) {
        User userToCreate = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, restaurantId, restaurantName, restaurantAddress, notification);
        return UserHelper.getUsersCollection().document(id).set(userToCreate);
    }

    // GET

    public static Task<DocumentSnapshot> getUser(String id){
        return UserHelper.getUsersCollection().document(id).get();
    }

    public static Task<QuerySnapshot> getUserByRestaurantId(String restaurantId){
        return UserHelper.getUsersCollection().whereEqualTo("restaurantId",restaurantId).get();
    }

    public static Task<QuerySnapshot> getAllUser(){
        return UserHelper.getUsersCollection().orderBy("username").get();
    }

    // UPDATE

    public static Task<Void> updateRestaurantInfo(String id, String restaurantId, String restaurantName, String restaurantAddress) {
        return UserHelper.getUsersCollection().document(id).update("restaurantId", restaurantId,"restaurantName", restaurantName, "restaurantAddress", restaurantAddress);
    }

    public static Task<Void> updateRestaurantLiked(String id, List<String> restaurantLiked) {
        return UserHelper.getUsersCollection().document(id).update("restaurantLiked", restaurantLiked);
    }

    public static Task<Void> updateNotification(String id, boolean notification) {
        return UserHelper.getUsersCollection().document(id).update("notification", notification);
    }

    public static Task<Void> updateUserInfo(String id, String username, String userMail) {
        return UserHelper.getUsersCollection().document(id).update("username", username, "userMail", userMail);
    }

    public static Task<Void> updateUserProfilePicture(String id, String urlProfilePicture) {
        return UserHelper.getUsersCollection().document(id).update("urlProfilePicture", urlProfilePicture);
    }

    public static Task<Void> updateUserLocation(String id, String userLocation) {
        return UserHelper.getUsersCollection().document(id).update("userLocation", userLocation);
    }

    // DELETE

    public static Task<Void> deleteUser(String id) {
        return UserHelper.getUsersCollection().document(id).delete();
    }
}
