package com.guillot.go4lunch.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.main.CoreViewModel;
import com.guillot.go4lunch.mates.MatesListAdapter;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {
    private final String TAG = RestaurantDetailActivity.class.getSimpleName();

    private ActivityDetailsRestaurantBinding binding;
    private RestaurantDetailsViewModel viewModel;
    private String intentRestaurantId;
    private int result;
    private List<User> users;
    private MatesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        initViewModel();
        viewBinding();
        configureRecycleView();

        getInfoRestaurant();
        Log.d("Intent", "intentRestaurantId: " + intentRestaurantId);
    }

    private void viewBinding() {
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);
    }

    private void getInfoRestaurant() {
        Intent intent = getIntent();
        intentRestaurantId = intent.getStringExtra(CoreActivity.RESTAURANT);
        Log.d("Intent", "intentRestaurantId2: " + intentRestaurantId);

        viewModel.init();
        viewModel.getCurrentUser();
        viewModel.executeNetworkRequest(intentRestaurantId);
        viewModel.getUsersEatingHere(intentRestaurantId);
        viewModel.getRestaurantDetails().observe(this, this::setGraphicElement);
//        setCheckFab();
        setUpUserList();
    }

    private void setGraphicElement(Restaurant restaurant) {
        binding.textViewNameRestaurant.setText(restaurant.getName());
        binding.textViewAddressRestaurant.setText(restaurant.getAddress());
        setCheckFab();

        if (restaurant.getPhotoReference() != null) {
            Glide.with(this)
                    .load(viewModel.getPhoto(restaurant.getPhotoReference()))
                    .centerCrop()
                    .into(binding.imageViewPictureRestaurant);
        } else {
            binding.imageViewPictureRestaurant.setImageResource(R.drawable.image_not_avaiable);
        }


        binding.fabSelectedRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckFab();
            }
        });


        binding.textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                startActivity(intent);
            }
        });
        binding.textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setData(Uri.parse("web:" + restaurant.getWebSite()));
                startActivity(intent);
            }
        });

    }
    private void setCheckFab() {
        viewModel.getMediatorLiveData().observe(this,this::checkFabIsSelected);
    }

    private void checkFabIsSelected(Pair<Restaurant, User> restaurantUserPair) {
        Log.d(TAG, "restaurantUserPair: " + restaurantUserPair);
        Log.d(TAG, "restaurant " + restaurantUserPair.first.getRestaurantID());
        Log.d(TAG, "user: " + restaurantUserPair.second.getId());
//        if (restaurantUserPair.second.getRestaurantId() != null) {
        assert restaurantUserPair.second.getRestaurantId() != null;
        if (restaurantUserPair.second.getRestaurantId().equals(restaurantUserPair.first.getRestaurantID())) {
                binding.fabSelectedRestaurant.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.fabSelectedRestaurant.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));
                viewModel.updateSelectedRestaurant(restaurantUserPair.first.getRestaurantID(), restaurantUserPair.first.getName());
            } else {
//        }
                binding.fabSelectedRestaurant.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                binding.fabSelectedRestaurant.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24));
                viewModel.updateSelectedRestaurant(null, null);
            }
    }

    private void configureRecycleView() {
        users = new ArrayList<>();
        adapter = new MatesListAdapter(users);
        binding.detailsMatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailsMatesRecyclerView.setAdapter(adapter);
    }

    private void setUpUserList() {
        viewModel.getListUsersEatingHere().observe(this, this::initUserList);
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