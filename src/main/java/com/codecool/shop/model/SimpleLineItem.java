package com.codecool.shop.model;

public abstract class SimpleLineItem {
    private int quantity;
    private int productId;

    protected SimpleLineItem(int productId, int quantity) {
        this.quantity = quantity;
        this.productId = productId;
    }

    protected int getProductId() {
        return this.productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
