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
@Table(name = "shopify_product",
        indexes = {
        }
)
public class ShopifyProduct {

    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String handle;

    @Column(length = 100)
    private String variantSku;

    @Column(name = "option1_name", length = 20)
    private String option1Name;

    @Column(name = "option1_value", length = 50)
    private String option1Value;

    @Column(name = "option2_name", length = 20)
    private String option2Name;

    @Column(name = "option2_value", length = 50)
    private String option2Value;

    @Column(name = "option3_name", length = 20)
    private String option3Name;

    @Column(name = "option3_value", length = 50)
    private String option3Value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public String getOption1Name() {
        return option1Name;
    }

    public void setOption1Name(String option1Name) {
        this.option1Name = option1Name;
    }

    public String getOption1Value() {
        return option1Value;
    }

    public void setOption1Value(String option1Value) {
        this.option1Value = option1Value;
    }

    public String getOption2Name() {
        return option2Name;
    }

    public void setOption2Name(String option2Name) {
        this.option2Name = option2Name;
    }

    public String getOption2Value() {
        return option2Value;
    }

    public void setOption2Value(String option2Value) {
        this.option2Value = option2Value;
    }

    public String getOption3Name() {
        return option3Name;
    }

    public void setOption3Name(String option3Name) {
        this.option3Name = option3Name;
    }

    public String getOption3Value() {
        return option3Value;
    }

    public void setOption3Value(String option3Value) {
        this.option3Value = option3Value;
    }
}
