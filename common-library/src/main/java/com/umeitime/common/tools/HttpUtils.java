package com.umeitime.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpUtils {
    /**
     * get的方式请求
     *
     * @param requestUrl
     * @return 返回null 登录异常
     */
    public static String loginByGet(String requestUrl) {
        //get的方式提交就是url拼接的方式
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                return IOSUtils.inputStream2String(is, "utf-8");
            } else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post的方式请求
     *
     * @param requestUrl
     * @param params
     * @return 返回null 登录异常
     */
    public static String loginByPost(String requestUrl, String params) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //数据准备
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", params.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(params.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
                return IOSUtils.inputStream2String(is, "utf-8");
            } else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}