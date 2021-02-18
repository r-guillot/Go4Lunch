package com.guillot.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RestaurantUnitTest {

    private Restaurant restaurant;
    private User user;
    private User user2;

    @Before
    public void setup() {
        restaurant = new Restaurant("ChIJz5Ors95x5kcRWuk_sUIMlpU", "Le Relais Saint Germain", 48.8531, 2.3402, "9 Carrefour de l'Odéon, Paris",  2300,
                "CmRaAAAAVoXMnGvGEizMJqF50OtYKt4R0hm3GIU_Q-Ec-DcDkgkDF6jkdvtuonJctKXv2DHZQ4F7mhC7r5UNw16zSrfodPbzhjOeum5_vhFY1OMAcGuaVsAmdHblIZ_7ucFp6SLm7EhCeR9Xr8iY8S2IG-dvjwuwWGhRrAjtt5RHn0Ft2T6J7Ll5WqK4MGw",
                4, "0144270797", "https://www.hotel-paris-relais-saint-germain.com");

        user = new User("2021", "username", "https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "48.8534,2.3488", "userMail@test.com", Collections.singletonList("ChIJz5Ors95x5kcRWuk_sUIMlpU"),
                "ChIJz5Ors95x5kcRWuk_sUIMlpU", "Le Relais Saint Germain", "9 Carrefour de l'Odéon, Paris", true);

        user2 = new User("2022", "username2", "https://images.pexels.com/photos/771742/pexels-photo-771742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "48.8534,2.3488", "userMail@test.com", Collections.singletonList("ChIJdQn7zN5x5kcRhKnVo8EDJS0"),
                "ChIJdQn7zN5x5kcRhKnVo8EDJS0", "Le Procope", "13 Rue de l'Ancienne Comédie, Paris", true);

    }

    @Test
    public void getInfoFromRestaurant(){
        String expectedId = "ChIJz5Ors95x5kcRWuk_sUIMlpU";
        String expectedName = "Le Relais Saint Germain";
        String expectedAddress = "9 Carrefour de l'Odéon, Paris";
        String expectedPhoto = "CmRaAAAAVoXMnGvGEizMJqF50OtYKt4R0hm3GIU_Q-Ec-DcDkgkDF6jkdvtuonJctKXv2DHZQ4F7mhC7r5UNw16zSrfodPbzhjOeum5_vhFY1OMAcGuaVsAmdHblIZ_7ucFp6SLm7EhCeR9Xr8iY8S2IG-dvjwuwWGhRrAjtt5RHn0Ft2T6J7Ll5WqK4MGw";
        float expectedRating = 4;
        String expectedPhone = "0144270797";
        String expectedWebsite = "https://www.hotel-paris-relais-saint-germain.com";

        assertEquals(expectedId, restaurant.getRestaurantID());
        assertEquals(expectedName, restaurant.getName());
        assertEquals(expectedAddress, restaurant.getAddress());
        assertEquals(expectedPhoto, restaurant.getPhotoReference());
        assertEquals(expectedRating, restaurant.getRating(), 0.0);
        assertEquals(expectedPhone, restaurant.getPhoneNumber());
        assertEquals(expectedWebsite, restaurant.getWebSite());
    }

    @Test
    public void getLocationFromRestaurant(){
        LatLng expectedResult = new LatLng(48.8531,2.3402);
        LatLng result = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());
        assertEquals(expectedResult, result);

        Double latitude = 48.8531;
        Double longitude = 2.3402;
        assertEquals(latitude, restaurant.getLatitude());
        assertEquals(longitude,restaurant.getLongitude());
    }

    @Test
    public void checkIfRestaurantIsSelected(){
        List<String> usersId = new ArrayList<>();
        usersId.add(user.getRestaurantId());
        usersId.add(user2.getRestaurantId());

        assertTrue(usersId.contains(restaurant.getRestaurantID()));
    }


}
