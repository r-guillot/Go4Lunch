package com.guillot.go4lunch.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.model.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantListAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurantList = restaurants;
        this.context = context;
    }

    @NotNull
    @Override
    public RestaurantListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        return new RestaurantListViewHolder(ItemRestaurantBinding.inflate(layoutInflater), context);
    }

    @Override
    public void onBindViewHolder(RestaurantListViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);
        holder.updateRestaurantInfo(restaurant);
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
