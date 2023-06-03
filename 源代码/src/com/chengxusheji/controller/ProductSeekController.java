package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.ProductSeekService;
import com.chengxusheji.po.ProductSeek;
import com.chengxusheji.service.ProductClassService;
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//ProductSeek管理控制层
@Controller
@RequestMapping("/ProductSeek")
public class ProductSeekController extends BaseController {

    /*业务层对象*/
    @Resource ProductSeekService productSeekService;

    @Resource ProductClassService productClassService;
    @Resource UserInfoService userInfoService;
	@InitBinder("productClassObj")
	public void initBinderproductClassObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productClassObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("productSeek")
	public void initBinderProductSeek(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productSeek.");
	}
	/*跳转到添加ProductSeek视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProductSeek());
		/*查询所有的ProductClass信息*/
		List<ProductClass> productClassList = productClassService.queryAllProductClass();
		request.setAttribute("productClassList", productClassList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "ProductSeek_add";
	}

	/*客户端ajax方式提交添加求购信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProductSeek productSeek, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			productSeek.setProductPhoto(this.handlePhotoUpload(request, "productPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        productSeekService.addProductSeek(productSeek);
        message = "求购添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式提交添加求购信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(@Validated ProductSeek productSeek, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		
		String userName = (String)session.getAttribute("user_name");
		if(userName == null) {
			message = "请先登录系统！";
			writeJsonResponse(response, success, message);
			return ;	
		}
		
		UserInfo userObj = new UserInfo();
		userObj.setUser_name(userName);
		productSeek.setUserObj(userObj);
		
		
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		try {
			productSeek.setProductPhoto(this.handlePhotoUpload(request, "productPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		productSeek.setAddTime(sdf.format(new java.util.Date()));
		
        productSeekService.addProductSeek(productSeek);
        message = "求购添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询求购信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		if(rows != 0)productSeekService.setRows(rows);
		List<ProductSeek> productSeekList = productSeekService.queryProductSeek(productName, productClassObj, publish, author, xjcd, userObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    productSeekService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSeekService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSeekService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProductSeek productSeek:productSeekList) {
			JSONObject jsonProductSeek = productSeek.getJsonObject();
			jsonArray.put(jsonProductSeek);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询求购信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProductSeek> productSeekList = productSeekService.queryAllProductSeek();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProductSeek productSeek:productSeekList) {
			JSONObject jsonProductSeek = new JSONObject();
			jsonProductSeek.accumulate("seekId", productSeek.getSeekId());
			jsonProductSeek.accumulate("productName", productSeek.getProductName());
			jsonArray.put(jsonProductSeek);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询求购信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		List<ProductSeek> productSeekList = productSeekService.queryProductSeek(productName, productClassObj, publish, author, xjcd, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productSeekService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSeekService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSeekService.getRecordNumber();
	    request.setAttribute("productSeekList",  productSeekList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productName", productName);
	    request.setAttribute("productClassObj", productClassObj);
	    request.setAttribute("publish", publish);
	    request.setAttribute("author", author);
	    request.setAttribute("xjcd", xjcd);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<ProductClass> productClassList = productClassService.queryAllProductClass();
	    request.setAttribute("productClassList", productClassList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProductSeek/productSeek_frontquery_result"; 
	}
	
	
	/*用户前台按照查询条件分页查询自己的求购信息*/
	@RequestMapping(value = { "/frontUserlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontUserlist(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		List<ProductSeek> productSeekList = productSeekService.queryProductSeek(productName, productClassObj, publish, author, xjcd, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productSeekService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSeekService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSeekService.getRecordNumber();
	    request.setAttribute("productSeekList",  productSeekList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productName", productName);
	    request.setAttribute("productClassObj", productClassObj);
	    request.setAttribute("publish", publish);
	    request.setAttribute("author", author);
	    request.setAttribute("xjcd", xjcd);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<ProductClass> productClassList = productClassService.queryAllProductClass();
	    request.setAttribute("productClassList", productClassList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProductSeek/productSeek_frontUserquery_result"; 
	}
	
	

     /*前台查询ProductSeek信息*/
	@RequestMapping(value="/{seekId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer seekId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键seekId获取ProductSeek对象*/
        ProductSeek productSeek = productSeekService.getProductSeek(seekId);

        List<ProductClass> productClassList = productClassService.queryAllProductClass();
        request.setAttribute("productClassList", productClassList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("productSeek",  productSeek);
        return "ProductSeek/productSeek_frontshow";
	}

	/*ajax方式显示求购修改jsp视图页*/
	@RequestMapping(value="/{seekId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer seekId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键seekId获取ProductSeek对象*/
        ProductSeek productSeek = productSeekService.getProductSeek(seekId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProductSeek = productSeek.getJsonObject();
		out.println(jsonProductSeek.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新求购信息*/
	@RequestMapping(value = "/{seekId}/update", method = RequestMethod.POST)
	public void update(@Validated ProductSeek productSeek, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String productPhotoFileName = this.handlePhotoUpload(request, "productPhotoFile");
		if(!productPhotoFileName.equals("upload/NoImage.jpg"))productSeek.setProductPhoto(productPhotoFileName); 


		try {
			productSeekService.updateProductSeek(productSeek);
			message = "求购更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "求购更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除求购信息*/
	@RequestMapping(value="/{seekId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer seekId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  productSeekService.deleteProductSeek(seekId);
	            request.setAttribute("message", "求购删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "求购删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条求购记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String seekIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = productSeekService.deleteProductSeeks(seekIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出求购信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(productName == null) productName = "";
        if(publish == null) publish = "";
        if(author == null) author = "";
        if(xjcd == null) xjcd = "";
        if(addTime == null) addTime = "";
        List<ProductSeek> productSeekList = productSeekService.queryProductSeek(productName,productClassObj,publish,author,xjcd,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProductSeek信息记录"; 
        String[] headers = { "求购id","农产品主图","农产品名称","农产品类别","生产地","制造商","求购价格","新旧程度","发布用户","用户发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<productSeekList.size();i++) {
        	ProductSeek productSeek = productSeekList.get(i); 
        	dataset.add(new String[]{productSeek.getSeekId() + "",productSeek.getProductPhoto(),productSeek.getProductName(),productSeek.getProductClassObj().getProductClassName(),productSeek.getPublish(),productSeek.getAuthor(),productSeek.getPrice() + "",productSeek.getXjcd(),productSeek.getUserObj().getName(),productSeek.getAddTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ProductSeek.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
