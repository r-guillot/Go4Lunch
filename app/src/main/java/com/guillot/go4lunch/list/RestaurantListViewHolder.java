package com.guillot.go4lunch.list;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.details.OpeningHours;

import io.reactivex.rxjava3.core.Observable;


public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;
//    private RestaurantListViewModel viewModel;

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
//        initViewModel();
    }

//    private void initViewModel() {
//        viewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(RestaurantListViewModel.class);
//    }

    public void updateRestaurantInfo(Restaurant restaurant, RequestManager glide){
        binding.textViewName.setText(restaurant.getName());

        String address = restaurant.getAddress();
        binding.textViewAddress.setText(address);

        String distanceToDisplay = "0m";
        if(restaurant.getDistance() != null){
            distanceToDisplay = String.format("%sm", restaurant.getDistance().toString());
        }
        binding.textViewDistance.setText(distanceToDisplay);

//        if(restaurant.getUrlPhoto() != null){
//            glide.load(restaurant.getUrlPhoto()).into(binding.imageViewRestaurant);
//        }
//
//        glide.load(restaurant.getUrlPhoto()).apply(RequestOptions.centerCropTransform())
//                .into(binding.imageViewRestaurant);

        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());

//        binding.imageViewRestaurant.setImageResource(.getPhotoRestaurant(restaurant.getUrlPhoto()));
    }

}