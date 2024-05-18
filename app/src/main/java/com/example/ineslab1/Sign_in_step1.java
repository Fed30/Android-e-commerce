package com.example.ineslab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sign_in_step1 extends AppCompatActivity {
    Button next_button1;
    ImageView back;

    ProgressBar progressBar;
    private static final String TAG = "Sign_in_step1";
    TextInputLayout layoutFirstName, layoutLastName,layoutEmail,layoutPassword,layoutConfirmPassword,layoutMobile;
    TextInputEditText editTextName, editTextLastName,editEmail, editTextPassword,editTConfirmPassword,editTextMobile;
    boolean isFirstnameOk = false;
    boolean isLastNameOk = false;
    boolean isEmailOk = false;
    boolean isPasswordOk = false;
    boolean isConfirmPasswordOk = false;
    boolean isMobileOk = false;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_step1);
        textView = findViewById(R.id.textView);
        YoYo.with(Techniques.Wobble).duration(3000).repeat(0).playOn(textView);

        layoutFirstName = findViewById(R.id.layoutName);
        layoutLastName = findViewById(R.id.layoutLastname);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);
        layoutMobile = findViewById(R.id.layourMobile);

        editTextName = findViewById(R.id.nameEditText);
        editTextLastName = findViewById(R.id.lastNameEditText);
        editEmail = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        editTConfirmPassword = findViewById(R.id.confirmPasswordEdittext);
        editTextMobile = findViewById(R.id.editTextMobile);

        next_button1 = findViewById(R.id.done_btn);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progress_bar);




        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if(name.length()<  0) {
                    layoutFirstName.setError("First name required");
                }else{
                    layoutFirstName.setHelperText("First name accepted");
                    isFirstnameOk = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String last_name = s.toString();
                if(last_name.length()<0){
                    layoutLastName.setError("Last name required");
                }else{
                    layoutLastName.setHelperText("Last name accepted");
                    isLastNameOk = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                // Create validation for mobile number
                String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
                Matcher emailMatcher;
                Pattern emailPattern = Pattern.compile(emailRegex);
                emailMatcher = emailPattern.matcher(email);
                if (s.length()>0){
                    if (!emailMatcher.find()) {
                        layoutEmail.setError("Email is not valid");

                    }else{
                        layoutEmail.setHelperText("Email accepted");
                        isEmailOk = true;
                    }
                }else{
                    layoutEmail.setHelperText("Email accepted");
                    isEmailOk = true;

                }


            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = s.toString();
                if(pwd.length() >= 8){
                    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher m = p.matcher(pwd);
                    boolean isPassword_containsSpecialChar = m.find();
                    if(isPassword_containsSpecialChar){
                        layoutPassword.setHelperText("Strong password");
                        layoutPassword.setError("");
                        isPasswordOk = true;
                    }
                    else{
                        layoutPassword.setHelperText("");
                        layoutPassword.setError("Weak password. Include minimum one special character");
                    }

                }else{
                    layoutPassword.setHelperText("Enter minimum 8 characters.");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirm_pwd = s.toString();
                if(confirm_pwd.length() >= 8){
                    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher m = p.matcher(confirm_pwd);
                    boolean isPassword_containsSpecialChar = m.find();
                    if(isPassword_containsSpecialChar){
                        layoutPassword.setHelperText("Strong password");
                        layoutPassword.setError("");
                        isConfirmPasswordOk = true;
                    }
                    else{
                        layoutPassword.setHelperText("");
                        layoutPassword.setError("Weak password. Include minimum one special character");
                        isConfirmPasswordOk = false;
                    }

                }else{
                    layoutPassword.setHelperText("Enter minimum 8 characters.");
                    isConfirmPasswordOk = false;

                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editTextPassword.getText().toString();
                String c_pwd = editTConfirmPassword.getText().toString();
                if (s.length()>0 && c_pwd.length()> 0){
                    if(!c_pwd.equals(password)){
                        layoutConfirmPassword.setError("Password not matching");
                        isConfirmPasswordOk = false;
                    }else{
                        layoutConfirmPassword.setHelperText("Password match");
                        isConfirmPasswordOk = true;
                    }
                }

            }
        });
        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString();
                // Create validation for mobile number
                String phone_numberRegex = "[1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9][1-9]";
                Matcher phoneMatcher;
                Pattern phonePattern = Pattern.compile(phone_numberRegex);
                phoneMatcher = phonePattern.matcher(phone);
                if (phone.length()> 10){
                    layoutMobile.setError("Mobile number should be 10 digits");
                } else if (!phoneMatcher.find()) {
                    layoutMobile.setError("Mobile number is not valid");

                }else{
                    layoutMobile.setHelperText("Mobile number accepted");
                    isMobileOk = true;
                }

            }
        });

        next_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = editTextName.getText().toString();
                String last_name = editTextLastName.getText().toString();
                String email = editEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String conf_password = editTConfirmPassword.getText().toString();
                String mobile = editTextMobile.getText().toString();

                if(!first_name.equals("") && !last_name.equals("") && !email.equals("") && !password.equals("")
                        && !conf_password.equals("") && !mobile.equals("")  ){
                    if(isFirstnameOk && isLastNameOk &&isEmailOk && isPasswordOk && isConfirmPasswordOk && isMobileOk  ){
                        RegisterUser(first_name, last_name, email, password, conf_password, mobile);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(Sign_in_step1.this,"Details incorrect", Toast.LENGTH_SHORT).show();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_in_step1.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void RegisterUser(String firstName, String lastName, String eMail, String pwd, String confirmPwd, String phone) {
        FirebaseAuth a = FirebaseAuth.getInstance();
        a.createUserWithEmailAndPassword(eMail,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                UserDetailModel userDetailModel = new UserDetailModel(firstName,lastName,eMail,phone);
                if(task.isSuccessful()){
                    FirebaseUser user = a.getCurrentUser();
                    String id = a.getUid();
                    Toast.makeText(Sign_in_step1.this,"Registration Step1 successful", Toast.LENGTH_SHORT).show();
                    // Save user details into Firebase realtime database
                    FirebaseDatabase.getInstance().getReference().child("user").child(id)
                            .child("User Details").setValue(userDetailModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Sign_in_step1.this, "USER DETAILS ADDED", Toast.LENGTH_SHORT).show();
                                    user.sendEmailVerification();
                                    Intent intent = new Intent(Sign_in_step1.this, sign_in_step2.class);
                                    intent.putExtra("id",a.getUid());
                                    startActivity(intent);
                                }
                            });
                }
                else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        layoutConfirmPassword.setError("The password entered is too weak. Use a mix of numbers, characters and special symbols");
                        layoutConfirmPassword.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        layoutEmail.setError("The email entered is invalid or already in use. ");
                        layoutEmail.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }catch (FirebaseAuthUserCollisionException e){
                        layoutEmail.setError("This email is already registered, please use another email. ");
                        layoutEmail.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Sign_in_step1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }

            }
        });


    }
}