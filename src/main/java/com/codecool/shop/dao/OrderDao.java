package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.util.List;

public interface OrderDao {


    void add(Order order);
    Order find(int id);
    void remove(int id);

    List<Order> getAll();
    boolean hasOrderFromUser(int userId);
    Order getFirstOrderFrom(int userId);
}
