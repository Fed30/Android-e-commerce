package com.example.ineslab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    TextView textView;

    TextInputLayout layoutEmailLogin,layoutPasswordLogin;
    TextInputEditText editTextEmailLogin, editTextPasswordLogin;
    Button login, sign_in;
    ProgressBar progressBar;
    TextView forgot_pwd;
    private FirebaseAuth check;
    private static final String TAG = "LoginActivity";
    boolean isEmailOk = false;
    boolean isPasswordOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
        YoYo.with(Techniques.Wobble).duration(3000).repeat(0).playOn(textView);
        layoutEmailLogin = findViewById(R.id.layoutEmailLogin);
        layoutPasswordLogin = findViewById(R.id.layoutPasswordLogin);

        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPassowrdLogin);

        progressBar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login_btn);
        sign_in = findViewById(R.id.signin_btn);
        forgot_pwd = findViewById(R.id.forgot_pwd);

        check = FirebaseAuth.getInstance();

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Sign_in_step1.class);
                startActivity(intent);
            }
        });
        editTextEmailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                // Create validation for email address
                String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
                Matcher emailMatcher;
                Pattern emailPattern = Pattern.compile(emailRegex);
                emailMatcher = emailPattern.matcher(email);
                if (s.length()>0){
                    if (!emailMatcher.find()) {
                        layoutEmailLogin.setError("Email is not valid");

                    }else{
                        layoutEmailLogin.setHelperText("Email accepted");
                        isEmailOk = true;
                    }
                }else{
                    layoutEmailLogin.setHelperText("Email accepted");
                    isEmailOk = true;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPasswordLogin.addTextChangedListener(new TextWatcher() {
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
                        layoutPasswordLogin.setHelperText("Strong password");
                        layoutPasswordLogin.setError("");
                        isPasswordOk = true;
                    }
                    else{
                        layoutPasswordLogin.setHelperText("");
                        layoutPasswordLogin.setError("Weak password. Include minimum one special character");
                    }

                }else{
                    layoutPasswordLogin.setHelperText("Enter minimum 8 characters.");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailLogin.getText().toString();
                String password = editTextPasswordLogin.getText().toString();

                if(!email.equals("") && !password.equals("")){
                    if(isEmailOk && isPasswordOk ){
                        CheckUserLoginInput( email, password);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(Login.this,"Details incorrect", Toast.LENGTH_SHORT).show();

                }

            }
        });
        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(Login.this);
                View dialog_view = getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);
                EditText email_box = dialog_view.findViewById(R.id.email_box);

                b.setView(dialog_view);
                AlertDialog dialog = b.create();
                dialog_view.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = email_box.getText().toString();
                        if(TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            Toast.makeText(Login.this,"Enter your registered email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FirebaseAuth a = FirebaseAuth.getInstance();
                        a.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this,"Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(Login.this,"Unable to send, failed", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }
                });
                dialog_view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() !=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }

    private void CheckUserLoginInput(String emailInput, String passwordInput) {
        check.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Get instance of the current user for email verification
                    FirebaseUser firebaseUser = check.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // open main activity
                        FirebaseAuth a = FirebaseAuth.getInstance();
                        FirebaseUser user = a.getCurrentUser();
                        String id = a.getUid();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }else{
                        firebaseUser.sendEmailVerification();
                        check.signOut();    // Sign user out
                        ShowAlertDialog();
                    }

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        layoutEmailLogin.setError("User does not exists or is not longer valid. Please Sign in again.");
                        layoutEmailLogin.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        layoutEmailLogin.setError("Invalid credentials. Please check and re-enter.");
                        layoutEmailLogin.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            ;
        });
    }

    private void ShowAlertDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(Login.this);
        b.setTitle("Email not Verified");
        b.setMessage("Please verify your email now. You cannot login without your email been verified.");

        // Open email app if user clicks continue
        b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN); // New task launcher
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);  // if user have many email, android show a list of email apps
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Email app to open in new window
                startActivity(intent);

            }
        });
        AlertDialog dialogBox = b.create();   // Create dialog box
        dialogBox.show();
    }

// Check the user is already logged in. in this case take the user directly into the main activity
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = check.getCurrentUser();
        if(check.getCurrentUser() != null && firebaseUser.isEmailVerified()){
            Toast.makeText(Login.this, "Already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            //finish(); // Close login activity
        }else{
            Toast.makeText(Login.this, "Please login", Toast.LENGTH_SHORT).show();

        }
    }

}