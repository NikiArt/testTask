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

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    @SneakyThrows
    public void authCallBack() {
    }
}
