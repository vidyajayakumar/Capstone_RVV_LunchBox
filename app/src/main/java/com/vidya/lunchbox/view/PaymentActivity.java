package com.vidya.lunchbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.model.Cart;
import com.vidya.lunchbox.model.Order;
import com.vidya.lunchbox.utils.OrderStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    MaterialButton done;
    CardForm cardForm;
    private ArrayList<Cart> cartArrayList;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        done = findViewById(R.id.btnDone);
        session = new SessionManager(getApplicationContext());
        cartArrayList = new ArrayList<>();
        getBundleData();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cardForm.isValid()) {
                    addOrderDetails(getOrder());
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
            ids = !ids.isEmpty() ? ids.concat(",").concat(cart.getProductid()) : ids.concat(cart.getProductid());
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
                    clearCart();
                    Log.e("TAG", "Order added : " + presenterId);
                } else {
                    Log.e("TAG", "Failed to add", databaseError.toException());
                }
            }
        });
    }

    private void getBundleData() {
        if (getIntent().getExtras() != null) {
            cartArrayList = getIntent().getExtras().getParcelableArrayList("items");
        }
    }
}