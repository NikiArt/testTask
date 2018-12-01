package ru.boiko.testvk;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        VkApi vkApi = new VkApi();
        vkApi.getUserInfoById("468849451");
        vkApi.getFriendsById("468849451");
    }
}
