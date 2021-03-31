package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {
    private DataSource dataSource;

    public SupplierDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void add(Supplier supplier) {

    }

    @Override
    public Supplier find(int id) throws IllegalArgumentException {
        String currentName;
        String currentDescription;
        try (Connection connectionObject = dataSource.getConnection()) {

            String sqlQuery = "SELECT name, description FROM supplier " +
                    "WHERE (id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find supplier id = " + id);
            }
            else {
                currentName = resultSet.getString(1);
                currentDescription = resultSet.getString(2);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get Product with id="+id+" from DB!");
        }

        Supplier foundSupplier = new Supplier(currentName, currentDescription);
        foundSupplier.setId(id);
        return foundSupplier;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Supplier> getAll() {
        return null;
    }
}
