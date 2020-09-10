package com.guillot.go4lunch.Restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.guillot.go4lunch.BaseFragment;
import com.guillot.go4lunch.databinding.ListFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends BaseFragment {
    private RestaurantViewModel mViewModel;
    private ListFragmentBinding binding;
    private RestaurantListRecyclerViewAdapter mAdapter;
//    public List<Restaurant> restaurantList;

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
//        createList();

        binding.restaurantList.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

//    @SuppressLint("FragmentLiveDataObserve")
//    private void createList() {
//        List<Restaurant>list = new ArrayList<>();
//        mRestaurantViewModel.RestaurantLiveData.observe(this, markerRestaurant -> {
//            if (markerRestaurant != null) {
//                list.add(markerRestaurant);
//            }
//        });
//        restaurantList = list;
//    }
    private void initList() {
//        mRestaurantViewModel.RestaurantLiveData.observe(this, markerRestaurant -> {
//            if (markerRestaurant != null) {
//                restaurantListRV.add(markerRestaurant);
//            }
//        });

        mAdapter = new RestaurantListRecyclerViewAdapter(restaurantListRV, context);

        binding.restaurantList.setAdapter(mAdapter);
    }}
