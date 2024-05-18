package com.example.ineslab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;


public class Product_specs_fragment extends Fragment  {



    ArrayList<imageSliderModel>imageSliderModels = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ViewPager2 viewPager2;
    CircleIndicator3 indicator3;
    Spinner spinner_quantity;
    Spinner spinner_size;
    TextView selection_result;
    TextView starting_price;
    TextView product_name;
    TextView product_description;
    Button select;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        String product_desc = b.getString("product_desc");
        String prod_name = b.getString("product_name");
        String start_price = b.getString("starting_price");
        int size = b.getInt("size");
        int quantity = b.getInt("quantity");
        String image = b.getString("image");
        String image1 = b.getString("image1");
        String image2 = b.getString("image2");
        images.add(image);
        images.add(image1);
        images.add(image2);

        select = view.findViewById(R.id.select);
        selection_result = view.findViewById(R.id.select_result);
        starting_price = view.findViewById(R.id.starting_price);
        product_description = view.findViewById(R.id.prod_description);
        product_name = view.findViewById(R.id.title);
        viewPager2 = view.findViewById(R.id.pager);
        indicator3 = view.findViewById(R.id.indicator);
        spinner_quantity = view.findViewById(R.id.spinner_quantity);
        spinner_size = view.findViewById(R.id.spinner_size);



        setUpItem();
        product_name.setText(prod_name);
        starting_price.setText(start_price);
        product_description.setText(product_desc);
        pagerAdapter adapter = new pagerAdapter(getContext(),imageSliderModels);
        viewPager2.setAdapter(adapter);
        indicator3.setViewPager(viewPager2);

        ArrayList<Integer> spinnerArray_quantity = new ArrayList<>();
        for(int i= 1; i<=quantity; i++){
            spinnerArray_quantity.add(i);
        }


        ArrayAdapter<Integer> spinner_quantityArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray_quantity);              // Selected item will look like a spinner set from XML
        spinner_quantityArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_quantity.setAdapter(spinner_quantityArrayAdapter);
        spinner_quantity.setSelection(0);

        ArrayList<Integer> spinnerArray_size = new ArrayList<>();
        for(int i= 1; i<=size; i++){
            spinnerArray_size.add(i);
        }

        ArrayAdapter<Integer> spinner_sizeArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item,
                        spinnerArray_size);              // Selected item will look like a spinner set from XML
        spinner_sizeArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(spinner_sizeArrayAdapter);
        spinner_size.setSelection(0);
        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner_size_value = spinner_size.getSelectedItem().toString();
                String spinner_quantity_value = spinner_quantity.getSelectedItem().toString();
                int size_value = Integer.parseInt(spinner_size_value);
                int quantity_value = Integer.parseInt(spinner_quantity_value);
                float price = Float.parseFloat(starting_price.getText().toString());
                float result = price * size_value * quantity_value;
                selection_result.setText(String.format("%.2f", result));
                selection_result.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner_size_value = spinner_size.getSelectedItem().toString();
                String spinner_quantity_value = spinner_quantity.getSelectedItem().toString();
                int size_value = Integer.parseInt(spinner_size_value);
                int quantity_value = Integer.parseInt(spinner_quantity_value);
                float price = Float.parseFloat(starting_price.getText().toString());
                float result = price * size_value * quantity_value;
                selection_result.setText(String.format("%.2f", result));
                selection_result.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = spinner_quantity.getSelectedItem().toString();
                MainActivity m = (MainActivity) getActivity();
                m.InsertProduct(image);
                m.Update_Notification(s);




            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_product_specs, container, false);
        MainActivity m = (MainActivity) getActivity();
        m.changeVisibility();

        return view;
    }



    //Method to model the model class for each of the category item
    private void setUpItem(){
        for (String image : images) {
            imageSliderModels.add(new imageSliderModel(image));
        }
    }
}
