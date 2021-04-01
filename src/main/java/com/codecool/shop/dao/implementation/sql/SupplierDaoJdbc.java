package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {
    private DataSource dataSource;

    public SupplierDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void add(Supplier supplier) {
        try(Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO supplier (" +
                    "name, description" +
                    ") VALUES (?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setString(1, supplier.getName());
            precompiledQuery.setString(2, supplier.getDescription());
            precompiledQuery.executeUpdate();

            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();
            resultCursor.next();
            supplier.setId(resultCursor.getInt(1));

        } catch (SQLException error) {
            throw new RuntimeException("Error while adding new supplier.", error);
        }
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
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "DELETE FROM supplier WHERE (id = ?) " +
                    "RETURNING name";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not remove Supplier id = " + id );
            }

        } catch (SQLException err) {
            throw new RuntimeException("Error while trying to remove Supplier with id="+id);
        }

    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> listofAllSuppliers = new ArrayList<>();
        String currentName;
        String currentDescription;
        int currentId;
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "SELECT id, name, description FROM supplier";

            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getGeneratedKeys();

            while (resultSet.next()) {
                currentId = resultSet.getInt(1);
                currentName = resultSet.getString(2);
                currentDescription = resultSet.getString(3);


                Supplier foundSupplier = new Supplier(currentName,currentDescription);
                foundSupplier.setId(currentId);

                listofAllSuppliers.add(foundSupplier);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get all Suppliers from DB!");
        }
        return listofAllSuppliers;
    }
}
