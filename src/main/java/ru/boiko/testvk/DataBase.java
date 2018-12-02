package ru.boiko.testvk;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBase {
    private static String LOGIN_DB = "root";
    private static String PASSWORD_DB = "03892459";
    private static String SQL = "jdbc:mysql://localhost/";
    private static String NAME_DB = "test_vk";

    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;

    final ExcelExport excelExport;

    public DataBase(final ExcelExport excelExport) {
        this.excelExport = excelExport;
        checkDbExist();
    }

    @SneakyThrows
    private void checkDbExist() {
        try {
            Boolean baseExist = false;
            connection = DriverManager.getConnection(SQL + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            resultSet = connection.getMetaData().getCatalogs();

            while (resultSet.next()) {
                final String databaseName = resultSet.getString(1);
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
    public void addUser(@NotNull final Integer id, final String firstName, final String lastName) {
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `users` WHERE `id` = ?");
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
    public void addFriend(@NotNull final Integer userId, @NotNull final Integer friendId) {
        final String uuid = UUID.randomUUID().toString();
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `friends` WHERE `user` = ? AND `friend` = ?");
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

    @SneakyThrows
    public List<Integer> getFriends(@NotNull final List<Integer> usersid) {
        final List<Integer> friends = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            statement = connection.createStatement();
            String sql = "SELECT friend FROM friends WHERE user IN (";
            for (int i = 0; i < usersid.size(); i++) {
                sql += usersid.get(i) + ((i == (usersid.size() - 1)) ? ")" : ", ");
            }
            final ResultSet resultSet = statement.executeQuery(sql);
            int i = 1;
            while ( resultSet != null && resultSet.next() ) {
                friends.add(resultSet.getInt(1));
            }
        }finally {
            if(statement!=null) {    statement.close();  }
            if(resultSet!=null) {    resultSet.close();  }
            if(connection!=null) {   connection.close(); }
        }
        return friends;
    }

    @SneakyThrows
    public List<Integer> getClosestFriends(@NotNull final List<Integer> usersid) {
        final List<Integer> friends = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            statement = connection.createStatement();
            final String requestValues = usersid.toString().replace("[","(").replace("]", ")");
            final String sql = "SELECT * FROM friends WHERE friend IN " + requestValues + " AND user IN " + requestValues;
            System.out.println(sql);
            final ResultSet resultSet = statement.executeQuery(sql);
            while ( resultSet != null && resultSet.next() ){
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                friends.add(resultSet.getInt(3));
            }
        }finally {
            if(connection!=null) {   connection.close(); }
        }
        return friends;
    }

    @SneakyThrows
    public List<Integer> getUserList() {
        final List<Integer> usersid = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            statement = connection.createStatement();
            final String sql = "SELECT id FROM users";
            final ResultSet resultSet = statement.executeQuery(sql);
            while ( resultSet != null && resultSet.next() ){
                usersid.add(resultSet.getInt(1));
            }
        }finally {
            if(connection!=null) {   connection.close(); }
        }

        return usersid;
    }

    @SneakyThrows
    public List<Integer> getNearestFriends(@NotNull final List<Integer> usersid) {
        final List<Integer> friends = new ArrayList<>();
        try {
        connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
        statement = connection.createStatement();
        final String requestValues = usersid.toString().replace("[","(").replace("]", ")");
        final String sql = "SELECT friend, user, COUNT(*) c FROM friends WHERE user in " + requestValues + " GROUP BY friend HAVING c > 1";
        final ResultSet resultSet = statement.executeQuery(sql);
        int i = 1;
        while ( resultSet != null && resultSet.next() ) {
            if (resultSet.getInt(2) > 1) {
                System.out.println(i + " - " + resultSet.getString(1) + " - " + resultSet.getString(2));
                friends.add(resultSet.getInt(1));
                i++;
            }
        }
        }finally {
            if(statement!=null) {    statement.close();  }
            if(resultSet!=null) {    resultSet.close();  }
            if(connection!=null) {   connection.close(); }
        }
        return friends;
    }

    @SneakyThrows
    public Boolean isEmpty() {
        Boolean isEmpty = true;
        connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
        statement = connection.createStatement();
        final String sql = "SELECT COUNT(id) FROM users";
        final ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        if (resultSet.getInt(1) > 0) isEmpty = false;
        return isEmpty;
    }

    @SneakyThrows
    public void writeFile(@NotNull final List<Integer> usersid, @Nullable final List<Integer> friendsid) {
        try {
            connection = DriverManager.getConnection(SQL + NAME_DB + "?user=" + LOGIN_DB + "&password=" + PASSWORD_DB);
            statement = connection.createStatement();
            final String requestValuesUser = usersid.toString().replace("[","(").replace("]", ")");
            final String requestValuesFriend = " AND friend in " + ((friendsid != null) ? usersid.toString().replace("[","(").replace("]", ")") : "");
            final String sql = "SELECT * FROM friends WHERE user in " + requestValuesUser + requestValuesFriend;
            final ResultSet resultSet = statement.executeQuery(sql);
            while ( resultSet != null && resultSet.next() ) {
                excelExport.writeString(resultSet.getString(2) + "," + resultSet.getString(3));
            }
        }finally {
            if(statement!=null) {    statement.close();  }
            if(resultSet!=null) {    resultSet.close();  }
            if(connection!=null) {   connection.close(); }
        }
    }


}
