package com.example.ineslab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Cart_Fragment extends Fragment {

    RecyclerView recyclerView_basket ;
    Button add_product;
    Button checkout;
    Cart_RV_adapter adapter;
    TextView final_amount;
    ImageView basket_empty;
    TextView empty_title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final_amount = view.findViewById(R.id.basket_final_amount);
        basket_empty = view.findViewById(R.id.basket_image);
        empty_title = view.findViewById(R.id.empty_title);
        add_product = view.findViewById(R.id.add_product);
        checkout = view.findViewById(R.id.checkout);
        recyclerView_basket = view.findViewById(R.id.basket_recyclerView);
        recyclerView_basket.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        FirebaseRecyclerOptions<Basket_model> options = new FirebaseRecyclerOptions.Builder<Basket_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("user").child(UserId)
                        .child("basket"),Basket_model.class).build();
        adapter = new Cart_RV_adapter(options);
        recyclerView_basket.setAdapter(adapter);
        adapter.onDataChanged();


        // check if basket is empty- if so disable the check out button
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference reference = database.getReference("user").child(UserId).child("basket");

         // Create a query to check if the node exists
         Query query = reference.limitToFirst(1);

         query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    checkout.setEnabled(true); // node exists
                    empty_title.setVisibility(View.GONE);
                    basket_empty.setVisibility(View.GONE);
                }
                else {
                    checkout.setEnabled(false); // node does not exist
                    empty_title.setVisibility(View.VISIBLE);
                    basket_empty.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Flash).duration(3000).repeat(2).playOn(add_product);
                    add_product.requestFocus();
                }
            }
        };
       });


        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Home_Fragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_wrapper,fragment).addToBackStack(null).commit();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total = final_amount.getText().toString();
                MainActivity m = (MainActivity) getActivity();
                m.goPay(new payment_fragment(),total);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_, container, false);
        return view;
    }

}