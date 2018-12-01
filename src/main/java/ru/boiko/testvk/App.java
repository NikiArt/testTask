package ru.boiko.testvk;

import ru.boiko.testvk.database.DataBase;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        DataBase dataBase = new DataBase();
        VkApi vkApi = new VkApi(dataBase);
        //vkApi.getUserInfoById("468849451");
        vkApi.getFriendsById(481912255);
    }
}
