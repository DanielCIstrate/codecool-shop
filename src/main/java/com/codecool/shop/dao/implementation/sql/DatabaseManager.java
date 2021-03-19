package com.codecool.shop.dao.implementation.sql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.security.InvalidKeyException;
import java.sql.SQLException;



public class DatabaseManager {
    private ProductDaoJdbc productDao;

    public void setup() throws SQLException, InvalidKeyException {
        DataSource dataSource = connect();

        productDao = new ProductDaoJdbc(dataSource);

    }

    private DataSource connect() throws SQLException, InvalidKeyException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        String databaseName = System.getenv("PSQL_DB_NAME");
        String userName = System.getenv("PSQL_USER_NAME");
        String userPassword = System.getenv("PSQL_PASSWORD");

        if (databaseName == null || userName == null || userPassword == null) {
            throw new InvalidKeyException("Some PSQL_.. environment variables are missing!");
        }

        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(userName);
        dataSource.setPassword(userPassword);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
