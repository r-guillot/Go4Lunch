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

public class MatesListViewHolder extends RecyclerView.ViewHolder {

    private ItemMatesBinding binding;
    private Context context;

    MatesListViewHolder(@NonNull ItemMatesBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateMatesInfo(User user){
        if (user !=null) {
            Glide.with(context)
                    .load(user.getUrlProfilePicture())
                    .override(500,500)
//                    .load(user.getUrlProfilePicture())
                    .centerCrop()
                    .circleCrop()
//                    .into(userPic);
//                    .centerCrop()
//                    .circleCrop()
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
//                    .error(android.R.drawable.stat_notify_error)
//                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.profilePictureImageView);

            binding.infoTextView.setText(user.getUsername());
        } else {
            binding.profilePictureImageView.setImageResource(R.drawable.image_not_avaiable);
            binding.infoTextView.setText(R.string.no_friends);

        }

    }
}
