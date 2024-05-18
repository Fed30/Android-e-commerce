package com.example.ineslab1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


public class delivery_addresses_fragment extends Fragment {
    RecyclerView recyclerView_addresses ;
    Button add_address;
    Button done;
    addresses_RV_adapter adapter;
    ImageView no_address;
    TextView no_address_message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        done = view.findViewById(R.id.done);
        no_address = view.findViewById(R.id.address_image);
        no_address_message = view.findViewById(R.id.empty_title);
        done = view.findViewById(R.id.done);
        add_address = view.findViewById(R.id.add_address);
        recyclerView_addresses = view.findViewById(R.id.addresses_recyclerView);
        recyclerView_addresses.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtain current user id
        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();

        FirebaseRecyclerOptions<UserDeliveryAddress> options = new FirebaseRecyclerOptions.Builder<UserDeliveryAddress>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("user").child(UserId)
                        .child("Delivery address"),UserDeliveryAddress.class).build();
        adapter = new addresses_RV_adapter(options);
        recyclerView_addresses.setAdapter(adapter);
        adapter.onDataChanged();

        // check if basket is empty- if so disable the check out button
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user").child(UserId).child("Delivery address");

        // Create a query to check if the node exists
        Query query = reference.limitToFirst(1);

        query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        no_address.setVisibility(View.GONE);
                        no_address_message.setVisibility(View.GONE);
                    }
                    else {
                        no_address.setVisibility(View.VISIBLE);
                        no_address_message.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.Flash).duration(3000).repeat(2).playOn(add_address);
                    }
                }
            };
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new Profile_Fragment());
            }
        });
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new addAddress());;
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
        View view = inflater.inflate(R.layout.fragment_delivery_addresses, container, false);
        return view;
    }
}