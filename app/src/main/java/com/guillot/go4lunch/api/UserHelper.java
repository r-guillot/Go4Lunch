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

import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";


    // COLLECTION REFERENCE

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE

    public static Task<Void> createUser(String id, String username, String urlProfilePicture, String userLocation, String userMail,
                                        String restaurantId, String restaurantName) {
        User userToCreate = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantId, restaurantName);
        return UserHelper.getUsersCollection().document(id).set(userToCreate);
    }

    // GET

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getUserByRestaurantId(String restaurantId){
        return UserHelper.getUsersCollection().whereEqualTo("restaurantId",restaurantId).get();
    }

    public static Task<QuerySnapshot> getAllUser(){
        return UserHelper.getUsersCollection().orderBy("username").get();
    }

    // UPDATE

    public static Task<Void> updateRestaurantId(String uid, String restaurantId, String restaurantName) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantId", restaurantId,"restaurantName", restaurantName);
    }

    public static Task<Void> updateRestaurantName(String uid, String restaurantName) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantName", restaurantName);
    }

    // DELETE

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
