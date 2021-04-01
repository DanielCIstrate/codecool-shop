package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private DataSource dataSource;

    public ProductCategoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ProductCategory category) {
        try(Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO category (" +
                    "name, description, department" +
                    ") VALUES (?, ?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setString(1, category.getName());
            precompiledQuery.setString(2, category.getDescription());
            precompiledQuery.setString(3, category.getDepartment());
            precompiledQuery.executeUpdate();

            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();
            resultCursor.next();
            category.setId(resultCursor.getInt(1));

        } catch (SQLException error) {
            throw new RuntimeException("Error while adding new product category.", error);
        }

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
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "DELETE FROM category WHERE (id = ?) " +
                    "RETURNING name";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not remove ProductCategory id = " + id );
            } 

        } catch (SQLException err) {
            throw new RuntimeException("Error while trying to remove ProductCategory with id="+id);
        }
    }

    @Override
    public List<ProductCategory> getAll() {
        List<ProductCategory> listOfAllCategories = new ArrayList<>();
        String currentName;
        String currentDescription;
        String currentDepartment;
        int currentId;
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "SELECT id, name, description, department FROM category";

            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getGeneratedKeys();

            while (resultSet.next()) {
                currentId = resultSet.getInt(1);
                currentName = resultSet.getString(2);
                currentDescription = resultSet.getString(3);
                currentDepartment = resultSet.getString(4);

                ProductCategory foundCategory = new ProductCategory(currentName, currentDepartment, currentDescription);
                foundCategory.setId(currentId);

                listOfAllCategories.add(foundCategory);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get all ProductCategories from DB!");
        }
        return listOfAllCategories;
    }
    
}
