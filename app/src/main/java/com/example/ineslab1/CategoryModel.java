package com.example.ineslab1;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class CategoryModel {

    String description_string;
    String image;




    public CategoryModel(String description_string, String image) {
        this.description_string = description_string;
        this.image = image;

    }

    public CategoryModel() {
    }


    public String getImage() {
        return image;
    }

    public String getDescription_string() {
        return description_string;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setDescription_string(String description_string) {
        this.description_string = description_string;
    }
}
