package com.mode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhaoweiwei on 2017/12/12.
 */
@Entity
@Table(name = "dxm_inventory",
        indexes = {
        }
)
public class DxmInventory {

    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String sku;

    @Column(length = 100)
    private String newSku;

    @Column(length = 50)
    private String warehousePosition;

    @Column(length = 50)
    private String shelfPosition;

    @Column(length = 10)
    private String inventory;

    @Column(length = 10)
    private String safeInventory;

    @Column(length = 10)
    private String price;

    @Column(length = 10)
    private String onTheWayPurchase;

    @Column(length = 10)
    private String preOrder;

    @Column(length = 10)
    private String availableInventory;

    @Column(length = 200)
    private String comment1;

    @Column(length = 100)
    private String skuAlias;

    @Column(length = 100)
    private String productName;

    @Column(length = 200)
    private String imageUrl;

    @Column(length = 10)
    private String weight;

    @Column(length = 10)
    private String purchasePrice;

    @Column(length = 10)
    private String length;

    @Column(length = 10)
    private String width;

    @Column(length = 10)
    private String height;

    @Column(length = 200)
    private String sourceUrl;

    @Column(length = 200)
    private String comment2;

    @Column(length = 100)
    private String enClearance;

    @Column(length = 100)
    private String cnClearance;

    @Column(length = 10)
    private String declarationWeight;

    @Column(length = 10)
    private String declarationPrice;

    @Column(length = 10)
    private String dangerous;

    @Column(length = 10)
    private String hsCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNewSku() {
        return newSku;
    }

    public void setNewSku(String newSku) {
        this.newSku = newSku;
    }

    public String getWarehousePosition() {
        return warehousePosition;
    }

    public void setWarehousePosition(String warehousePosition) {
        this.warehousePosition = warehousePosition;
    }

    public String getShelfPosition() {
        return shelfPosition;
    }

    public void setShelfPosition(String shelfPosition) {
        this.shelfPosition = shelfPosition;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSafeInventory() {
        return safeInventory;
    }

    public void setSafeInventory(String safeInventory) {
        this.safeInventory = safeInventory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOnTheWayPurchase() {
        return onTheWayPurchase;
    }

    public void setOnTheWayPurchase(String onTheWayPurchase) {
        this.onTheWayPurchase = onTheWayPurchase;
    }

    public String getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(String preOrder) {
        this.preOrder = preOrder;
    }

    public String getAvailableInventory() {
        return availableInventory;
    }

    public void setAvailableInventory(String availableInventory) {
        this.availableInventory = availableInventory;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getSkuAlias() {
        return skuAlias;
    }

    public void setSkuAlias(String skuAlias) {
        this.skuAlias = skuAlias;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getEnClearance() {
        return enClearance;
    }

    public void setEnClearance(String enClearance) {
        this.enClearance = enClearance;
    }

    public String getCnClearance() {
        return cnClearance;
    }

    public void setCnClearance(String cnClearance) {
        this.cnClearance = cnClearance;
    }

    public String getDeclarationWeight() {
        return declarationWeight;
    }

    public void setDeclarationWeight(String declarationWeight) {
        this.declarationWeight = declarationWeight;
    }

    public String getDeclarationPrice() {
        return declarationPrice;
    }

    public void setDeclarationPrice(String declarationPrice) {
        this.declarationPrice = declarationPrice;
    }

    public String getDangerous() {
        return dangerous;
    }

    public void setDangerous(String dangerous) {
        this.dangerous = dangerous;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }
}
