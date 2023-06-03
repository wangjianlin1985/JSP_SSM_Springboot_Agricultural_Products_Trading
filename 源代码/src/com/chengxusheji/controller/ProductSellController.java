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
import com.chengxusheji.service.ProductOrderService;
import com.chengxusheji.service.ProductSellService;
import com.chengxusheji.po.ProductOrder;
import com.chengxusheji.po.ProductSell;
import com.chengxusheji.service.ProductClassService;
import com.chengxusheji.po.ProductClass;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//ProductSell管理控制层
@Controller
@RequestMapping("/ProductSell")
public class ProductSellController extends BaseController {

    /*业务层对象*/
    @Resource ProductSellService productSellService;
    @Resource ProductOrderService productOrderService;

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
	@InitBinder("productSell")
	public void initBinderProductSell(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productSell.");
	}
	/*跳转到添加ProductSell视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProductSell());
		/*查询所有的ProductClass信息*/
		List<ProductClass> productClassList = productClassService.queryAllProductClass();
		request.setAttribute("productClassList", productClassList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "ProductSell_add";
	}

	/*客户端ajax方式提交添加农产品预售信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProductSell productSell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			productSell.setProductPhoto(this.handlePhotoUpload(request, "productPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        productSellService.addProductSell(productSell);
        message = "农产品预售添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式提交添加农产品预售信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(@Validated ProductSell productSell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		
		UserInfo userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		productSell.setUserObj(userObj);
		
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			productSell.setProductPhoto(this.handlePhotoUpload(request, "productPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		productSell.setAddTime(sdf.format(new java.util.Date()));
		
        productSellService.addProductSell(productSell);
        message = "农产品预售添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*ajax方式按照查询条件分页查询农产品预售信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		if(rows != 0)productSellService.setRows(rows);
		List<ProductSell> productSellList = productSellService.queryProductSell(productName, productClassObj, publish, author, xjcd, userObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    productSellService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSellService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProductSell productSell:productSellList) {
			JSONObject jsonProductSell = productSell.getJsonObject();
			jsonArray.put(jsonProductSell);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询农产品预售信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProductSell> productSellList = productSellService.queryAllProductSell();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProductSell productSell:productSellList) {
			JSONObject jsonProductSell = new JSONObject();
			jsonProductSell.accumulate("sellId", productSell.getSellId());
			jsonProductSell.accumulate("productName", productSell.getProductName());
			jsonArray.put(jsonProductSell);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询农产品预售信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		List<ProductSell> productSellList = productSellService.queryProductSell(productName, productClassObj, publish, author, xjcd, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productSellService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSellService.getRecordNumber();
	    request.setAttribute("productSellList",  productSellList);
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
		return "ProductSell/productSell_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询农产品预售信息*/
	@RequestMapping(value = { "/frontUserlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String fronUsertlist(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (productName == null) productName = "";
		if (publish == null) publish = "";
		if (author == null) author = "";
		if (xjcd == null) xjcd = "";
		if (addTime == null) addTime = "";
		
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<ProductSell> productSellList = productSellService.queryProductSell(productName, productClassObj, publish, author, xjcd, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productSellService.queryTotalPageAndRecordNumber(productName, productClassObj, publish, author, xjcd, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productSellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productSellService.getRecordNumber();
	    request.setAttribute("productSellList",  productSellList);
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
		return "ProductSell/productSell_frontUserquery_result"; 
	}
	

     /*前台查询ProductSell信息*/
	@RequestMapping(value="/{sellId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer sellId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键sellId获取ProductSell对象*/
        ProductSell productSell = productSellService.getProductSell(sellId);
        ArrayList<ProductOrder> productOrderList = productOrderService.queryProductOrder(productSell, null, "");
        List<ProductClass> productClassList = productClassService.queryAllProductClass();
        request.setAttribute("productClassList", productClassList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("productSell",  productSell);
        request.setAttribute("productOrderList", productOrderList); 
        
        return "ProductSell/productSell_frontshow";
	}

	/*ajax方式显示农产品预售修改jsp视图页*/
	@RequestMapping(value="/{sellId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer sellId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键sellId获取ProductSell对象*/
        ProductSell productSell = productSellService.getProductSell(sellId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProductSell = productSell.getJsonObject();
		out.println(jsonProductSell.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新农产品预售信息*/
	@RequestMapping(value = "/{sellId}/update", method = RequestMethod.POST)
	public void update(@Validated ProductSell productSell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String productPhotoFileName = this.handlePhotoUpload(request, "productPhotoFile");
		if(!productPhotoFileName.equals("upload/NoImage.jpg"))productSell.setProductPhoto(productPhotoFileName); 


		try {
			productSellService.updateProductSell(productSell);
			message = "农产品预售更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "农产品预售更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除农产品预售信息*/
	@RequestMapping(value="/{sellId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer sellId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  productSellService.deleteProductSell(sellId);
	            request.setAttribute("message", "农产品预售删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "农产品预售删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条农产品预售记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String sellIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = productSellService.deleteProductSells(sellIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出农产品预售信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String productName,@ModelAttribute("productClassObj") ProductClass productClassObj,String publish,String author,String xjcd,@ModelAttribute("userObj") UserInfo userObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(productName == null) productName = "";
        if(publish == null) publish = "";
        if(author == null) author = "";
        if(xjcd == null) xjcd = "";
        if(addTime == null) addTime = "";
        List<ProductSell> productSellList = productSellService.queryProductSell(productName,productClassObj,publish,author,xjcd,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProductSell信息记录"; 
        String[] headers = { "出售id","农产品主图","农产品名称","农产品类别","生产地","制造商","出售价格","新旧程度","发布用户","用户发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<productSellList.size();i++) {
        	ProductSell productSell = productSellList.get(i); 
        	dataset.add(new String[]{productSell.getSellId() + "",productSell.getProductPhoto(),productSell.getProductName(),productSell.getProductClassObj().getProductClassName(),productSell.getPublish(),productSell.getAuthor(),productSell.getSellPrice() + "",productSell.getXjcd(),productSell.getUserObj().getName(),productSell.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ProductSell.xls");//filename是下载的xls的名，建议最好用英文 
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
