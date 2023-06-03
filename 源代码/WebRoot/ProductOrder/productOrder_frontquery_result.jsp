<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.ProductOrder" %>
<%@ page import="com.chengxusheji.po.ProductSell" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<ProductOrder> productOrderList = (List<ProductOrder>)request.getAttribute("productOrderList");
    //获取所有的productSellObj信息
    List<ProductSell> productSellList = (List<ProductSell>)request.getAttribute("productSellList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    ProductSell productSellObj = (ProductSell)request.getAttribute("productSellObj");
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String addTime = (String)request.getAttribute("addTime"); //下单时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>农产品订单查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#productOrderListPanel" aria-controls="productOrderListPanel" role="tab" data-toggle="tab">农产品订单列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>ProductOrder/productOrder_frontAdd.jsp" style="display:none;">添加农产品订单</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="productOrderListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>订单id</td><td>农产品信息</td><td>意向用户</td><td>意向出价</td><td>用户备注</td><td>下单时间</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<productOrderList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		ProductOrder productOrder = productOrderList.get(i); //获取到农产品订单对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=productOrder.getOrderId() %></td>
 											<td><%=productOrder.getProductSellObj().getProductName() %></td>
 											<td><%=productOrder.getUserObj().getName() %></td>
 											<td><%=productOrder.getPrice() %></td>
 											<td><%=productOrder.getOrderMemo() %></td>
 											<td><%=productOrder.getAddTime() %></td>
 											<td>
 												<a href="<%=basePath  %>ProductOrder/<%=productOrder.getOrderId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="productOrderEdit('<%=productOrder.getOrderId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="productOrderDelete('<%=productOrder.getOrderId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

				    		<div class="row">
					            <div class="col-md-12">
						            <nav class="pull-left">
						                <ul class="pagination">
						                    <li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
						                     <%
						                    	int startPage = currentPage - 5;
						                    	int endPage = currentPage + 5;
						                    	if(startPage < 1) startPage=1;
						                    	if(endPage > totalPage) endPage = totalPage;
						                    	for(int i=startPage;i<=endPage;i++) {
						                    %>
						                    <li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
						                    <%  } %> 
						                    <li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						                </ul>
						            </nav>
						            <div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
					            </div>
				            </div> 
				    </div>
				</div>
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>农产品订单查询</h1>
		</div>
		<form name="productOrderQueryForm" id="productOrderQueryForm" action="<%=basePath %>ProductOrder/frontlist" class="mar_t15">
            <div class="form-group">
            	<label for="productSellObj_sellId">农产品信息：</label>
                <select id="productSellObj_sellId" name="productSellObj.sellId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(ProductSell productSellTemp:productSellList) {
	 					String selected = "";
 					if(productSellObj!=null && productSellObj.getSellId()!=null && productSellObj.getSellId().intValue()==productSellTemp.getSellId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=productSellTemp.getSellId() %>" <%=selected %>><%=productSellTemp.getProductName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="userObj_user_name">意向用户：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="addTime">下单时间:</label>
				<input type="text" id="addTime" name="addTime" value="<%=addTime %>" class="form-control" placeholder="请输入下单时间">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="productOrderEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;农产品订单信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="productOrderEditForm" id="productOrderEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="productOrder_orderId_edit" class="col-md-3 text-right">订单id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="productOrder_orderId_edit" name="productOrder.orderId" class="form-control" placeholder="请输入订单id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="productOrder_productSellObj_sellId_edit" class="col-md-3 text-right">农产品信息:</label>
		  	 <div class="col-md-9">
			    <select id="productOrder_productSellObj_sellId_edit" name="productOrder.productSellObj.sellId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="productOrder_userObj_user_name_edit" class="col-md-3 text-right">意向用户:</label>
		  	 <div class="col-md-9">
			    <select id="productOrder_userObj_user_name_edit" name="productOrder.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="productOrder_price_edit" class="col-md-3 text-right">意向出价:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="productOrder_price_edit" name="productOrder.price" class="form-control" placeholder="请输入意向出价">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="productOrder_orderMemo_edit" class="col-md-3 text-right">用户备注:</label>
		  	 <div class="col-md-9">
			    <textarea id="productOrder_orderMemo_edit" name="productOrder.orderMemo" rows="8" class="form-control" placeholder="请输入用户备注"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="productOrder_addTime_edit" class="col-md-3 text-right">下单时间:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="productOrder_addTime_edit" name="productOrder.addTime" class="form-control" placeholder="请输入下单时间">
			 </div>
		  </div>
		</form> 
	    <style>#productOrderEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxProductOrderModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.productOrderQueryForm.currentPage.value = currentPage;
    document.productOrderQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.productOrderQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.productOrderQueryForm.currentPage.value = pageValue;
    documentproductOrderQueryForm.submit();
}

/*弹出修改农产品订单界面并初始化数据*/
function productOrderEdit(orderId) {
	$.ajax({
		url :  basePath + "ProductOrder/" + orderId + "/update",
		type : "get",
		dataType: "json",
		success : function (productOrder, response, status) {
			if (productOrder) {
				$("#productOrder_orderId_edit").val(productOrder.orderId);
				$.ajax({
					url: basePath + "ProductSell/listAll",
					type: "get",
					success: function(productSells,response,status) { 
						$("#productOrder_productSellObj_sellId_edit").empty();
						var html="";
		        		$(productSells).each(function(i,productSell){
		        			html += "<option value='" + productSell.sellId + "'>" + productSell.productName + "</option>";
		        		});
		        		$("#productOrder_productSellObj_sellId_edit").html(html);
		        		$("#productOrder_productSellObj_sellId_edit").val(productOrder.productSellObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#productOrder_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#productOrder_userObj_user_name_edit").html(html);
		        		$("#productOrder_userObj_user_name_edit").val(productOrder.userObjPri);
					}
				});
				$("#productOrder_price_edit").val(productOrder.price);
				$("#productOrder_orderMemo_edit").val(productOrder.orderMemo);
				$("#productOrder_addTime_edit").val(productOrder.addTime);
				$('#productOrderEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除农产品订单信息*/
function productOrderDelete(orderId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "ProductOrder/deletes",
			data : {
				orderIds : orderId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#productOrderQueryForm").submit();
					//location.href= basePath + "ProductOrder/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交农产品订单信息表单给服务器端修改*/
function ajaxProductOrderModify() {
	$.ajax({
		url :  basePath + "ProductOrder/" + $("#productOrder_orderId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#productOrderEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#productOrderQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

})
</script>
</body>
</html>

