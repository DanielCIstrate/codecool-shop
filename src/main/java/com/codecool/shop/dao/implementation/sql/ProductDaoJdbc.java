package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.security.InvalidKeyException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJdbc implements ProductDao {
    private DataSource dataSource;

    public ProductDaoJdbc(DataSource newDataSource) {
        this.dataSource = newDataSource;
    }

    @Override
    public void add(Product product) {
        try (Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO product (" +
                    "name, description, default_price, default_currency, category_id, supplier_id" +
                    ") VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setString(1, product.getName());
            precompiledQuery.setString(2, product.getDescription());
            precompiledQuery.setFloat(3, product.getDefaultPrice());
            precompiledQuery.setString(4, "USD");
            precompiledQuery.setInt(5, product.getProductCategory().getId());
            precompiledQuery.setInt(6, product.getSupplier().getId());
            precompiledQuery.executeUpdate();

            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();
            resultCursor.next();
            product.setId(resultCursor.getInt(1));

        } catch (SQLException error) {
            throw new RuntimeException("Error while adding new Product.", error);
        }
    }

    @Override
    public Product find(int id) {
        // for duplication we could first check if id is in a list/set variable and only then get it from the SQL

        // 2. get just the Product object information and get the Supplier and CategoryProduct via other methods
        String currentName;
        float currentPrice;
        String currentCurrencyType;
        String currentDescription;
        int supplierId;
        int categoryId;

        try (Connection connectionObject = dataSource.getConnection()) {
  
            String sqlQuery = "SELECT name, description, default_price, default_currency, " +
                    "supplier_id, category_id FROM product " +
                    "WHERE (id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();
            ResultSet  resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                return null;
            }
            else {
                currentName = resultSet.getString(1);
                currentDescription = resultSet.getString(2);
                currentPrice = resultSet.getFloat(3);
                currentCurrencyType = resultSet.getString(4);
                supplierId = resultSet.getInt(5);
                categoryId = resultSet.getInt(6);
            }

        
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get Product with id="+id+" from DB!");
        }

        //TODO find a way not to use nulls here
        Supplier currentSupplier = null;
        ProductCategory currentCategory = null;

        try {
        // WORRY ABOUT DUPLICATION LATER
            currentSupplier = DatabaseManager.getInstance().getSupplierDao().find(supplierId);

        // get ProductCategory object for category_id
            currentCategory = DatabaseManager.getInstance().getProductCategoryDao().find(categoryId);
        } catch (SQLException | InvalidKeyException e) {

            throw new RuntimeException("Could not connect Category and Supplier when getting Product", e);
        }

        Product foundProduct = new Product(
            currentName,
            currentPrice,
            currentCurrencyType,
            currentDescription,
            currentCategory,
            currentSupplier
        );
        foundProduct.setId(id);
        return foundProduct;
    }

    @Override
    public void remove(int id) {
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "DELETE FROM product WHERE (id = ?) " +
                    "RETURNING name";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not remove Product id = " + id );
            }

        } catch (SQLException err) {
            throw new RuntimeException("Error while trying to remove Product with id="+id);
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> listOfAllProducts = new ArrayList<>();
        String productName;
        float productPrice;
        String productCurrencyType;
        String productDescription;
        int productId;
        int supplierId;
        int categoryId;

        try (Connection connectionObject = dataSource.getConnection()) {

            String sqlQuery = "SELECT id, name, description, default_price, default_currency, " +
                    "supplier_id, category_id FROM product";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.executeQuery();
            ResultSet  resultSet = precompiledQuery.getResultSet();
            while (resultSet.next()) {
                productId = resultSet.getInt(1);
                productName = resultSet.getString(2);
                productDescription = resultSet.getString(3);
                productPrice = resultSet.getFloat(4);
                productCurrencyType = resultSet.getString(5);
                supplierId = resultSet.getInt(6);
                categoryId = resultSet.getInt(7);

                //TODO find a way not to use nulls here
                // REFACTOR LATER
                Supplier currentSupplier = null;
                ProductCategory currentCategory = null;

                try {
                    // WORRY ABOUT DUPLICATION LATER
                    currentSupplier = DatabaseManager.getInstance().getSupplierDao().find(supplierId);

                    // get ProductCategory object for category_id
                    currentCategory = DatabaseManager.getInstance().getProductCategoryDao().find(categoryId);
                } catch (SQLException | InvalidKeyException e) {

                    throw new RuntimeException("Could not connect Category and Supplier when getting Product", e);
                }

                Product foundProduct = new Product(
                        productName,
                        productPrice,
                        productCurrencyType,
                        productDescription,
                        currentCategory,
                        currentSupplier
                );
                foundProduct.setId(productId);
                listOfAllProducts.add(foundProduct);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get all Products.");
        }

        
        return listOfAllProducts;

    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> listOfAllProducts = new ArrayList<>();
        String productName;
        float productPrice;
        String productCurrencyType;
        String productDescription;
        int productId;
        int supplierId = supplier.getId();
        int categoryId;

        try (Connection connectionObject = dataSource.getConnection()) {

            String sqlQuery = "SELECT id, name, description, default_price, default_currency, " +
                    "category_id FROM product " + 
                    "WHERE (supplier_id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, supplierId);
            precompiledQuery.executeQuery();
            ResultSet  resultSet = precompiledQuery.getResultSet();
            while (resultSet.next()) {
                productId = resultSet.getInt(1);
                productName = resultSet.getString(2);
                productDescription = resultSet.getString(3);
                productPrice = resultSet.getFloat(4);
                productCurrencyType = resultSet.getString(5);
                categoryId = resultSet.getInt(6);

                //TODO find a way not to use nulls here
                // REFACTOR LATER
                ProductCategory currentCategory = null;

                try {
                    // get ProductCategory object for category_id
                    currentCategory = DatabaseManager.getInstance().getProductCategoryDao().find(categoryId);
                } catch (SQLException | InvalidKeyException e) {
                    throw new RuntimeException("Could not connect Category when getting Product", e);
                }
                Product foundProduct = new Product(
                        productName,
                        productPrice,
                        productCurrencyType,
                        productDescription,
                        currentCategory,
                        supplier
                );
                foundProduct.setId(productId);
                listOfAllProducts.add(foundProduct);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get products by supplier.");
        }
        return listOfAllProducts;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> listOfAllProducts = new ArrayList<>();
        String productName;
        float productPrice;
        String productCurrencyType;
        String productDescription;
        int productId;
        int supplierId;
        int categoryId = productCategory.getId();

        try (Connection connectionObject = dataSource.getConnection()) {

            String sqlQuery = "SELECT id, name, description, default_price, default_currency, " +
                    "supplier_id FROM product " +
                    "WHERE (category_id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, categoryId);
            precompiledQuery.executeQuery();
            ResultSet  resultSet = precompiledQuery.getResultSet();
            while (resultSet.next()) {
                productId = resultSet.getInt(1);
                productName = resultSet.getString(2);
                productDescription = resultSet.getString(3);
                productPrice = resultSet.getFloat(4);
                productCurrencyType = resultSet.getString(5);
                supplierId = resultSet.getInt(6);

                //TODO find a way not to use nulls here
                // REFACTOR LATER
                Supplier currentSupplier = null;

                try {
                    // get ProductCategory object for category_id
                    currentSupplier = DatabaseManager.getInstance().getSupplierDao().find(supplierId);
                } catch (SQLException | InvalidKeyException e) {
                    throw new RuntimeException("Could not connect Supplier when getting Product", e);
                }
                Product foundProduct = new Product(
                        productName,
                        productPrice,
                        productCurrencyType,
                        productDescription,
                        productCategory,
                        currentSupplier
                );
                foundProduct.setId(productId);
                listOfAllProducts.add(foundProduct);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get products by category.");
        }
        return listOfAllProducts;
    }
}
