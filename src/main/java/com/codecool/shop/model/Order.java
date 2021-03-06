package com.codecool.shop.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order extends BaseModel {

    private final int senderId;
    private List<LineItem> itemList;

    @JsonCreator
    public Order(@JsonProperty("userId") int senderId, @JsonProperty("name") String name) {
        super(name);
        this.senderId = senderId;
    }

    public float getTotalPrice() {
        return itemList.stream()
                .map(item -> item.getProductPrice()* item.getQuantity())
                .reduce(0.0f, Float::sum);
    }

    public void addToOrder(LineItem item) {
        itemList.add(item);
    }

    public void buildFromArray(int[][] array) {
        int currentProductId;
        int currentQuantity;
        LineItem currentItem;

        for (int[] rowArray : array) {
            if (rowArray.length >= 2) {
                currentProductId = rowArray[0];
                currentQuantity = rowArray[1];
                currentItem = new LineItem(currentProductId, currentQuantity);
                this.addToOrder(currentItem);
            }
        }
    }

    public void rebuildFromLineItems(List<LineItem> newItemList) {
        if (this.itemList != null) { this.itemList.clear(); }
        else { this.itemList = new ArrayList<>(); }

        if (newItemList != null) {
            newItemList.forEach(this::addToOrder);
        }
    }

    public static Order deserializeFromBuffer(BufferedReader reader) {
        String dataBeingRead = reader.lines().collect(Collectors.joining());
        return deserializeFromString(dataBeingRead);
    }

    public static Order deserializeFromString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Order deserialized = null;
        try {
            deserialized = objectMapper.readValue(json, Order.class);
        } catch (JsonProcessingException e) {
            //TODO should throw new RuntimeException here and handle it further down the code
            e.printStackTrace();
        }
        return deserialized;
    }

    public int getSenderId() {
        return senderId;
    }

    public List<LineItem> getItemList() {
        if (this.itemList == null) { return null; };
        return new ArrayList<>(this.itemList);
    }

    public float getTotal() {
        float sum = 0.0f;
        for (LineItem item: itemList) {
            sum+=item.getSubtotal();
        }

        return sum;
    }

    public String getTotalAsString() {
        return String.format("%.2f",this.getTotal());
    }
}
