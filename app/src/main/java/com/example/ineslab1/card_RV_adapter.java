package com.example.ineslab1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.view.CardForm;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class card_RV_adapter extends FirebaseRecyclerAdapter<UserCardDetails, card_RV_adapter.MyViewHolder> {




    public card_RV_adapter(@NonNull FirebaseRecyclerOptions<UserCardDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull UserCardDetails model) {
        holder.card_name.setText(model.getCardName().toString());
        String c_number = model.getCardNumber().toString();
        String formatted_number = c_number.substring(c_number.length()-4,c_number.length());
        holder.card_number.setText(formatted_number);



        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(holder.card_name.getContext(),
                        androidx.constraintlayout.widget.R.style.Base_Theme_AppCompat_Dialog_Alert);

                builder.setTitle("Are you sure to delete?");
                builder.setMessage("Deleted Items cannot be Undo.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Card details")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.card_name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });
        holder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_name = holder.card_name.getText().toString();
                String card_number = holder.card_number.getText().toString();

                AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                View dialog_view = MainActivity.getInstance().getInflaterCardForm();
                CardForm cardForm= dialog_view.findViewById(R.id.card_form);
                cardForm.cardRequired(true)
                        .expirationRequired(true)
                        .cvvRequired(true)
                        .cardholderName(CardForm.FIELD_REQUIRED)
                        .setup((AppCompatActivity) dialog_view.getContext());
                cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


                cardForm.getCardholderNameEditText().setText(card_name);
                cardForm.getCardEditText().setText(c_number);
                cardForm.validate();

                b.setView(dialog_view);
                AlertDialog dialog = b.create();

                dialog_view.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String card_name = cardForm.getCardholderName().toString();
                        String card_number = cardForm.getCardNumber().toString();
                        String expiry_date = cardForm.getExpirationDateEditText().toString();
                        String Cvv = cardForm.getCvv().toString();

                        FirebaseAuth a = FirebaseAuth.getInstance();
                        FirebaseUser user = a.getCurrentUser();
                        String UserId = a.getUid();
                        if (cardForm.isValid()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("cardName", card_name);
                            map.put("cardNumber", card_number);
                            map.put("expiryDate", expiry_date);
                            map.put("cvv", Cvv);
                            FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("Card details")
                                    .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.card_name.getContext(), "New payment method updated successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.card_name.getContext(), "Payment method update error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
        View view = inflater.inflate(R.layout.cards_recycle_row_layout,parent,false);
        return new MyViewHolder(view);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView card_name, card_number;
        RadioButton radioButton;
        public ImageView delete_icon, edit_icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_name = itemView.findViewById(R.id.card_name);
            card_number = itemView.findViewById(R.id.card_number);
            delete_icon = itemView.findViewById(R.id.delete_icon);
            edit_icon = itemView.findViewById(R.id.edit_icon);


        }
    }
}
