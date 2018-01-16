<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		var ctx = '${ctx}';
		function piliangFahuoList(){
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
			top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
			return false;
			}
			var checkedidArray = [];
			var checkedDom =  $("#contentTable tbody tr td input.i-checks:checkbox:checked");
			$.each(checkedDom,function(i,v){
			 checkedidArray.push($(this).attr("id"));
			});
			confirmx("是否批量发货？",function(){
			 openDialog('批量发货', '${ctx}/goodsorder/goodsOrder/piliangFahuoList?ids='+checkedidArray,'85%', '95%');
			 });
		}
		
		$(document).ready(function() {
		    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('check');
		    	});

		    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	  $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
		    	});
		    
		});
		
		function err(){
			top.layer.msg("演示界面，此功能不允许操作！", {icon:2});
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>菜品订单列表 </h5>
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
	<form:form id="searchForm" modelAttribute="goodsOrder" action="${ctx}/goodsorder/goodsOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			 <div>
			 	<span>订单编号：</span>
					<form:input path="ddh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>套餐卡号：</span>
					<form:input path="cardid" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>订单状态：</span>
					<form:select path="status"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('orderStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>购买会员：</span>
					<form:input path="memberName" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				
			 </div>
			<div style="margin-top:10px;">
				<span>收货人：</span>
					<form:input path="shrname" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>收货人电话：</span>
					<form:input path="lxdh" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>物流单号：</span>
					<form:input path="wldh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			</div>
			<div style="margin-top:10px;">
			<span>注册手机号：</span>
					<form:input path="membermobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
			<span>下单时间起：</span>
				<form:input path="begintime" htmlEscape="false" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  onfocus="this.blur()"  class="form-control" />
				&nbsp;&nbsp;&nbsp;&nbsp;
			<span>下单时间止：</span>
				<form:input path="endtime" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  onfocus="this.blur()"  class="form-control" />
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
			<shiro:hasPermission name="goodsorder:goodsOrder:add">
				<table:addRow url="${ctx}/goodsorder/goodsOrder/form" title="菜品订单" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goodsorder:goodsOrder:edit">
				<table:editRow url="${ctx}/goodsorder/goodsOrder/form" title="菜品订单" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
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
			<a href="#" onclick="piliangFahuoList()" class="btn btn-primary btn-outline btn-sm " ><i class="fa fa-edit"></i> 批量发货</a>
		
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
				<th  class="sort-column cardid">套餐卡号	</th>
				<th  class="sort-column memberid">购买会员</th>
				<th  class="sort-column membermobile">注册手机号</th>
				<th  class="sort-column num">选菜数量</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shrprovince">收货地址</th>
				<th  class="sort-column shrname">收货人</th>
				<th  class="sort-column lxdh">收货人电话</th>
				<th  class="sort-column createDate">下单时间</th>
				<th  class="sort-column kdgs">快递公司</th>
				<th  class="sort-column wldh">物流单号</th>
				<th  class="sort-column fahuotime">发货时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="goodsOrder">
			<tr>
				<td> <input type="checkbox" id="${goodsOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看菜品订单', '${ctx}/goodsorder/goodsOrder/form?id=${goodsOrder.id}','85%', '95%')">
					${goodsOrder.ddh}
				</a></td>
				<td>
					${goodsOrder.cardName}
				</td>
				<td>
					${goodsOrder.memberName}
				</td>
				<td>
					${goodsOrder.membermobile}
				</td>
				<td>
					${goodsOrder.num}
				</td>
				<td>
					${fns:getDictLabel(goodsOrder.status, 'orderStatus', '')}
				</td>
				<td>
					${goodsOrder.shrprovince}
					${goodsOrder.shrcity}
					${goodsOrder.shrcounty}
					${fns:abbr(goodsOrder.shraddress,15)}
				</td>
				<td>
					${goodsOrder.shrname}
				</td>
				<td>
					${goodsOrder.lxdh}
				</td>
				<td>
					<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${goodsOrder.createDate}"/>
				</td>
				<td>
					${goodsOrder.kdgs}
				</td>
				<td>
					${goodsOrder.wldh}
				</td>
				<td>
					${goodsOrder.fahuotime}
				</td>
				<td>
					<shiro:hasPermission name="goodsorder:goodsOrder:view">
						<a href="#" onclick="openDialogView('查看菜品订单', '${ctx}/goodsorder/goodsOrder/form?id=${goodsOrder.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
    				<c:if test="${goodsOrder.status =='0' }">
	    				<a href="#" onclick="openDialog('发货', '${ctx}/goodsorder/goodsOrder/form?id=${goodsOrder.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 发货</a>
					</c:if>
					<c:if test="${goodsOrder.status =='1' }">
						<a href="#" onclick="err()" class="btn btn-primary btn-xs" ><i class="fa fa-search-plus"></i> 物流信息</a>
						<%-- <a href="#" onclick="openDialogView('查看物流信息', '${ctx}/sf/showList?wldh=${goodsOrder.wldh}','60%', '95%')" class="btn btn-primary btn-xs" ><i class="fa fa-search-plus"></i> 物流信息</a> --%>
					</c:if>
    				<shiro:hasPermission name="goodsorder:goodsOrder:del">
						<a href="${ctx}/goodsorder/goodsOrder/delete?id=${goodsOrder.id}" onclick="return confirmx('确认要删除该菜品订单吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${goodsOrder.status != '1'  and goodsOrder.status !='4' and goodsOrder.status !='2'}">
	    				<a href="#" onclick="openDialog('作废订单 警告：此操作为作废订单，请谨慎操作！', '${ctx}/goodsorder/goodsOrder/formZuoFei?id=${goodsOrder.id}','30%', '30%')" class="btn btn-danger btn-xs" ><i class="fa fa-edit"></i> 取消</a>
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
<script>
	var str = "警告：此操作为作废订单，请谨慎操作！";
</script>
</body>
</html>