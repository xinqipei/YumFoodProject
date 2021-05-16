package com.example.yummfoodapp.Model;

public class Recipes {
    Integer productId;
    String recipeName;
    String description;
    Integer imageUri;
    String details;

    public Recipes(Integer productId, String recipeName, String description, Integer imageUri, String details) {
        this.productId = productId;
        this.recipeName = recipeName;
        this.description = description;
        this.imageUri = imageUri;
        this.details = details;
    }

    public Recipes(String details) {
        this.details = details;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImageUri() {
        return imageUri;
    }

    public void setImageUri(Integer imageUri) {
        this.imageUri = imageUri;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
