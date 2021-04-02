package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    private OrderDao orderDataStore = OrderDaoMem.getInstance();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String requestData = reader.lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        final ObjectNode orderChunkAsNode = objectMapper.readValue(requestData, ObjectNode.class);
        if (orderChunkAsNode.has("orderId")) {
//            System.out.println("Got order id=" + orderChunkAsNode.get("orderId"));  // debug logging
            int orderId = orderChunkAsNode.get("orderId").asInt();
            Order orderToCheckOut = orderDataStore.find(orderId);
        }
    }

}
