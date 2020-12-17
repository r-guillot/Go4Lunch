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
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;
    private List<User> users;
    private Resources resources;
    private final String TAG = RestaurantListViewHolder.class.getSimpleName();

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context, List<User> users) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.users = users;
        resources = binding.getRoot().getResources();
    }

    public void updateRestaurantInfo(Restaurant restaurant, List<User> users){
        binding.textViewName.setText(restaurant.getName());

        binding.textViewAddress.setText(restaurant.getAddress());

        if (users != null) {
            binding.usersTextView.setText(users.size());
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

        String distanceToDisplay;
        if(restaurant.getDistance() != null){
            distanceToDisplay = String.format("%sm", restaurant.getDistance());
        } else {
            distanceToDisplay = "0m";
        }
        binding.textViewDistance.setText(distanceToDisplay);
        Log.d(TAG, "restaurantDistance: " + restaurant.getDistance());
        Log.d(TAG, "distanceToDisplay: "+ distanceToDisplay);

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
}