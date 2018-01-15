<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
	
	<script>
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			//循环取值
			var arr = [];
			$("#contentTable tbody tr").each(function(){
				var goodsid = $(this).find("td").eq(0).find("input").attr("id");
				var kdgs = $(this).find("#kdgs").val();
				var wldh = $(this).find("#wldh").val();
				var each = {};
				each.id = goodsid;
				each.kdgs = kdgs;
				each.wldh = wldh;
				arr.push(each); 
			});
		
			if(validateForm.form()){
				$("#ids").val(JSON.stringify(arr));
				$("#inputForm").submit();
			    return true;
			} 
			
			return false;
		}
		
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>菜品订单批量发货列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<form:form id="inputForm" modelAttribute="goodsOrder" action="${ctx}/goodsorder/goodsOrder/piliangFahuo" method="post" class="form-horizontal">
		<input type="hidden" name="ids" id="ids"/>
	
	<!-- 工具栏 -->
	<div class="row" hidden="hidden">
	<div class="col-sm-12">
		<div class="pull-left">
		
			<shiro:hasPermission name="goodsorder:goodsOrder:add">
				<table:addRow url="${ctx}/goodsorder/goodsOrder/form" title="菜品订单" width="60%" height="80%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="goodsorder:goodsOrder:edit">
				    <table:editRow url="${ctx}/goodsorder/goodsOrder/form" title="菜品订单" id="contentTable" width="60%" height="80%"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="goodsorder:goodsOrder:del">
				<table:delRow url="${ctx}/goodsorder/goodsOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goodsorder:goodsOrder:import">
				<table:importExcel url="${ctx}/goodsorder/goodsOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goodsorder:goodsOrder:export">
	       		<table:exportExcel url="${ctx}/goodsorder/goodsOrder/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       <shiro:hasPermission name="goodsorder:goodsOrder:edit">
				    <table:editRow url="${ctx}/goodsorder/goodsOrder/form" title="菜品订单" id="contentTable" width="60%" height="80%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
		
			</div>
		<div class="pull-right" hidden="hidden">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->

	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column ddh">订单编号</th>
				<th  class="sort-column cardid">套餐卡卡号	</th>
				<th  class="sort-column memberid">购买会员</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shrname">收货人</th>
				<th  class="sort-column kdgs">快递公司</th>
				<th  class="sort-column wldh">物流单号</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${lists}" var="goodsOrder">
			<tr>
				
				<td> <input type="checkbox" name="id" id="${goodsOrder.id}" class="i-checks"></td>
				<td>
					${goodsOrder.ddh}<br/>
				</td>
				<td>
					${goodsOrder.cardName}
				</td>
				<td>
					${goodsOrder.memberName}
				</td>
				<td>
					${fns:getDictLabel(goodsOrder.status, 'orderStatus', '')}
				</td>
				<td>
					${goodsOrder.shrname}
				</td>
				<td>
					<form:input path="kdgs" htmlEscape="false" value="顺丰速递" class="form-control" type="hidden"/>
					<label class="pull-left">顺丰速递</label>
				</td>
				<td>
					<form:input path="wldh" htmlEscape="false" class="form-control digits" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form:form>
		<!-- 分页代码 -->
	<%-- <table:page page="${page}"></table:page> --%>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>