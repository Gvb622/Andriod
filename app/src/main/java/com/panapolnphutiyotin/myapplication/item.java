package com.panapolnphutiyotin.myapplication;

/**
 * Created by panapolnphutiyotin on 9/3/16 AD.
 */
public class item {
    private String Barcode;
    private String Name;
    private String Image;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    private String Unit;
    private int Number;



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
    };
    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }
}
