package com.guillot.go4lunch.mates;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ItemMatesBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.User;

import java.util.Objects;

public class MatesListViewHolder extends RecyclerView.ViewHolder {

    private ItemMatesBinding binding;
    private Context context;

    MatesListViewHolder(@NonNull ItemMatesBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateMatesInfo(User user){
        String textUser;
        if (user !=null) {
            Glide.with(context)
                    .load(user.getUrlProfilePicture())
                    .override(500,500)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.profilePictureImageView);

            if (Objects.equals(user.getRestaurantName(), "")){
                String message = context.getString(R.string.mates_not_place);
                textUser = String.format(message, user.getUsername());
            } else {
                String message = context.getString(R.string.place_mates_eating);
                textUser = String.format(message, user.getUsername(), user.getRestaurantName());
            }
            binding.infoTextView.setText(textUser);
        } else {
            binding.profilePictureImageView.setImageResource(R.drawable.image_not_avaiable);
            binding.infoTextView.setText(R.string.no_friends);

        }

    }
}
