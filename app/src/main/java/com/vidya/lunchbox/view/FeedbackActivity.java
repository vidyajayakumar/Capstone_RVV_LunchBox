//package com.vidya.lunchbox.view;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//import android.widget.Toolbar;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.vidya.lunchbox.R;
//import com.vidya.lunchbox.helper.Functions;
//import com.vidya.lunchbox.helper.SessionManager;
//import com.vidya.lunchbox.model.User;
//
//public class FeedbackActivity extends AppCompatActivity {
//
//    private Toolbar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feedback);
//
//        getSupportActionBar().hide();
//        // Set a Toolbar to replace the ActionBar.
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        toolbar.setTitleTextColor(Color.WHITE);
//        setActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//
//    }
//}
//
//
//
//package com.vidya.lunchbox.view;
//        import android.app.ProgressDialog;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.util.Log;
//        import android.view.View;
//        import android.view.WindowManager;
//        import android.widget.Toast;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import com.vidya.lunchbox.R;
//        import com.vidya.lunchbox.helper.Functions;
//        import com.vidya.lunchbox.helper.SessionManager;
//        import com.vidya.lunchbox.model.User;
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.OnFailureListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.android.material.button.MaterialButton;
//        import com.google.android.material.textfield.TextInputLayout;
//        import com.google.firebase.auth.AuthResult;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.auth.FirebaseUser;
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//        import com.google.firebase.database.DatabaseReference;
//        import com.google.firebase.database.ValueEventListener;
//
//        import com.google.firebase.database.FirebaseDatabase;
//public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private MaterialButton btnLogin, btnLinkToRegister;
//    private TextInputLayout inputEmail, inputPassword;
//
//    private FirebaseAuth mAuth;
//    private ProgressDialog progressDialog;
//    private DatabaseReference mRef;
//
//    private SessionManager session;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_login);
//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        mRef = database.getReference();
//
//        inputEmail = findViewById(R.id.lTextEmail);
//        inputPassword = findViewById(R.id.lTextPassword);
//        btnLogin = findViewById(R.id.btnLogin);
//        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
//        progressDialog = new ProgressDialog(this);
//
//        mAuth = FirebaseAuth.getInstance();
//        session = new SessionManager(getApplicationContext());
//
///*        if (session.isLoggedIn()) {
//            Intent i = new Intent(LoginActivity.this, CategoryListActivity.class);
//            startActivity(i);
//            finish();
//        }*/
//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        btnLogin.setOnClickListener(this);
//        btnLinkToRegister.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.btnLogin:
//                loginUser();
//                break;
//            case R.id.btnLinkToRegisterScreen:
//                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(i);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void loginUser() {
//
//        Functions.hideSoftKeyboard(LoginActivity.this);
//
//        final String email = inputEmail.getEditText().getText().toString().trim();
//        final String password = inputPassword.getEditText().getText().toString().trim();
//
//        progressDialog.setMessage("Logging-In Please Wait...");
//        progressDialog.show();
//
//        if (!email.isEmpty() && !password.isEmpty()) {
//            if (Functions.isValidEmailAddress(email)) {
//
//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressDialog.dismiss();
//                                if (task.isSuccessful()) {
//                                    progressDialog.cancel();
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    saveUserData(user.getUid());
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                progressDialog.cancel();
//                            }
//                        });
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
//                progressDialog.cancel();
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_SHORT).show();
//            progressDialog.cancel();
//        }
//    }
//
//    private void saveUserData(String userId) {
//
//        mRef.child(userId).getRoot();
//        mRef.child("users").child(userId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            User user = dataSnapshot.getValue(User.class);
//                            session.setUserData(user);
//                            session.setLogin(true);
//                            Intent i = new Intent(LoginActivity.this, CategoryListActivity.class);
//                            startActivity(i);
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.d("Databaseerror", databaseError + "");
//                    }
//                });
//    }
//}