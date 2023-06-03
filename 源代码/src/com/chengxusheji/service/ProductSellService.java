package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.ProductSell;

import com.chengxusheji.mapper.ProductSellMapper;
@Service
public class ProductSellService {

	@Resource ProductSellMapper productSellMapper;
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

    /*添加农产品预售记录*/
    public void addProductSell(ProductSell productSell) throws Exception {
    	productSellMapper.addProductSell(productSell);
    }

    /*按照查询条件分页查询农产品预售记录*/
    public ArrayList<ProductSell> queryProductSell(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSell.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSell.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSell.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSell.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSell.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_productSell.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSell.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return productSellMapper.queryProductSell(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProductSell> queryProductSell(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSell.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSell.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSell.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSell.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSell.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productSell.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSell.addTime like '%" + addTime + "%'";
    	return productSellMapper.queryProductSellList(where);
    }

    /*查询所有农产品预售记录*/
    public ArrayList<ProductSell> queryAllProductSell()  throws Exception {
        return productSellMapper.queryProductSellList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSell.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSell.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSell.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSell.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSell.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productSell.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSell.addTime like '%" + addTime + "%'";
        recordNumber = productSellMapper.queryProductSellCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取农产品预售记录*/
    public ProductSell getProductSell(int sellId) throws Exception  {
        ProductSell productSell = productSellMapper.getProductSell(sellId);
        return productSell;
    }

    /*更新农产品预售记录*/
    public void updateProductSell(ProductSell productSell) throws Exception {
        productSellMapper.updateProductSell(productSell);
    }

    /*删除一条农产品预售记录*/
    public void deleteProductSell (int sellId) throws Exception {
        productSellMapper.deleteProductSell(sellId);
    }

    /*删除多条农产品预售信息*/
    public int deleteProductSells (String sellIds) throws Exception {
    	String _sellIds[] = sellIds.split(",");
    	for(String _sellId: _sellIds) {
    		productSellMapper.deleteProductSell(Integer.parseInt(_sellId));
    	}
    	return _sellIds.length;
    }
}
