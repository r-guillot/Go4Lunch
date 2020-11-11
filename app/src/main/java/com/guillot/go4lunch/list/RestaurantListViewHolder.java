package com.guillot.go4lunch.list;

import android.content.Context;
import android.graphics.PorterDuff;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.Restaurant;



public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;
    private final String TAG = RestaurantListViewHolder.class.getSimpleName();

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateRestaurantInfo(Restaurant restaurant){
        binding.textViewName.setText(restaurant.getName());

        binding.textViewAddress.setText(restaurant.getAddress());

        String distanceToDisplay = "0m";
        if(restaurant.getDistance() != null){
            distanceToDisplay = String.format("%sm", restaurant.getDistance().toString());
        }
        binding.textViewDistance.setText(distanceToDisplay);

        Glide.with(context)
                .load("https://upload.wikimedia.org/wikipedia/commons/4/4b/Ursidae-01.jpg")
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");
                        return false;
                    }
                })
                .into(binding.imageViewRestaurant);
        Log.d(TAG, "photo reference " + RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()));


        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());
    }

}