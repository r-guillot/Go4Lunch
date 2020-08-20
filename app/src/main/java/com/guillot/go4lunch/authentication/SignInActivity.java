package com.guillot.go4lunch.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.FacebookSignatureValidator;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.data.remote.FacebookSignInHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.activities.CoreActivity;
import com.guillot.go4lunch.databinding.ActivitySignInBinding;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private SignInViewModel mViewModel;
    private static final int RC_SIGN_IN = 1337;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager fbCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding();
        initViewModel();
        googleSignInBuilder();
        onClickButton();
        facebookLoginInitiating();

        mAuth = FirebaseAuth.getInstance();
        //Initiate FB SDK
        FacebookSdk.sdkInitialize(this);
    }

    private void viewBinding() {
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    /**
     * Google authentication
     */
    private void onClickButton() {
        binding.googleSignInButton.setOnClickListener(v -> signIn());
    }

    private void googleSignInBuilder() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallback.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.getId());
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignInActivity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(credential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        mViewModel.signInWithGoogle(googleAuthCredential);
        Log.d("user value", "test ");
        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser != null) {
                Log.d("SignInActivity", "signInGoogleWithCredential:success");
                Intent coreActivityIntent = new Intent(getBaseContext(), CoreActivity.class);
                coreActivityIntent.putExtra(CONSTANTS.USER_INTENT, authenticatedUser);
                startActivity(coreActivityIntent);
                saveData();
                finish();
            } else {
                Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * FaceBook authentication
     */
    private void facebookLoginInitiating() {
        // Initialize Facebook Login button
        fbCallback = CallbackManager.Factory.create();
        LoginButton loginButton = binding.facebookSignInButton;
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(fbCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SignInActivity", "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("SignInActivity", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("SignInActivity", "facebook:onError", error);
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken facebookToken) {
        String facebookTokenId = facebookToken.getToken();
        AuthCredential credential = FacebookAuthProvider.getCredential(facebookTokenId);
        signInWithFacebookAuthCredential(credential);
    }

    private void signInWithFacebookAuthCredential(AuthCredential facebookAuthCredential) {
        mViewModel.signInWithFacebook(facebookAuthCredential);
        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser != null) {
                Log.d("SignInActivity", "signInFacebookWithCredential:success");
                Intent coreActivityIntent = new Intent(getBaseContext(), CoreActivity.class);
                coreActivityIntent.putExtra(CONSTANTS.USER_INTENT, authenticatedUser);
                startActivity(coreActivityIntent);
                saveData();
                finish();
            } else {
                Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(CONSTANTS.USER_ID, Objects.requireNonNull(mViewModel.authenticatedUserLiveData.getValue()).getId()).apply();
        editor.putString(CONSTANTS.USER_USERNAME, mViewModel.authenticatedUserLiveData.getValue().getUsername()).apply();
        editor.putString(CONSTANTS.URL_PROFILE_PICTURE, mViewModel.authenticatedUserLiveData.getValue().getUrlProfilePicture().toString()).apply();
        editor.putString(CONSTANTS.USER_LOCATION, mViewModel.authenticatedUserLiveData.getValue().getUserLocation().toString()).apply();
    }
}