package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private DataSource dataSource;

    public ProductCategoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ProductCategory category) {

    }

    @Override
    public ProductCategory find(int id) throws IllegalArgumentException {
        String currentName;
        String currentDescription;
        String currentDepartment;
        try (Connection connectionObject = dataSource.getConnection()) {

            String sqlQuery = "SELECT name, description,department FROM category " +
                    "WHERE (id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find product category id = " + id);
            }
            else {
                currentName = resultSet.getString(1);
                currentDescription = resultSet.getString(2);
                currentDepartment = resultSet.getString(3);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get Product with id="+id+" from DB!");
        }

        ProductCategory foundCategory = new ProductCategory(currentName,currentDepartment,currentDescription);
        return foundCategory;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }

}
