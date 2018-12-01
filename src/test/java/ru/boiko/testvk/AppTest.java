package ru.boiko.testvk;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static final String AUTH_URL = "https://oauth.vk.com/authorize?client_id={APP_ID}&scope={PERMISSIONS}&redirect_uri={REDIRECT_URI}&display={DISPLAY}&v={API_VERSION}&response_type=token";
    private static final String API_REQUEST = "https://api.vk.com/method/{METHOD}?{PARAMS}&access_token={TOKEN}&v=5.21";

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    

    @Test
    @SneakyThrows
    public void authCallBack() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://oauth.vk.com/access_token?client_id=6768580&client_secret=YuQ9DOn25t0BhtZSWCbV&v=5.92&grant_type=client_credentials");

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String data = EntityUtils.toString(entity);

        JSONObject jsonObject = new JSONObject(data);
        String val = jsonObject.getString("access_token");
        System.out.println(val);
        //8c8243788c8243788c30cf62528ce504bc88c828c824378d080d7f8ed116ab19141a29d

        httpPost = new HttpPost("https://api.vk.com/method/users.get?user_ids=481912255&fields=bdate&access_token=5335221d5335221d5335221ddc535265d9553355335221d0f373febfb5b04c63e9df51a&v=5.92");
        response = httpClient.execute(httpPost);
        entity = response.getEntity();
        data = EntityUtils.toString(entity);

        jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("response");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            Iterator<?> keys = jsonObject1.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (jsonObject1.get(key) instanceof String) {
                    String val1 = jsonObject1.getString(key);
                    System.out.println(val1);
                }
            }
        }
    }

    //"https://oauth.vk.com/access_token?client_id= + CLIENT_ID + &client_secret= + CLIENT_SECRET + &v=5.92&grant_type=client_credentials"
    //5335221d5335221d5335221ddc535265d9553355335221d0f373febfb5b04c63e9df51a
}
