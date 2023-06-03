package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.ProductSeek;

import com.chengxusheji.mapper.ProductSeekMapper;
@Service
public class ProductSeekService {

	@Resource ProductSeekMapper productSeekMapper;
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

    /*添加求购记录*/
    public void addProductSeek(ProductSeek productSeek) throws Exception {
    	productSeekMapper.addProductSeek(productSeek);
    }

    /*按照查询条件分页查询求购记录*/
    public ArrayList<ProductSeek> queryProductSeek(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSeek.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSeek.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSeek.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSeek.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSeek.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_productSeek.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSeek.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return productSeekMapper.queryProductSeek(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProductSeek> queryProductSeek(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSeek.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSeek.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSeek.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSeek.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSeek.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productSeek.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSeek.addTime like '%" + addTime + "%'";
    	return productSeekMapper.queryProductSeekList(where);
    }

    /*查询所有求购记录*/
    public ArrayList<ProductSeek> queryAllProductSeek()  throws Exception {
        return productSeekMapper.queryProductSeekList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String productName,ProductClass productClassObj,String publish,String author,String xjcd,UserInfo userObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(!productName.equals("")) where = where + " and t_productSeek.productName like '%" + productName + "%'";
    	if(null != productClassObj && productClassObj.getProductClassId()!= null && productClassObj.getProductClassId()!= 0)  where += " and t_productSeek.productClassObj=" + productClassObj.getProductClassId();
    	if(!publish.equals("")) where = where + " and t_productSeek.publish like '%" + publish + "%'";
    	if(!author.equals("")) where = where + " and t_productSeek.author like '%" + author + "%'";
    	if(!xjcd.equals("")) where = where + " and t_productSeek.xjcd like '%" + xjcd + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_productSeek.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_productSeek.addTime like '%" + addTime + "%'";
        recordNumber = productSeekMapper.queryProductSeekCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取求购记录*/
    public ProductSeek getProductSeek(int seekId) throws Exception  {
        ProductSeek productSeek = productSeekMapper.getProductSeek(seekId);
        return productSeek;
    }

    /*更新求购记录*/
    public void updateProductSeek(ProductSeek productSeek) throws Exception {
        productSeekMapper.updateProductSeek(productSeek);
    }

    /*删除一条求购记录*/
    public void deleteProductSeek (int seekId) throws Exception {
        productSeekMapper.deleteProductSeek(seekId);
    }

    /*删除多条求购信息*/
    public int deleteProductSeeks (String seekIds) throws Exception {
    	String _seekIds[] = seekIds.split(",");
    	for(String _seekId: _seekIds) {
    		productSeekMapper.deleteProductSeek(Integer.parseInt(_seekId));
    	}
    	return _seekIds.length;
    }
}
