package ru.boiko.testvk;

import lombok.SneakyThrows;

import java.sql.*;
import java.util.UUID;

public class DataBase {
    private static String LOGIN_DB = "root";
    private static String PASSWORD_DB = "03892459";
    private static String SQL = "jdbc:mysql://localhost/";
    private static String NAME_DB = "test_vk";

    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;

    public DataBase() {
        checkDbExist();
    }

    @SneakyThrows
    private void checkDbExist() {
        try {
            Boolean baseExist = false;
            connection = DriverManager.getConnection(SQL + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            resultSet = connection.getMetaData().getCatalogs();

            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(NAME_DB)) {
                    baseExist = true;
                    break;
                }
            }

            if (!baseExist) {
                System.out.println("Creating database");
                statement = connection.createStatement();
                int Result = statement.executeUpdate("CREATE DATABASE " + NAME_DB);
                System.out.println("Database \"" + NAME_DB + "\" was created");
                statement.close();
            }

            checkTableExist();
        } finally {
            if(statement!=null) {    statement.close();  }
            if(resultSet!=null) {    resultSet.close();  }
            if(connection!=null) {   connection.close(); }
        }
    }

    @SneakyThrows
    private void checkTableExist() {
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            statement = connection.createStatement();
            String table;
            DatabaseMetaData md = connection.getMetaData();
            resultSet = md.getTables(null, null, "users", null);
            if (!resultSet.next()) {
                table = "CREATE TABLE users " +
                        "(id INTEGER not NULL, " +
                        " name VARCHAR(50), " +
                        " last_name VARCHAR (50), " +
                        " PRIMARY KEY (id))";

                statement.executeUpdate(table);
            }

            resultSet = md.getTables(null, null, "friends", null);
            if (!resultSet.next()) {
                table = "CREATE TABLE friends " +
                        "(id VARCHAR(100) not NULL, " +
                        " user INTEGER not NULL, " +
                        " friend INTEGER not NULL, " +
                        " PRIMARY KEY (id))";

                statement.executeUpdate(table);
            }
        }finally {
            if(statement!=null) {    statement.close();  }
            if(resultSet!=null) {    resultSet.close();  }
            if(connection!=null) {   connection.close(); }
        }
    }

    @SneakyThrows
    public void addUser(Integer id, String firstName, String lastName) {
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            PreparedStatement statement = connection.prepareStatement("select 1 from `users` where `id` = ?");
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    if(connection!=null) {   connection.close(); }
                    return;
                }
            }
            statement = connection.prepareStatement("INSERT INTO users (`id`, `name`, `last_name`) VALUES (?, ?, ?)");
            statement.setInt(1,id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.execute();
        }finally {
            if(connection!=null) {   connection.close(); }
        }
    }

    @SneakyThrows
    public void addFriend(Integer userId, Integer friendId) {
        String uuid = UUID.randomUUID().toString();
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            PreparedStatement statement = connection.prepareStatement("select 1 from `friends` where `user` = ? and `friend` = ?");
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    if(connection!=null) {   connection.close(); }
                    return;
                }
            }
            statement = connection.prepareStatement("INSERT INTO friends (`id`, `user`, `friend`) VALUES (?, ?, ?)");
            statement.setString(1,uuid);
            statement.setInt(2, userId);
            statement.setInt(3, friendId);
            statement.execute();
        }finally {
            if(connection!=null) {   connection.close(); }
        }
    }


}
