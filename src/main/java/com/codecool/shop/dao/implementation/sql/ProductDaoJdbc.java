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
            ResultSet  resultSet = precompiledQuery.getGeneratedKeys();
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


        
        // WORRY ABOUT DUPLICATION LATER
        Supplier currentSupplier = DatabaseManager.getInstance().getSupplierDao().find(supplierId);

        // get ProductCategory object for category_id
        ProductCategory currentCategory = DatabaseManager.getInstance().getProductCategoryDao().find(categoryId);


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
