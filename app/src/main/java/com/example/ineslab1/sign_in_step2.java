package com.example.ineslab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_in_step2 extends AppCompatActivity {
    Button next_button2;
    TextInputLayout layoutFlatNumber,layoutPostcode,layoutStreetName,layoutCity;
    TextInputEditText editTextFlatNumber,editTextPostcode,editTextStreetName,editTextCity;
    ProgressBar progressBar;
    boolean isFlatNumberOk = false;
    boolean isPostcodeOk = false;
    boolean isStreetNameOk = false;
    boolean isCityOk = false;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_step2);

        textView = findViewById(R.id.textView);
        YoYo.with(Techniques.Wobble).duration(3000).repeat(0).playOn(textView);

        layoutFlatNumber = findViewById(R.id.layoutFlatNumber);
        layoutPostcode = findViewById(R.id.layoutPostcode);
        layoutStreetName = findViewById(R.id.new_password);
        layoutCity = findViewById(R.id.layoutConfirmPwd);

        editTextFlatNumber = findViewById(R.id.editTextFlatNumber);
        editTextPostcode = findViewById(R.id.editTextPostcode);
        editTextStreetName = findViewById(R.id.editTextNewPassword);
        editTextCity = findViewById(R.id.editTextConfirmPwd);

        next_button2 = findViewById(R.id.next_button2);
        progressBar = findViewById(R.id.progress_bar);

        editTextFlatNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String f_number = s.toString();
                if(f_number.length()< 1){
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
                    layoutPostcode.setError("Postcode should be not longer than 7 characters");
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
                    layoutPostcode.setError("Postcode should be not longer than 7 characters");
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
        next_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flat_number = editTextFlatNumber.getText().toString();
                String post_code = editTextPostcode.getText().toString();
                String street = editTextStreetName.getText().toString();
                String city  =editTextCity.getText().toString();

                if(!flat_number.equals("") && !post_code.equals("") && !street.equals("") && !city.equals("")){
                    if(isFlatNumberOk && isPostcodeOk && isStreetNameOk && isCityOk ){
                        RegisterUserDeliveryAddress(flat_number, post_code, street, city);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(sign_in_step2.this,"Details incorrect", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void RegisterUserDeliveryAddress(String flatNumber, String postCode, String street, String city) {

        String id = getIntent().getExtras().getString("id");
        if(id !=null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String address_id = databaseReference.push().getKey();
            UserDeliveryAddress userDeliveryAddress = new UserDeliveryAddress(flatNumber, postCode, street, city);
            FirebaseDatabase.getInstance().getReference().child("user").
                    child(id).child("Delivery address").child(address_id).setValue(userDeliveryAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(sign_in_step2.this, "DELIVERY ADDRESS UPLOADED", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            //intent to the next to the final step
                            Intent intent = new Intent(sign_in_step2.this, Sign_in_step3.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                    });
        }
        else{
            Toast.makeText(sign_in_step2.this, "ERROR OF RETRIEVING THE USER ID", Toast.LENGTH_SHORT).show();

        }

    }
}