package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/create-order"})
@MultipartConfig
public class OrderCreatorController extends HttpServlet {

    private OrderDao orderDataStore = OrderDaoMem.getInstance();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
//        System.out.println("Create Order has entered POST method!");    // debug log
        BufferedReader reader = request.getReader();
        String requestData = reader.lines().collect(Collectors.joining());
//        System.out.println(requestData);    // debug log
        Order newOrder = Order.deserializeFromString(requestData);
        int senderId = newOrder.getSenderId();
        if ( orderDataStore.hasOrderFromUser(senderId) ) {
            newOrder = orderDataStore.getFirstOrderFrom(senderId);
        } else {
            addOrder(newOrder);
        }

        // Debug output for success
        //
        //System.out.printf("Successfully deserialized order sent by userId=%d and name %s \n",
        //        orders.get(orders.size()-1).getSenderId(),
        //       orders.get(orders.size()-1).getName()
        //);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("{\"orderId\":"+newOrder.getId()+"}");

    }




    public void addOrder(Order newOrder) {
        orderDataStore.add(newOrder);
    }


}
