package com.guillot.go4lunch.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
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
import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.databinding.ActivitySignInBinding;
import com.guillot.go4lunch.model.User;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

public class SignInActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener{
    private ActivitySignInBinding binding;
    private SignInViewModel mViewModel;
    private static final int RC_SIGN_IN = 1337;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager fbCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterSignInBuilder();
        viewBinding();
        initViewModel();
        googleSignInBuilder();
        facebookLoginInitiating();
        updateTwitterButton();
        onClickGoogleButton();
        onClickTwitterButton();
        onClickMailButton();
        onClickLogIn();
        onClickSignIn();
        OnClickCancel();

        FirebaseAuth auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        facebookLoginInitiating();
        if(AccessToken.getCurrentAccessToken() != null){
        LoginManager.getInstance().logOut();}
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
        binding.socialButtonLayout.googleSignInButton.setOnClickListener(v -> signIn());
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
        binding.socialButtonLayout.twitterSignInButton.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
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
        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser != null) {
                coreActivityIntent(authenticatedUser);
                finish();
            } else {
                Snackbar.make(binding.getRoot(), getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * FaceBook authentication
     */
    private void facebookLoginInitiating() {
        // Initialize Facebook Login button
        fbCallback = CallbackManager.Factory.create();
        LoginButton loginButton = binding.socialButtonLayout.facebookSignInButton;
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(fbCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
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
                coreActivityIntent(authenticatedUser);
                finish();
            } else {
                Snackbar.make(binding.getRoot(), getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Twitter authentication
     */
    private void twitterSignInBuilder(){
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(BuildConfig.twitter_consumer_key,
                BuildConfig.twitter_consumer_secret);
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);
    }

    private void onClickTwitterButton(){
        binding.socialButtonLayout.twitterSignInButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                firebaseAuthWithTwitter(result.data);
                binding.socialButtonLayout.twitterSignInButton.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                updateTwitterButton();
            }
        });
    }

    private void firebaseAuthWithTwitter(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);
        signInWithTwitterAuthCredential(credential);
    }

    private void signInWithTwitterAuthCredential(AuthCredential twitterAuthCredential) {
        mViewModel.signInWithTwitter(twitterAuthCredential);
        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser != null) {
                coreActivityIntent(authenticatedUser);
                finish();
            } else {
                Snackbar.make(binding.getRoot(), getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTwitterButton(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            binding.socialButtonLayout.twitterSignInButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Mail authentication
     */
    private void onClickMailButton(){
        binding.socialButtonLayout.mailLoginButton.setOnClickListener(v -> openBottomSheet());
    }

    private void openBottomSheet(){
        onBackPressed();
        BottomSheetDialog bottomSheet = BottomSheetDialog.getInstance();
        bottomSheet.showNow(getSupportFragmentManager(), BottomSheetDialog.class.getSimpleName());
    }

    @Override
    public void onButtonClicked(int result) {
        if (result == 1){
            binding.socialButtonLayout.getRoot().setVisibility(View.GONE);
            binding.edittextMailLayout.getRoot().setVisibility(View.VISIBLE);
            binding.logInMailLayout.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.socialButtonLayout.getRoot().setVisibility(View.GONE);
            binding.edittextMailLayout.getRoot().setVisibility(View.VISIBLE);
            binding.signInMailLayout.getRoot().setVisibility(View.VISIBLE);
        }

    }

    private void onClickLogIn() {
        binding.logInMailLayout.mailLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMailAndPassword(1);
            }
        });
    }

    private void onClickSignIn(){
        binding.signInMailLayout.mailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMailAndPassword(0);
            }
        });
    }

    private void getMailAndPassword(int check) {
                String email = binding.edittextMailLayout.mailEditText.getText().toString().trim();
                String password = binding.edittextMailLayout.passwordEditText.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mViewModel.checkLogOrSignWithMail(email, password, check, getApplicationContext());
                        mViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
                            if (authenticatedUser != null) {
                                coreActivityIntent(authenticatedUser);
                                finish();
                            } else {
                                Snackbar.make(binding.getRoot(), getString(R.string.authentication_failed), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else{
                    Toast.makeText(getApplicationContext(),R.string.valid_mail_password, Toast.LENGTH_SHORT).show();
                }
    }

    private void OnClickCancel(){
        binding.signInMailLayout.mailCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.socialButtonLayout.getRoot().setVisibility(View.VISIBLE);
                binding.edittextMailLayout.getRoot().setVisibility(View.GONE);
                binding.signInMailLayout.getRoot().setVisibility(View.GONE);
            }
        });

        binding.logInMailLayout.mailCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.socialButtonLayout.getRoot().setVisibility(View.VISIBLE);
                binding.edittextMailLayout.getRoot().setVisibility(View.GONE);
                binding.logInMailLayout.getRoot().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void coreActivityIntent(User authenticatedUser){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent coreActivityIntent = new Intent(getBaseContext(), CoreActivity.class);
                coreActivityIntent.putExtra(Constants.USER_INTENT, authenticatedUser);
                startActivity(coreActivityIntent);
            }
        }, 600);
    }


}