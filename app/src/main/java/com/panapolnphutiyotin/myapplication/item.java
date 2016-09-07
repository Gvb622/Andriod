package com.panapolnphutiyotin.myapplication;

/**
 * Created by panapolnphutiyotin on 9/3/16 AD.
 */
public class item {

    private String Barcode;
    private String Name;
    private String Image;
    private String Unit;
    private String Madein;
    private String Price;
    private String Type;

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

    public String getMadein() {
        return Madein;
    }

    public void setMadein(String madein) {
        Madein = madein;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }



}
