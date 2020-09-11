package com.guillot.go4lunch.restaurantlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.Restaurant.Restaurant;
import com.guillot.go4lunch.databinding.RestaurantRecyclerviewBinding;

import java.util.List;

public class RestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantListRecyclerViewAdapter.MyViewHolder> {

    private List<Restaurant> mRestaurants;
    private Context context;
    private OnItemClickListener mListener;

    // data is passed into the constructor
    public RestaurantListRecyclerViewAdapter(List<Restaurant> restaurants, Context context, OnItemClickListener mOnItemClickListener) {
        this.mRestaurants = restaurants;
        this.context = context;
        this.mListener = mOnItemClickListener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RestaurantRecyclerviewBinding binding;
        OnItemClickListener mOnItemClickListener;

        private MyViewHolder(@NonNull RestaurantRecyclerviewBinding b, OnItemClickListener mOnItemClickListener) {
            super(b.getRoot());
            binding = b;
            this.mOnItemClickListener = mOnItemClickListener;

            b.getRoot().setOnClickListener(this);
        }

        @Override
            public void onClick(View v) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new MyViewHolder(RestaurantRecyclerviewBinding.inflate(layoutInflater), mListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Restaurant currentRestaurant = mRestaurants.get(position);
        Log.d("RecyclerViewAdapter", "currentRestaurant " + currentRestaurant);
        // TODO: 18/08/2020 hold when we have info
        Glide.with(holder.binding.imageViewRestaurant.getContext())
                .load(currentRestaurant.getUriIcon())
                .into(holder.binding.imageViewRestaurant);
        holder.binding.textViewName.setText(currentRestaurant.getName());
}

    // total number of rows
    @Override
    public int getItemCount() {
        return this.mRestaurants.size();
    }


}
