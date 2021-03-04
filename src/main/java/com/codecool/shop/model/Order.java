package com.codecool.shop.model;

import java.util.List;

public class Order extends BaseModel {


    private List<LineItem> itemList;

    public Order(String name) {
        super(name);
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

    public void buildFromSimpleLineItem(List<SimpleLineItem> itemList) {
        LineItem currentItem;
        for (SimpleLineItem item : itemList) {
            currentItem = (LineItem) item;
            currentItem.updateProductPrice();
            this.addToOrder(currentItem);
        }
    }
}
