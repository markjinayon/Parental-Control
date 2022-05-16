package com.parentalcontrol.seesharp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.parentalcontrol.seesharp.child.ChildDashboardActivity;
import com.parentalcontrol.seesharp.entity.User;
import com.parentalcontrol.seesharp.parent.ParentDashboardActivity;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);

        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.reference = FirebaseDatabase.getInstance().getReference("Users");
        this.userId = user.getUid();

        this.reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    if (userProfile.userType.equals("Parent")) {
                        openParentDashBoardActivity();
                    } else {
                        openChildDashboardActivity();
                    }
                } else {
                    setContentView(R.layout.activity_dashboard);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openChildDashboardActivity() {
        startActivity(new Intent(this, ChildDashboardActivity.class));
    }

    public void openParentDashBoardActivity() {
        startActivity(new Intent(this, ParentDashboardActivity.class));
    }
}