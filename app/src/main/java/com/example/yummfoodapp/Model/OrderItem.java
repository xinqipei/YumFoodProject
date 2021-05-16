package com.example.yummfoodapp.Model;

public class OrderItem {
    public OrderItem(String nameofdrink, String pricesofdrink, String quantityofdrink, String yeshasCream, String yeshastopping) {
        this.nameofdrink = nameofdrink;
        this.pricesofdrink = pricesofdrink;
        this.quantitysofdrink = quantityofdrink;
        this.yeshasCream = yeshasCream;
        this.yeshastopping = yeshastopping;
    }

    public String getNameofdrink() {
        return nameofdrink;
    }

    public void setNameofdrink(String nameofdrink) {
        this.nameofdrink = nameofdrink;
    }

    public String getPricesofdrink() {
        return pricesofdrink;
    }

    public void setPricesofdrink(String pricesofdrink) {
        this.pricesofdrink = pricesofdrink;
    }

    public String getQuantitysofdrink() {
        return quantitysofdrink;
    }

    public void setQuantitysofdrink(String quantitysofdrink) {
        this.quantitysofdrink = quantitysofdrink;
    }

    public String getYeshasCream() {
        return yeshasCream;
    }

    public void setYeshasCream(String yeshasCream) {
        this.yeshasCream = yeshasCream;
    }

    public String getYeshastopping() {
        return yeshastopping;
    }

    public void setYeshastopping(String yeshastopping) {
        this.yeshastopping = yeshastopping;
    }

    String nameofdrink;
    String pricesofdrink;
    String quantitysofdrink;
    String yeshasCream;
    String yeshastopping;
}
