package com.example.ineslab1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Product_Fragment extends Fragment implements RV_interface  {

    ArrayList<ProductModel>productModels = new ArrayList<>();
    RecyclerView recyclerView_product ;
    DatabaseReference databaseReference;
    Product_RV_adapter adapter;
    androidx.appcompat.widget.SearchView searchView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView_product);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchData(newText);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        recyclerView_product = view.<RecyclerView>findViewById(R.id.product_recyclerView);
        adapter = new Product_RV_adapter(getContext(),productModels,this);
        recyclerView_product.setAdapter(adapter);
        recyclerView_product.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle b = getArguments();
        String desc_title = b.getString("desc");
        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(desc_title);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ProductModel productModel = dataSnapshot.getValue(ProductModel.class);
                    productModels.add(productModel);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_, container, false);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Fragment fragment = new Product_specs_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("product_desc", productModels.get(position).getDescription());
        bundle.putString("product_name",productModels.get(position).getName());
        bundle.putString("starting_price",productModels.get(position).getStarting_price());
        bundle.putString("image",productModels.get(position).getImage());
        bundle.putString("image1",productModels.get(position).getImage1());
        bundle.putString("image2",productModels.get(position).getImage2());
        bundle.putInt("size",productModels.get(position).getSize());
        bundle.putInt("quantity", Integer.parseInt(productModels.get(position).getQuantity()));
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_wrapper,fragment).addToBackStack(null).commit();

    }

    public  void searchData(String str){
        ArrayList<ProductModel> search_list = new ArrayList<>();
        for(ProductModel p: productModels){
            if(p.getName().toLowerCase().contains(str.toLowerCase())){
                search_list.add(0,p);
            }
            productModels =search_list;
            adapter.searchData(search_list);
            adapter.notifyDataSetChanged();
        }


    }
}