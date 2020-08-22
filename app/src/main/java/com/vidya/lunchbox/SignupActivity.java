package com.vidya.lunchbox;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vidya.lunchbox.Utils.GALLERY;
import static com.vidya.lunchbox.Utils.PERMISSION_CODE;

public class SignupActivity extends AppCompatActivity {

    private EditText et_firstName, et_lastName, et_mail, et_phone, et_username, et_password, et_confirmpswd;
    private RadioGroup radioGroupGender;
    private Button signUp;
    private CircleImageView profileImage;
    private boolean empty = false;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initviews();
    }

    private void initviews() {
        et_firstName = findViewById(R.id.txtFirstName);
        et_lastName = findViewById(R.id.txtLastName);
        et_mail = findViewById(R.id.txtEmail);
        et_phone = findViewById(R.id.txtPhoneNumber);
        et_username = findViewById(R.id.txtUsername);
        et_password = findViewById(R.id.txtPassword);
        et_confirmpswd = findViewById(R.id.txtPassword2);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        profileImage = findViewById(R.id.imgprofile);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    OpenImages();
                }
            }
        });
        signUp = findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = et_firstName.getText().toString();
                String lastName = et_lastName.getText().toString();
                String mail = et_mail.getText().toString();
                String phoneNumber = et_phone.getText().toString();
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String password2 = et_confirmpswd.getText().toString();
                gender = checkButton(v, radioGroupGender);
                validateFields(firstName, lastName, mail, phoneNumber, username, password, password2);

                if (!empty) {
                    if (password.equals(password2)) {
//                        authUser(mail, password);
                        toastMessage("Register Successul");
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        toastMessage("Passwords must be the same!");

                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == Utils.CAMERA) {
                openCameraResult(data);
            } else if (requestCode == GALLERY) {
                openGalleryResult(data);
            }
        }

    }

    private void validateFields(String firstName, String lastName, String mail, String phoneNumber, String username,
                                String password, String password2) {
        if (firstName.trim().equalsIgnoreCase("")) {
            et_firstName.setError("This field can not be blank");
            empty = true;
        }
        if (lastName.trim().equalsIgnoreCase("")) {
            et_lastName.setError("This field can not be blank");
            empty = true;
        }
        if (mail.trim().equalsIgnoreCase("")) {
            et_mail.setError("This field can not be blank");
            empty = true;
        } else {

            if (!mail.contains("@") ||
                    !mail.contains(".com")) {
                et_mail.setError("Not valid mail");
                empty = true;
            }
        }
        if (phoneNumber.trim().equalsIgnoreCase("")) {
            et_phone.setError("This field can not be blank");
            empty = true;
        }
        if (username.trim().equalsIgnoreCase("")) {
            et_username.setError("This field can not be blank");
            empty = true;
        }
        if (password.trim().equalsIgnoreCase("")) {
            et_password.setError("This field can not be blank");
            empty = true;
        } else {
            boolean isDigit = false;
            boolean isCapital = false;
            boolean isLower = false;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    isDigit = true;
                } else if (Character.isUpperCase(c)) {
                    isCapital = true;
                } else if (Character.isLowerCase(c)) {
                    isLower = true;
                }
            }
            if (password.length() < 6 || !isDigit || !isCapital || !isLower) {
                et_password.setError("Password must contain 6 characters or more and at least one digit and one big letter");
                empty = true;
            }
        }
        if (password2.trim().equalsIgnoreCase("")) {
            et_confirmpswd.setError("This field can not be blank");
            empty = true;
        } else {
            boolean isDigit = false;
            boolean isCapital = false;
            boolean isLower = false;
            for (char c : password2.toCharArray()) {
                if (Character.isDigit(c)) {
                    isDigit = true;
                } else if (Character.isUpperCase(c)) {
                    isCapital = true;
                } else if (Character.isLowerCase(c)) {
                    isLower = true;
                }
            }
            if (password2.length() < 6 || !isDigit || !isCapital || !isLower) {
                et_confirmpswd.setError("Password must contain 6 characters or more and at least one digit and one big letter");
                empty = true;
            }
        }
        RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            radioFemale.setError("You need to select one field");
            radioMale.setError("You need to select one field");
            empty = true;
        } else {
            radioFemale.setError(null);
            radioMale.setError(null);
        }

    }

    private String checkButton(View v, RadioGroup radioGroup) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        try {
            String textButton = radioButton.getText().toString();
            return textButton;
        } catch (Exception e) {
            return "";
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void OpenImages() {

        final CharSequence[] option = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Select Action");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (option[which].equals("Camera")) {
                    CameraIntent();
                }
                if (option[which].equals("Gallery")) {
                    GalleryIntent();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void CameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Utils.CAMERA);
    }

    private void GalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY);
    }

    public void openGalleryResult(Intent data) {

        Bitmap bitmap = null;

        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(SignupActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImage.setImageBitmap(bitmap);
    }

    public void openCameraResult(Intent data) {

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File paths = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        Toast.makeText(SignupActivity.this, "Path -> " + paths.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        Log.d("tag", "File Path -> " + paths.getName());

        try {
            FileOutputStream fos = new FileOutputStream(paths);
            paths.createNewFile();

            if (!paths.exists()) {
                paths.mkdir();
            }

            fos.write(bytes.toByteArray());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        profileImage.setImageBitmap(bitmap);
    }

    private void authUser(final String email, final String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //authenticate user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            toastMessage("Please enter valid credentials");
                        } else {
                            toastMessage("Registered Successful");
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;

    }

    public void requestPermission() {

        ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_CODE:

                if (grantResults.length > 0) {

                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameras = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storage && cameras) {

                        Toast.makeText(SignupActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(SignupActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                showMsg("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
        }

    }

    private void showMsg(String s, DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(SignupActivity.this)
                .setMessage(s)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}