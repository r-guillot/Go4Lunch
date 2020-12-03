package com.guillot.go4lunch.authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.facebook.FacebookSdk;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.databinding.ActivitySignInBinding;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private final String TAG = SignInActivity.class.getSimpleName();
    private ActivitySignInBinding binding;
    private SignInViewModel mViewModel;
    private static final int RC_SIGN_IN = 1337;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager fbCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Twitter.initialize(this);
        twitterSignInBuilder();
        viewBinding();
        initViewModel();
        googleSignInBuilder();
        facebookLoginInitiating();
        updateTwitterButton();
        onClickGoogleButton();
        onClickTwitterButton();

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
    private void onClickGoogleButton() {
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
        binding.twitterSignInButton.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);

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
                coreActivityIntent.putExtra(Constants.USER_INTENT, authenticatedUser);
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
                coreActivityIntent.putExtra(Constants.USER_INTENT, authenticatedUser);
                startActivity(coreActivityIntent);
                saveData();
                finish();
            } else {
                Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Twitter authentication
     */
    private void twitterSignInBuilder(){
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);
    }

    private void onClickTwitterButton(){
        Log.d(TAG, "onClickTwitterButton: ");
        binding.twitterSignInButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "success: " + result);
                firebaseAuthWithTwitter(result.data);
                binding.twitterSignInButton.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                updateTwitterButton();
            }
        });
    }

    private void firebaseAuthWithTwitter(TwitterSession session) {
        Log.d(TAG, "firebaseAuthWithTwitter: ");
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);
        signInWithTwitterAuthCredential(credential);
    }

    private void signInWithTwitterAuthCredential(AuthCredential twitterAuthCredential) {
        mViewModel.signInWithTwitter(twitterAuthCredential);
        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser != null) {
                Log.d("SignInActivity", "signInTwitterWithCredential:success");
                Intent coreActivityIntent = new Intent(getBaseContext(), CoreActivity.class);
                coreActivityIntent.putExtra(Constants.USER_INTENT, authenticatedUser);
                startActivity(coreActivityIntent);
                saveData();
                finish();
            } else {
                Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTwitterButton(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            binding.twitterSignInButton.setVisibility(View.VISIBLE);
        }
        else{
//            binding.twitterSignInButton.setVisibility(View.GONE);
        }
    }

    /**
     * Mail authentication
     */
    private void onClickMailButton(){
        binding.mailLoginButton.setOnClickListener(v -> openDialog());
    }

    private void openDialog(){
        new AlertDialog.Builder(this)
                .setTitle("R.string.title")
                .setMessage("Have you already a count with this mail ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openLogInDialog();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSignInDialog();
                    }
                })
                .show();
    }

    private void openLogInDialog() {
        // Inflate and set the layout for the dialog
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        new AlertDialog.Builder(this)
                .setView(inflater.inflate(R.layout.layout_log_in, null))
                // Add action buttons
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...


                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void openSignInDialog() {
        // Inflate and set the layout for the dialog
        LayoutInflater inflater = this.getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        new AlertDialog.Builder(this)
                .setView(inflater.inflate(R.layout.layout_log_in, null))
                // Add action buttons
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.USER_ID, Objects.requireNonNull(mViewModel.authenticatedUserLiveData.getValue()).getId()).apply();
        editor.putString(Constants.USER_USERNAME, mViewModel.authenticatedUserLiveData.getValue().getUsername()).apply();
        editor.putString(Constants.URL_PROFILE_PICTURE, mViewModel.authenticatedUserLiveData.getValue().getUrlProfilePicture().toString()).apply();
        editor.putString(Constants.USER_LOCATION, mViewModel.authenticatedUserLiveData.getValue().getUserLocation().toString()).apply();
    }
}