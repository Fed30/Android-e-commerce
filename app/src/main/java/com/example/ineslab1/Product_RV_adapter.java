package com.example.ineslab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Product_RV_adapter extends RecyclerView.Adapter<Product_RV_adapter.MyViewHolder>  {
    ArrayList<ProductModel> productModels;

    Context context;
    private final RV_interface RV_interface;


    public Product_RV_adapter(Context context, ArrayList<ProductModel> productModels, RV_interface RV_interface) {
        this.context = context;
        this.productModels = productModels;
        this.RV_interface = RV_interface;
    }

    @NonNull
    @Override
    public Product_RV_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you gonna inflate the layout (Giving the look of my rv rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_recycle_row_layout, parent, false);
        return new Product_RV_adapter.MyViewHolder(view, RV_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_RV_adapter.MyViewHolder holder, int position) {
        // Assigning values to each of the rows as they come back on screen
        // Based on position of the recycler view
        // Change color of the background of the card view holder
        holder.tv_name.setText(productModels.get(position).getName());
        holder.tv_start_price.setText(productModels.get(position).getStarting_price());
        Glide.with(context).load(productModels.get(position).getImage()).error(R.drawable.cake_placeholder).into(holder.imageView);
        holder.quantity.setText(productModels.get(position).getQuantity());
        String q = holder.quantity.getText().toString();
        if (Integer.parseInt(q) > 0) {
            holder.itemView.setEnabled(true);
            holder.out_of_stock_label.setVisibility(View.GONE);
        } else {
            holder.itemView.setEnabled(false);
            holder.out_of_stock_label.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        // Give a total number of item in the recycle view
        return productModels.size();
    }

    public void searchData(ArrayList<ProductModel> search_list) {
        this.productModels = search_list;
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Grabbing the views from from the category_recyclerview layout

        ImageView imageView;
        TextView tv_name;
        TextView tv_start_price, quantity, out_of_stock_label;

        public MyViewHolder(@NonNull View itemView, RV_interface RV_interface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            tv_name = itemView.findViewById(R.id.product_name);
            tv_start_price = itemView.findViewById(R.id.starting_price);
            quantity = itemView.findViewById(R.id.quantity_indicator);
            out_of_stock_label = itemView.findViewById(R.id.out_of_stock);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RV_interface != null) {
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            RV_interface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
