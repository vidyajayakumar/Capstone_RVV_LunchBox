package com.vidya.lunchbox.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.Functions;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private MaterialButton btnRegister, btnLinkToLogin;
    private TextInputLayout inputName, inputEmail, inputPassword;
    private SessionManager session;
    private FirebaseAuth mAuth;

    private DatabaseReference mRef;
    private ProgressDialog progressDialog;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(getApplicationContext());

        inputName = findViewById(R.id.rTextName);
        inputEmail = findViewById(R.id.rTextEmail);
        inputPassword = findViewById(R.id.rTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);
        progressDialog = new ProgressDialog(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnRegister.setOnClickListener(this);
        btnLinkToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLinkToLoginScreen:
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnRegister:
                checkUser();
                break;
        }
    }

    private void checkUser() {

        Functions.hideSoftKeyboard(RegisterActivity.this);

        final String name = inputName.getEditText().getText().toString().trim();
        final String email = inputEmail.getEditText().getText().toString().trim();
        final String password = inputPassword.getEditText().getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            if (Functions.isValidEmailAddress(email)) {

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    saveUserData(user.getUid(), name, email, password);
                                } else {
                                    //display some message here
//                                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Email already registered or Password is less than 6 character", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserData(String userId, String name, String email, String password) {
        User userInformation = new User(userId, name, email, password);
        mRef.child("users").child(userId).setValue(userInformation);
        session.setUserData(userInformation);
        session.setLogin(true);
        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(RegisterActivity.this, CategoryListActivity.class);
        startActivity(i);
        finish();
    }


}