package com.guillot.go4lunch.mates;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.FragmentMatesBinding;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;


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
            user.setUrlProfilePicture("https://www.beenaroundtheglobe.com/wp-content/uploads/2018/09/solomangarephobia.jpg");
            user.setUsername("No FRIENDS");
            noFriendsList.add(user);
            this.users = noFriendsList;
        }
        Log.d("MatesFragment", "initUserList: " + this.users);
        adapter.update(this.users);
    }
}