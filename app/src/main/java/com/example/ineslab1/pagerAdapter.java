package com.example.ineslab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class pagerAdapter extends RecyclerView.Adapter<pagerAdapter.ViewPagerViewHolder>{
    ArrayList<imageSliderModel> imageSliderModels;
    Context context;

    public pagerAdapter(Context context, ArrayList<imageSliderModel> imageSliderModels) {
        this.context = context;
        this.imageSliderModels = imageSliderModels;
    }


    @NonNull
    @Override
    public pagerAdapter.ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pagerAdapter.ViewPagerViewHolder holder, int position) {
        //holder.imageView.setImageResource(imageSliderModels.get(position).getImage());
        Glide.with(context).load(imageSliderModels.get(position).getImage()).error(R.drawable.cake_placeholder).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return imageSliderModels.size();
    }

    public static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageV);
        }
    }
}
