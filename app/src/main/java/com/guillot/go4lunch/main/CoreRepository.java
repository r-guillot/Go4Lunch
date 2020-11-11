package com.guillot.go4lunch.main;


import android.net.Uri;

import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.model.User;

class CoreRepository {

    private static CoreRepository newsRepository;
    private User user;
    private UserHelper mUserHelper;

    public static CoreRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new CoreRepository();
        }
        return newsRepository;
    }

    public User getUser() {
        return user;}

    public String getChosenRestaurant() {
        return user.getRestaurantId();
    }

    public String getUserName() {
        return user.getUsername();
    }

    public String getUserMail() {
        return user.getUserMail();
    }

    public Uri GetProfilePic() {
        return user.getUrlProfilePicture();
    }
}
