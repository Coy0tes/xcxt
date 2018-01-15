<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购套餐订单管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		$(document).ready(function() {
		    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
		    	});

		    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
		    	});
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>团购套餐订单管理列表 </h5>
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
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="tuangouPackageOrder" action="${ctx}/tuangoupackageorder/tuangouPackageOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<div>
				<span>订单编号：</span>
					<form:input path="ddh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/> &nbsp;&nbsp;&nbsp;&nbsp;
				<span>注册手机号：</span>
					<form:input path="membermobile" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>购买会员：</span>
					<form:input path="memberName" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>收货人姓名：</span>
					<form:input path="shrname" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>收货人电话：</span>
					<form:input path="lxdh" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="padding:5px">
				<span>下单开始日期：</span>
					<form:input path="startTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  onfocus="this.blur()" class=" form-control input-sm"/>
				<span>下单结束日期：</span>
					<form:input path="endTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  onfocus="this.blur()" class=" form-control input-sm"/>
			</div>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:add">
				<table:addRow url="${ctx}/tuangoupackageorder/tuangouPackageOrder/form" title="团购套餐订单管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:edit">
			    <table:editRow url="${ctx}/tuangoupackageorder/tuangouPackageOrder/form" title="团购套餐订单管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:del">
				<table:delRow url="${ctx}/tuangoupackageorder/tuangouPackageOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:import">
				<table:importExcel url="${ctx}/tuangoupackageorder/tuangouPackageOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:export">
	       		<table:exportExcel url="${ctx}/tuangoupackageorder/tuangouPackageOrder/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
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
				<th  class="sort-column tuangouOrderName">团购套餐</th>
				<th  class="sort-column memberid">购买会员</th>
				<th  class="sort-column membermobile">注册手机号</th>
				<th  class="sort-column num">购买总数量</th>
				<th  class="sort-column sfprice">支付总价格</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shrname">收货人姓名</th>
				<th  class="sort-column lxdh">收货人电话</th>
				<!-- <th  class="sort-column shrprovince">收货人所在省</th>
				<th  class="sort-column shrcity">收货人所在市</th>
				<th  class="sort-column shrcounty">收货人所在区</th>
				<th  class="sort-column shraddress">收货详细地址</th>
				<th  class="sort-column kdgs">快递公司</th>
				<th  class="sort-column wldh">物流单号</th>
				<th  class="sort-column fahuotime">发货时间</th> -->
				<th  class="sort-column zuofeitime">作废时间</th>
				<th  class="sort-column sftk">是否退款</th>
				<th  class="sort-column principal">作废处理人</th>
				<th  class="sort-column qxzt">取消状态</th>
				<th  class="sort-column createDate">下单时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tuangouPackageOrder">
			<tr>
				<td> <input type="checkbox" id="${tuangouPackageOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看团购套餐订单管理', '${ctx}/tuangoupackageorder/tuangouPackageOrder/form?id=${tuangouPackageOrder.id}','85%', '95%')">
					${tuangouPackageOrder.ddh}
				</a></td>
				<td>
					${tuangouPackageOrder.tuangouOrderName}
				</td>
				<td>
					${tuangouPackageOrder.memberName}
				</td>
				<td>
					${tuangouPackageOrder.membermobile}
				</td>
				<td>
					${tuangouPackageOrder.num}
				</td>
				<td>
					${tuangouPackageOrder.sfprice}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackageOrder.status, 'orderStatus', '')}
				</td>
				<td>
					${tuangouPackageOrder.shrname}
				</td>
				<td>
					${tuangouPackageOrder.lxdh}
				</td>
				<%-- <td>
					${tuangouPackageOrder.shrprovince}
				</td>
				<td>
					${tuangouPackageOrder.shrcity}
				</td>
				<td>
					${tuangouPackageOrder.shrcounty}
				</td>
				<td>
					${tuangouPackageOrder.shraddress}
				</td>
				
				<td>
					${tuangouPackageOrder.kdgs}
				</td>
				<td>
					${tuangouPackageOrder.wldh}
				</td>
				<td>
					${tuangouPackageOrder.fahuotime}
				</td> --%>
				<td>
					${tuangouPackageOrder.zuofeitime}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackageOrder.sftk, 'yes_no', '')}
				</td>
				<td>
					${tuangouPackageOrder.principal1}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackageOrder.qxzt, 'qxzt', '')}
				</td>
				<td>
					<fmt:formatDate value="${tuangouPackageOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:view">
						<a href="#" onclick="openDialogView('查看团购套餐订单管理', '${ctx}/tuangoupackageorder/tuangouPackageOrder/form?id=${tuangouPackageOrder.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:edit">
    					<a href="#" onclick="openDialog('编辑团购套餐订单管理', '${ctx}/tuangoupackageorder/tuangouPackageOrder/form?id=${tuangouPackageOrder.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="tuangoupackageorder:tuangouPackageOrder:del">
						<a href="${ctx}/tuangoupackageorder/tuangouPackageOrder/delete?id=${tuangouPackageOrder.id}" onclick="return confirmx('确认要删除该团购套餐订单管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${tuangouPackageOrder.status != '1'  and tuangouPackageOrder.status !='4' and tuangouPackageOrder.status !='2'}">
	    				<a href="#" onclick="openDialog('作废订单 警告：此操作为作废订单，请谨慎操作！', '${ctx}/tuangoupackageorder/tuangouPackageOrder/zuofeiForm?id=${tuangouPackageOrder.id}','85%', '95%')" class="btn btn-danger btn-xs" ><i class="fa fa-edit"></i> 取消</a>
	    			</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>