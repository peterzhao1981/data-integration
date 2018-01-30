package com.mode.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a restful http client for making calls to restful apis. It uses apache http client and connection pooling.
 */
@Component
public class RestClient {

    // the Connection Timeout (http.connection.timeout) – the time to establish the connection with the remote host
    private static final int HTTP_CONN_TIMEOUT = 60 * 1000;
    // the Socket Timeout (http.socket.timeout) – the time waiting for data – after the connection was established;
    // maximum time of inactivity between two data packets
    private static final int HTTP_SOCKET_TIMEOUT = 60 * 1000;
    //the Connection Manager Timeout (http.connection-manager.timeout) – the time to wait for a connection from the
    // connection manager/pool
    private static final int HTTP_REQUEST_TIMEOUT = 5 * 1000;

    // total open connections
    private static final int HTTP_CONN_MAX_TOTAL = 20;
    // total open connection per each route or target host
    private static final int HTTP_CONN_MAX_PER_ROUTE = 10;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    public RestTemplate basicAuthRestTemplate(String username, String password) {
        return new RestTemplateBuilder()
                .requestFactory(httpRequestFactory())
                .basicAuthorization(username, password)
                .build();
    }

    private ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(acceptsUntrustedCertsHttpClient());
    }

    @Bean
    public CloseableHttpClient acceptsUntrustedCertsHttpClient() {

        // setup a custom config for http timeout properties
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(HTTP_CONN_TIMEOUT)
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_REQUEST_TIMEOUT)
                .build();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(HTTP_CONN_MAX_TOTAL);
        connMgr.setDefaultMaxPerRoute(HTTP_CONN_MAX_PER_ROUTE);

        // finally, build the HttpClient;
        //      -- done!
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(sslContext)
                .setConnectionManager(connMgr)
                .build();

        return client;
    }
}
