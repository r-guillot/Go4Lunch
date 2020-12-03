package com.guillot.go4lunch.settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.NotificationHelper;
import com.guillot.go4lunch.authentication.SignInActivity;
import com.guillot.go4lunch.databinding.ActivitySettingsBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.model.User;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = SettingsActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 13;
    private static String USER_ID;
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;
    private NotificationHelper notification;
//    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        viewBinding();
        notification = new NotificationHelper(this);
        initViewModel();
//        notification.resetRestaurantData();
//        notification.createNotificationChannel();
//        viewModel.getCurrentUser();
        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewModel();
        getUserInfo();
    }

    private void viewBinding() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    private void getUserInfo() {
        viewModel.init();
        viewModel.getCurrentUser();
        viewModel.getCurrentUserLiveData().observe(this, this::setGraphicElements);
//        notification.initNotification();
    }

    private void setGraphicElements(User user) {
        Log.d(TAG, "user: " + user);
        if (user.getUrlProfilePicture() != null) {
            Glide.with(this)
                    .load(user.getUrlProfilePicture())
                    .centerCrop()
                    .circleCrop()
                    .into(binding.profilePictureImageButton);
        } else {
            binding.profilePictureImageButton.setImageResource(R.drawable.image_not_avaiable);
        }

        binding.mailEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        binding.nameEditText.setText(user.getUsername() != null ? user.getUsername() : "");
        binding.mailEditText.setText(user.getUserMail() != null ? user.getUserMail() : "");

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(user);
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertDialog(user);
            }
        });

        checkSwitchNotificationState(user);
        binding.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.updateNotification(user.getId(), isChecked);
                notification.configureNotification(isChecked);
            }
        });

        updateProfilePicture(user);
    }

    private void updateUserInfo(User user) {
        viewModel.updateUserInfo(user.getId(), binding.nameEditText.getText().toString().trim(), binding.mailEditText.getText().toString().trim());
        getUserInfo();
    }

    private void updateProfilePicture(User user) {
        binding.profilePictureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(user);
            }
        });
    }

    private void openFileChooser(User user) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(USER_ID, user.getId());
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Log.d(TAG, "openFileChooser: " + intent + PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userId = getIntent().getStringExtra(USER_ID);
        Log.d(TAG, "onActivityResult: start");

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Log.d(TAG, "onActivityResult: " + requestCode + "  " +resultCode + data);
            Uri imageUri = data.getData();
            Log.d(TAG, "image uri " + imageUri);
            viewModel.uploadPhotoStorage(imageUri);
            getUserInfo();
            Log.d(TAG, "onActivityResult: end ok");
        } else {
            Log.d(TAG, "onActivityResult: end no");
            Toast.makeText(this, R.string.toast_error_image, Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteAlertDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserFromFirestore(user);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteUserFromFirestore(User user) {
        viewModel.deleteUser(user.getId());
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void checkSwitchNotificationState(User user){
        Log.d(TAG, "checkSwitchNotificationState: " + user.isNotification());
        binding.notificationSwitch.setChecked(user.isNotification());
//        if (user.isNotification()) {
//            binding.notificationSwitch.setChecked(true);
//        } else {
//            binding.notificationSwitch.setChecked(false);
//        }
    }
}