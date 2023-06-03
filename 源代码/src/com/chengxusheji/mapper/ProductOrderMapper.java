package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProductOrder;

public interface ProductOrderMapper {
	/*添加农产品订单信息*/
	public void addProductOrder(ProductOrder productOrder) throws Exception;

	/*按照查询条件分页查询农产品订单记录*/
	public ArrayList<ProductOrder> queryProductOrder(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有农产品订单记录*/
	public ArrayList<ProductOrder> queryProductOrderList(@Param("where") String where) throws Exception;

	/*按照查询条件的农产品订单记录数*/
	public int queryProductOrderCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条农产品订单记录*/
	public ProductOrder getProductOrder(int orderId) throws Exception;

	/*更新农产品订单记录*/
	public void updateProductOrder(ProductOrder productOrder) throws Exception;

	/*删除农产品订单记录*/
	public void deleteProductOrder(int orderId) throws Exception;

}
