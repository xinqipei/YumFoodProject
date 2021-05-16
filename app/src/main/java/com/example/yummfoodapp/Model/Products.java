package com.example.yummfoodapp.Model;

public class Products {
    Integer productId;
    String productName;
    String productPrice;
    Integer imageUri;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    int price;

    //$2/5000

    public Products(Integer productId, String productName, String productPrice, Integer imageUri,int price) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUri = imageUri;
        this.price =price;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getImageUri() {
        return imageUri;
    }

    public void setImageUri(Integer imageUri) {
        this.imageUri = imageUri;
    }
}
