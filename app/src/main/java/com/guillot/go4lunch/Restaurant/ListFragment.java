package com.guillot.go4lunch.Restaurant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.guillot.go4lunch.databinding.ListFragmentBinding;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    private RestaurantViewModel mViewModel;
    private ListFragmentBinding binding;
    private RestaurantListRecyclerViewAdapter mAdapter;

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
        mViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    private void initList() {
        mAdapter = new RestaurantListRecyclerViewAdapter(
                , getContext());
        binding.restaurantList.setAdapter(mAdapter);
    }
}
