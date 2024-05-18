package com.example.ineslab1;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Profile_Fragment extends Fragment {

    TextView setting, my_details, my_address, my_payment, logout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setting = view.findViewById(R.id.setting_title);
        my_details = view.findViewById(R.id.myDetails_title);
        my_address = view.findViewById(R.id.myAddress_title);
        my_payment = view.findViewById(R.id.myPayment_title);
        logout = view.findViewById(R.id.logout_title);
        MainActivity m = (MainActivity) getActivity();
        m.ChangeProfileName();


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new UpdateUserDetails_fragment());

            }
        });
        my_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new delivery_addresses_fragment());


            }
        });
        my_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new cards_fragment());

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext(),
                        androidx.constraintlayout.widget.R.style.Base_Theme_AppCompat_Dialog_Alert);

                builder.setTitle("Are you sure you want to log out?");
                //builder.setMessage("Deleted Items cannot be Undo.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        MainActivity m = (MainActivity) getActivity();
                        m.Sign_out();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        return view;
    }
}