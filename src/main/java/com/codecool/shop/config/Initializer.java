package com.codecool.shop.config;

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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.security.InvalidKeyException;
import java.sql.SQLException;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ProductDao productDataStore;
        ProductCategoryDao productCategoryDataStore;
        SupplierDao supplierDataStore;
        boolean useDaoMem = false;
        if (useDaoMem) {
            productDataStore = ProductDaoMem.getInstance();
            productCategoryDataStore = ProductCategoryDaoMem.getInstance();
            supplierDataStore = SupplierDaoMem.getInstance();

            //setting up a new supplier
            ///getSuppliers
            Supplier amazon = new Supplier("Amazon", "Digital content and services");
            supplierDataStore.add(amazon);
            Supplier lenovo = new Supplier("Lenovo", "Computers");
            supplierDataStore.add(lenovo);
            Supplier dell = new Supplier("Dell", "Computers");
            supplierDataStore.add(dell);

            //setting up new product categories

            //TODO When implementing JDBC check if this could be written differently in the DAO
            ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
            ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "A small portable computer that can run on battery power and has the main parts (as keyboard and display screen) combined into a single unit.");
            ProductCategory desktop = new ProductCategory("Desktop Computer", "Hardware", "A general-purpose computer equipped with a microprocessor and designed to run especially commercial software (such as a word processor or Internet browser) for an individual user");
            productCategoryDataStore.add(tablet);
            productCategoryDataStore.add(laptop);
            productCategoryDataStore.add(desktop);

            //setting up products and printing it
            productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
            productDataStore.add(new Product("Lenovo IdeaPad Mix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
            productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
            productDataStore.add(new Product("Dell Inspiron 250", 400.01f, "USD", "Meh", laptop, dell));
            productDataStore.add(new Product("Dell Inspiron 330", 249.99f, "USD", "Meh x Meh", laptop, dell));
            productDataStore.add(new Product("Gaming DEck", 350, "USD", "Shine bright like a diamond", desktop, dell));

//        System.out.println(productCategoryDataStore.find(1).getName());

        } else {
            try {
                DatabaseManager database = DatabaseManager.getInstance();
                productDataStore = database.getProductDao();
                productCategoryDataStore = database.getProductCategoryDao();
                supplierDataStore = database.getSupplierDao();

            } catch (SQLException | InvalidKeyException e) {
                throw new RuntimeException("Could not get DB manager", e);
            }
        }


    }
}