package com.guillot.go4lunch.restaurantlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.guillot.go4lunch.BaseFragment;
import com.guillot.go4lunch.Restaurant.DetailsRestaurant;
import com.guillot.go4lunch.Restaurant.model.Restaurant;
import com.guillot.go4lunch.databinding.ListFragmentBinding;

import java.util.List;


public class ListFragment extends BaseFragment {
    private ListRestaurantViewModel mViewModel;
    private ListFragmentBinding binding;
    private RestaurantListRecyclerViewAdapter mAdapter;
    private List<Restaurant> restaurantList;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        initViewModel();

        binding.restaurantList.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(ListRestaurantViewModel.class);
    }

    private void initList() {
        mViewModel.setRetrofit(LocationUser, radius, type, fields, key);
        mViewModel.getRestaurants();
        mViewModel.RestaurantListLiveData.observe(this, liveDataListRestaurant -> {
            Log.d("locationUpdate", "observer");
            if (liveDataListRestaurant != null) {
                restaurantList = liveDataListRestaurant;
                Log.d("locationUpdate", "list size base " + restaurantList.size());
            }

        mAdapter = new RestaurantListRecyclerViewAdapter(restaurantList, context, this::onItemClick);

        binding.restaurantList.setAdapter(mAdapter);
        });
    }

    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailsRestaurant.class);
        detailIntent.putExtra("Restaurant", String.valueOf(restaurantList.get(position)));
        startActivity(detailIntent);

    }
}
