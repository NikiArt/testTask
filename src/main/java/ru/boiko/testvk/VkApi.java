package ru.boiko.testvk;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.boiko.testvk.DataBase;

import java.util.Iterator;
import java.util.List;

public class VkApi {
    private static String APPLICATION_ID = "";
    private static String SECRET_ID = "";
    private static String APP_TOKEN = "5335221d5335221d5335221ddc535265d9553355335221d0f373febfb5b04c63e9df51a";

    private String authToken;

    final DataBase dataBase;

    public VkApi(final DataBase dataBase) {
        this.dataBase = dataBase;
    }


    public void getFriendsById(@NotNull final Integer id) {
        getUserInfoById(id);
        final String uri = "https://api.vk.com/method/friends.get?user_id=" + id + "&fields=bdate&access_token=" + APP_TOKEN + "&v=5.92";
        final String data = makeRequest(uri);
        final JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("error")) {
            final JSONObject responseObject = (JSONObject) jsonObject.get("error");
            System.out.println(responseObject.getString("error_msg"));
            return;
        }
        if (jsonObject.has("response")) {
            final JSONObject responseObject = (JSONObject) jsonObject.get("response");
            final JSONArray jsonArray = responseObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentValue = jsonArray.getJSONObject(i);
                dataBase.addUser(currentValue.getInt("id"),currentValue.getString("first_name"),currentValue.getString("last_name"));
                dataBase.addFriend(id, currentValue.getInt("id"));
            }
        }
    }

    /**
     * if need it to iterate through the values, use the code below
     * Iterator<?> keys = jsonObject1.keys();
     * while (keys.hasNext()) {
     * String key = (String) keys.next();
     * if (jsonObject1.get(key) instanceof String) {
     * String val = jsonObject1.getString(key);
     * System.out.println(val);
     * }
     * }
     */

    @SneakyThrows
    public void getUserInfoById(@NotNull final Integer id) {
        final String uri = "https://api.vk.com/method/users.get?user_id=" + id + "&fields=bdate&access_token=" + APP_TOKEN + "&v=5.92";
        final String data = makeRequest(uri);
        final JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("error")) {
            final JSONObject responseObject = (JSONObject) jsonObject.get("error");
            System.out.println(responseObject.getString("error_msg"));
            return;
        }
        if (jsonObject.has("response")) {
            final JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject currentValue = jsonArray.getJSONObject(i);
                dataBase.addUser(currentValue.getInt("id"),currentValue.getString("first_name"),currentValue.getString("last_name"));
            }
        }

    }

    @SneakyThrows
    private void getAuthToken() {
        final String uri = "https://oauth.vk.com/access_token?client_id=" + APPLICATION_ID + "&client_secret=" + SECRET_ID + "&v=5.92&grant_type=client_credentials";
        final String data = makeRequest(uri);
        final JSONObject jsonObject = new JSONObject(data);
        try {
            authToken = jsonObject.getString("access_token");
            System.out.println(authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private String makeRequest(final String uri) {
        final HttpClient httpClient = HttpClients.createDefault();
        final HttpPost httpPost = new HttpPost(uri);

        final HttpResponse response = httpClient.execute(httpPost);
        final HttpEntity entity = response.getEntity();
        final String data = EntityUtils.toString(entity);
        return data;
    }
}
