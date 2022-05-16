package com.parentalcontrol.seesharp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;
import com.parentalcontrol.seesharp.entity.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView textViewBackToSignIn_signUp;
    private EditText editTextEmail_signUp, editTextPassword_signUp, editTextConfirmPassword_signUp, editTextFullName_signUp;
    private RadioGroup radioGroupUserType_signUp;
    private Button buttonSignUp_signUp;
    private ProgressBar progressBar_signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // firebase
        this.mAuth = FirebaseAuth.getInstance();

        // assigning components and action

        // text view
        this.textViewBackToSignIn_signUp = (TextView) findViewById(R.id.textViewSignIn_signUp);
        this.textViewBackToSignIn_signUp.setOnClickListener(view -> finish());

        //edit text
        this.editTextEmail_signUp = (EditText) findViewById(R.id.editTextEmail_signUp);
        this.editTextPassword_signUp = (EditText) findViewById(R.id.editTextPassword_signUp);
        this.editTextConfirmPassword_signUp = (EditText) findViewById(R.id.editTextConfirmPassword_signUp);
        this.editTextFullName_signUp = (EditText) findViewById(R.id.editTextFullName_signUp);

        // radio
        this.radioGroupUserType_signUp = (RadioGroup) findViewById(R.id.radioGroupUserType_signUp);
        this.radioGroupUserType_signUp.check(R.id.radioButtonParent_signUp);

        // progress bar
        this.progressBar_signUp = (ProgressBar) findViewById(R.id.progressBar_signUp);

        // button
        this.buttonSignUp_signUp = (Button) findViewById(R.id.buttonSignUp_signUp);
        this.buttonSignUp_signUp.setOnClickListener(view -> signUp());
    }

    public void signUp() {
        String email = this.editTextEmail_signUp.getText().toString().trim();
        String password = this.editTextPassword_signUp.getText().toString().trim();
        String confirmPassword = this.editTextConfirmPassword_signUp.getText().toString().trim();
        String fullName = this.editTextFullName_signUp.getText().toString().trim();
        String userType = ((RadioButton) findViewById(this.radioGroupUserType_signUp.getCheckedRadioButtonId())).getText().toString().trim();

        if (email.isEmpty()) {
            this.editTextEmail_signUp.setError("Enter your email!");
            this.editTextEmail_signUp.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.editTextEmail_signUp.setError("Enter a valid email!");
            this.editTextEmail_signUp.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            this.editTextPassword_signUp.setError("Enter your password!");
            this.editTextPassword_signUp.requestFocus();
            return;
        }

        if (password.length() < 6) {
            this.editTextPassword_signUp.setError("Minimum password length is 6");
            this.editTextPassword_signUp.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            this.editTextConfirmPassword_signUp.setError("Enter your confirm password!");
            this.editTextConfirmPassword_signUp.requestFocus();
            return;
        }

        if (!confirmPassword.equals(password)) {
            this.editTextConfirmPassword_signUp.setError("Confirm password mismatch");
            this.editTextConfirmPassword_signUp.requestFocus();
            return;
        }

        if (fullName.isEmpty()) {
            this.editTextFullName_signUp.setError("Enter your full name!");
            this.editTextFullName_signUp.requestFocus();
            return;
        }


        this.progressBar_signUp.setVisibility(View.VISIBLE);
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), fullName, userType);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseDatabase Failure", e.toString());
                                });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                    }
                    this.progressBar_signUp.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    if ( e instanceof FirebaseAuthUserCollisionException) {
                        this.editTextEmail_signUp.setError(e.getMessage());
                        this.editTextEmail_signUp.requestFocus();
                    } else if (e instanceof FirebaseAuthWeakPasswordException) {
                        this.editTextPassword_signUp.setError(e.getMessage());
                        this.editTextPassword_signUp.requestFocus();
                    } else {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}