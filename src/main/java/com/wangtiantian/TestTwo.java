package com.wangtiantian;

import com.google.gson.annotations.JsonAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestTwo {


    public static void main(String[] args) {
        try {
            // 创建HttpClient实例
            HttpClient httpClient = HttpClientBuilder.create().build();

            // 创建POST请求
            HttpPost request = new HttpPost("http://localhost:46096/api/cigarette_delivery_system_gx/custLevelAddBussTypePut/getAllCustLevel");

            // 添加请求头
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Cookie","ajs_anonymous_id=ed05256e-52be-4b43-a624-4c82cca72991; Role=admin; m=59b9:true");
            request.addHeader("Authorization","Basic YKKOw9MPlY3JldA=");
            request.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            // 添加请求体（JSON数据）
            String jsonBody = "{\"userName\":\"UevBweeUXo\",\"password\":\"QfwP9rYXClmTa6n\"}";
            request.setEntity(new StringEntity(jsonBody));

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(request);

            // 读取响应内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // 打印响应内容
            System.out.println("Response: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

