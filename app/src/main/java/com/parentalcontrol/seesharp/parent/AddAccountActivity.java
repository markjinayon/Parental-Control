package com.parentalcontrol.seesharp.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parentalcontrol.seesharp.R;
import com.parentalcontrol.seesharp.entity.User;

public class AddAccountActivity extends AppCompatActivity {

    private User currentUser;

    private Button buttonAddAccount_addAccount, buttonCancel_addAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        this.initUserData();

        this.buttonCancel_addAccount = (Button) findViewById(R.id.buttonCancel_addAccount);
        this.buttonCancel_addAccount.setOnClickListener(view -> finish());

        this.buttonAddAccount_addAccount = (Button) findViewById(R.id.buttonAddAccount_addAccount);
        this.buttonAddAccount_addAccount.setOnClickListener(view -> this.addAccount());
    }

    public void addAccount() {
        String newAccountId = ((EditText) findViewById(R.id.editTextEmail_addAccount)).getText().toString().trim();

        if (this.currentUser == null) {
            return;
        }

        if (this.currentUser.connectedAccountId.contains(newAccountId)) {
            return;
        }


        this.currentUser.connectedAccountId.add(newAccountId);
        FirebaseDatabase.getInstance().getReference("Users").child(this.currentUser.accountId).setValue(this.currentUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(AddAccountActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void initUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseUser.getUid();

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                if (currentUser == null) {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddAccountActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}