package com.guillot.go4lunch.Restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.guillot.go4lunch.authentication.User;
import com.guillot.go4lunch.databinding.ListFragmentBinding;


class ListFragment extends Fragment {
    private RestaurantViewModel mViewModel;
    private ListFragmentBinding binding;
    private RestaurantListRecyclerViewAdapter mAdapter;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initList();
    }

    @Override
    public void onResume() {
        super.onResume();
//        initList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initViewModel();
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

//    private void initList() {
//        mAdapter = new RestaurantListRecyclerViewAdapter(
//                // list resto
//                , getContext());
//        binding.restaurantList.setAdapter(mAdapter);
//    }
}
