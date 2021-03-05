package com.codecool.shop.model;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductDao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class LineItem extends SimpleLineItem{
    private float productPrice;
    private String productName;

    public LineItem(int productId, int quantity, float productPrice) {
        super(productId, quantity);
        this.productPrice = productPrice;
    }

    @JsonCreator
    public LineItem(@JsonProperty("productId")int productId, @JsonProperty("quantity")int quantity) {
        super(productId, quantity);
        this.productPrice = 0.0f;
        updateProductDetails();
    }

    public void updateProductDetails() {
        ProductDao dataStoreForProduct = ProductController.getProductDaoInstance();
        Product product;
        product = dataStoreForProduct.find(this.getProductId());
        if (product != null) {
            productPrice = product.getDefaultPrice();
            productName = product.getName();
        }
    }

    public float getProductPrice() {
        return productPrice;
    }

    public static List<LineItem> deserializeListFromString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LineItem> deserializedList = null;
        try {
            deserializedList = objectMapper.readValue(json, new TypeReference<List<LineItem>>() {});
        } catch (JsonProcessingException e) {
            //TODO should throw new RuntimeException here and handle it further down the code
            e.printStackTrace();
        }
        return deserializedList;
    }


    public String getProductName() {
        return productName;
    }
}
