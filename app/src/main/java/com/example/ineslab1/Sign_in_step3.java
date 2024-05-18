package com.example.ineslab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sign_in_step3 extends AppCompatActivity {
    Button finish;
    ProgressBar progressBar;
    TextView textView;
    CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_step3);

        textView = findViewById(R.id.textView);
        YoYo.with(Techniques.Wobble).duration(3000).repeat(0).playOn(textView);

        progressBar = findViewById(R.id.progress_bar);
        finish = findViewById(R.id.finish_button);
        cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .setup(Sign_in_step3.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardForm.validate();
                String card_name = cardForm.getCardholderName().toString();
                String card_number = cardForm.getCardNumber().toString();
                String expiry_date = cardForm.getExpirationDateEditText().toString();
                String Cvv = cardForm.getCvv().toString();
                if (cardForm.isValid()) {
                    RegisterUserCardDetails(card_name, card_number, expiry_date, Cvv);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(Sign_in_step3.this, "Please insert card details", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void RegisterUserCardDetails(String cardName, String cardNumber, String expiryDate, String cvv) {
        String id = getIntent().getExtras().getString("id");
        if(id !=null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String card_id = databaseReference.push().getKey();
            UserCardDetails userCardDetails = new UserCardDetails(cardName, cardNumber, expiryDate, cvv );
            FirebaseDatabase.getInstance().getReference().child("user").
                    child(id).child("Card details").child(card_id)
                    .setValue(userCardDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Sign_in_step3.this, "CARD DETAILS UPLOADED", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
            Intent intent = new Intent(Sign_in_step3.this, Login.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(Sign_in_step3.this, "ERROR OF RETRIEVING THE USER ID", Toast.LENGTH_SHORT).show();

        }
    }
}