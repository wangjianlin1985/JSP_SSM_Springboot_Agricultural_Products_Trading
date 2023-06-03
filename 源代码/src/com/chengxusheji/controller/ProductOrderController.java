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
import com.chengxusheji.po.ProductOrder;
import com.chengxusheji.service.ProductSellService;
import com.chengxusheji.po.ProductSell;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//ProductOrder管理控制层
@Controller
@RequestMapping("/ProductOrder")
public class ProductOrderController extends BaseController {

    /*业务层对象*/
    @Resource ProductOrderService productOrderService;

    @Resource ProductSellService productSellService;
    @Resource UserInfoService userInfoService;
	@InitBinder("productSellObj")
	public void initBinderproductSellObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productSellObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("productOrder")
	public void initBinderProductOrder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productOrder.");
	}
	/*跳转到添加ProductOrder视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProductOrder());
		/*查询所有的ProductSell信息*/
		List<ProductSell> productSellList = productSellService.queryAllProductSell();
		request.setAttribute("productSellList", productSellList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "ProductOrder_add";
	}

	/*客户端ajax方式提交添加农产品订单信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProductOrder productOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		int sellId = productOrder.getProductSellObj().getSellId();
		String sellUserName = productSellService.getProductSell(sellId).getUserObj().getUser_name();
		 
		String orderUserName = productOrder.getUserObj().getUser_name();
		if(sellUserName.equals(orderUserName)) {
			message = "你不能购买自己的农产品";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		productOrder.setAddTime(sdf.format(new java.util.Date()));
		
        productOrderService.addProductOrder(productOrder);
        message = "农产品订单添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询农产品订单信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productSellObj") ProductSell productSellObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (addTime == null) addTime = "";
		if(rows != 0)productOrderService.setRows(rows);
		List<ProductOrder> productOrderList = productOrderService.queryProductOrder(productSellObj, userObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    productOrderService.queryTotalPageAndRecordNumber(productSellObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productOrderService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProductOrder productOrder:productOrderList) {
			JSONObject jsonProductOrder = productOrder.getJsonObject();
			jsonArray.put(jsonProductOrder);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询农产品订单信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProductOrder> productOrderList = productOrderService.queryAllProductOrder();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProductOrder productOrder:productOrderList) {
			JSONObject jsonProductOrder = new JSONObject();
			jsonProductOrder.accumulate("orderId", productOrder.getOrderId());
			jsonArray.put(jsonProductOrder);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询农产品订单信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productSellObj") ProductSell productSellObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (addTime == null) addTime = "";
		List<ProductOrder> productOrderList = productOrderService.queryProductOrder(productSellObj, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productOrderService.queryTotalPageAndRecordNumber(productSellObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productOrderService.getRecordNumber();
	    request.setAttribute("productOrderList",  productOrderList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productSellObj", productSellObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<ProductSell> productSellList = productSellService.queryAllProductSell();
	    request.setAttribute("productSellList", productSellList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProductOrder/productOrder_frontquery_result"; 
	}

	/*前台按照查询条件分页查询农产品订单信息*/
	@RequestMapping(value = { "/frontUserlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontUserlist(@ModelAttribute("productSellObj") ProductSell productSellObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (addTime == null) addTime = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		List<ProductOrder> productOrderList = productOrderService.queryProductOrder(productSellObj, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    productOrderService.queryTotalPageAndRecordNumber(productSellObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = productOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = productOrderService.getRecordNumber();
	    request.setAttribute("productOrderList",  productOrderList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productSellObj", productSellObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<ProductSell> productSellList = productSellService.queryAllProductSell();
	    request.setAttribute("productSellList", productSellList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProductOrder/productOrder_frontUserquery_result"; 
	}
	
	
     /*前台查询ProductOrder信息*/
	@RequestMapping(value="/{orderId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer orderId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键orderId获取ProductOrder对象*/
        ProductOrder productOrder = productOrderService.getProductOrder(orderId);

        List<ProductSell> productSellList = productSellService.queryAllProductSell();
        request.setAttribute("productSellList", productSellList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("productOrder",  productOrder);
        return "ProductOrder/productOrder_frontshow";
	}

	/*ajax方式显示农产品订单修改jsp视图页*/
	@RequestMapping(value="/{orderId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer orderId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键orderId获取ProductOrder对象*/
        ProductOrder productOrder = productOrderService.getProductOrder(orderId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProductOrder = productOrder.getJsonObject();
		out.println(jsonProductOrder.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新农产品订单信息*/
	@RequestMapping(value = "/{orderId}/update", method = RequestMethod.POST)
	public void update(@Validated ProductOrder productOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			productOrderService.updateProductOrder(productOrder);
			message = "农产品订单更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "农产品订单更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除农产品订单信息*/
	@RequestMapping(value="/{orderId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer orderId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  productOrderService.deleteProductOrder(orderId);
	            request.setAttribute("message", "农产品订单删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "农产品订单删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条农产品订单记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String orderIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = productOrderService.deleteProductOrders(orderIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出农产品订单信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productSellObj") ProductSell productSellObj,@ModelAttribute("userObj") UserInfo userObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(addTime == null) addTime = "";
        List<ProductOrder> productOrderList = productOrderService.queryProductOrder(productSellObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProductOrder信息记录"; 
        String[] headers = { "订单id","农产品信息","意向用户","意向出价","用户备注","下单时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<productOrderList.size();i++) {
        	ProductOrder productOrder = productOrderList.get(i); 
        	dataset.add(new String[]{productOrder.getOrderId() + "",productOrder.getProductSellObj().getProductName(),productOrder.getUserObj().getName(),productOrder.getPrice() + "",productOrder.getOrderMemo(),productOrder.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ProductOrder.xls");//filename是下载的xls的名，建议最好用英文 
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
