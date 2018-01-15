<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>修改剩余次数管理</title>
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
		<h5>修改剩余次数列表 </h5>
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
	<form:form id="searchForm" modelAttribute="dmCardNum" action="${ctx}/dmcardnum/dmCardNum/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="dmcardnum:dmCardNum:add">
				<table:addRow url="${ctx}/dmcardnum/dmCardNum/form" title="添加次数" width="700px" height="400px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcardnum:dmCardNum:edit">
			    <table:editRow url="${ctx}/dmcardnum/dmCardNum/form" title="添加次数" id="contentTable" width="700px" height="400px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcardnum:dmCardNum:del">
				<table:delRow url="${ctx}/dmcardnum/dmCardNum/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcardnum:dmCardNum:import">
				<table:importExcel url="${ctx}/dmcardnum/dmCardNum/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmcardnum:dmCardNum:export">
	       		<table:exportExcel url="${ctx}/dmcardnum/dmCardNum/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column cardidname">套餐卡卡号</th>
				<!-- <th  class="sort-column num">添加次数</th> -->
				<th  class="sort-column numhou">剩余次数</th>
				<th  class="sort-column numadmin">处理人</th>
				<th  class="sort-column numremarks">添加原因</th>
				<th  class="sort-column numtime">添加时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmCardNum">
			<tr>
				<td> <input type="checkbox" id="${dmCardNum.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看添加次数', '${ctx}/dmcardnum/dmCardNum/form?id=${dmCardNum.id}','800px', '400px')">
					${dmCardNum.cardidname}
				</a></td>
				<%-- <td>
					${dmCardNum.num}
				</td> --%>
				<td>
					${dmCardNum.numhou}
				</td>
				<td>
					${dmCardNum.numadminname}
				</td>
				<td>
					${dmCardNum.numremarks}
				</td>
				<td>
					${dmCardNum.numtime}
				</td>
				<td>
					<shiro:hasPermission name="dmcardnum:dmCardNum:view">
						<a href="#" onclick="openDialogView('查看添加次数', '${ctx}/dmcardnum/dmCardNum/form?id=${dmCardNum.id}','800px', '400px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmcardnum:dmCardNum:edit">
    					<a href="#" onclick="openDialog('编辑添加次数', '${ctx}/dmcardnum/dmCardNum/form?id=${dmCardNum.id}','800px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="dmcardnum:dmCardNum:del">
						<a href="${ctx}/dmcardnum/dmCardNum/delete?id=${dmCardNum.id}" onclick="return confirmx('确认要删除该添加次数吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
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