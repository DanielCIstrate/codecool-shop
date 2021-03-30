package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ProductDaoJdbc implements ProductDao {
    private DataSource dataSource;
    public ProductDaoJdbc(DataSource newDataSource) {
        this.dataSource = newDataSource;
    }

    @Override
    public void add(Product product) {
        try(Connection connectionObject = dataSource.getConnection()) {
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
        // 2. get just the Product object information and get the Supplier and CategoryProduct via other methods
        String currentName;
        float currentPrice;
        String currentCurrencyType;
        String currentDescription;

        try (Connection connectionObject = dataSource.getConnection()) {
  
            String sqlQuery = "SELECT (id, name, description, default_price, default_currency) FROM product"
            + "WHERE (id = ?)";
        PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
        precompiledQuery.setInt(1, id);
        precompiledQuery.executeQuery();
        ResultSet  resultSet = precompiledQuery.getGeneratedKeys();
        resultSet.next();
         
        
        
        } catch (SQLException error) {
            
        }


        
        // get Supplier object for supplier_id - WORRY ABOUT DUPLICATION LATER
        // ... Supplier currentSupplier = SupplierDaoJdbc.find(supplier_id)

        // get ProductCategory object for category_id
        // ... ProductCategory currentCategory = ProductCategoryDaoJdbc.find(category_id)

        Product = new Product(
            currentName,
            currentPrice,
            currentCurrencyType,
            description,
            currentCategory,
            currentSupplier
        );
        // Product.setId()
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }

    @Override
    public List<Product> getIntersection(ProductCategory someCategory, Supplier someSupplier) {
        return null;
    }
}
