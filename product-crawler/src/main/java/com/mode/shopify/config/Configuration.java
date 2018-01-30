package com.mode.shopify.config;

public class Configuration {

    // Store unique name
    private String store;
    // Store admin api base baseUrl
    private String baseUrl;
    // Admin api key
    private String apiKey;
    // Admin api password
    private String password;
    // sdk version
    private String version = "1.0";

    public Configuration(String store, String apiKey, String password) {
        this.store = store;
        this.baseUrl = "https://" + store + ".myshopify.com";
        this.apiKey = apiKey;
        this.password = password;
    }

    public String getStore() {
        return store;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getPassword() {
        return password;
    }

    public String getVersion() {
        return version;
    }
}
