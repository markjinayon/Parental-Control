package com.parentalcontrol.seesharp.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parentalcontrol.seesharp.R;
import com.parentalcontrol.seesharp.adapters.UserViewAdapter;
import com.parentalcontrol.seesharp.entity.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ParentDashboardActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonAddAccount;
    private ListView listViewConnectedAccount;

    UserViewAdapter userViewAdapter;

    private User currentUser;
    private ArrayList<User> connectedAccountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        this.connectedAccountData = new ArrayList<>();

        this.listViewConnectedAccount = (ListView) findViewById(R.id.listViewConnectedAccount_parentDashboard);

        this.floatingActionButtonAddAccount = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddAccount);
        this.floatingActionButtonAddAccount.setOnClickListener(view -> this.addAccount());

        this.initUserData();
    }

    public void addAccount() {
        startActivity(new Intent(this, AddAccountActivity.class));
    }

    public void initUserData() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseUser.getUid();

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                connectedAccountData = new ArrayList<>();
                currentUser = snapshot.getValue(User.class);
                for (String id: currentUser.connectedAccountId) {
                    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                connectedAccountData.add(user);
                                listViewConnectedAccount.setAdapter(new UserViewAdapter(ParentDashboardActivity.this, connectedAccountData));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ParentDashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ParentDashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}