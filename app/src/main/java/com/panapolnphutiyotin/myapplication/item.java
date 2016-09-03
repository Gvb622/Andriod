package com.panapolnphutiyotin.myapplication;

/**
 * Created by panapolnphutiyotin on 9/3/16 AD.
 */
public class item {
    private String Barcode;
    private String Name;
    private String Image;


    public item() {
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
