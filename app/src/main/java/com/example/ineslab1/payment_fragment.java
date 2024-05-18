package com.example.ineslab1;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class payment_fragment extends Fragment  {
    RecyclerView recyclerView_basket ;
    Button pay;
    Checkout_cart_RV_adapter adapter;
    TextView final_amount;
    Spinner spinner_address, spinner_card;
    ArrayList<Basket_model> basket = new ArrayList<>();
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    ImageView add_address, add_card;
    boolean isCardVerified =false;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<UserDeliveryAddress> spinnerArray_address = new ArrayList<>();
        ArrayList<String> address_list = new ArrayList<>();
        address_list.add("Choose a Delivery address");

        ArrayList<UserCardDetails> spinnerArray_card = new ArrayList<>();
        ArrayList<String> card_list = new ArrayList<>();
        card_list.add("Choose a payment method");


        final_amount = view.findViewById(R.id.basket_final_amount);
        add_address = view.findViewById(R.id.add_checkout_address);
        add_card = view.findViewById(R.id.add_checkout_card);
        progressBar = view.findViewById(R.id.progress_bar);
        pay = view.findViewById(R.id.pay);
        recyclerView_basket = view.findViewById(R.id.basket_recyclerView);
        recyclerView_basket.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle b = getArguments();
        String amount_to_pay = b.getString("total");
        final_amount.setText(amount_to_pay);

        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        FirebaseRecyclerOptions<Basket_model> options = new FirebaseRecyclerOptions.Builder<Basket_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("user").child(UserId)
                        .child("basket"), Basket_model.class).build();
        adapter = new Checkout_cart_RV_adapter(options);
        recyclerView_basket.setAdapter(adapter);
        // Retrieving all addresses on current user account
        spinner_address = view.findViewById(R.id.spinner_address);
        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Delivery address")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            UserDeliveryAddress model = dataSnapshot.getValue(UserDeliveryAddress.class);

                                spinnerArray_address.add(model);

                        }
                        for(int i = 0; i< spinnerArray_address.size(); i ++){
                            String flat_num = spinnerArray_address.get(i).getFlatNumber();
                            String street_name = spinnerArray_address.get(i).getStreetName();
                            String postcode = spinnerArray_address.get(i).getPostcode();
                            String city = spinnerArray_address.get(i).getCity();
                            String full_address = flat_num + " " + street_name + ",  " + postcode + ",  " + city;
                            address_list.add(full_address);
                        }

                        ArrayAdapter<String> spinner_quantityArrayAdapter = new ArrayAdapter<>
                                (getContext(), android.R.layout.simple_spinner_dropdown_item, address_list);
                        spinner_quantityArrayAdapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        spinner_address.setAdapter(spinner_quantityArrayAdapter);
                        spinner_address.setSelection(0);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Retrieving all card on current user account
        spinner_card = view.findViewById(R.id.spinner_card);
        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Card details")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            UserCardDetails model = dataSnapshot.getValue(UserCardDetails.class);

                            spinnerArray_card.add(model);

                        }
                        for(int i = 0; i< spinnerArray_card.size(); i ++){
                            String card_name = spinnerArray_card.get(i).getCardName();
                            String card_number = spinnerArray_card.get(i).getCardNumber();
                            String full_card = card_name + ",  " + "Card Ending: "+ card_number ;
                            card_list.add(full_card);
                        }

                        ArrayAdapter<String> spinner_cardArrayAdapter = new ArrayAdapter<>
                                (getContext(), android.R.layout.simple_spinner_dropdown_item, card_list);              // Selected item will look like a spinner set from XML
                        spinner_cardArrayAdapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        spinner_card.setAdapter(spinner_cardArrayAdapter);
                        spinner_card.setSelection(0);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        spinner_card.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected_card = (String) spinner_card.getSelectedItem();
                if(selected_card != "Choose a payment method"){
                    String cardHolderName =selected_card.substring(0,selected_card.length()-32);
                    String cardNumber =selected_card.substring(selected_card.length()-16,selected_card.length());
                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    View dialog_view = MainActivity.getInstance().getInflaterConfirmCardForm();
                    CardForm cardForm = dialog_view.findViewById(R.id.card_form);
                    cardForm.cardRequired(true)
                            .expirationRequired(true)
                            .cvvRequired(true)
                            .cardholderName(CardForm.FIELD_REQUIRED)
                            .setup((AppCompatActivity) dialog_view.getContext());
                    cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


                    cardForm.getCardholderNameEditText().setText(cardHolderName);
                    cardForm.getCardEditText().setText(cardNumber);
                    cardForm.validate();

                    b.setView(dialog_view);
                    AlertDialog dialog = b.create();

                    dialog_view.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (cardForm.isValid()) {
                                Toast.makeText(dialog.getContext(), "Details correct", Toast.LENGTH_SHORT).show();
                                isCardVerified = true;
                                dialog.dismiss();

                            } else {
                                Toast.makeText(dialog.getContext(), "Details incorrect", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    dialog_view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    dialog.show();
                }else{
                    Toast.makeText(getContext(),"SELECT A PAYMENT METHOD", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new addAddress());

            }
        });
        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new addCard());
            }
        });



        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_selected = (String) spinner_card.getSelectedItem();
                String address_selected = (String) spinner_address.getSelectedItem();

                if(card_selected != "Choose a payment method" && address_selected != "Choose a Delivery address" ){
                    if(isCardVerified){
                        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(UserId).child("basket");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Basket_model basketModel = dataSnapshot.getValue(Basket_model.class);
                                    basket.add(basketModel);
                                    progressBar.setVisibility(View.VISIBLE);
                                    String id = databaseReference.push().getKey();
                                    for (Basket_model model : basket) {
                                        String prod_name = model.getProduct_name();
                                        String quantity_purchase = model.getQuantity();
                                        if (prod_name.contains("mousse cake")) {
                                            FirebaseDatabase.getInstance().getReference("Products").child("Mousse cakes")
                                                    .child(prod_name).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String stock_quantity = snapshot.getValue(String.class);
                                                            int update_stock_quantity = Integer.parseInt(stock_quantity) - Integer.parseInt(quantity_purchase);
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("quantity", String.valueOf(update_stock_quantity));
                                                            FirebaseDatabase.getInstance().getReference("Products").child("Mousse cakes")
                                                                    .child(prod_name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATED", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATE ERROR", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                        else if (prod_name.contains("mini")) {
                                            FirebaseDatabase.getInstance().getReference("Products").child("Mini mousse cake")
                                                    .child(prod_name).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String stock_quantity = snapshot.getValue(String.class);
                                                            int update_stock_quantity = Integer.parseInt(stock_quantity) - Integer.parseInt(quantity_purchase);
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("quantity", String.valueOf(update_stock_quantity));
                                                            FirebaseDatabase.getInstance().getReference("Products").child("Mini mousse cake")
                                                                    .child(prod_name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATED", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATE ERROR", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                        else if (prod_name.contains("giftbox")) {
                                            FirebaseDatabase.getInstance().getReference("Products").child("Giftbox")
                                                    .child(prod_name).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String stock_quantity = snapshot.getValue(String.class);
                                                            int update_stock_quantity = Integer.parseInt(stock_quantity) - Integer.parseInt(quantity_purchase);
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("quantity", String.valueOf(update_stock_quantity));
                                                            FirebaseDatabase.getInstance().getReference("Products").child("Giftbox")
                                                                    .child(prod_name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATED", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATE ERROR", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                        else if (prod_name.contains("craquelin")) {
                                            FirebaseDatabase.getInstance().getReference("Products").child("Choux au craquelin")
                                                    .child(prod_name).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String stock_quantity = snapshot.getValue(String.class);
                                                            int update_stock_quantity = Integer.parseInt(stock_quantity) - Integer.parseInt(quantity_purchase);
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("quantity", String.valueOf(update_stock_quantity));
                                                            FirebaseDatabase.getInstance().getReference("Products").child("Choux au craquelin")
                                                                    .child(prod_name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATED", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATE ERROR", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                        else if (prod_name.contains("tray")) {
                                            FirebaseDatabase.getInstance().getReference("Products").child("Macarons tray")
                                                    .child(prod_name).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String stock_quantity = snapshot.getValue(String.class);
                                                            int update_stock_quantity = Integer.parseInt(stock_quantity) - Integer.parseInt(quantity_purchase);
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("quantity", String.valueOf(update_stock_quantity));
                                                            FirebaseDatabase.getInstance().getReference("Products").child("Macarons tray")
                                                                    .child(prod_name).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATED", Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getContext(),"STOCK QUANTITY UPDATE ERROR", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }
                                    /**
                                     // put basket in my order node
                                     String id_prod = databaseReference.push().getKey();
                                     FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("My orders").child(id).setValue(basket).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                    //Toast.makeText(MainActivity.this, "ITEM ADDED TO THE CART", Toast.LENGTH_SHORT).show();

                                    }

                                    }
                                    });**/
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket").removeValue();
                        MainActivity x = (MainActivity) getActivity();
                        x.itemPurchased();
                    }else{
                        //if card is still not verified, dialog box to pop up again
                        String selected_card = (String) spinner_card.getSelectedItem();
                        if(selected_card != "Choose a payment method"){
                            String cardHolderName =selected_card.substring(0,selected_card.length()-32);
                            String cardNumber =selected_card.substring(selected_card.length()-16,selected_card.length());
                            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                            View dialog_view = MainActivity.getInstance().getInflaterConfirmCardForm();
                            CardForm cardForm = dialog_view.findViewById(R.id.card_form);
                            cardForm.cardRequired(true)
                                    .expirationRequired(true)
                                    .cvvRequired(true)
                                    .cardholderName(CardForm.FIELD_REQUIRED)
                                    .setup((AppCompatActivity) dialog_view.getContext());
                            cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


                            cardForm.getCardholderNameEditText().setText(cardHolderName);
                            cardForm.getCardEditText().setText(cardNumber);
                            cardForm.validate();

                            b.setView(dialog_view);
                            AlertDialog dialog = b.create();

                            dialog_view.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (cardForm.isValid()) {
                                        Toast.makeText(dialog.getContext(), "Details correct", Toast.LENGTH_SHORT).show();
                                        isCardVerified = true;
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(dialog.getContext(), "Details incorrect", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            dialog_view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            if (dialog.getWindow() != null) {
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            }
                            dialog.show();
                        }else{
                            Toast.makeText(getContext(),"SELECT A PAYMENT METHOD", Toast.LENGTH_SHORT).show();

                        }
                    }

                }else{

                    Toast.makeText(getContext(), "CHOOSE DELIVER ADDRESS AND PAYMENT METHOD", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_fragment, container, false);
        return view;
    }

}