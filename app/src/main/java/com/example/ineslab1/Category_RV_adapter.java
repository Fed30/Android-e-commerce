package com.example.ineslab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Category_RV_adapter extends RecyclerView.Adapter<Category_RV_adapter.MyViewHolder> {
    ArrayList<CategoryModel> categoryModels;
    Context context;
    private final RV_interface RV_interface;

    public Category_RV_adapter(Context context, ArrayList<CategoryModel> categoryModels, RV_interface RV_interface) {
        this.context = context.getApplicationContext();
        this.categoryModels = categoryModels;
        this.RV_interface = RV_interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you gonna inflate the layout (Giving the look of my rv rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_recycle_row_layout, parent, false);
        return new Category_RV_adapter.MyViewHolder(view, RV_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Assigning values to each of the rows as they come back on screen
        // Based on position of the recycler view
        // Change color of the background of the card view holder
        holder.tv_desc.setText(categoryModels.get(position).getDescription_string());
        Glide.with(context).load(categoryModels.get(position).getImage()).error(R.drawable.cake_placeholder).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        // Give a total number of item in the recycle view
        return categoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Grabbing the views from from the category_recyclerview layout

        ImageView imageView;
        TextView tv_desc;


        public MyViewHolder(@NonNull View itemView, RV_interface RV_interface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_img);
            tv_desc = itemView.findViewById(R.id.category_name);
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
