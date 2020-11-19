package com.guillot.go4lunch.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.authentication.SignInActivity;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private FirebaseUser user;
    private SharedPreferences mSharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mSharedPreferences = getPreferences(0);
        Log.d("userId" ,"user splash"+ userId);

        checkIfUserIsConnected();
    }

    /**
     * If user is not authenticated, the sign in activity is launch and the user can be authenticate
     * himself,else the maps activity opens
     */
    private void checkIfUserIsConnected() {
        Log.d(TAG, "checkIfUserIsConnected: ");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "user: " +user);
                Intent intent = (user != null )? new Intent(getBaseContext(), CoreActivity.class):
                        new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}