package com.guillot.go4lunch.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListViewHolder> {

    private List<Restaurant> restaurantList;
    private RequestManager glide;
    private Context context;

    public RestaurantListAdapter(List<Restaurant> restaurants, RequestManager glide) {
        this.restaurantList = restaurants;
        this.glide = glide;
    }

    @Override
    public RestaurantListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        return new RestaurantListViewHolder(ItemRestaurantBinding.inflate(layoutInflater), context);
    }

    @Override
    public void onBindViewHolder(RestaurantListViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);
        holder.updateRestaurantInfo(restaurant, this.glide);
    }

    @Override
    public int getItemCount() {
        return this.restaurantList.size();
    }

    void update(List<Restaurant> restaurants){
        this.restaurantList = restaurants;
        notifyDataSetChanged();
    }
}
