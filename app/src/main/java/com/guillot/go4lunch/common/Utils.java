package com.guillot.go4lunch.common;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Joiner;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.base.BaseFragment;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.details.OpeningHours;
import com.guillot.go4lunch.model.details.OpeningPeriods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    /**
     * Convert user location to string.
     *
     */
    public static String convertLocationForApi(LatLng position) {
        if (position != null) {
            double lat = position.latitude;
            double lng = position.longitude;
            return lat + "," + lng;
        }
        return null;
    }

    public static String convertListToStringForNotification(List<String> list){
        return Joiner.on("& ").join(list);
    }

    public static int getOpeningTime(OpeningHours openingHours){
        if(openingHours == null || openingHours.getOpeningPeriods() == null) return R.string.no_time;
        if(openingHours.getOpenNow() != null && !openingHours.getOpenNow()){
            return R.string.closed;
        }
        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1;
        if(openingHours.getOpeningPeriods().size() >= dayOfTheWeek+1){
            OpeningPeriods periodOfTheDay = openingHours.getOpeningPeriods().get(dayOfTheWeek);

            if(periodOfTheDay.getCloseHour() == null) return R.string.open_24_7;

            String closureString = periodOfTheDay.getCloseHour().getTime();
            int closure = Integer.parseInt(closureString);

            Date todayDate = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.FRANCE);
            String todayDateString = dateFormat.format(todayDate);
            int timeNow = Integer.parseInt(todayDateString);
            int timeBeforeClosure = closure - timeNow;
            if(timeBeforeClosure <= 100){
                return R.string.closing_soon;
            } else {
                return closure;
            }
        }
        return R.string.no_time;
    }

    public static Date convertStringToDate(int hour){
        String hourInString = String.valueOf(hour);
        DateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.FRANCE);
        try {
            return dateFormat.parse(hourInString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int distanceToRestaurant(Restaurant restaurant){
        Location locationUser = new Location("point A");

        locationUser.setLatitude(BaseFragment.locationUser.latitude);
        locationUser.setLongitude(BaseFragment.locationUser.longitude);

        Location locationRestaurant = new Location("point B");

        locationRestaurant.setLatitude(restaurant.getLatitude());
        locationRestaurant.setLongitude(restaurant.getLongitude());

        return Math.round(locationUser.distanceTo(locationRestaurant));
    }

}
