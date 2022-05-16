package com.parentalcontrol.seesharp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView textViewSignUp_signIn;
    private Button buttonSignIn_signIn;
    private EditText editTextEmail_signIn, editTextPassword_signIn;
    private ProgressBar progressBar_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.mAuth = FirebaseAuth.getInstance();

        // assign components
        this.editTextEmail_signIn = (EditText) findViewById(R.id.editTextEmail_signIn);
        this.editTextPassword_signIn = (EditText) findViewById(R.id.editTextPassword_signIn);

        // progress bar
        this.progressBar_signIn = (ProgressBar) findViewById(R.id.progressBar_signIn);

        // assign action to sign up
        this.textViewSignUp_signIn = (TextView) findViewById(R.id.textViewSignup_signIn);
        this.textViewSignUp_signIn.setOnClickListener(view -> openSignUpActivity());

        // assign button
        this.buttonSignIn_signIn = (Button) findViewById(R.id.buttonSignIn_signIn);
        this.buttonSignIn_signIn.setOnClickListener(view -> signIn());

    }

    public void signIn() {
        String email = this.editTextEmail_signIn.getText().toString().trim();
        String password = this.editTextPassword_signIn.getText().toString().trim();

        if (email.isEmpty()) {
            this.editTextEmail_signIn.setError("Enter your email!");
            this.editTextEmail_signIn.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            this.editTextPassword_signIn.setError("Enter your password!");
            this.editTextPassword_signIn.requestFocus();
            return;
        }

        this.progressBar_signIn.setVisibility(View.VISIBLE);
        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        openDashboardActivity();
                    } else {
                        Toast.makeText(SignInActivity.this, "Failed to sign in! Please check your credentials.", Toast.LENGTH_LONG).show();
                    }
                    this.progressBar_signIn.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> Log.e("FirebaseAuth Failure", e.toString()));
    }

    public void openSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void openDashboardActivity() {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}