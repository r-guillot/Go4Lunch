package com.guillot.go4lunch.mates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    private void configureRecycleView() {
        users = new ArrayList<>();
        adapter = new MatesListAdapter(users);
        binding.matesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.matesRecyclerView.setAdapter(adapter);
    }

//    private void initUserList(List<User> users) {
//        this.users = users;
//        adapter.update(this.users);
//    }
}