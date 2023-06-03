package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductOrder {
    /*订单id*/
    private Integer orderId;
    public Integer getOrderId(){
        return orderId;
    }
    public void setOrderId(Integer orderId){
        this.orderId = orderId;
    }

    /*农产品信息*/
    private ProductSell productSellObj;
    public ProductSell getProductSellObj() {
        return productSellObj;
    }
    public void setProductSellObj(ProductSell productSellObj) {
        this.productSellObj = productSellObj;
    }

    /*意向用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*意向出价*/
    @NotNull(message="必须输入意向出价")
    private Float price;
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    /*用户备注*/
    private String orderMemo;
    public String getOrderMemo() {
        return orderMemo;
    }
    public void setOrderMemo(String orderMemo) {
        this.orderMemo = orderMemo;
    }

    /*下单时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProductOrder=new JSONObject(); 
		jsonProductOrder.accumulate("orderId", this.getOrderId());
		jsonProductOrder.accumulate("productSellObj", this.getProductSellObj().getProductName());
		jsonProductOrder.accumulate("productSellObjPri", this.getProductSellObj().getSellId());
		jsonProductOrder.accumulate("userObj", this.getUserObj().getName());
		jsonProductOrder.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonProductOrder.accumulate("price", this.getPrice());
		jsonProductOrder.accumulate("orderMemo", this.getOrderMemo());
		jsonProductOrder.accumulate("addTime", this.getAddTime());
		return jsonProductOrder;
    }}