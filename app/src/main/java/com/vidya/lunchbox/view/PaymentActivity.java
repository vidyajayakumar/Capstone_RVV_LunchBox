package com.vidya.lunchbox.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.model.Order;
import com.vidya.lunchbox.utils.NotificationUtils;
import com.vidya.lunchbox.utils.OrderStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    MaterialButton done;
    CardForm cardForm;
    private ArrayList<Cart> cartArrayList;
    private SessionManager session;
    private NotificationUtils mNotificationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        done = findViewById(R.id.btnDone);
        session = new SessionManager(getApplicationContext());
        cartArrayList = new ArrayList<>();
        getBundleData();
//        sendNotification("Sample Vidya");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addOrderDetails(getOrder());
//                sendNotification("Heloo");
                if (cardForm.isValid()) {
                } else {

                    Toast.makeText(PaymentActivity.this, "Please fill all required Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_OPTIONAL)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(PaymentActivity.this);
    }

    private void clearCart() {
        ItemsActivity.cs.clearCart(PaymentActivity.this);
        Intent i = new Intent(PaymentActivity.this, CategoryListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(PaymentActivity.this, "Payment Done Succesfully", Toast.LENGTH_SHORT).show();
    }

    private Order getOrder() {
        Order order = new Order();
        order.setUserId(session.getUserData().getEmail());
        order.setDateTime(Calendar.getInstance().getTimeInMillis());
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setProductIds(getCartProductIds());
        return order;
    }

    private String getCartProductIds() {
        String ids = "";
        for (int i = 0; i < cartArrayList.size(); i++) {
            Cart cart = cartArrayList.get(i);
            ids = !ids.isEmpty() ? ids.concat(",").concat(cart.getProductid()).concat(":").concat(String.valueOf(cart.getQuantity()))
                    : ids.concat(cart.getProductid()).concat(":").concat(String.valueOf(cart.getQuantity()));
        }
        return ids;
    }

    private void addOrderDetails(Order order) {
        DatabaseReference presentersReference = FirebaseDatabase.getInstance().getReference("orders");
        final String presenterId = UUID.randomUUID().toString();
        order.setOrderId(presenterId);
        presentersReference.child(presenterId).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
//                    sendNotification("Sample Vidya");
                    clearCart();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mNotificationUtils = new NotificationUtils(PaymentActivity.this);
                        Notification.Builder nb = mNotificationUtils.
                                getAndroidChannelNotification("You Ordered Successfully", "Your Order id is: " + presenterId);

                        mNotificationUtils.getManager().notify(101, nb.build());
                    }
                    Log.e("TAG", "Order added : " + presenterId);
                } else {
                    Log.e("TAG", "Failed to add", databaseError.toException());
                }
            }
        });
    }

    public void sendNotification(String orderId) {

        Intent intent = new Intent(PaymentActivity.this, OrderListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(PaymentActivity.this, 0, intent, 0);
        startActivity(intent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "LUNCHBOX")
                .setOngoing(true)
                .setContentTitle("Some Message")
                .setContentText("You've received new messages!")
                .setSmallIcon(R.drawable.appicon)
                .setContentIntent(pendingIntent).build();
        notificationManager.notify(123, notification);

    }


    private void getBundleData() {
        if (getIntent().getExtras() != null) {
            cartArrayList = getIntent().getExtras().getParcelableArrayList("items");
        }
    }
}