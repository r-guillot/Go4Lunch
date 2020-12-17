package com.guillot.go4lunch.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivitySignInMailBinding;

public class SignInMailActivity extends AppCompatActivity {
    private ActivitySignInMailBinding binding;
    private SignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_mail);
        viewBinding();
        initViewModel();
        getMailAndPassword();
    }

    private void viewBinding() {
        binding = ActivitySignInMailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    private void getMailAndPassword() {
        binding.passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String email = binding.mailEditText.getText().toString().trim();
                    String password = binding.passwordEditText.getText().toString();
                    if (!email.isEmpty() && !password.isEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            viewModel.signInWithEmail(email, password, getApplicationContext());
                        }
                    } else{
                        Toast.makeText(getApplicationContext(),"veuillez remplir une adress mail et un mot de passe valide", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
                return false;
            }
        });
    }
}