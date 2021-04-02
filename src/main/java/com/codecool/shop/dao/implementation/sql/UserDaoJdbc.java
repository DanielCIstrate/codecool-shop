package com.codecool.shop.dao.implementation.sql;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoJdbc implements UserDao {
    private DataSource dataSource;

    public UserDaoJdbc(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    @Override
    public void add(User user, String password) {
        try(Connection connectionObject = dataSource.getConnection()) {
            String sqlQuery = "INSERT INTO public.user (" +
                    "name, password, email" +
                    ") VALUES (?, ?, ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(
                    sqlQuery,
                    Statement.RETURN_GENERATED_KEYS
            );


            precompiledQuery.setString(1, user.getName());
            precompiledQuery.setString(2, password);
            precompiledQuery.setString(3, user.getEmail());
            precompiledQuery.executeUpdate();

            ResultSet resultCursor = precompiledQuery.getGeneratedKeys();
            resultCursor.next();
            user.setId(resultCursor.getInt(1));

        } catch (SQLException error) {
            throw new RuntimeException("Error while adding new username.", error);
        }

    }

    @Override
    public User find(int id) {

        String currentName;
        String currentEmail;
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "SELECT name, email FROM public.user " +
                    "WHERE (id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find User id = " + id);
            } else {
                currentName = resultSet.getString(1);
                currentEmail = resultSet.getString(2);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get User with id=" + id + " from DB!");
        }

        User foundUser = new User(currentName, currentEmail);
        foundUser.setId(id);

        return foundUser;
    }


    @Override
    public void remove(int id) {

        try (
                Connection connectionObject = dataSource.getConnection()
        ) {
            String sqlQuery = "SELECT name, email FROM public.user " +
                    "WHERE (id = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setInt(1, id);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find User id = " + id + " to remove!");
            } else {
                sqlQuery = "DELETE FROM public.user WHERE (id = ?)";
                PreparedStatement anotherPQuery = connectionObject.prepareStatement(sqlQuery);
                anotherPQuery.setInt(1, id);
                anotherPQuery.executeQuery();
            }

        } catch (SQLException err) {
            throw new RuntimeException("Error while trying to remove User with id="+id);
        }

    }

    @Override
    public List<User> getAll() {
        List<User> listOfAllUsers = new ArrayList<>();
        String currentName;
        String currentEmail;
        int currentId;
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "SELECT id, name, email FROM public.user";

            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getResultSet();

            while (resultSet.next()) {

                currentId = resultSet.getInt(1);
                currentName = resultSet.getString(2);
                currentEmail = resultSet.getString(3);

                User foundUser = new User(currentName, currentEmail);
                foundUser.setId(currentId);

                listOfAllUsers.add(foundUser);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get all Users from DB!");
        }

        return listOfAllUsers;
    }

    @Override
    public String getPasswordFor(String username) {
        String password;
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "SELECT password FROM public.user " +
                    "WHERE (name = ?)";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setString(1, username);
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find User with name: " + username);
            } else {
                password = resultSet.getString(1);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get User with name=" + username + " from DB!");
        }



        return password;
    }

    @Override
    public void setPasswordFor(String username, String newPassword) {
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "UPDATE public.user SET password = ? " +
                    "WHERE (name = ?) RETURNING name";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setString(1, newPassword);
            precompiledQuery.setString(2, username);
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find User with name: " + username);
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to update User with name=" + username + " from DB!");
        }

    }

    @Override
    public Set<String> getUsernameSet() {
        Set<String> userNameSet = new HashSet<>();
        String currentName;                
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "SELECT name FROM public.user";

            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.executeQuery();

            ResultSet resultSet = precompiledQuery.getResultSet();

            while (resultSet.next()) {
                currentName = resultSet.getString(1);
                userNameSet.add(currentName);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to get set of user names from DB!");
        }

        return userNameSet;
    }

    @Override
    public void updateUser(User user) {
        try (
                Connection connectionObject = dataSource.getConnection()
        ) {

            String sqlQuery = "UPDATE public.user SET name = ?, email = ? " +
                    "WHERE (id = ?) RETURNING name";
            PreparedStatement precompiledQuery = connectionObject.prepareStatement(sqlQuery);
            precompiledQuery.setString(1, user.getName());
            precompiledQuery.setString(2, user.getEmail());
            precompiledQuery.executeQuery();
            ResultSet resultSet = precompiledQuery.getResultSet();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Could not find User with id: " + user.getId());
            }


        } catch (SQLException error) {
            throw new RuntimeException("Error while attempting to update User with id=" + user.getId() + " from DB!");
        }
    }
}
