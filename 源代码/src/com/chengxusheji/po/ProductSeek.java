package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductSeek {
    /*求购id*/
    private Integer seekId;
    public Integer getSeekId(){
        return seekId;
    }
    public void setSeekId(Integer seekId){
        this.seekId = seekId;
    }

    /*农产品主图*/
    private String productPhoto;
    public String getProductPhoto() {
        return productPhoto;
    }
    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    /*农产品名称*/
    @NotEmpty(message="农产品名称不能为空")
    private String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /*农产品类别*/
    private ProductClass productClassObj;
    public ProductClass getProductClassObj() {
        return productClassObj;
    }
    public void setProductClassObj(ProductClass productClassObj) {
        this.productClassObj = productClassObj;
    }

    /*生产地*/
    @NotEmpty(message="生产地不能为空")
    private String publish;
    public String getPublish() {
        return publish;
    }
    public void setPublish(String publish) {
        this.publish = publish;
    }

    /*制造商*/
    @NotEmpty(message="制造商不能为空")
    private String author;
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    /*求购价格*/
    @NotNull(message="必须输入求购价格")
    private Float price;
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    /*新旧程度*/
    @NotEmpty(message="新旧程度不能为空")
    private String xjcd;
    public String getXjcd() {
        return xjcd;
    }
    public void setXjcd(String xjcd) {
        this.xjcd = xjcd;
    }

    /*求购说明*/
    @NotEmpty(message="求购说明不能为空")
    private String seekDesc;
    public String getSeekDesc() {
        return seekDesc;
    }
    public void setSeekDesc(String seekDesc) {
        this.seekDesc = seekDesc;
    }

    /*发布用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*用户发布时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProductSeek=new JSONObject(); 
		jsonProductSeek.accumulate("seekId", this.getSeekId());
		jsonProductSeek.accumulate("productPhoto", this.getProductPhoto());
		jsonProductSeek.accumulate("productName", this.getProductName());
		jsonProductSeek.accumulate("productClassObj", this.getProductClassObj().getProductClassName());
		jsonProductSeek.accumulate("productClassObjPri", this.getProductClassObj().getProductClassId());
		jsonProductSeek.accumulate("publish", this.getPublish());
		jsonProductSeek.accumulate("author", this.getAuthor());
		jsonProductSeek.accumulate("price", this.getPrice());
		jsonProductSeek.accumulate("xjcd", this.getXjcd());
		jsonProductSeek.accumulate("seekDesc", this.getSeekDesc());
		jsonProductSeek.accumulate("userObj", this.getUserObj().getName());
		jsonProductSeek.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonProductSeek.accumulate("addTime", this.getAddTime());
		return jsonProductSeek;
    }}