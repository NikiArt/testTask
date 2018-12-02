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
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
