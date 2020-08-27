package com.vidya.lunchbox.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.model.Category;
import com.vidya.lunchbox.model.CategoryNew;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    public static String getDateTime(long dateTime) {
        String strDateTime = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm");
        Date d = new Date(dateTime);
        strDateTime = sdf.format(d);
        return strDateTime;
    }

    public static ArrayList<String> getArrFromString(String val){
        ArrayList<String> arrStr;
        String[] fp = val.split(",");
        arrStr = new ArrayList<String>(Arrays.asList(fp));
        return arrStr;
    }

}