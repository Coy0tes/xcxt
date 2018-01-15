<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐卡订单管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
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
			 openDialog('批量发货', '${ctx}/dmcardorder/dmCardOrder/piliangFahuoList?ids='+checkedidArray,'85%', '95%');
			 });
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>套餐卡订单管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="dmCardOrder" action="${ctx}/dmcardorder/dmCardOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<div>
			<span>订单编号：</span>
				<form:input path="ddh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>所属套餐：</span>
				<form:select path="packageid" class="form-control required">
					<form:option value="" label=""/>
					<form:options items="${dmPackageName}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>订单状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('orderStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>购卡会员：</span>
				<form:input path="memberName" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>注册手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
		<div hidden="hidden">
			<shiro:hasPermission name="dmcardorder:dmCardOrder:add">
				<table:addRow url="${ctx}/dmcardorder/dmCardOrder/form" title="套餐卡订单管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
		</div>
		<div hidden="hidden">
			<shiro:hasPermission name="dmcardorder:dmCardOrder:edit">
			    <table:editRow url="${ctx}/dmcardorder/dmCardOrder/form" title="套餐卡订单管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
		</div>
			<shiro:hasPermission name="dmcardorder:dmCardOrder:del">
				<table:delRow url="${ctx}/dmcardorder/dmCardOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
		<div hidden="hidden">
			<shiro:hasPermission name="dmcardorder:dmCardOrder:import">
				<table:importExcel url="${ctx}/dmcardorder/dmCardOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
		</div>
			 <shiro:hasPermission name="dmcardorder:dmCardOrder:export">
	       		<table:exportExcel url="${ctx}/dmcardorder/dmCardOrder/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column num">购卡数量</th>
				<th  class="sort-column gclx">购卡类型</th>
				<th  class="sort-column packageid">所属套餐</th>
				<th class="sort-column member.id">购卡会员</th>
				<th class="sort-column moblie">注册手机号</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shraddress">收货地址</th>
				<th  class="sort-column shrname">收货人</th>
				<th  class="sort-column lxdh">收货人电话</th>
				<th  class="sort-column kdgs">快递公司</th>
				<th  class="sort-column wldh">物流单号</th>
				<th  class="sort-column fahuotime">发货时间</th>
				<th  class="sort-column createDate">下单时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmCardOrder">
			<tr>
				<td> <input type="checkbox" id="${dmCardOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看套餐卡订单管理', '${ctx}/dmcardorder/dmCardOrder/form?id=${dmCardOrder.id}','85%', '95%')">
					${dmCardOrder.ddh}
				</a></td>
				<td>
					${dmCardOrder.num}
				</td>
				<td>
					${fns:getDictLabel(dmCardOrder.gclx,'cardType','')}
				</td>
				<td>
					${dmCardOrder.packageName }
				</td>
				<td>
					${dmCardOrder.memberName}
				</td>
				<td>
					${dmCardOrder.mobile}
				</td>
				<td>
					${fns:getDictLabel(dmCardOrder.status,'orderStatus','')}
				</td>
				<td>
					${dmCardOrder.shrprovince}
					${dmCardOrder.shrcity}
					${dmCardOrder.shrcounty}
					${dmCardOrder.shraddress}
				</td>
				<td>
					${dmCardOrder.shrname}
				</td>
				<td>
					${dmCardOrder.lxdh}
				</td>
				<td>
					${dmCardOrder.kdgs}
				</td>
				<td>
					${dmCardOrder.wldh}
				</td>
				<td>
					${dmCardOrder.fahuotime}
				</td>
				<td>
					<fmt:formatDate value="${dmCardOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="dmcardorder:dmCardOrder:view">
						<a href="#" onclick="openDialogView('查看套餐卡订单管理', '${ctx}/dmcardorder/dmCardOrder/form?id=${dmCardOrder.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="dmcardorder:dmCardOrder:edit">
    					<a href="#" onclick="openDialog('编辑套餐卡订单管理', '${ctx}/dmcardorder/dmCardOrder/form?id=${dmCardOrder.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission> --%>
    				<shiro:hasPermission name="dmcardorder:dmCardOrder:del">
						<a href="${ctx}/dmcardorder/dmCardOrder/delete?id=${dmCardOrder.id}" onclick="return confirmx('确认要删除该套餐卡订单管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${dmCardOrder.status =='1' }">
						<a href="#" onclick="openDialogView('查看物流信息', '${ctx}/sf/showList?wldh=${dmCardOrder.wldh}','50%', '80%')" class="btn btn-primary btn-xs" ><i class="fa fa-search-plus"></i> 物流信息</a>
					</c:if>
					<c:if test="${dmCardOrder.status == '0'}">
						<c:if test="${dmCardOrder.gclx == '1' }">
						<shiro:hasPermission name="dmcardorder:dmCardOrder:edit">
	    					<a href="#" onclick="openDialog('发货', '${ctx}/dmcardorder/dmCardOrder/fahuoForm?id=${dmCardOrder.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 发货</a>
	    				</shiro:hasPermission>
						</c:if>
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