<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分销佣金记录管理</title>
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
		<h5>分销佣金记录列表 </h5>
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
	<form:form id="searchForm" modelAttribute="fenxiaoYongjin" action="${ctx}/fenxiaoyongjin/fenxiaoYongjin/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>提成人姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>提成人手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:add">
				<table:addRow url="${ctx}/fenxiaoyongjin/fenxiaoYongjin/form" title="分销佣金记录" width="50%" height="50%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:edit">
			    <table:editRow url="${ctx}/fenxiaoyongjin/fenxiaoYongjin/form" title="分销佣金记录" id="contentTable" width="540px" height="577px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:del">
				<table:delRow url="${ctx}/fenxiaoyongjin/fenxiaoYongjin/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:import">
				<table:importExcel url="${ctx}/fenxiaoyongjin/fenxiaoYongjin/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:export">
	       		<table:exportExcel url="${ctx}/fenxiaoyongjin/fenxiaoYongjin/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column headimgurl">头像</th>
				<th  class="sort-column nickname">昵称</th>
				<th  class="sort-column name">提成人姓名</th>
				<th  class="sort-column mobile">提成人手机号</th> 
				<th  class="sort-column jine">提成人佣金</th>
	 			<th  class="sort-column layer">佣金层级</th>
				<th  class="sort-column orderid">来源订单</th>
				<th  class="sort-column ordertype">订单类型</th>
				<!-- <th  class="sort-column xfzwxopenid">客户微信Openid</th> -->
				<th  class="sort-column xfzname">客户姓名</th>
				<th  class="sort-column xfjine">消费金额</th>
				<th  class="sort-column ffzt">发放状态</th>
				<th  class="sort-column updateDate">更新时间</th>
				<th  class="sort-column remarks">备注信息</th> 
			</tr>
		</thead>
		<tbody>
		<!-- fenxiaoYongjin就是list中每个元素entity,entity循环赋值到fenxiaoYongjin -->
		<c:forEach items="${page.list}" var="fenxiaoYongjin">
			<tr>
				<td> <input type="checkbox" id="${fenxiaoYongjin.id}" class="i-checks"></td>
				<td>
					<div style="text-align:center">
						<img src="${fenxiaoYongjin.headimgurl}" style="width:60px;heigth:60px;"/>
 					</div>
				</td>
				<td>
					${fenxiaoYongjin.nickname}
				</td>
 				<td>
					${fenxiaoYongjin.name}
				</td>
				<td>
					${fenxiaoYongjin.mobile}
				</td>
				<td>
					${fenxiaoYongjin.jine}
				</td>
				<td>
					${fenxiaoYongjin.layer}
				</td>
				<td>
					<c:if test="${fenxiaoYongjin.ordertype==0}">
						<a  href="#" onclick="openDialogView('查看套餐卡订单管理', '${ctx}/dmcardorder/dmCardOrder/form?id=${fenxiaoYongjin.orderid}','80%', '95%')">${fenxiaoYongjin.orderddh}</a>
					</c:if>
					<c:if test="${fenxiaoYongjin.ordertype==1}">
						<a  href="#" onclick="openDialogView('查看菜品订单', '${ctx}/goodsorder/goodsOrder/form?id=${fenxiaoYongjin.orderid}','80%', '95%')">${fenxiaoYongjin.orderddh}</a>
					</c:if>
					<c:if test="${fenxiaoYongjin.ordertype==2}">
						<a  href="#" onclick="openDialogView('查看团购订单管理', '${ctx}/tuangouorder/tuangouOrder/form?id=${fenxiaoYongjin.orderid}','80%', '95%')">${fenxiaoYongjin.orderddh}</a>
					</c:if>
					<c:if test="${fenxiaoYongjin.ordertype==3}">
						<a  href="#" onclick="openDialogView('查看团购套餐订单管理', '${ctx}/tuangoupackageorder/tuangouPackageOrder/form?id=${fenxiaoYongjin.orderid}','80%', '95%')">${fenxiaoYongjin.orderddh}</a>
					</c:if>
				</td>
				<td>
					${fns:getDictLabel(fenxiaoYongjin.ordertype, 'ordertype', '')}
				</td>
				<%-- <td>
					${fenxiaoYongjin.xfzwxopenid}
				</td> --%>
				<td>
					${fenxiaoYongjin.xfzname}
				</td>
				<td>
					${fenxiaoYongjin.xfjine}
				</td>
				<td>
					<%-- ${fenxiaoYongjin.ffzt} --%>
					${fns:getDictLabel(fenxiaoYongjin.ffzt, 'ffzt', '')}
				</td>
				<td>
					<fmt:formatDate 
					 value="${fenxiaoYongjin.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:view">
						<a href="#" onclick="openDialogView('查看分销佣金记录', '${ctx}/fenxiaoyongjin/fenxiaoYongjin/form?id=${fenxiaoYongjin.id}','60%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:edit">
    					<a href="#" onclick="openDialog('编辑分销佣金记录', '${ctx}/fenxiaoyongjin/fenxiaoYongjin/form?id=${fenxiaoYongjin.id}','60%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="fenxiaoyongjin:fenxiaoYongjin:del">
						<a href="${ctx}/fenxiaoyongjin/fenxiaoYongjin/delete?id=${fenxiaoYongjin.id}" onclick="return confirmx('确认要删除该分销佣金记录吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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