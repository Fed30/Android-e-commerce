package com.example.ineslab1;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductModel {
    String description;
    String image;
    String image1;
    String image2;
    String name;
    String quantity;
    int size;
    String starting_price;




    public ProductModel(String description, String image , String image1 , String image2 , String name, String quantity, int size, String starting_price) {
        this.description = description;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.starting_price = starting_price;

    }

    public ProductModel() {
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
    public String getImage1() {
        return image1;
    }
    public String getImage2() {
        return image2;
    }
    public String getStarting_price() {
        return starting_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getSize() {
        return size;
    }



    public void setDescription(String product_description) {
        this.description = product_description;
    }

    public void setProduct_name(String product_name) {
        this.name = product_name;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setImage1(String image1) {
        this.image1 = image1;
    }
    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStarting_price(String starting_price) {
        this.starting_price = starting_price;
    }
}
