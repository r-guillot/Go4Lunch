package com.guillot.go4lunch.mates;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guillot.go4lunch.R;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.common.ItemClickListener;
import com.guillot.go4lunch.databinding.FragmentMatesBinding;
import com.guillot.go4lunch.details.RestaurantDetailActivity;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MatesFragment extends Fragment {
    private FragmentMatesBinding binding;
    private MatesViewModel viewModel;
    private List<User> users;
    private MatesListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mates, container, false);
        configureBinding(view);
        initViewModel();
        viewModel.getAllUsers();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureRecycleView();
    }

    private void configureBinding(View view) {
        binding = FragmentMatesBinding.bind(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MatesViewModel.class);
        viewModel.init();
        setUpUserList();
    }

    private void setUpUserList() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), this::initUserList);
    }

    private void configureRecycleView() {
        users = new ArrayList<>();
        adapter = new MatesListAdapter(users);
        binding.matesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.matesRecyclerView.setAdapter(adapter);
    }

    private void initUserList(List<User> users) {
        if (!users.isEmpty()) {
        this.users = users;
        } else {
            List<User> noFriendsList= new ArrayList<>();
            User user = new User();
            user.setUrlProfilePicture("https://dupasquier.ch/wp-content/uploads/2017/05/marais10.jpg");
            user.setUsername(getString(R.string.no_friends));
            noFriendsList.add(user);
            this.users = noFriendsList;
        }
        adapter.update(this.users);
        configureOnClickRecyclerView();
    }
    private void configureOnClickRecyclerView() {
        ItemClickListener
                .addTo(binding.matesRecyclerView, R.layout.item_mates)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Intent detailIntent = new Intent(getActivity(), RestaurantDetailActivity.class);
                    if (!Objects.equals(users.get(position).getRestaurantId(), "")) {
                        detailIntent.putExtra(Constants.RESTAURANT, users.get(position).getRestaurantId());
                        startActivity(detailIntent);
                    }
                });
    }}