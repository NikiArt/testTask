package ru.boiko.testvk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author Nikita Boiko
 *
 */
public class App {
    private static Boolean DEEP_CHECK = false;

    public static void main( String[] args ) {
        final List<Integer> userids = new ArrayList<>(Arrays.asList(481912255, 473985858, 469584270, 468849451, 454992562, 437930392, 432905063, 427347865, 423967541, 418024900));
        final ExcelExport excelExport = new ExcelExport();
        final DataBase dataBase = new DataBase(excelExport);
        final VkApi vkApi = new VkApi(dataBase);

        if (dataBase.isEmpty()) {
            for (Integer userid : userids) {
                vkApi.getFriendsById(userid);
            }
        }

        final List<Integer> closestFriends = dataBase.getClosestFriends(userids);
        if (closestFriends.size() > 0) dataBase.writeFile(userids, closestFriends);
        final List<Integer> nearestFriends = dataBase.getNearestFriends(userids);
        if (nearestFriends.size() > 0) dataBase.writeFile(userids, nearestFriends);
        if (DEEP_CHECK) {
            final List<Integer> friends = dataBase.getFriends(userids);
            final List<Integer> closestFriendsByFriends = dataBase.getClosestFriends(friends);
            if (nearestFriends.size() > 0) {
               dataBase.writeFile(userids, closestFriendsByFriends);
               dataBase.writeFile(closestFriendsByFriends, closestFriendsByFriends);
            }
        }
    }


}
