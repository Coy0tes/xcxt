<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>

<head>
	<title>套餐卡管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>套餐卡管理列表 </h5>
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
	<div class="row" hidden="hidden">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>套餐卡号：</span>
				<form:input path="cardid" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>套餐名称：</span>
				<form:select path="packageid" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${dmPackageName}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('taoCanKaZT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row" hidden="hidden">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="dmcard:dmCard:add">
				<table:addRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" width="47%" height="760px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="dmcard:dmCard:edit">
			    	<table:editRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" id="contentTable" width="47%" height="760px"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="dmcard:dmCard:del">
				<table:delRow url="${ctx}/dmcard/dmCard/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcard:dmCard:import">
				<table:importExcel url="${ctx}/dmcard/dmCard/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcard:dmCard:export">
	       		<table:exportExcel url="${ctx}/dmcard/dmCard/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       
			<button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="openDialog('批量生成套餐卡', '${ctx}/dmcard/dmCard/zdForm?id=${dmCard.id}','500px', '230px')" title="批量生成套餐卡"><i class="fa fa-plus"></i> 批量生成套餐卡</button>
			<button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="openDialog('批量编辑佣金提成', '${ctx}/dmcard/dmCard/zdForm?id=${dmCard.id}','500px', '230px')" title="批量编辑佣金提成"><i class="fa fa-plus"></i> 批量编辑佣金提成</button>
		
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
				<!-- <th> <input type="checkbox" class="i-checks"></th> -->
				<th  class="sort-column cardid">套餐卡号</th>
				<th  class="sort-column packageid">所属套餐</th>
				<th  class="sort-column member.id">所属会员</th>
				<th  class="sort-column status">状态</th>
				
				<th  class="sort-column fxjebl">分销提成类型</th>
				<th  class="sort-column fxjebl">分销提成比例(%)</th>
				<th  class="sort-column fxtcje">分销提成金额(元)</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${lists}" var="dmCard">
			<tr>
				<td><a  href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','47%', '760px')">
					${dmCard.cardid}
				</a></td>
				<td>
					${dmCard.packageName}
				</td>
				<td>
					${dmCard.memberName}
				</td>
				<td>
					${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
				</td>
				
				<td>
					<%-- ${fns:getDictLabel(dmCard.fxtclx, 'fxtclx', '')} --%>
					<form:select path="dmCard.fxtclx" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fxtclx')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</td>
				<td>
					<%-- ${dmCard.fxtcbl} --%>
					<form:input path="dmCard.fxtcbl" htmlEscape="false"    class="form-control "/>
				</td>
				<td>
					<%-- ${dmCard.fxtcje} --%>
					<form:input path="dmCard.fxtcje" htmlEscape="false"    class="form-control "/>
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