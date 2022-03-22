package com.example.demo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by 180686 on 2021/8/16 13:56
 */

public class HttpUtils {

    public static String sendGet(String url, String param)
    {
        String string = null;
        try
        {
            String urlNameString = url + "?" + param;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlNameString);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity entity = response.getEntity();
                //使用工具类EntityUtils，从响应中取出实体表示的内容并转换成字符串
                string = EntityUtils.toString(entity, "utf-8");
            }
            //5.关闭资源
            response.close();
            httpClient.close();
            return string;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
