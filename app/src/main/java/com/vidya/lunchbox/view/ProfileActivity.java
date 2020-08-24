package com.vidya.lunchbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.User;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager session;
    private TextView tvName, tvEmail;
    private MaterialButton btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();
        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        btnLogout = findViewById(R.id.logout);
        // session manager
        session = new SessionManager(getApplicationContext());

        if (session.getUserData() != null) {
            User user = session.getUserData();
            tvName.setText("Name : " + user.getName());
            tvEmail.setText("Email : " + user.getEmail());
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.clearPreferences();
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "You are successfully logged out..", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }
}
