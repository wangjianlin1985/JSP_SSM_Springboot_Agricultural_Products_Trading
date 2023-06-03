package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ProductSell;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.ProductOrder;

import com.chengxusheji.mapper.ProductOrderMapper;
@Service
public class ProductOrderService {

	@Resource ProductOrderMapper productOrderMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加农产品订单记录*/
    public void addProductOrder(ProductOrder productOrder) throws Exception {
    	productOrderMapper.addProductOrder(productOrder);
    }

    /*按照查询条件分页查询农产品订单记录*/
    public ArrayList<ProductOrder> queryProductOrder(ProductSell productSellObj,UserInfo userObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != productSellObj && productSellObj.getSellId()!= null && productSellObj.getSellId()!= 0)  where += " and t_productOrder.productSellObj=" + productSellObj.getSellId();
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_productOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productOrder.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return productOrderMapper.queryProductOrder(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProductOrder> queryProductOrder(ProductSell productSellObj,UserInfo userObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != productSellObj && productSellObj.getSellId()!= null && productSellObj.getSellId()!= 0)  where += " and t_productOrder.productSellObj=" + productSellObj.getSellId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productOrder.addTime like '%" + addTime + "%'";
    	return productOrderMapper.queryProductOrderList(where);
    }

    /*查询所有农产品订单记录*/
    public ArrayList<ProductOrder> queryAllProductOrder()  throws Exception {
        return productOrderMapper.queryProductOrderList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(ProductSell productSellObj,UserInfo userObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(null != productSellObj && productSellObj.getSellId()!= null && productSellObj.getSellId()!= 0)  where += " and t_productOrder.productSellObj=" + productSellObj.getSellId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productOrder.addTime like '%" + addTime + "%'";
        recordNumber = productOrderMapper.queryProductOrderCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取农产品订单记录*/
    public ProductOrder getProductOrder(int orderId) throws Exception  {
        ProductOrder productOrder = productOrderMapper.getProductOrder(orderId);
        return productOrder;
    }

    /*更新农产品订单记录*/
    public void updateProductOrder(ProductOrder productOrder) throws Exception {
        productOrderMapper.updateProductOrder(productOrder);
    }

    /*删除一条农产品订单记录*/
    public void deleteProductOrder (int orderId) throws Exception {
        productOrderMapper.deleteProductOrder(orderId);
    }

    /*删除多条农产品订单信息*/
    public int deleteProductOrders (String orderIds) throws Exception {
    	String _orderIds[] = orderIds.split(",");
    	for(String _orderId: _orderIds) {
    		productOrderMapper.deleteProductOrder(Integer.parseInt(_orderId));
    	}
    	return _orderIds.length;
    }
}
