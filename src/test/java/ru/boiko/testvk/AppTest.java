package ru.boiko.testvk;

import lombok.SneakyThrows;
import org.junit.Test;

import java.sql.*;

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
}
