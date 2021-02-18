package com.guillot.go4lunch.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserUnitTest{

    private User user;

    @Before
    public void setup() {
        user = new User("2021", "username", "https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "48.8534,2.3488", "userMail@test.com", Collections.singletonList("ChIJz5Ors95x5kcRWuk_sUIMlpU"),
                "ChIJz5Ors95x5kcRWuk_sUIMlpU", "Le Relais Saint Germain", "9 Carrefour de l'Odéon, Paris", true);
    }

    @Test
    public void getIdFromUser(){
        String expectedResult = "2021";
        String result = user.getId();
        assertEquals(expectedResult, result);
    }

    @Test
    public void getUsernameFromUser(){
        String expectedResult = "username";
        String result = user.getUsername();
        assertEquals(expectedResult, result);
    }

    @Test
    public void getMailFromUser(){
        String expectedResult = "userMail@test.com";
        String result = user.getUserMail();
        assertEquals(expectedResult, result);
    }

    @Test
    public void getPhotoFromUser(){
        String expectedResult = "https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500";
        String result = user.getUrlProfilePicture();
        assertEquals(expectedResult, result);
    }

    @Test
    public void getAndSetLocationFromUser(){
        String expectedResult = "48.8534,2.3488";
        String result = user.getUserLocation();
        assertEquals(expectedResult, result);

        String newExpectedResult = "35.6894,139.6917";
        user.setUserLocation(newExpectedResult);
        assertEquals(newExpectedResult, user.getUserLocation());
    }

    @Test
    public void getAndSetRestaurantInfoFromUser(){
        String expectedResult = "ChIJz5Ors95x5kcRWuk_sUIMlpU"+ "Le Relais Saint Germain"+ "9 Carrefour de l'Odéon, Paris";
        String result = user.getRestaurantId()+ user.getRestaurantName()+ user.getRestaurantAddress();
        assertEquals(expectedResult, result);

        String newRestaurantId = "ChIJByxXLuFx5kcRX36mXlrU714";
        String newRestaurantName = "La Fourmi Ailée";
        String newRestaurantAddress = "8 Rue du Fouarre, Paris";
        user.setRestaurantId(newRestaurantId);
        user.setRestaurantName(newRestaurantName);
        user.setRestaurantAddress(newRestaurantAddress);
        String newExpectedResult = "ChIJByxXLuFx5kcRX36mXlrU714"+ "La Fourmi Ailée"+ "8 Rue du Fouarre, Paris";
        result = user.getRestaurantId()+ user.getRestaurantName()+ user.getRestaurantAddress();

        assertNotEquals(expectedResult, result);
        assertEquals(newExpectedResult, result);
    }

    @Test
    public void addLikedRestaurantToUser(){
        String firstRestaurantLikedList = "ChIJz5Ors95x5kcRWuk_sUIMlpU";
        List<String> restaurantLikedList = new ArrayList<>(user.getRestaurantLiked());
        String restaurantLiked = "ChIJdQn7zN5x5kcRhKnVo8EDJS0";
        restaurantLikedList.add(restaurantLiked);
        user.setRestaurantLiked(restaurantLikedList);
        assertTrue(user.getRestaurantLiked().contains(firstRestaurantLikedList));
        assertTrue(user.getRestaurantLiked().contains(restaurantLiked));
    }

    @Test
    public void checkIfNotificationIsEnabled(){
        assertTrue(user.isNotification());
    }
}
