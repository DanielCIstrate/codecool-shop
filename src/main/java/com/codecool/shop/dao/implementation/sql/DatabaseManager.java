package com.codecool.shop.dao.implementation.sql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.SQLException;



public class DatabaseManager {
    private static DatabaseManager instance;
    private ProductDaoJdbc productDao;
    private SupplierDaoJdbc supplierDao;
    private ProductCategoryDaoJdbc productCategoryDao;
    private UserDaoJdbc userDao;

    private DatabaseManager() throws SQLException, InvalidKeyException {
        this.setup();
    }


    public void setup() throws SQLException, InvalidKeyException {
        DataSource dataSource = connect();
        Connection commonConnection = dataSource.getConnection();
        commonConnection.setAutoCommit(true);

        productDao = new ProductDaoJdbc(dataSource);
        supplierDao = new SupplierDaoJdbc(dataSource);
        productCategoryDao = new ProductCategoryDaoJdbc(dataSource);
        userDao = new UserDaoJdbc(dataSource);

    }

    public ProductDaoJdbc getProductDao() {
        return productDao;
    }

    public SupplierDaoJdbc getSupplierDao() {
        return supplierDao;
    }

    public ProductCategoryDaoJdbc getProductCategoryDao() {
        return productCategoryDao;
    }

    public UserDaoJdbc getUserDao() {return userDao;}
    
    public static DatabaseManager getInstance() throws SQLException, InvalidKeyException {

            if (instance == null) {
                DatabaseManager newManager = new DatabaseManager();
                return newManager;

            }
            return instance;

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
