package com.example.ineslab1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addCard extends Fragment {

    Button done;
    ImageView back;
    CardForm cardForm;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        done = view.findViewById(R.id.done);
        back = view.findViewById(R.id.back);
        cardForm = view.findViewById(R.id.card_form);
        cardForm.cardRequired(true);
        cardForm.expirationRequired(true);
        cardForm.cvvRequired(true);
        cardForm.cardholderName(CardForm.FIELD_REQUIRED);
        cardForm.setup(getActivity());
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardForm.validate();
                String card_name = cardForm.getCardholderName().toString();
                String card_number = cardForm.getCardNumber().toString();
                String expiry_date = cardForm.getExpirationDateEditText().toString();
                String Cvv = cardForm.getCvv().toString();

                if (cardForm.isValid()) {
                    RegisterUserCardDetails(card_name, card_number, expiry_date, Cvv);

                } else {
                    Toast.makeText(getContext(), "Please insert card details", Toast.LENGTH_SHORT).show();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new cards_fragment());
            }
        });

    }
    private void RegisterUserCardDetails(String cardName, String cardNumber, String expiryDate, String cvv) {
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        if(UserId !=null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String card_id = databaseReference.push().getKey();
            UserCardDetails userCardDetails = new UserCardDetails(cardName, cardNumber, expiryDate, cvv );
            FirebaseDatabase.getInstance().getReference().child("user").
                    child(UserId).child("Card details").child(card_id).setValue(userCardDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "CARD DETAILS UPLOADED", Toast.LENGTH_SHORT).show();

                            MainActivity m = (MainActivity) getActivity();
                            m.replaceFragment(new cards_fragment());
                        }
                    });
        }
        else{
            Toast.makeText(getContext(), "ERROR OF RETRIEVING THE USER ID", Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);
        return view;
    }
}