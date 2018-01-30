package com.mode.shopify.product.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopifyProduct {

    protected Long id;

    protected String title;

    @JsonProperty(value = "body_html")
    protected String bodyHtml;

    protected String vendor;

    @JsonProperty(value = "product_type")
    protected String productType;

    protected String handle;

    @JsonProperty(value = "published_at")
    protected String publishedAt;

    @JsonProperty(value = "template_suffix")
    protected String templateSuffix;

    @JsonProperty(value = "published_scope")
    protected String publishedScope;

    protected String tags;

    protected List<ShopifyProductVariant> variants;

    protected List<ShopifyOption> options;

    protected List<ShopifyProductImage> images;

    protected ShopifyProductImage image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTemplateSuffix() {
        return templateSuffix;
    }

    public void setTemplateSuffix(String templateSuffix) {
        this.templateSuffix = templateSuffix;
    }

    public String getPublishedScope() {
        return publishedScope;
    }

    public void setPublishedScope(String publishedScope) {
        this.publishedScope = publishedScope;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<ShopifyProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ShopifyProductVariant> variants) {
        this.variants = variants;
    }

    public List<ShopifyOption> getOptions() {
        return options;
    }

    public void setOptions(List<ShopifyOption> options) {
        this.options = options;
    }

    public List<ShopifyProductImage> getImages() {
        return images;
    }

    public void setImages(List<ShopifyProductImage> images) {
        this.images = images;
    }

    public ShopifyProductImage getImage() {
        return image;
    }

    public void setImage(ShopifyProductImage image) {
        this.image = image;
    }
}
