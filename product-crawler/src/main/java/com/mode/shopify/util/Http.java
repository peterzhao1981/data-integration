package com.mode.shopify.util;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.mode.shopify.config.Configuration;
import com.mode.util.RestClient;


public class Http {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String baseUrl;
    private RestTemplate restTemplate;

    public Http(Configuration configuration) {
        this.baseUrl = configuration.getBaseUrl();
        this.restTemplate = new RestClient().basicAuthRestTemplate(
                configuration.getApiKey(),
                configuration.getPassword());
    }

    /**
     * Low-level HTTP request method. Synchronous, blocks till response or timeout.
     *
     * @param method       HttpMethod method
     * @param path         REST url path (i.e. /admin/order.json)
     * @param responseType class for the return object type
     * @param params       parameters to encode as query string or body parameters
     * @param data         JSON data to put in body
     * @return results marshalled into class specified in responseType parameter
     */
    private <T> T httpRequest(HttpMethod method, String path, Class<T> responseType,
                              MultiValueMap<String, String> params, Object data) {

        // Construct request header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // Construct request entity, including body and header
        HttpEntity<?> requestEntity = null;
        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
            if (ObjectUtils.isEmpty(data)) {
                data = JsonNodeFactory.instance.objectNode();
            }
            requestEntity = new HttpEntity<Object>(data, requestHeaders);
        } else {
            requestEntity = new HttpEntity<Object>(requestHeaders);
        }

        // Construct request uri
        final String url = baseUrl + path;
        URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build().encode().toUri();

        // Make the http request call
        logger.info("Client.httpRequest(): uri: " + uri.toString());
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, method, requestEntity, responseType);
        logger.info("Client.httpRequest(): response body: " + responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    /**
     * Public method for http gets, and return a wrapper up java object by the given responseType. Synchronous,
     * blocks till response or timeout.
     *
     * @param path
     * @param responseType
     * @param queryParams
     * @param <T>
     * @return results marshalled into class specified in responseType parameter
     */
    public <T> T getForObject(String path, Class<T> responseType, Map<String, String> queryParams) {
        return httpRequest(HttpMethod.GET, path, responseType, convertToMultiValueMap(queryParams), null);
    }

    /**
     * Convert request parameters to Spring Http MultiValueMap
     */
    private MultiValueMap<String, String> convertToMultiValueMap(Map<String, String> params) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return multiValueMap;
    }

    public <T> T postForObject(String path, Class<T> responseType, Object requestData) {
        return httpRequest(HttpMethod.POST, path, responseType, null, requestData);
    }
}