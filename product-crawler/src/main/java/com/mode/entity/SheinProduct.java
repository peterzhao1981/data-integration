package com.mode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by zhaoweiwei on 2017/11/16.
 */
public class SheinProduct {

    @JsonProperty("goods_id")
    private Integer goodsId;
    @JsonProperty("goods_name")
    private String goodsName;
    @JsonProperty("goods_thumb")
    private String goodsThumb;
    @JsonProperty("shop_price")
    private String shopPrice;
    @JsonProperty("special_price")
    private String specialPrice;
    private String url;
    private Comment comment;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public static class Comment {
        @JsonProperty("comment_num")
        private String commentNum;
        @JsonProperty("comment_rank")
        private String commentRank;

        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getCommentRank() {
            return commentRank;
        }

        public void setCommentRank(String commentRank) {
            this.commentRank = commentRank;
        }
    }


    public static void main(String[] args) throws Exception {
        /*ObjectMapper om = new ObjectMapper();
//        String s = "{\"goods_id\": 370612, \"goods_sn\": \"sweater170706468\", \"us_in_stock\": 1, \"url_goods_id\": " +
//                "370612, \"shop_price\": 40, \"special_price\": 16, \"special_price_start\": \"2017-11-16\", " +
//                "\"special_price_end\": \"2035-12-01\", \"goods_thumb\": \"http://img.ltwebstatic" +
//                ".com/images/pi/201707/b6/14992486209842116504_thumbnail_220x293.jpg\", \"goods_name\": \"Low Back " +
//                "Scallop Raw Edge Jumper\", \"goods_url_name\": \"Low Back Scallop Raw Edge Jumper\", \"cat_id\": " +
//                "1734, \"parent_id\": 1766, \"stock\": 1000000, \"is_stock_enough\": 1, \"goods_relation_id\": " +
//                "\"171106602\", \"goods_desc\": \"\", \"goods_img\": \"http://img.ltwebstatic" +
//                ".com/images/pi/201707/b6/14992486209842116504_thumbnail_405x552.jpg\", \"is_on_sale\": 1, " +
//                "\"original_img\": \"http://img.ltwebstatic.com/images/pi/201707/b6/14992486209842116504.jpg\", " +
//                "\"goods_color_image\": \"http://img.ltwebstatic.com/images/pi/201711/e8/15099620047454940933.jpg\", " +
//                "\"cost\": \"36\", \"tag_id\": 0, \"tag_name\": \"\", \"spuColorRank\": [370612 ], \"grade\": " +
//                "\"畅销款\", \"supplier_id\": 8300, \"brand\": \"SHEIN\", \"original_img_no_water\": \"http://img" +
//                ".ltwebstatic.com/images/goods_img_bak/pi/201707/b6/14992486209842116504.jpg\", \"name\": \"Low Back " +
//                "Scallop Raw Edge Jumper\", \"image\": \"http://img.ltwebstatic" +
//                ".com/images/pi/201707/b6/14992486209842116504_thumbnail_220x293.jpg\", \"goods_image\": \"http://img" +
//                ".ltwebstatic.com/images/pi/201707/b6/14992486209842116504_thumbnail_405x552.jpg\", \"image_big\": " +
//                "\"http://img.ltwebstatic.com/images/pi/201707/b6/14992486209842116504_thumbnail_405x552.jpg\", " +
//                "\"color\": 1, \"relatedColor\": [\"http://img.ltwebstatic" +
//                ".com/images/pi/201711/e8/15099620047454940933.jpg\", \"http://img.ltwebstatic" +
//                ".com/images/pi/201711/5d/15099619726189918646.jpg\"], \"is_pre_sale_end\": 1, \"is_pre_sale\": 0, " +
//                "\"comment\": {\"comment_num\": \"47\", \"comment_rank\": 5 }, \"top_comment_content\": \"I am so " +
//                "glad I ordered this sweater! It is super comfy and fits perfectly. It is definitely one of my " +
//                "favorites. My only complaint is that the elastic band on the back snapped after only washing it once" +
//                ". Other than that, I would recommend!\", \"seoattr\": {\"Style\": \"Sexy\", \"Sweater Type\": " +
//                "\"Pullovers\", \"Color\": \"White\", \"Pattern Type\": \"Plain\", \"Neckline\": \"Round Neck\", " +
//                "\"Material\": \"Acrylic\", \"Fit Type\": \"Regular Fit\", \"Sleeve Length\": \"Long Sleeve\", " +
//                "\"Size\": \"one-size\", \"Fabric\": \"Fabric has some stretch\", \"Shoulder(cm)\": \"52cm\", \"Bust" +
//                "(cm)\": \"104cm\", \"Length(cm)\": \"54cm\", \"Sleeve Length(cm)\": \"48cm\", \"Size Available\": " +
//                "\"one-size\"}, \"memcachekey\": \"0fb9f75fa1d7cb4afed9a431005abbd0\", \"goods_price\": " +
//                "{\"shop_price\": \"40.00\", \"unit_price\": \"16.00\", \"special_price\": \"16.00\", " +
//                "\"is_clearance\": \"0\", \"limit_count\": \"7\", \"flash_goods\": {\"is_flash_goods\": \"0\"}, " +
//                "\"unit_discount\": 60, \"member_price\": {\"wholesale_11\": \"13.60\", \"wholesale_12\": \"12.80\"}," +
//                " \"rate\": {\"USD\": {\"shop_price\": \"40.00\", \"shop_price_symbol\": \"$40.00\", \"unit_price\": " +
//                "\"16.00\", \"unit_price_symbol\": \"$16.00\", \"special_price\": \"16.00\", " +
//                "\"special_price_symbol\": \"$16.00\", \"member_price\": {\"wholesale_11\": \"$13.60\", " +
//                "\"wholesale_12\": \"$12.80\"} }, \"EUR\": {\"shop_price\": \"34.97\", \"shop_price_symbol\": \"34" +
//                ".97€\", \"unit_price\": \"13.99\", \"unit_price_symbol\": \"13.99€\", \"special_price\": \"13.99\", " +
//                "\"special_price_symbol\": \"13.99€\", \"member_price\": {\"wholesale_11\": \"11.89€\", " +
//                "\"wholesale_12\": \"11.19€\"} }, \"CAD\": {\"shop_price\": \"51.69\", \"shop_price_symbol\": \"CA$51" +
//                ".69\", \"unit_price\": \"20.68\", \"unit_price_symbol\": \"CA$20.68\", \"special_price\": \"20.68\"," +
//                " \"special_price_symbol\": \"CA$20.68\", \"member_price\": {\"wholesale_11\": \"CA$17.57\", " +
//                "\"wholesale_12\": \"CA$16.54\"} }, \"CHF\": {\"shop_price\": \"40.61\", \"shop_price_symbol\": " +
//                "\"CHF$40.61\", \"unit_price\": \"16.25\", \"unit_price_symbol\": \"CHF$16.25\", \"special_price\": " +
//                "\"16.25\", \"special_price_symbol\": \"CHF$16.25\", \"member_price\": {\"wholesale_11\": \"CHF$13" +
//                ".81\", \"wholesale_12\": \"CHF$13.00\"} }, \"GBP\": {\"shop_price\": \"30.87\", " +
//                "\"shop_price_symbol\": \"GBP£30.87\", \"unit_price\": \"12.35\", \"unit_price_symbol\": \"GBP£12" +
//                ".35\", \"special_price\": \"12.35\", \"special_price_symbol\": \"GBP£12.35\", \"member_price\": " +
//                "{\"wholesale_11\": \"GBP£10.50\", \"wholesale_12\": \"GBP£9.88\"} }, \"MXN\": {\"shop_price\": \"778" +
//                ".59\", \"shop_price_symbol\": \"$MXN778.59\", \"unit_price\": \"311.44\", \"unit_price_symbol\": " +
//                "\"$MXN311.44\", \"special_price\": \"311.44\", \"special_price_symbol\": \"$MXN311.44\", " +
//                "\"member_price\": {\"wholesale_11\": \"$MXN264.72\", \"wholesale_12\": \"$MXN249.15\"} } }, " +
//                "\"default_price\": {\"shop_price\": \"40.00\", \"shop_price_symbol\": \"$40.00\", \"unit_price\": " +
//                "\"16.00\", \"unit_price_symbol\": \"$16.00\", \"special_price\": \"16.00\", " +
//                "\"special_price_symbol\": \"$16.00\", \"member_price\": {\"wholesale_11\": \"$13.60\", " +
//                "\"wholesale_12\": \"$12.80\"} }, \"usd_price\": {\"shop_price\": \"40.00\", \"shop_price_symbol\": " +
//                "\"$40.00\", \"unit_price\": \"16.00\", \"unit_price_symbol\": \"$16.00\", \"special_price\": \"16" +
//                ".00\", \"special_price_symbol\": \"$16.00\", \"member_price\": {\"wholesale_11\": \"$13.60\", " +
//                "\"wholesale_12\": \"$12.80\"} } }, \"good_price\": {\"shop_price\": \"40.00\", \"unit_price\": \"16" +
//                ".00\", \"special_price\": \"16.00\", \"is_clearance\": \"0\", \"limit_count\": \"7\", " +
//                "\"flash_goods\": {\"is_flash_goods\": \"0\"}, \"unit_discount\": 60, \"member_price\": " +
//                "{\"wholesale_11\": \"13.60\", \"wholesale_12\": \"12.80\"}, \"rate\": {\"USD\": {\"shop_price\": " +
//                "\"40.00\", \"shop_price_symbol\": \"$40.00\", \"unit_price\": \"16.00\", \"unit_price_symbol\": " +
//                "\"$16.00\", \"special_price\": \"16.00\", \"special_price_symbol\": \"$16.00\", \"member_price\": " +
//                "{\"wholesale_11\": \"$13.60\", \"wholesale_12\": \"$12.80\"} }, \"EUR\": {\"shop_price\": \"34.97\"," +
//                " \"shop_price_symbol\": \"34.97€\", \"unit_price\": \"13.99\", \"unit_price_symbol\": \"13.99€\", " +
//                "\"special_price\": \"13.99\", \"special_price_symbol\": \"13.99€\", \"member_price\": " +
//                "{\"wholesale_11\": \"11.89€\", \"wholesale_12\": \"11.19€\"} }, \"CAD\": {\"shop_price\": \"51.69\"," +
//                " \"shop_price_symbol\": \"CA$51.69\", \"unit_price\": \"20.68\", \"unit_price_symbol\": \"CA$20" +
//                ".68\", \"special_price\": \"20.68\", \"special_price_symbol\": \"CA$20.68\", \"member_price\": " +
//                "{\"wholesale_11\": \"CA$17.57\", \"wholesale_12\": \"CA$16.54\"} }, \"CHF\": {\"shop_price\": \"40" +
//                ".61\", \"shop_price_symbol\": \"CHF$40.61\", \"unit_price\": \"16.25\", \"unit_price_symbol\": " +
//                "\"CHF$16.25\", \"special_price\": \"16.25\", \"special_price_symbol\": \"CHF$16.25\", " +
//                "\"member_price\": {\"wholesale_11\": \"CHF$13.81\", \"wholesale_12\": \"CHF$13.00\"} }, \"GBP\": " +
//                "{\"shop_price\": \"30.87\", \"shop_price_symbol\": \"GBP£30.87\", \"unit_price\": \"12.35\", " +
//                "\"unit_price_symbol\": \"GBP£12.35\", \"special_price\": \"12.35\", \"special_price_symbol\": " +
//                "\"GBP£12.35\", \"member_price\": {\"wholesale_11\": \"GBP£10.50\", \"wholesale_12\": \"GBP£9.88\"} " +
//                "}, \"MXN\": {\"shop_price\": \"778.59\", \"shop_price_symbol\": \"$MXN778.59\", \"unit_price\": " +
//                "\"311.44\", \"unit_price_symbol\": \"$MXN311.44\", \"special_price\": \"311.44\", " +
//                "\"special_price_symbol\": \"$MXN311.44\", \"member_price\": {\"wholesale_11\": \"$MXN264.72\", " +
//                "\"wholesale_12\": \"$MXN249.15\"} } }, \"default_price\": {\"shop_price\": \"40.00\", " +
//                "\"shop_price_symbol\": \"$40.00\", \"unit_price\": \"16.00\", \"unit_price_symbol\": \"$16.00\", " +
//                "\"special_price\": \"16.00\", \"special_price_symbol\": \"$16.00\", \"member_price\": " +
//                "{\"wholesale_11\": \"$13.60\", \"wholesale_12\": \"$12.80\"} }, \"usd_price\": {\"shop_price\": \"40" +
//                ".00\", \"shop_price_symbol\": \"$40.00\", \"unit_price\": \"16.00\", \"unit_price_symbol\": \"$16" +
//                ".00\", \"special_price\": \"16.00\", \"special_price_symbol\": \"$16.00\", \"member_price\": " +
//                "{\"wholesale_11\": \"$13.60\", \"wholesale_12\": \"$12.80\"} } }, \"url\": " +
//                "\"/Low-Back-Scallop-Raw-Edge-Jumper-p-370612-cat-1734.html\", \"cat_name\": \"Sweaters\", " +
//                "\"top_comment_username\": \"emilyemedwid\"}";

        String s = "{\"goods_id\": 386108, \"goods_sn\": \"blouse170908702\", \"us_in_stock\": 0, \"url_goods_id\": " +
                "386108, \"shop_price\": 25, \"special_price\": 10, \"special_price_start\": \"2017-11-16\", " +
                "\"special_price_end\": \"2035-12-01\", \"goods_thumb\": \"http://img.ltwebstatic" +
                ".com/images/pi/201709/05/15047768578477022176_thumbnail_220x293.jpg\", \"goods_name\": \"Flower " +
                "Print Keyhole Back Curved Hem Blouse\", \"goods_url_name\": \"Flower Print Keyhole Back Curved Hem " +
                "Blouse\", \"cat_id\": 1733, \"parent_id\": 1766, \"stock\": 1000000, \"is_stock_enough\": 1, " +
                "\"goods_relation_id\": \"170821zxq01\", \"goods_desc\": \"\", \"goods_img\": \"http://img" +
                ".ltwebstatic.com/images/pi/201709/05/15047768578477022176_thumbnail_405x552.jpg\", \"is_on_sale\": " +
                "1, \"original_img\": \"http://img.ltwebstatic.com/images/pi/201709/05/15047768578477022176.jpg\", " +
                "\"goods_color_image\": \"http://img.ltwebstatic.com/images/pi/201709/02/15047768543186598309.jpg\", " +
                "\"cost\": \"20\", \"tag_id\": 0, \"tag_name\": \"\", \"spuColorRank\": [386108, 381339, 369989 ], " +
                "\"grade\": \"畅销款\", \"supplier_id\": 1223, \"brand\": \"SHEIN\", \"original_img_no_water\": " +
                "\"http://img.ltwebstatic.com/images/goods_img_bak/pi/201709/05/15047768578477022176.jpg\", \"name\":" +
                " \"Flower Print Keyhole Back Curved Hem Blouse\", \"image\": \"http://img.ltwebstatic" +
                ".com/images/pi/201709/05/15047768578477022176_thumbnail_220x293.jpg\", \"goods_image\": \"http://img" +
                ".ltwebstatic.com/images/pi/201709/05/15047768578477022176_thumbnail_405x552.jpg\", \"image_big\": " +
                "\"http://img.ltwebstatic.com/images/pi/201709/05/15047768578477022176_thumbnail_405x552.jpg\", " +
                "\"color\": 1, \"relatedColor\": [\"http://img.ltwebstatic" +
                ".com/images/pi/201708/3a/15030442944361285652.jpg\", \"http://img.ltwebstatic" +
                ".com/images/pi/201708/1c/15030484952853747620.jpg\", \"http://img.ltwebstatic" +
                ".com/images/pi/201709/02/15047768543186598309.jpg\", \"http://img.ltwebstatic" +
                ".com/images/pi/201710/09/15087511908433924306.jpg\"], \"is_pre_sale_end\": 1, \"is_pre_sale\": 0, " +
                "\"comment\": {\"comment_num\": \"15\", \"comment_rank\": 5 }, \"top_comment_content\": \"Looks " +
                "pretty much like the photos, and though it was a slight loose fit for me (which I prefer) - it looks" +
                " great when worn with formal pants!\", \"seoattr\": {\"Material\": \"Polyester\", \"Color\": \"Multi" +
                " Color\", \"Pattern Type\": \"Floral\", \"Collar\": \"Round Neck\", \"Fit Type\": \"Regular Fit\", " +
                "\"Decoration\": \"Button\", \"Sleeve Length\": \"Long Sleeve\", \"Shirt Type\": \"Top\", \"Fabric\":" +
                " \"Fabric has no stretch\", \"Shoulder(cm)\": \"XS:36.5cm, S:37.5cm, M:38.5cm, L:39.5cm\", \"Bust" +
                "(cm)\": \"XS:92cm, S:96cm, M:100cm, L:104cm\", \"Waist Size(cm)\": \"XS:91cm, S:95cm, M:99cm, " +
                "L:103cm\", \"Length(cm)\": \"XS:65cm, S:66cm, M:67cm, L:68cm\", \"Sleeve Length(cm)\": \"XS:59cm, " +
                "S:60cm, M:61cm, L:62cm\", \"Bicep Length(cm)\": \"XS:32cm, S:33cm, M:34cm, L:35cm\", \"Cuff(cm)\": " +
                "\"XS:24cm, S:25cm, M:26cm, L:27cm\", \"Size Available\": \"XS,S,M,L\"}, \"memcachekey\": " +
                "\"0fb9f75fa1d7cb4afed9a431005abbd0\", \"goods_price\": {\"shop_price\": \"25.00\", \"unit_price\": " +
                "\"10.00\", \"special_price\": \"10.00\", \"is_clearance\": \"0\", \"limit_count\": \"7\", " +
                "\"flash_goods\": {\"is_flash_goods\": \"0\"}, \"unit_discount\": 60, \"member_price\": " +
                "{\"wholesale_11\": \"8.50\", \"wholesale_12\": \"8.00\"}, \"rate\": {\"USD\": {\"shop_price\": \"25" +
                ".00\", \"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", \"unit_price_symbol\": \"$10" +
                ".00\", \"special_price\": \"10.00\", \"special_price_symbol\": \"$10.00\", \"member_price\": " +
                "{\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} }, \"EUR\": {\"shop_price\": \"21.85\", " +
                "\"shop_price_symbol\": \"21.85€\", \"unit_price\": \"8.74\", \"unit_price_symbol\": \"8.74€\", " +
                "\"special_price\": \"8.74\", \"special_price_symbol\": \"8.74€\", \"member_price\": " +
                "{\"wholesale_11\": \"7.43€\", \"wholesale_12\": \"6.99€\"} }, \"CAD\": {\"shop_price\": \"32.31\", " +
                "\"shop_price_symbol\": \"CA$32.31\", \"unit_price\": \"12.92\", \"unit_price_symbol\": \"CA$12.92\"," +
                " \"special_price\": \"12.92\", \"special_price_symbol\": \"CA$12.92\", \"member_price\": " +
                "{\"wholesale_11\": \"CA$10.98\", \"wholesale_12\": \"CA$10.34\"} }, \"CHF\": {\"shop_price\": \"25" +
                ".38\", \"shop_price_symbol\": \"CHF$25.38\", \"unit_price\": \"10.15\", \"unit_price_symbol\": " +
                "\"CHF$10.15\", \"special_price\": \"10.15\", \"special_price_symbol\": \"CHF$10.15\", " +
                "\"member_price\": {\"wholesale_11\": \"CHF$8.63\", \"wholesale_12\": \"CHF$8.12\"} }, \"GBP\": " +
                "{\"shop_price\": \"19.29\", \"shop_price_symbol\": \"GBP£19.29\", \"unit_price\": \"7.72\", " +
                "\"unit_price_symbol\": \"GBP£7.72\", \"special_price\": \"7.72\", \"special_price_symbol\": \"GBP£7" +
                ".72\", \"member_price\": {\"wholesale_11\": \"GBP£6.56\", \"wholesale_12\": \"GBP£6.17\"} }, " +
                "\"MXN\": {\"shop_price\": \"486.62\", \"shop_price_symbol\": \"$MXN486.62\", \"unit_price\": \"194" +
                ".65\", \"unit_price_symbol\": \"$MXN194.65\", \"special_price\": \"194.65\", " +
                "\"special_price_symbol\": \"$MXN194.65\", \"member_price\": {\"wholesale_11\": \"$MXN165.45\", " +
                "\"wholesale_12\": \"$MXN155.72\"} } }, \"default_price\": {\"shop_price\": \"25.00\", " +
                "\"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", \"unit_price_symbol\": \"$10.00\", " +
                "\"special_price\": \"10.00\", \"special_price_symbol\": \"$10.00\", \"member_price\": " +
                "{\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} }, \"usd_price\": {\"shop_price\": \"25" +
                ".00\", \"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", \"unit_price_symbol\": \"$10" +
                ".00\", \"special_price\": \"10.00\", \"special_price_symbol\": \"$10.00\", \"member_price\": " +
                "{\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} } }, \"good_price\": {\"shop_price\": " +
                "\"25.00\", \"unit_price\": \"10.00\", \"special_price\": \"10.00\", \"is_clearance\": \"0\", " +
                "\"limit_count\": \"7\", \"flash_goods\": {\"is_flash_goods\": \"0\"}, \"unit_discount\": 60, " +
                "\"member_price\": {\"wholesale_11\": \"8.50\", \"wholesale_12\": \"8.00\"}, \"rate\": {\"USD\": " +
                "{\"shop_price\": \"25.00\", \"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", " +
                "\"unit_price_symbol\": \"$10.00\", \"special_price\": \"10.00\", \"special_price_symbol\": \"$10" +
                ".00\", \"member_price\": {\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} }, \"EUR\": " +
                "{\"shop_price\": \"21.85\", \"shop_price_symbol\": \"21.85€\", \"unit_price\": \"8.74\", " +
                "\"unit_price_symbol\": \"8.74€\", \"special_price\": \"8.74\", \"special_price_symbol\": \"8.74€\", " +
                "\"member_price\": {\"wholesale_11\": \"7.43€\", \"wholesale_12\": \"6.99€\"} }, \"CAD\": " +
                "{\"shop_price\": \"32.31\", \"shop_price_symbol\": \"CA$32.31\", \"unit_price\": \"12.92\", " +
                "\"unit_price_symbol\": \"CA$12.92\", \"special_price\": \"12.92\", \"special_price_symbol\": \"CA$12" +
                ".92\", \"member_price\": {\"wholesale_11\": \"CA$10.98\", \"wholesale_12\": \"CA$10.34\"} }, " +
                "\"CHF\": {\"shop_price\": \"25.38\", \"shop_price_symbol\": \"CHF$25.38\", \"unit_price\": \"10" +
                ".15\", \"unit_price_symbol\": \"CHF$10.15\", \"special_price\": \"10.15\", \"special_price_symbol\":" +
                " \"CHF$10.15\", \"member_price\": {\"wholesale_11\": \"CHF$8.63\", \"wholesale_12\": \"CHF$8.12\"} " +
                "}, \"GBP\": {\"shop_price\": \"19.29\", \"shop_price_symbol\": \"GBP£19.29\", \"unit_price\": \"7" +
                ".72\", \"unit_price_symbol\": \"GBP£7.72\", \"special_price\": \"7.72\", \"special_price_symbol\": " +
                "\"GBP£7.72\", \"member_price\": {\"wholesale_11\": \"GBP£6.56\", \"wholesale_12\": \"GBP£6.17\"} }, " +
                "\"MXN\": {\"shop_price\": \"486.62\", \"shop_price_symbol\": \"$MXN486.62\", \"unit_price\": \"194" +
                ".65\", \"unit_price_symbol\": \"$MXN194.65\", \"special_price\": \"194.65\", " +
                "\"special_price_symbol\": \"$MXN194.65\", \"member_price\": {\"wholesale_11\": \"$MXN165.45\", " +
                "\"wholesale_12\": \"$MXN155.72\"} } }, \"default_price\": {\"shop_price\": \"25.00\", " +
                "\"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", \"unit_price_symbol\": \"$10.00\", " +
                "\"special_price\": \"10.00\", \"special_price_symbol\": \"$10.00\", \"member_price\": " +
                "{\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} }, \"usd_price\": {\"shop_price\": \"25" +
                ".00\", \"shop_price_symbol\": \"$25.00\", \"unit_price\": \"10.00\", \"unit_price_symbol\": \"$10" +
                ".00\", \"special_price\": \"10.00\", \"special_price_symbol\": \"$10.00\", \"member_price\": " +
                "{\"wholesale_11\": \"$8.50\", \"wholesale_12\": \"$8.00\"} } }, \"url\": " +
                "\"/Flower-Print-Keyhole-Back-Curved-Hem-Blouse-p-386108-cat-1733.html\", \"cat_name\": \"Blouses\", " +
                "\"relatedProduct\": [{\"goods_id\": 386108, \"goods_color_image\": \"http://img.ltwebstatic" +
                ".com/images/pi/201709/02/15047768543186598309.jpg\", \"goods_sn\": \"blouse170908702\", \"url\": " +
                "\"/Flower-Print-Keyhole-Back-Curved-Hem-Blouse-p-386108-cat-1733.html\"}, {\"goods_id\": 381339, " +
                "\"goods_color_image\": \"http://img.ltwebstatic.com/images/pi/201708/1c/15030484952853747620.jpg\", " +
                "\"goods_sn\": \"blouse170821705\", \"url\": " +
                "\"/Flower-Print-Keyhole-Back-Curve-Hem-Blouse-p-381339-cat-1733.html\"}, {\"goods_id\": 369989, " +
                "\"goods_color_image\": \"http://img.ltwebstatic.com/images/pi/201708/3a/15030442944361285652.jpg\", " +
                "\"goods_sn\": \"blouse170704702\", \"url\": " +
                "\"/Flower-Print-Keyhole-Back-Curve-Hem-Blouse-p-369989-cat-1733.html\"} ], \"top_comment_username\":" +
                " \"devikamenon1011\"}";
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SheinProduct sheinProduct = om.readValue(s, SheinProduct.class);
        System.out.println(sheinProduct.getGoodsId());
        System.out.println(sheinProduct.getShopPrice());
        System.out.println(sheinProduct.getGoodsThumb());
        System.out.println(sheinProduct.getSpecialPrice());
        System.out.println(sheinProduct.getComment().getCommentNum());*/
    }

}
