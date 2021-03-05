package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
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
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(
                request.getServletContext()
        );
        WebContext context = new WebContext(request, response, request.getServletContext());
        engine.process("cart.html", context, response.getWriter());
    }
}
