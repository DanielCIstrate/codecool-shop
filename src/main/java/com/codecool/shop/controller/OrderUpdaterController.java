package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.LineItem;
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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/update-order"})
public class OrderUpdaterController extends HttpServlet {

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
            Order orderToUpdate = orderDataStore.find(orderId);
//            System.out.println("Found order instance with that id as "+ orderToUpdate.toString());  // debug print
            if (orderToUpdate != null && orderChunkAsNode.has("item_list")) {
                String itemListAsString = orderChunkAsNode.get("item_list").toString();
//                System.out.println("Item list:"+itemListAsString);  // debug logging
                List<LineItem> itemList = LineItem.deserializeListFromString(itemListAsString);
//                itemList.forEach(item -> System.out.println("Price for 1x: "+item.getProductPrice()));  // debug logging
                orderToUpdate.rebuildFromLineItems(itemList);
//                System.out.println("Total Price: "+orderToUpdate.getTotalPrice());    // debug log
            }
        }
    }
}
