package com.guillot.go4lunch.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.authentication.SignInActivity;

public class SplashActivity extends AppCompatActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIfUserIsConnected();
    }

    /**
     * If user is not authenticated, the sign in activity is launch and the user can be authenticate
     * himself,else the maps activity opens
     */
    private void checkIfUserIsConnected() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = (user != null )? new Intent(getBaseContext(), CoreActivity.class):
                        new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}