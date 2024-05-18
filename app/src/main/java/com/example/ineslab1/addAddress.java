package com.example.ineslab1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addAddress extends Fragment {
    Button done;
    ImageView back;
    TextInputLayout layoutFlatNumber,layoutPostcode,layoutStreetName,layoutCity;
    TextInputEditText editTextFlatNumber,editTextPostcode,editTextStreetName,editTextCity;
    boolean isFlatNumberOk = false;
    boolean isPostcodeOk = false;
    boolean isStreetNameOk = false;
    boolean isCityOk = false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutFlatNumber = view.findViewById(R.id.layoutFlatNumber);
        layoutPostcode = view.findViewById(R.id.layoutPostcode);
        layoutStreetName = view.findViewById(R.id.new_password);
        layoutCity = view.findViewById(R.id.layoutConfirmPwd);

        editTextFlatNumber = view.findViewById(R.id.editTextFlatNumber);
        editTextPostcode = view.findViewById(R.id.editTextPostcode);
        editTextStreetName = view.findViewById(R.id.editTextNewPassword);
        editTextCity = view.findViewById(R.id.editTextConfirmPwd);
        done = view.findViewById(R.id.done_button);
        back = view.findViewById(R.id.back);



        editTextFlatNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String f_number = s.toString();
                if(f_number.length()< 3){
                    layoutFlatNumber.setError("Enter valid flat number");
                }else{
                    layoutFlatNumber.setHelperText("Flat number accepted");
                    isFlatNumberOk = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPostcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String postcode = s.toString();
                // Create validation for postcode
                String postCodeRegex = "^[A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}$";
                Matcher postcodeMatcher;
                Pattern postcodePattern = Pattern.compile(postCodeRegex);
                postcodeMatcher = postcodePattern.matcher(postcode);
                if (postcode.length()> 7){
                    layoutPostcode.setError("Postcode should be not longer than characters");
                } else if (!postcodeMatcher.find()) {
                    layoutPostcode.setError("Postcode invalid");

                }else{
                    layoutPostcode.setHelperText("Postcode accepted");
                    isPostcodeOk = true;
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                String pCode = s.toString();
                // Create validation for postcode
                String postCodeRegex = "^[A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}$";
                Matcher postcodeMatcher;
                Pattern postcodePattern = Pattern.compile(postCodeRegex);
                postcodeMatcher = postcodePattern.matcher(pCode);
                if (pCode.length()> 7){
                    layoutPostcode.setError("Postcode should be not longer than characters");
                } else if (!postcodeMatcher.find()) {
                    layoutPostcode.setError("Postcode invalid");

                }else{
                    layoutPostcode.setHelperText("Postcode accepted");
                    isPostcodeOk = true;
                }

            }
        });
        editTextStreetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String streetName = s.toString();
                if(streetName.length() > 30){
                    layoutStreetName.setError("Street name cannot be longer then 30 characters");
                }
                else{
                    layoutStreetName.setHelperText("Street name accepted");
                    isStreetNameOk = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city = s.toString();
                if(city.length() > 30){
                    layoutCity.setError("City cannot be longer then 30 characters");
                }else{
                    layoutCity.setHelperText("City accepted");
                    isCityOk = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flat_number = editTextFlatNumber.getText().toString();
                String post_code = editTextPostcode.getText().toString();
                String street = editTextStreetName.getText().toString();
                String city  =editTextCity.getText().toString();

                if(!flat_number.equals("") && !post_code.equals("") && !street.equals("") && !city.equals("")){
                    if(isFlatNumberOk && isPostcodeOk && isStreetNameOk && isCityOk ){
                        RegisterUserDeliveryAddress(flat_number, post_code, street, city);

                    }
                }else{
                    Toast.makeText(getContext(),"Details incorrect", Toast.LENGTH_SHORT).show();

                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new delivery_addresses_fragment());;
            }
        });

    }
    private void RegisterUserDeliveryAddress(String flatNumber, String postCode, String street, String city) {

        FirebaseAuth a = FirebaseAuth.getInstance();
        FirebaseUser user = a.getCurrentUser();
        String UserId = a.getUid();
        if(UserId !=null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String address_id = databaseReference.push().getKey();
            UserDeliveryAddress userDeliveryAddress = new UserDeliveryAddress(flatNumber, postCode, street, city );
            FirebaseDatabase.getInstance().getReference().child("user").
                    child(UserId).child("Delivery address").child(address_id).setValue(userDeliveryAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "DELIVERY ADDRESS UPLOADED", Toast.LENGTH_SHORT).show();
                            //back to address list
                            MainActivity m = (MainActivity) getActivity();
                            m.replaceFragment(new delivery_addresses_fragment());;
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
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        return view;
    }
}
