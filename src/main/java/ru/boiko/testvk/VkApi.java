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

import java.util.Iterator;

public class VkApi {
    private static String APPLICATION_ID = "";
    private static String SECRET_ID = "";
    private static String APP_TOKEN = "5335221d5335221d5335221ddc535265d9553355335221d0f373febfb5b04c63e9df51a";

    private String authToken;



    public void getFriendsById(@NotNull final String id) {
        String uri = "https://api.vk.com/method/friends.get?user_id=" + id + "&fields=bdate&access_token=" + APP_TOKEN + "&v=5.92";
        String data = makeRequest(uri);
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("error")) {
            JSONObject responseObject = (JSONObject) jsonObject.get("error");
            System.out.println(responseObject.getString("error_msg"));
            return;
        }
        if (jsonObject.has("response")) {
            JSONObject responseObject = (JSONObject) jsonObject.get("response");
            JSONArray jsonArray = responseObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                Iterator<?> keys = jsonObject1.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (jsonObject1.get(key) instanceof String) {
                        String val = jsonObject1.getString(key);
                        System.out.println(val);
                    }
                }
            }
        }
    }

    @SneakyThrows
    public void getUserInfoById(@NotNull final String id) {
        String uri = "https://api.vk.com/method/users.get?user_id=" + id + "&fields=bdate&access_token=" + APP_TOKEN + "&v=5.92";
        String data = makeRequest(uri);
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("error")) {
            JSONObject responseObject = (JSONObject) jsonObject.get("error");
            System.out.println(responseObject.getString("error_msg"));
            return;
        }
        if (jsonObject.has("response")) {
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                Iterator<?> keys = jsonObject1.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (jsonObject1.get(key) instanceof String) {
                        String val = jsonObject1.getString(key);
                        System.out.println(val);
                    }
                }
            }
        }

    }

    @SneakyThrows
    private void getAuthToken() {
        String uri = "https://oauth.vk.com/access_token?client_id=" + APPLICATION_ID + "&client_secret=" + SECRET_ID + "&v=5.92&grant_type=client_credentials";
        String data = makeRequest(uri);
        JSONObject jsonObject = new JSONObject(data);
        try {
            authToken = jsonObject.getString("access_token");
            System.out.println(authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private String makeRequest(String uri) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String data = EntityUtils.toString(entity);
        return data;
    }

}
