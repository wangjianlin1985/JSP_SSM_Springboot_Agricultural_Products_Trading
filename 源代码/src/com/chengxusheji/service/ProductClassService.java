﻿package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ProductClass;

import com.chengxusheji.mapper.ProductClassMapper;
@Service
public class ProductClassService {

	@Resource ProductClassMapper productClassMapper;
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

    /*添加农产品类别记录*/
    public void addProductClass(ProductClass productClass) throws Exception {
    	productClassMapper.addProductClass(productClass);
    }

    /*按照查询条件分页查询农产品类别记录*/
    public ArrayList<ProductClass> queryProductClass(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return productClassMapper.queryProductClass(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProductClass> queryProductClass() throws Exception  { 
     	String where = "where 1=1";
    	return productClassMapper.queryProductClassList(where);
    }

    /*查询所有农产品类别记录*/
    public ArrayList<ProductClass> queryAllProductClass()  throws Exception {
        return productClassMapper.queryProductClassList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = productClassMapper.queryProductClassCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取农产品类别记录*/
    public ProductClass getProductClass(int productClassId) throws Exception  {
        ProductClass productClass = productClassMapper.getProductClass(productClassId);
        return productClass;
    }

    /*更新农产品类别记录*/
    public void updateProductClass(ProductClass productClass) throws Exception {
        productClassMapper.updateProductClass(productClass);
    }

    /*删除一条农产品类别记录*/
    public void deleteProductClass (int productClassId) throws Exception {
        productClassMapper.deleteProductClass(productClassId);
    }

    /*删除多条农产品类别信息*/
    public int deleteProductClasss (String productClassIds) throws Exception {
    	String _productClassIds[] = productClassIds.split(",");
    	for(String _productClassId: _productClassIds) {
    		productClassMapper.deleteProductClass(Integer.parseInt(_productClassId));
    	}
    	return _productClassIds.length;
    }
}
