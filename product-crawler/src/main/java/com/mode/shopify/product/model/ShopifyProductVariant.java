package com.mode.shopify.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopifyProductVariant {

    protected Long variantId;

    @JsonProperty(value = "product_id")
    protected Long productId;

    protected String title;

    protected String price;

    protected String sku;

    protected Integer position;

    protected Integer grams;

    @JsonProperty(value = "inventory_policy")
    protected String inventoryPolicy;

    @JsonProperty(value = "compare_at_price")
    protected String compareAtPrice;

    @JsonProperty(value = "fulfillment_service")
    protected String fulfillmentService;

    @JsonProperty(value = "inventory_management")
    protected String inventoryManagement;

    protected String option1;

    protected String option2;

    protected String option3;

    protected Boolean taxable;

    protected String barcode;

    @JsonProperty(value = "image_id")
    protected Long imageId;

    @JsonProperty(value = "inventory_quantity")
    protected Integer inventoryQuantity;

    protected Integer weight;

    @JsonProperty(value = "weight_unit")
    protected String weightUnit;

    @JsonProperty(value = "old_inventory_quantity")
    protected Integer oldInventoryQuantity;

    @JsonProperty(value = "requires_shipping")
    protected Boolean requiresShipping;

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long id) {
        this.variantId = variantId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getGrams() {
        return grams;
    }

    public void setGrams(Integer grams) {
        this.grams = grams;
    }

    public String getInventoryPolicy() {
        return inventoryPolicy;
    }

    public void setInventoryPolicy(String inventoryPolicy) {
        this.inventoryPolicy = inventoryPolicy;
    }

    public String getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(String compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public String getFulfillmentService() {
        return fulfillmentService;
    }

    public void setFulfillmentService(String fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    public String getInventoryManagement() {
        return inventoryManagement;
    }

    public void setInventoryManagement(String inventoryManagement) {
        this.inventoryManagement = inventoryManagement;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Integer getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Integer getOldInventoryQuantity() {
        return oldInventoryQuantity;
    }

    public void setOldInventoryQuantity(Integer oldInventoryQuantity) {
        this.oldInventoryQuantity = oldInventoryQuantity;
    }

    public Boolean getRequiresShipping() {
        return requiresShipping;
    }

    public void setRequiresShipping(Boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }
}
