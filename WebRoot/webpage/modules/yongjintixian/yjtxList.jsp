<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>佣金提现管理管理</title>
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
		<h5>佣金提现管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="yjtx" action="${ctx}/yongjintixian/yjtx/" method="post" class="form-inline">
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
			<shiro:hasPermission name="yongjintixian:yjtx:add">
				<table:addRow url="${ctx}/yongjintixian/yjtx/form" title="佣金提现管理" height="90%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="yongjintixian:yjtx:edit">
			    <table:editRow url="${ctx}/yongjintixian/yjtx/form" title="佣金提现管理" id="contentTable" height="90%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="yongjintixian:yjtx:del">
				<table:delRow url="${ctx}/yongjintixian/yjtx/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="yongjintixian:yjtx:import">
				<table:importExcel url="${ctx}/yongjintixian/yjtx/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="yongjintixian:yjtx:export">
	       		<table:exportExcel url="${ctx}/yongjintixian/yjtx/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column wxopenid">微信OPENID</th>
				<th  class="sort-column jine">提现金额</th>
				<th  class="sort-column status">领取状态</th>
				<th  class="sort-column sdate">申请时间</th>
				<th  class="sort-column clzt">处理状态</th>
				<th  class="sort-column clremark">处理备注</th>
				<th  class="sort-column cldate">处理日期</th>
				<th  class="sort-column bank">开户银行</th>
				<th  class="sort-column cardid">银行卡号</th>
				<th  class="sort-column username">银行户名</th>
				<th  class="sort-column mobile">手机号</th>
				<th  class="sort-column createDate">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="yjtx">
			<tr>
				<td> <input type="checkbox" id="${yjtx.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看佣金提现管理', '${ctx}/yongjintixian/yjtx/form?id=${yjtx.id}','60%', '90%')">
					${yjtx.wxopenid}
				</a></td>
				<td>
					${yjtx.jine}
				</td>
				<td>
					${fns:getDictLabel(yjtx.status, 'lqzt', '')}
				</td>
				<td>
					<fmt:formatDate value="${yjtx.sdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(yjtx.clzt, 'htclzt', '')}
				</td>
				<td>
					${yjtx.clremark}
				</td>
				<td>
					<fmt:formatDate value="${yjtx.cldate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${yjtx.bank}
				</td>
				<td>
					${yjtx.cardid}
				</td>
				<td>
					${yjtx.username}
				</td>
				<td>
					${yjtx.mobile}
				</td>
				<td>
					<fmt:formatDate value="${yjtx.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="yongjintixian:yjtx:view">
						<a href="#" onclick="openDialogView('查看佣金提现管理', '${ctx}/yongjintixian/yjtx/form?id=${yjtx.id}','60%', '90%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="yongjintixian:yjtx:edit">
    					<a href="#" onclick="openDialog('编辑佣金提现管理', '${ctx}/yongjintixian/yjtx/form?id=${yjtx.id}','60%', '90%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission> --%>
    				<shiro:hasPermission name="yongjintixian:yjtx:del">
						<a href="${ctx}/yongjintixian/yjtx/delete?id=${yjtx.id}" onclick="return confirmx('确认要删除该佣金提现管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${yjtx.clzt == 0}">
						<a href="${ctx}/yongjintixian/yjtx/dakuanSave?id=${yjtx.id}" onclick="return confirmx('确认给 ${yjtx.wxname} 打款吗？', this.href)"   class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 是否打款</a>
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