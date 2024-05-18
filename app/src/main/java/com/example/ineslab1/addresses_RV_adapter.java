package com.example.ineslab1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addresses_RV_adapter extends FirebaseRecyclerAdapter<UserDeliveryAddress,addresses_RV_adapter.MyViewHolder> {


    public addresses_RV_adapter(@NonNull FirebaseRecyclerOptions<UserDeliveryAddress> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull UserDeliveryAddress model) {
        holder.city.setText(model.getCity().toString());
        holder.flatNumber.setText(model.getFlatNumber().toString());
        holder.postcode.setText(model.getPostcode().toString());
        holder.streetName.setText(model.getStreetName().toString());





        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(holder.streetName.getContext(),
                        androidx.constraintlayout.widget.R.style.Base_Theme_AppCompat_Dialog_Alert);

                builder.setTitle("Are you sure to delete?");
                builder.setMessage("Deleted Items cannot be Undo.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Delivery address")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.streetName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            boolean isFlatNumberOk = false;
            boolean isPostcodeOk = false;
            boolean isStreetNameOk = false;
            boolean isCityOk = false;
            @Override
            public void onClick(View v) {
                String city = holder.city.getText().toString();
                String flat_num = holder.flatNumber.getText().toString();
                String postcode = holder.postcode.getText().toString();
                String street = holder.streetName.getText().toString();

                AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                View dialog_view = MainActivity.getInstance().getInflater();
                TextInputLayout layoutFlatNumber = dialog_view.findViewById(R.id.layoutFlatNumber);
                TextInputLayout layoutPostcode = dialog_view.findViewById(R.id.layoutPostcode);
                TextInputLayout layoutCity = dialog_view.findViewById(R.id.layoutConfirmPwd);
                TextInputLayout layoutStreet = dialog_view.findViewById(R.id.new_password);

                TextInputEditText editTextFlatNumber = dialog_view.findViewById(R.id.editTextFlatNumber);
                TextInputEditText editTextPostcode = dialog_view.findViewById(R.id.editTextPostcode);
                TextInputEditText editTextStreetName = dialog_view.findViewById(R.id.editTextNewPassword);
                TextInputEditText editTextCity = dialog_view.findViewById(R.id.editTextConfirmPwd);

                editTextFlatNumber.setText(flat_num);
                editTextPostcode.setText(postcode);
                editTextStreetName.setText(street);
                editTextCity.setText(city);

                b.setView(dialog_view);
                AlertDialog dialog = b.create();
                editTextFlatNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String f_number = s.toString();
                        if (f_number.length() < 1) {
                            layoutFlatNumber.setError("Enter valid flat number");
                        } else {
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
                        if (postcode.length() < 5) {
                            layoutPostcode.setError("Enter valid postcode");
                        } else {
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
                        if (pCode.length() > 7) {
                            layoutPostcode.setError("Postcode should be not longer than 7 characters");
                        } else if (!postcodeMatcher.find()) {
                            layoutPostcode.setError("Postcode invalid");

                        } else {
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
                        if (streetName.length() > 30) {
                            layoutStreet.setError("Street name cannot be longer then 30 characters");
                        } else {
                            layoutStreet.setHelperText("Street name accepted");
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
                        if (city.length() > 30) {
                            layoutCity.setError("City cannot be longer then 30 characters");
                        } else {
                            layoutCity.setHelperText("City accepted");
                            isCityOk = true;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dialog_view.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String flat_number = editTextFlatNumber.getText().toString();
                        String post_code = editTextPostcode.getText().toString();
                        String street = editTextStreetName.getText().toString();
                        String city = editTextCity.getText().toString();

                        if (!flat_number.equals("") && !post_code.equals("") && !street.equals("") && !city.equals("")) {
                            if (isFlatNumberOk && isPostcodeOk && isStreetNameOk && isCityOk) {
                                FirebaseAuth a = FirebaseAuth.getInstance();
                                FirebaseUser user = a.getCurrentUser();
                                String UserId = a.getUid();
                                if (UserId != null) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("city", city);
                                    map.put("flatNumber", flat_number);
                                    map.put("postcode", post_code);
                                    map.put("streetName", street);
                                    FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Delivery address")
                                            .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(holder.streetName.getContext(), "Address updated successfully",
                                                            Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(holder.streetName.getContext(), "Address update error", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(holder.streetName.getContext(), "ERROR OF RETRIEVING THE USER ID", Toast.LENGTH_SHORT).show();

                                }

                            }
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

        }

    });

}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you gonna inflate the layout (Giving the look of my rv rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.addresses_recycle_row_layout,parent,false);
        return new MyViewHolder(view);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView flatNumber, streetName, postcode, city;
        public ImageView delete_icon, edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flatNumber = itemView.findViewById(R.id.flat_number);
            streetName = itemView.findViewById(R.id.Street_name);
            postcode = itemView.findViewById(R.id.postcode);
            city = itemView.findViewById(R.id.city);
            delete_icon = itemView.findViewById(R.id.delete_icon);
            edit = itemView.findViewById(R.id.edit_icon_address);


        }
    }



}
