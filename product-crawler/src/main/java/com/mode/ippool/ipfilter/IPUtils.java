package com.mode.ippool.ipfilter;

import static java.lang.System.out;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mode.ippool.ipmodel.IPMessage;

/**
 * Created by paranoid on 18-05-16. 测试此IP是否有效
 */
public class IPUtils {
    public static void IPIsable(List<IPMessage> ipMessages1) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        for (int i = 0; i < ipMessages1.size(); i++) {
            String ip = ipMessages1.get(i).getIPAddress();
            int port = ipMessages1.get(i).getIPPort();

            HttpHost proxy = new HttpHost(ip, port);
            RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(5000)
                    .setSocketTimeout(5000).build();
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            httpGet.setConfig(config);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;"
                    + "q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit"
                    + "/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            try {
                response = httpClient.execute(httpGet);
            } catch (IOException e) {
                out.println("不可用代理已删除" + ipMessages1.get(i).getIPAddress() + ": "
                        + ipMessages1.get(i).getIPPort());
                ipMessages1.remove(ipMessages1.get(i));
                i--;
            }
        }
        try {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 检测ip是否可用,参数是单个ip对象
    public static boolean IPIsable(IPMessage ipMessages1) {
        boolean flag = true;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        String ip = ipMessages1.getIPAddress();
        int port = ipMessages1.getIPPort();

        HttpHost proxy = new HttpHost(ip, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(5000)
                .setSocketTimeout(5000).build();
        HttpGet httpGet = new HttpGet("https://www.baidu.com");
        httpGet.setConfig(config);

        httpGet.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;" + "q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit"
                + "/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            out.println("不可用代理已删除" + ipMessages1.getIPAddress());
        } finally {
            if (response == null) {
                flag = false;
            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
                // return flag;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    // 检测ip是否可用,参数是ip地址与port端口
    public static boolean IPIsable(String ip, int port) {
        boolean flag = true;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        HttpHost proxy = new HttpHost(ip, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(5000)
                .setSocketTimeout(2000).build();
        HttpGet httpGet = new HttpGet("https://www.baidu.com");
        httpGet.setConfig(config);

        httpGet.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;" + "q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit"
                + "/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            out.println("不可用代理已删除" + ip);
            flag = false;
        }
        try {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static void main(String[] args) {
        IPMessage ipMessage = new IPMessage();
        ipMessage.setIPAddress("223.241.119.139");
        ipMessage.setIPPort(35501);
        List<IPMessage> list = new ArrayList<>();
        list.add(ipMessage);
        System.out.println((IPUtils.IPIsable(ipMessage)));

    }
}
