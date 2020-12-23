package com.guillot.go4lunch.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.mates.MatesListAdapter;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        intentRestaurantId = intent.getStringExtra(Constants.RESTAURANT);
        Log.d("Intent", "intentRestaurantId2: " + intentRestaurantId);

        viewModel.init();
        viewModel.getUserId();
        viewModel.getCurrentUser(intentRestaurantId);
        viewModel.getRestaurantDetails().observe(this, this::setGraphicElement);
        Log.d(TAG, "7 ");
        setUpUserList();
    }

    private void setGraphicElement(Restaurant restaurant) {
        binding.textViewNameRestaurant.setText(restaurant.getName());
        binding.textViewAddressRestaurant.setText(restaurant.getAddress());

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
                checkIfUserDisableNotification();
                updateRestaurantSelected();
            }
        });

        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());

        binding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                startActivity(intent);
            }
        });

        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = viewModel.getCurrentUserLiveData().getValue();
                assert user != null;
                List<String> restaurantLiked = user.getRestaurantLiked();
                updateRestaurantLiked(restaurantLiked);
            }
        });

        binding.websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebSite()));
//                intent.setData(Uri.parse("web:" + restaurant.getWebSite()));
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFabIsSelected();
                checkRestaurantIsLiked();
            }
        }, 100);
    }

    private void checkFabIsSelected() {
        Log.d(TAG, "checkFabIsSelected: 1 " + viewModel.checkIfRestaurantIsChosen() );
        if (viewModel.checkIfRestaurantIsChosen()) {
            Log.d(TAG, "checkFabIsSelected: 2 " + viewModel.checkIfRestaurantIsChosen() );
            binding.fabSelectedRestaurant.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.fabSelectedRestaurant.setImageResource(R.drawable.ic_selected);
        } else {
            Log.d(TAG, "checkFabIsSelected: 3 " + viewModel.checkIfRestaurantIsChosen() );
            binding.fabSelectedRestaurant.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orangered)));
            binding.fabSelectedRestaurant.setImageResource(R.drawable.ic_add_24);
        }
    }

    private void checkRestaurantIsLiked(){
        if (viewModel.checkIfRestaurantIsLiked()) {
            binding.likeButton.setTextColor(getResources().getColor(R.color.quantum_yellow));
        } else {
            binding.likeButton.setTextColor(getResources().getColor(R.color.orange));
        }
    }

    private void updateRestaurantSelected() {
        viewModel.getCurrentUser(intentRestaurantId);
        if (viewModel.checkIfRestaurantIsChosen()) {
            viewModel.setRestaurantUnselected();
        } else {
            viewModel.setRestaurantSelected();
        }
        checkFabIsSelected();
    }

    private void updateRestaurantLiked(List<String> restaurantLiked) {
        viewModel.getCurrentUser(intentRestaurantId);
        if (viewModel.checkIfRestaurantIsLiked()) {
            viewModel.setRestaurantUnliked(restaurantLiked);
        } else {
            viewModel.setRestaurantLiked(restaurantLiked);
        }
            checkRestaurantIsLiked();
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
            user.setUrlProfilePicture("https://dupasquier.ch/wp-content/uploads/2017/05/marais10.jpg");
            user.setUsername("no soul treads these lands!");
            noFriendsList.add(user);
            this.users = noFriendsList;
        }
        Log.d("MatesFragment", "initUserList: " + this.users);
        adapter.update(this.users);
    }

    private void checkIfUserDisableNotification(){
        User user = viewModel.getCurrentUserLiveData().getValue();
        assert user != null;
        if (!user.isNotification()){
            Toast.makeText(this,R.string.notification_disable,Toast.LENGTH_LONG).show();
        }
    }
}