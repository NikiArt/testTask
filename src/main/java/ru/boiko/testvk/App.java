package ru.boiko.testvk;

import ru.boiko.testvk.database.DataBase;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        int[] userids = {481912255, 473985858, 469584270, 468849451, 454992562, 437930392, 432905063, 427347865, 423967541, 418024900};
        DataBase dataBase = new DataBase();
        VkApi vkApi = new VkApi(dataBase);
        for (int userid : userids) {
            vkApi.getFriendsById(userid);
        }

    }
}
