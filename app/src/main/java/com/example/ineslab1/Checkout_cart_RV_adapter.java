package com.example.ineslab1;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class Checkout_cart_RV_adapter extends FirebaseRecyclerAdapter<Basket_model, Checkout_cart_RV_adapter.MyViewHolder>   {


    public Checkout_cart_RV_adapter(@NonNull FirebaseRecyclerOptions<Basket_model> options) {
        super(options);
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);

    }

    @Override
    protected void onBindViewHolder(@NonNull Checkout_cart_RV_adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Basket_model model) {
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

    }


    @NonNull
    @Override
    public Checkout_cart_RV_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you gonna inflate the layout (Giving the look of my rv rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkout_cart_rv_layout,parent,false);
        return new MyViewHolder(view);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Grabbing the views from from the category_recyclerview layout

        ImageView imageView;
        TextView product_name;
        TextView quantity;
        TextView size;
        TextView price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.selected_price);
            quantity = itemView.findViewById(R.id.quantity_update);
            size = itemView.findViewById(R.id.selected_size);
        }
    }
}

