package com.guillot.go4lunch.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.common.base.Strings;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.Restaurant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;
    private Resources resources;
    private final String TAG = RestaurantListViewHolder.class.getSimpleName();
    int result = 0;

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        resources = binding.getRoot().getResources();
    }

    public void updateRestaurantInfo(Restaurant restaurant, List<String> usersRestaurantsIds){
        Log.d(TAG, "users: " +usersRestaurantsIds);
        binding.textViewName.setText(restaurant.getName());

        binding.textViewAddress.setText(restaurant.getAddress());

        checkIfUsersEatingHere(usersRestaurantsIds, restaurant.getRestaurantID());
        if (result != 0) {
            binding.usersTextView.setText(String.valueOf(result));
        } else {
            binding.usersTextView.setVisibility(View.GONE);
        }

        if (restaurant.getPhotoReference() != null) {
            Glide.with(context)
                    .load(RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()))
                    .override(1500, 750)
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.RED))
                    .error(android.R.drawable.stat_notify_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imageViewRestaurant);
            Log.d(TAG, "photo reference " + RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()));
        } else {
            binding.imageViewRestaurant.setImageResource(R.drawable.image_not_avaiable);
        }

        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());

        if (restaurant.getDistance() != null) {
            String distanceToDisplay = String.format("%sm", restaurant.getDistance());
            binding.textViewDistance.setText(distanceToDisplay);
        }
        Log.d(TAG, "restaurantDistance: " + restaurant.getDistance());

        displayOpeningHours(restaurant);
    }

    private void displayOpeningHours(Restaurant restaurant){
        Log.d(TAG, "displayOpeningHours: " + restaurant.getOpeningHours());
        int timeOpening = restaurant.getOpeningHours();
        switch (timeOpening){
            case R.string.closed:
                binding.hoursTextView.setText(timeOpening);
                TextViewCompat.setTextAppearance(binding.hoursTextView, R.style.TimeRestaurantClosed);
                break;
            case R.string.closing_soon:
                binding.hoursTextView.setText(timeOpening);
                TextViewCompat.setTextAppearance(binding.hoursTextView, R.style.TimeRestaurantClosingSoon);
                break;
            case R.string.no_time:
            case R.string.open_24_7:
                binding.hoursTextView.setText(timeOpening);
                TextViewCompat.setTextAppearance(binding.hoursTextView, R.style.TimeRestaurantOpen);
                break;
            default:
                DateFormat dateFormat = new SimpleDateFormat("hh.mma", Locale.FRANCE);
                String timeToDisplay = dateFormat.format(Objects.requireNonNull(Utils.convertStringToDate(timeOpening)));
                binding.hoursTextView.setText(String.format(resources.getString(R.string.open_until), timeToDisplay));
                TextViewCompat.setTextAppearance(binding.hoursTextView, R.style.TimeRestaurantOpen);
                break;
        }
    }

    private void checkIfUsersEatingHere(List<String> usersRIds, String restaurantId) {
        if (usersRIds != null) {
            for (int i = 0; i < usersRIds.size(); i++) {
                String item = usersRIds.get(i);
                if (item.equals(restaurantId)) {
                    result++;
                }
            }
            if (result != 0) {
                binding.usersTextView.setText(result);
            } else {
                binding.usersTextView.setVisibility(View.GONE);
            }
        }
    }
}