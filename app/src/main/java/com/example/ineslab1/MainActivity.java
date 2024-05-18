package com.example.ineslab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ineslab1.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private static MainActivity instance;
    ActivityMainBinding binding;
    FrameLayout tool_bar;
    TextView name;

    TextView price, notification_number,basket_final_amount;
    Spinner quantity_amount,size_amount;
    String prod_image;
    DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    View notificationIndicator;
    float total = 0;
    boolean product_updated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        instance = this;

        tool_bar = findViewById(R.id.include);
        ShowBasketNotification();

        replaceFragment(new Home_Fragment());   // Set Home fragment as a default when app opens

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int icon = item.getItemId();
            if(icon == R.id.home){
                replaceFragment(new Home_Fragment());
                tool_bar.setVisibility(View.VISIBLE);

            }

            else if(icon == R.id.cart){
                replaceFragment(new Cart_Fragment());
                tool_bar.setVisibility(View.VISIBLE);
                calculateTotalBasketPrice();

            }
            else if(icon == R.id.profile){
                replaceFragment(new Profile_Fragment());
                tool_bar.setVisibility(View.VISIBLE);

            }
            return true;
        });
    }
    public static MainActivity getInstance() {
        return instance;
    }

    // Replace fragment function
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_wrapper,fragment);
        fragmentTransaction.commit();
    }
    public void changeVisibility(){
        tool_bar = findViewById(R.id.include);
        tool_bar.setVisibility(View.GONE);

    }

    public void InsertProduct(String image) {

        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.title);
        price = findViewById(R.id.select_result);
        size_amount = findViewById(R.id.spinner_size);
        quantity_amount = findViewById(R.id.spinner_quantity);
        prod_image = image;
        String product_name = name.getText().toString();
        String total_price = price.getText().toString();
        String size= size_amount.getSelectedItem().toString();
        String quantity = String.valueOf(quantity_amount.getSelectedItem());
        String id = databaseReference.push().getKey();

        Basket_model basket = new Basket_model(prod_image,product_name,quantity,size,total_price);

        databaseReference.child("user").child(UserId).child("basket").child(id).setValue(basket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "ITEM ADDED TO THE CART", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void calculateTotalBasketPrice(){
        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        FirebaseDatabase.getInstance().getReference("user").child(UserId).child("basket").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String item_price = dataSnapshot.child("total_price").getValue(String.class);
                    total += Float.parseFloat(item_price);
                }
                basket_final_amount = findViewById(R.id.basket_final_amount);
                basket_final_amount.setText(String.valueOf(String.format("%.2f",total)));
                total =0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ShowBasketNotification(){
        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        FirebaseDatabase.getInstance().getReference("user").child(UserId).child("basket").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total_quantity = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String item_quantity = dataSnapshot.child("quantity").getValue(String.class);
                    total_quantity += Integer.parseInt(item_quantity);
                }

                bottomNavigationView = findViewById(R.id.bottomNavigationView);
                BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.cart);
                notificationIndicator = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_notification,bottomNavigationView,false);
                itemView.addView(notificationIndicator);
                notification_number = findViewById(R.id.notification_number);
                if(total_quantity> 0){
                    notification_number.setText(String.valueOf(String.format("%.0f",total_quantity)));
                    notificationIndicator.setVisibility(View.VISIBLE);
                    notification_number.setVisibility(View.VISIBLE);
                }
                else{
                    notificationIndicator.setVisibility(View.GONE);
                    notification_number.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public  void Update_Notification(String selected_quantity){

        int q = Integer.parseInt(String.valueOf(notification_number.getText()));
        int update_q = q + Integer.parseInt(selected_quantity);
        notificationIndicator.setVisibility(View.VISIBLE);
        notification_number.setVisibility(View.VISIBLE);
        notification_number.setText(String.valueOf(update_q));
    }
    public  void UpdatePlus(){

        int q = Integer.parseInt(String.valueOf(notification_number.getText()));
        int update_q = q + 1;
        notification_number.setText(String.valueOf(update_q));
    }
    public  void UpdateMinus(){
        int q = Integer.parseInt(String.valueOf(notification_number.getText()));
        int update_q = q - 1;
        notification_number.setText(String.valueOf(update_q));
    }
    public  void UpdateOnDeleteItem(String sub_quantity){

        int q = Integer.parseInt(String.valueOf(notification_number.getText()));
        int update_q = q - Integer.parseInt(sub_quantity);
        if(update_q>0){
            notificationIndicator.setVisibility(View.VISIBLE);
            notification_number.setVisibility(View.VISIBLE);
            notification_number.setText(String.valueOf(update_q));
        }else{
            notificationIndicator.setVisibility(View.GONE);
            notification_number.setVisibility(View.GONE);
            notification_number.setText("0");
        }


    }
    public View getInflaterChangePassword() {
        return  getLayoutInflater().inflate(R.layout.dialog_update_password,null);
    }

    public View getInflater() {
        return  getLayoutInflater().inflate(R.layout.dialog_update_address,null);
    }
    public View getInflaterCardForm() {
        return  getLayoutInflater().inflate(R.layout.dialog_update_card,null);
    }
    public View getInflaterConfirmCardForm() {
        return  getLayoutInflater().inflate(R.layout.dialog_confirm_card,null);
    }

    public void ChangeProfileName(){
        TextView prof_name;
        prof_name = findViewById(R.id.welcomeName);
        DatabaseReference databaseReference;

        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(UserId).child("User Details")
                .child("first_name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String firstName = snapshot.getValue(String.class);
                    prof_name.setText(firstName.toUpperCase(Locale.ROOT));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goPay(Fragment fragment, String extra) {
        Bundle b = new Bundle();
        b.putString("total",extra);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(b);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_wrapper,fragment);
        fragmentTransaction.commit();
    }

    public void Sign_out() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void itemPurchased() {
        Intent intent = new Intent(MainActivity.this, Order_placed.class);
        startActivity(intent);
    }

/**
    public boolean CheckProductInCart() {
        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.title);
        price = findViewById(R.id.select_result);
        size_amount = findViewById(R.id.spinner_size);
        quantity_amount = findViewById(R.id.spinner_quantity);
        String product_name = name.getText().toString();
        String total_price = price.getText().toString();
        String size= size_amount.getSelectedItem().toString();
        String quantity = String.valueOf(quantity_amount.getSelectedItem());
        String id = databaseReference.push().getKey();
        databaseReference.child("user").child(UserId).child("basket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Basket_model model = dataSnapshot.getValue(Basket_model.class);
                    String database_id = dataSnapshot.getKey();
                    if (model.getProduct_name().toString().equals(product_name)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("quantity", quantity.toString());
                        databaseReference.child("user").child(UserId).child("basket").child(database_id)
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        product_updated = true;
                                        //String prev_quantity = model.getQuantity().toString();
                                        //Update_Notification(prev_quantity,quantity);

                                    }
                                });
                        product_updated = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return product_updated;
    }**/
}


