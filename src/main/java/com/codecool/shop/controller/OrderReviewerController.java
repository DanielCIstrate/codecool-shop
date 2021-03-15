package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/view-cart"})
public class OrderReviewerController extends HttpServlet {

    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        Order currentOrder = null;
        Integer currentOrderId = null;
        try {
            currentOrderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            System.out.println("Could not parse int");
            System.out.println(e.getMessage());
        }
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(
                request.getServletContext()
        );
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        if (currentOrderId != null) {
            currentOrder = orderDataStore.find(currentOrderId);
        }
        WebContext context = new WebContext(request, response, request.getServletContext());
        if (currentOrder != null && currentOrder.getItemList() != null) {
            context.setVariable("order", currentOrder.getItemList());
        }

        engine.process("cart.html", context, response.getWriter());
    }
}
