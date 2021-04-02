package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.dao.implementation.sql.DatabaseManager;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.security.InvalidKeyException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {""})        // localhost/?sort-category=2
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean useDaoMem = true;
        ProductDao productDataStore;
        ProductCategoryDao productCategoryDataStore;
        SupplierDao supplierDataStore;
        if (useDaoMem) {
            productDataStore = ProductDaoMem.getInstance();
            productCategoryDataStore = ProductCategoryDaoMem.getInstance();
            supplierDataStore = SupplierDaoMem.getInstance();
        } else {
            DatabaseManager database;
            try {
                database = DatabaseManager.getInstance();
            } catch (SQLException | InvalidKeyException e) {
                throw new RuntimeException("Could not get DB manager", e);
            }
            productDataStore = database.getProductDao();
            productCategoryDataStore = database.getProductCategoryDao();
            supplierDataStore = database.getSupplierDao();
        }


        String categoryIdAsString;
        try {
            categoryIdAsString = req.getParameter("category");
        } catch (NullPointerException e) {
            categoryIdAsString = "";
        }


        int categoryId = processIdInput(categoryIdAsString);

        String supplierIdAsString;

        try {
            supplierIdAsString = req.getParameter("supplier");
        } catch (NullPointerException e) {
            supplierIdAsString = "";
        }
        int supplierId = processIdInput(supplierIdAsString);





        List<Product> productsByCategory = displayFilteredByCategory(
                categoryId,
                productDataStore,
                productCategoryDataStore
        );
        List<Product> productsBySupplier = displayFilteredBySupplier(
                supplierId, productDataStore, supplierDataStore
        );

        /*  This is where we perform a filter action on the intersection
        * between supplier x category   */
        productsByCategory.retainAll(productsBySupplier);

        List<Product> finalList = productsByCategory;


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("products", finalList);

        engine.process("product/index.html", context, resp.getWriter());
    }



    private int processIdInput(String categoryIdAsString) {
        int categoryId = -1;
        try {
            categoryId = Integer.parseInt(categoryIdAsString);
        } catch (NumberFormatException ignored) {

        }

        return categoryId;
    }

    public static ProductDao getProductDaoInstance() {
        return ProductDaoMem.getInstance();
    }

    private List<Product> displayFilteredByCategory(
            int categoryId,
            ProductDao productDataStore,
            ProductCategoryDao productCategoryDataStore
    ) {
        try {
            if (categoryId == -1) {
                return productDataStore.getAll();
            } else {
                ProductCategory selectedCategory = productCategoryDataStore.find(categoryId);
                return productDataStore.getBy(selectedCategory);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Product> displayFilteredBySupplier(
            int idSupplier,
            ProductDao productDataStore,
            SupplierDao supplierDataStore
    ) {
        try {
            if (idSupplier == -1) {
                return productDataStore.getAll();
            } else {
                Supplier selectedSupplier = supplierDataStore.find(idSupplier);
                return productDataStore.getBy(selectedSupplier);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
