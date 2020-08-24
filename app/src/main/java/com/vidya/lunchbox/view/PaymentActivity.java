package com.vidya.lunchbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.button.MaterialButton;
import com.vidya.lunchbox.R;

public class PaymentActivity extends AppCompatActivity {

    MaterialButton done;
    CardForm cardForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        done = findViewById(R.id.btnDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cardForm.isValid()) {
                    //clear cart after payment
                    MainActivity.cs.clearCart(PaymentActivity.this);

                    Intent i = new Intent(PaymentActivity.this, CategoryListActivity.class);
                    // set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(PaymentActivity.this, "Payment Done Succesfully", Toast.LENGTH_SHORT).show();
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
}
