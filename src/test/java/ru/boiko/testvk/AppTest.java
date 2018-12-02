package ru.boiko.testvk;

import lombok.SneakyThrows;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    @SneakyThrows
    public void authCallBack() {
    }

    @Test
    @SneakyThrows
    public void testDbConnection() {
        final String baseName = "test_vk";
        Boolean baseExist = false;

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=03892459"); //Open a connection

        ResultSet resultSet = connection.getMetaData().getCatalogs();

        while (resultSet.next()) {
            String databaseName = resultSet.getString(1);
            if (databaseName.equals(baseName)) {
                baseExist = true;
                break;
            }
        }

        if (!baseExist) {
            System.out.println("Creating database");
            Statement statement = connection.createStatement();
            int Result = statement.executeUpdate("CREATE DATABASE " + baseName);
            statement.close();
        }
        resultSet.close();
        connection.close();
    }

    @Test
    @SneakyThrows
    public void createTables(){
        Connection connection = null;
        Statement statement = null;
        try {
            System.out.println("Creating connection to database...");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test_vk?user=root&password=03892459");

            System.out.println("Creating table in selected database...");
            statement = connection.createStatement();


            String table;
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "users", null);
            if (!rs.next()) {
                table = "CREATE TABLE users " +
                        "(id VARCHAR (100) not NULL, " +
                        " name VARCHAR(50), " +
                        " last_name VARCHAR (50), " +
                        " PRIMARY KEY (id))";

                statement.executeUpdate(table);
            }


            rs = md.getTables(null, null, "friends", null);
            if (!rs.next()) {
                table = "CREATE TABLE friends " +
                        "(id VARCHAR (100) not NULL, " +
                        " user VARCHAR (100) not NULL, " +
                        " friend VARCHAR (100) not NULL, " +
                        " PRIMARY KEY (id))";

                statement.executeUpdate(table);
            }
        }finally {
            if(statement!=null){
                statement.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
    }

    @Test
    @SneakyThrows
    public void volume(){
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/test_vk?user=root&password=03892459");
        Statement statement = connection.createStatement();
        int rowCountUsers = 0;
        int rowCountFriends = 0;
        String sql = "SELECT * FROM friends WHERE user in (481912255, 473985858, 469584270, 468849451, 454992562, 437930392, 432905063, 427347865, 423967541, 418024900)";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.last();
        int rowCount = resultSet.getRow();
        System.out.println("friends - " + rowCount);
        while (true) {
            sql = "SELECT * FROM users";
            resultSet = statement.executeQuery(sql);
            resultSet.last();
            rowCount = resultSet.getRow();
            System.out.println("users - " + rowCount);

            sql = "SELECT * FROM friends";
            resultSet = statement.executeQuery(sql);
            resultSet.last();
            rowCount = resultSet.getRow();
            System.out.println("friends - " + rowCount);
            if (rowCount == rowCountFriends) break;

            rowCountFriends = rowCount;
            TimeUnit.SECONDS.sleep(20);
        }
    }

    @Test
    @SneakyThrows
    public void volumeFreinds(){
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/test_vk?user=root&password=03892459");
        Statement statement = connection.createStatement();
        //String sql = "SELECT DISTINCT friend FROM friends WHERE user in (481912255, 473985858, 469584270, 468849451, 454992562, 437930392, 432905063, 427347865, 423967541, 418024900)";
        String sql = "SELECT friend, COUNT(*) c FROM friends GROUP BY friend HAVING c > 1";
        ResultSet resultSet = statement.executeQuery(sql);
        int i = 1;
        while ( resultSet != null && resultSet.next() ) {
            if (resultSet.getInt(2) > 40) {
                System.out.println(i + " - " + resultSet.getString(1) + " - " + resultSet.getString(2));
                i++;
            }

        }
        resultSet.close();
        statement.close();
        connection.close();
        //ResultSet resultSetFriend;
        //writer.writeAll(myResultSet, includeHeaders);
        /*List<String> frrr = new ArrayList<>();
        int i = 0;
        while ( resultSet != null && resultSet.next() ) {
            frrr.add(resultSet.getString(1));
            i++;
        }
        System.out.println(i);
        for (String s : frrr) {
            System.out.println(s);
            sql = "SELECT * FROM friends WHERE user in (481912255, 473985858, 469584270, 468849451, 454992562, 437930392, 432905063, 427347865, 423967541, 418024900) AND friend = " + s;
            resultSet = statement.executeQuery(sql);
            resultSet.last();
            if (resultSet.getRow() > 1) {
                resultSet.first();
                System.out.print("friend - " + resultSet.getString(2) + " - users - ");
                while ( resultSet != null && resultSet.next() ){
                    System.out.print(resultSet.getString(3) + " ");
                }
                System.out.println();
            }
        }*/

        /*while ( resultSet != null && resultSet.next() ){
            System.out.println(resultSet.getString(1));
        }
        System.out.println("\n\n");
        resultSet.first();
        while ( resultSet != null && resultSet.next() ){
            System.out.println(resultSet.getString(2));
        }
        System.out.println("\n\n");
        resultSet.last();
        int rowCount = resultSet.getRow();
        System.out.println("friends - " + rowCount);*/
        //123
    }

    @Test
    @SneakyThrows
    public void closestF(){
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/test_vk?user=root&password=03892459");
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM friends WHERE friend = 179053837";
        ResultSet resultSet = statement.executeQuery(sql);
        while ( resultSet != null && resultSet.next() ){
            System.out.println(resultSet.getString(2) + " - " + resultSet.getString(3));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }


}
