package com.guillot.go4lunch.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.ApiDistanceMatrix;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.ApiDistanceResponse;
import com.guillot.go4lunch.model.Restaurant;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;
    private final String TAG = RestaurantListViewHolder.class.getSimpleName();

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateRestaurantInfo(Restaurant restaurant, String distance){
        binding.textViewName.setText(restaurant.getName());

        binding.textViewAddress.setText(restaurant.getAddress());

        String distanceToDisplay = "0m";
        if(distance != null){
            distanceToDisplay = String.format("%sm", distance);
        }
        binding.textViewDistance.setText(distanceToDisplay);


        if (restaurant.getPhotoReference() != null) {
            Glide.with(context)
                    .load(RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()))
                    .override(1500, 750)
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.RED))
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.imageViewRestaurant);
            Log.d(TAG, "photo reference " + RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()));
        } else {
            binding.imageViewRestaurant.setImageResource(R.drawable.image_not_avaiable);
        }


        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());
    }
}