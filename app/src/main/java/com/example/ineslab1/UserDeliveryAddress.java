package com.example.ineslab1;

public class UserDeliveryAddress {
    String flatNumber, postcode, streetName, city;




    public UserDeliveryAddress(String flatNumber, String postcode, String streetName, String city) {
        this.flatNumber = flatNumber;
        this.postcode = postcode;
        this.streetName = streetName;
        this.city = city;

    }

    public UserDeliveryAddress() {
    }
    public String getFlatNumber() {
        return flatNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
