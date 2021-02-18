package com.guillot.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.guillot.go4lunch.model.User;

import java.util.List;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";


    // COLLECTION REFERENCE

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE

    public static void createUser(String id, String username, String urlProfilePicture, String userLocation, String userMail, List<String> restaurantLiked,
                                  String restaurantId, String restaurantName, String restaurantAddress, boolean notification) {
        User userToCreate = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, restaurantId, restaurantName, restaurantAddress, notification);
        UserHelper.getUsersCollection().document(id).set(userToCreate);
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

    public static void updateRestaurantInfo(String id, String restaurantId, String restaurantName, String restaurantAddress) {
        UserHelper.getUsersCollection().document(id).update("restaurantId", restaurantId, "restaurantName", restaurantName, "restaurantAddress", restaurantAddress);
    }

    public static void updateRestaurantLiked(String id, List<String> restaurantLiked) {
        UserHelper.getUsersCollection().document(id).update("restaurantLiked", restaurantLiked);
    }

    public static void updateNotification(String id, boolean notification) {
        UserHelper.getUsersCollection().document(id).update("notification", notification);
    }

    public static void updateUserInfo(String id, String username, String userMail) {
        UserHelper.getUsersCollection().document(id).update("username", username, "userMail", userMail);
    }

    public static void updateUserProfilePicture(String id, String urlProfilePicture) {
        UserHelper.getUsersCollection().document(id).update("urlProfilePicture", urlProfilePicture);
    }

    public static void updateUserLocation(String id, String userLocation) {
        UserHelper.getUsersCollection().document(id).update("userLocation", userLocation);
    }

    // DELETE

    public static void deleteUser(String id) {
        UserHelper.getUsersCollection().document(id).delete();
    }
}
