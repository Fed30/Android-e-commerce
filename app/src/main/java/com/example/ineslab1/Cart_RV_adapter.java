package com.example.ineslab1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Cart_RV_adapter extends FirebaseRecyclerAdapter<Basket_model,Cart_RV_adapter.MyViewHolder>   {


    public Cart_RV_adapter(@NonNull FirebaseRecyclerOptions<Basket_model> options) {
        super(options);
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);

    }

    @Override
    protected void onBindViewHolder(@NonNull Cart_RV_adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Basket_model model) {
        //  Assigning values to each of the rows as they come back on screen
        //  Based on position of the recycler view
        //  Change color of the background of the card view holder

        Glide.with(holder.imageView.getContext()).load(model.getProduct_image())
                .placeholder(R.drawable.cake_placeholder).error(com.firebase.ui.auth.R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.imageView);
        holder.product_name.setText(model.getProduct_name());
        holder.quantity.setText(model.getQuantity());
        holder.size.setText(model.getSize());
        holder.price.setText(model.getTotal_price());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                String productName = holder.product_name.getText().toString();
                int q = Integer.parseInt((String) holder.quantity.getText());
                float p = Float.parseFloat(holder.price.getText().toString());
                int s = Integer.parseInt((String) holder.size.getText());
                float price_breakdown = p / q / s;
                int update = q + 1;
                if(productName.contains("mousse cake")){
                    FirebaseDatabase.getInstance().getReference("Products").child("Mousse cakes")
                            .child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String maxQuantity = snapshot.getValue(String.class);
                                    if (update > Integer.parseInt(maxQuantity)) {
                                        Toast.makeText(holder.quantity.getContext(), "Quantity cannot exceed stock value", Toast.LENGTH_SHORT).show();
                                    } else {
                                        float result = update * price_breakdown * s;
                                        holder.quantity.setText(String.valueOf(update));
                                        holder.price.setText(String.valueOf(String.format("%.2f", result)));
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity",holder.quantity.getText());
                                        map.put("total_price",holder.price.getText());
                                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        MainActivity.getInstance().calculateTotalBasketPrice();
                                        MainActivity.getInstance().UpdatePlus();
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                else if (productName.contains("mini")) {
                    FirebaseDatabase.getInstance().getReference("Products").child("Mini mousse cake")
                            .child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String maxQuantity = snapshot.getValue(String.class);
                                    if (update > Integer.parseInt(maxQuantity)) {
                                        Toast.makeText(holder.quantity.getContext(), "Quantity cannot exceed stock value", Toast.LENGTH_SHORT).show();
                                    } else {
                                        float result = update * price_breakdown * s;
                                        holder.quantity.setText(String.valueOf(update));
                                        holder.price.setText(String.valueOf(String.format("%.2f", result)));
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity",holder.quantity.getText());
                                        map.put("total_price",holder.price.getText());
                                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        MainActivity.getInstance().calculateTotalBasketPrice();
                                        MainActivity.getInstance().UpdatePlus();
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                else if (productName.contains("giftbox")) {
                    FirebaseDatabase.getInstance().getReference("Products").child("Giftbox")
                            .child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String maxQuantity = snapshot.getValue(String.class);
                                    if (update > Integer.parseInt(maxQuantity)) {
                                        Toast.makeText(holder.quantity.getContext(), "Quantity cannot exceed stock value", Toast.LENGTH_SHORT).show();
                                    } else {
                                        float result = update * price_breakdown * s;
                                        holder.quantity.setText(String.valueOf(update));
                                        holder.price.setText(String.valueOf(String.format("%.2f", result)));
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity",holder.quantity.getText());
                                        map.put("total_price",holder.price.getText());
                                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        MainActivity.getInstance().calculateTotalBasketPrice();
                                        MainActivity.getInstance().UpdatePlus();
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                else if (productName.contains("craquelin")) {
                    FirebaseDatabase.getInstance().getReference("Products").child("Choux au craquelin")
                            .child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String maxQuantity = snapshot.getValue(String.class);
                                    if (update > Integer.parseInt(maxQuantity)) {
                                        Toast.makeText(holder.quantity.getContext(), "Quantity cannot exceed stock value", Toast.LENGTH_SHORT).show();
                                    } else {
                                        float result = update * price_breakdown * s;
                                        holder.quantity.setText(String.valueOf(update));
                                        holder.price.setText(String.format("%.2f", result));
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity",holder.quantity.getText());
                                        map.put("total_price",holder.price.getText());
                                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        MainActivity.getInstance().calculateTotalBasketPrice();
                                        MainActivity.getInstance().UpdatePlus();
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                else if (productName.contains("tray")) {
                    FirebaseDatabase.getInstance().getReference("Products").child("Macarons tray")
                            .child(productName).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String maxQuantity = snapshot.getValue(String.class);
                                    if (update > Integer.parseInt(maxQuantity)) {
                                        Toast.makeText(holder.quantity.getContext(), "Quantity cannot exceed stock value", Toast.LENGTH_SHORT).show();
                                    } else {
                                        float result = update * price_breakdown * s;
                                        holder.quantity.setText(String.valueOf(update));
                                        holder.price.setText(String.valueOf(String.format("%.2f", result)));
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("quantity",holder.quantity.getText());
                                        map.put("total_price",holder.price.getText());
                                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        MainActivity.getInstance().calculateTotalBasketPrice();
                                        MainActivity.getInstance().UpdatePlus();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                int q = Integer.parseInt((String) holder.quantity.getText());
                float p = Float.parseFloat(holder.price.getText().toString());
                int s = Integer.parseInt((String) holder.size.getText());
                float price_breakdown = p / q / s;
                int update = q - 1;
                if(update<1){
                    Toast.makeText(holder.quantity.getContext(),"Quantity minimum limit reached", Toast.LENGTH_SHORT).show();
                }
                else{
                    float result =  update * price_breakdown * s ;
                    holder.quantity.setText(String.valueOf(update));
                    holder.price.setText(String.valueOf(String.format("%.2f",result)));
                    MainActivity.getInstance().UpdateMinus();
                    MainActivity.getInstance().calculateTotalBasketPrice();

                }

                Map<String, Object> map = new HashMap<>();
                map.put("quantity",holder.quantity.getText());
                map.put("total_price",holder.price.getText());
                FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                        .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.quantity.getContext(),"Quantity updated successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.quantity.getContext(),"Quantity update error", Toast.LENGTH_SHORT).show();
                            }
                        });
                MainActivity.getInstance().calculateTotalBasketPrice();
                notifyDataSetChanged();

            }
        });
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain current user id
                FirebaseAuth a = FirebaseAuth.getInstance();
                FirebaseUser user = a.getCurrentUser();
                String UserId = a.getUid();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(holder.product_name.getContext(),
                        androidx.constraintlayout.widget.R.style.Base_Theme_AppCompat_Dialog_Alert);

                builder.setTitle("Are you sure to delete?");
                builder.setMessage("Deleted Items cannot be Undo.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("user").child(UserId).child("basket")
                                .child(getRef(position).getKey()).removeValue();



                        MainActivity.getInstance().UpdateOnDeleteItem(holder.quantity.getText().toString());
                        MainActivity.getInstance().calculateTotalBasketPrice();
                        notifyItemRemoved(holder.getAdapterPosition());

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.product_name.getContext(),"Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }

        });



    }


    @NonNull
    @Override
    public Cart_RV_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you gonna inflate the layout (Giving the look of my rv rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_recycle_row_layout,parent,false);
        return new MyViewHolder(view);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Grabbing the views from from the category_recyclerview layout

        ImageView imageView;
        TextView product_name;
        TextView quantity;
        TextView size;
        TextView price;
        public ImageView plus;
        public ImageView minus;
        public ImageView delete_icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.selected_price);
            quantity = itemView.findViewById(R.id.quantity_update);
            size = itemView.findViewById(R.id.selected_size);
            delete_icon = itemView.findViewById(R.id.delete_icon);
            plus = itemView.findViewById(R.id.button_plus);
            minus = itemView.findViewById(R.id.button_minus);




        }
    }
}

