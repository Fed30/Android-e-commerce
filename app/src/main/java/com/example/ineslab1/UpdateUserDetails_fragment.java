package com.example.ineslab1;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateUserDetails_fragment extends Fragment {
    Button done, update_pwd;
    TextInputLayout layoutFirstName, layoutLastName,layoutEmail,layoutMobile;
    TextInputEditText editTextName, editTextLastName,editEmail, editTextMobile;
    TextInputLayout old_pwd_layout, new_pdw_layout,confirm_pwd_layout;
    TextInputEditText editText_old_pwd,editText_new_pwd,editText_confirm_pwd;
    ImageView editFirstname, editLastName, editMobile, back;
    boolean isFirstnameOk = false;
    boolean isLastNameOk = false;
    boolean isMobileOk = false;
    boolean is_old_PasswordOk =false;
    boolean is_new_PasswordOk =false;
    boolean is_confirm_PasswordOk =false;
    DatabaseReference databaseReference;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutFirstName = view.findViewById(R.id.layoutName);
        layoutLastName = view.findViewById(R.id.layoutLastname);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutMobile = view.findViewById(R.id.layourMobile);

        editTextName = view.findViewById(R.id.nameEditText);
        editTextLastName = view.findViewById(R.id.lastNameEditText);
        editEmail = view.findViewById(R.id.emailEditText);
        editTextMobile = view.findViewById(R.id.editTextMobile);

        editFirstname = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        editMobile = view.findViewById(R.id.edit_mobile);

        done = view.findViewById(R.id.done_btn);
        back = view.findViewById(R.id.back);
        update_pwd = view.findViewById(R.id.change_password);

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
                    editTextName.setText(firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(UserId).child("User Details")
                .child("last_name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String lastName = snapshot.getValue(String.class);
                    editTextLastName.setText(lastName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(UserId).child("User Details")
                .child("email");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String email = snapshot.getValue(String.class);
                    editEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(UserId).child("User Details")
                .child("mobile");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mobile = snapshot.getValue(String.class);
                    editTextMobile.setText(mobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        editFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setFocusable(true);
                editTextName.setEnabled(true);
                editTextName.setFocusableInTouchMode(true);

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
            }
        });
        editLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextLastName.setFocusable(true);
                editTextLastName.setEnabled(true);
                editTextLastName.setFocusableInTouchMode(true);

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
            }
        });
        editMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextMobile.setFocusable(true);
                editTextMobile.setEnabled(true);
                editTextMobile.setFocusableInTouchMode(true);
                editTextMobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity) getActivity();
                m.replaceFragment(new Profile_Fragment());
            }
        });

        update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                View dialog_view = MainActivity.getInstance().getInflaterChangePassword();
                b.setView(dialog_view);
                AlertDialog dialog = b.create();
                // access fields dialog edit text
                editText_old_pwd = dialog_view.findViewById(R.id.editTextOldPassword);
                editText_new_pwd = dialog_view.findViewById(R.id.editTextNewPassword);
                editText_confirm_pwd =dialog_view.findViewById(R.id.editTextConfirmPwd);
                // access fields dialog layout input text
                old_pwd_layout = dialog_view.findViewById(R.id.old_password);
                new_pdw_layout = dialog_view.findViewById(R.id.new_password);
                confirm_pwd_layout =dialog_view.findViewById(R.id.layoutConfirmPwd);

                editText_old_pwd.addTextChangedListener(new TextWatcher() {
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
                                old_pwd_layout.setHelperText("Strong password");
                                old_pwd_layout.setError("");
                                is_old_PasswordOk = true;
                            }
                            else{
                                old_pwd_layout.setHelperText("");
                                old_pwd_layout.setError("Weak password. Include minimum one special character");
                            }

                        }else{
                            old_pwd_layout.setHelperText("Enter minimum 8 characters.");
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String pwd = s.toString();
                        if(pwd.length() >= 8){
                            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
                            Matcher m = p.matcher(pwd);
                            boolean isPassword_containsSpecialChar = m.find();
                            if(isPassword_containsSpecialChar){
                                old_pwd_layout.setHelperText("Strong password");
                                old_pwd_layout.setError("");
                                is_old_PasswordOk = true;
                            }
                            else{
                                old_pwd_layout.setHelperText("");
                                old_pwd_layout.setError("Weak password. Include minimum one special character");
                            }

                        }else{
                            old_pwd_layout.setHelperText("Enter minimum 8 characters.");
                        }

                    }
                });
                editText_new_pwd.addTextChangedListener(new TextWatcher() {
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
                                new_pdw_layout.setHelperText("Strong password");
                                new_pdw_layout.setError("");
                                is_new_PasswordOk = true;
                            }
                            else{
                                new_pdw_layout.setHelperText("");
                                new_pdw_layout.setError("Weak password. Include minimum one special character");
                            }

                        }else{
                            new_pdw_layout.setHelperText("Enter minimum 8 characters.");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                editText_confirm_pwd.addTextChangedListener(new TextWatcher() {
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
                                confirm_pwd_layout.setHelperText("Strong password");
                                confirm_pwd_layout.setError("");
                                is_confirm_PasswordOk = true;
                            }
                            else{
                                confirm_pwd_layout.setHelperText("");
                                confirm_pwd_layout.setError("Weak password. Include minimum one special character");
                                is_confirm_PasswordOk = false;
                            }

                        }else{
                            confirm_pwd_layout.setHelperText("Enter minimum 8 characters.");
                            is_confirm_PasswordOk = false;

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String password = editText_new_pwd.getText().toString();
                        String c_pwd = editText_confirm_pwd.getText().toString();
                        if (s.length()>0 && c_pwd.length()> 0){
                            if(!c_pwd.equals(password)){
                                confirm_pwd_layout.setError("Password not matching");
                                is_confirm_PasswordOk = false;
                            }else{
                                confirm_pwd_layout.setHelperText("Password match");
                                is_confirm_PasswordOk = true;
                            }
                        }

                    }
                });

                dialog_view.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String old_pwd = editText_old_pwd.getText().toString();
                        String new_pwd = editText_new_pwd.getText().toString();
                        String confirm_pwd = editText_confirm_pwd.getText().toString();

                        if (!old_pwd.equals("") && !new_pwd.equals("") && !confirm_pwd.equals("")) {
                            if (is_old_PasswordOk && is_new_PasswordOk && is_confirm_PasswordOk) {
                                updatePassword(old_pwd,new_pwd);
                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(dialog.getContext(), "Details incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } 
                        else {
                            Toast.makeText(dialog.getContext(), "Details incorrect", Toast.LENGTH_SHORT).show();
                        }
                };
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

            }

        });




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = editTextName.getText().toString();
                String last_name = editTextLastName.getText().toString();
                String email = editEmail.getText().toString();
                String mobile = editTextMobile.getText().toString();

                if(!first_name.equals("") && !last_name.equals("") && !email.equals("") && !mobile.equals("")  ){
                    if(isFirstnameOk && isLastNameOk && isMobileOk  ){
                        Map<String, Object> map = new HashMap<>();
                        map.put("first_name",first_name);
                        map.put("last_name",last_name);
                        map.put("email",email);
                        map.put("mobile",mobile);
                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("User Details")
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(),"Details updated successfully",
                                                Toast.LENGTH_SHORT).show();
                                        MainActivity m = (MainActivity) getActivity();
                                        m.replaceFragment(new Profile_Fragment());

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Details update error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }else{
                    Toast.makeText(getContext(),"Details incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword(String oldPwd, String newPwd) {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(u.getEmail(),oldPwd);
        u.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                u.updatePassword(newPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Password updated successfully", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user_details_fragment, container, false);
        return view;
    }
}




