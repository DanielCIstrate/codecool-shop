package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {

    private List<Order> data;
    private static OrderDaoMem instance = null;

    private OrderDaoMem() {
         this.data = new ArrayList<>();
    }


    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        order.setId(data.size() + 1);
        data.add(order);
    }

    @Override
    public Order find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Order> getAll() {
        return data;
    }

    @Override
    public boolean hasOrderFromUser(int userId) {
        return data.stream()
                .anyMatch(order -> order.getSenderId() == userId);
    }

    @Override
    public Order getFirstOrderFrom(int userId) {
        return data.stream()
                .filter(order -> order.getSenderId() == userId)
                .findFirst().orElse(null);
    }
}
