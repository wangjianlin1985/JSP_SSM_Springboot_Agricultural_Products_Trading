package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProductSeek;

public interface ProductSeekMapper {
	/*添加求购信息*/
	public void addProductSeek(ProductSeek productSeek) throws Exception;

	/*按照查询条件分页查询求购记录*/
	public ArrayList<ProductSeek> queryProductSeek(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有求购记录*/
	public ArrayList<ProductSeek> queryProductSeekList(@Param("where") String where) throws Exception;

	/*按照查询条件的求购记录数*/
	public int queryProductSeekCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条求购记录*/
	public ProductSeek getProductSeek(int seekId) throws Exception;

	/*更新求购记录*/
	public void updateProductSeek(ProductSeek productSeek) throws Exception;

	/*删除求购记录*/
	public void deleteProductSeek(int seekId) throws Exception;

}
