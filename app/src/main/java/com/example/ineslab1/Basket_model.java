package com.example.ineslab1;

public class Basket_model {
    String product_image;
    String product_name;
    String quantity;
    String size;
    String total_price;

    public Basket_model() {
    }

    public Basket_model(String product_image, String product_name, String quantity,String size, String total_price) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.quantity = quantity;
        this.size = size;
        this.total_price = total_price;
    }

    public String getProduct_image() {
        return product_image;
    }
    public String getQuantity() {
        return quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getProduct_name() {
        return product_name;
    }
    public String getSize() {return size;}

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }



    public void setSize(String size) {this.size = size;}
}
