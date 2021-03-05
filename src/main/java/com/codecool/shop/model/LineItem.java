package com.codecool.shop.model;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductDao;

public class LineItem extends SimpleLineItem{
    private float productPrice;

    public LineItem(int productId, int quantity, float productPrice) {
        super(productId, quantity);
        this.productPrice = productPrice;
    }

    public LineItem(int productId, int quantity) {
        super(productId, quantity);
        this.productPrice = 0.0f;
        updateProductPrice();
    }

    public void updateProductPrice() {
        ProductDao dataStoreForProduct = ProductController.getProductDaoInstance();
        Product product;
        product = dataStoreForProduct.find(this.getProductId());
        if (product != null) {
            productPrice = product.getDefaultPrice();
        }
    }

    public float getProductPrice() {
        return productPrice;
    }


}
