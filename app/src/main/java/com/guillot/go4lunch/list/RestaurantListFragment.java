package com.guillot.go4lunch.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.base.BaseFragment;
import com.guillot.go4lunch.common.ItemClickListener;
import com.guillot.go4lunch.databinding.RestaurantListFragmentBinding;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.restaurantDetails.RestaurantDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListFragment extends BaseFragment {

    private final String TAG = RestaurantListFragment.class.getSimpleName();

    private RestaurantListFragmentBinding binding;
    private RestaurantListViewModel viewModel;
    private List<Restaurant> restaurants;
    private RestaurantListAdapter adapter;
    public final static String RESTAURANT = "RESTAURANT_ID";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_list_fragment, container, false);
        configureBinding(view);
        initViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureRecycleView();
    }

    @Override
    public void getLocationUser(LatLng locationUser) {
        viewModel.init();
        viewModel.executeNetworkRequest(locationUser);
        viewModel.getRestaurantsList().observe(this, this::initRestaurantList);
    }

    private void configureBinding(View view) {
        binding = RestaurantListFragmentBinding.bind(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);
    }

    private void configureRecycleView() {
        restaurants = new ArrayList<>();
        adapter = new RestaurantListAdapter(restaurants);
        binding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.restaurantRecyclerView.setAdapter(adapter);
    }

    private void initRestaurantList(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        adapter.update(this.restaurants);
        configureOnClickRecyclerView();
    }

    private void configureOnClickRecyclerView() {
        ItemClickListener
                .addTo(binding.restaurantRecyclerView, R.layout.item_restaurant)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Intent detailIntent = new Intent(getActivity(), RestaurantDetailActivity.class);
                    detailIntent.putExtra(RESTAURANT, restaurants.get(position).getRestaurantID());
                    startActivity(detailIntent);
                });
    }
}
