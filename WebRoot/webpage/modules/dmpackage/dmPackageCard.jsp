<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>查看套餐卡</title>
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
		<h5>查看套餐卡 </h5>
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
	<sys:message content="${message}" />
	
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="dmPackage" action="${ctx}/dmpackage/dmPackage/dmPackageCard?id=${dmPackage.id}" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<%-- <span>套餐名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/> --%>
			<%-- <span>套餐状态：</span>
				<form:select path="packageid" class="form-control required">
					<form:option value="" label=""/>
					<form:options items="${dmPackage}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select> --%>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<!-- <th> <input type="checkbox" class="i-checks"></th> -->
				<!-- <th  class="sort-column name">套餐名称</th> -->
				<th  class="sort-column cardid">套餐卡号</th>
				<th  class="sort-column packageid">所属套餐</th>
				<th  class="sort-column member.id">所属会员</th>
				<th  class="sort-column numuse">使用次数</th>
				<th  class="sort-column numshengyu">剩余次数</th>
				<th  class="sort-column status">状态</th>
				
				<!-- <th  class="sort-column contents">套餐卡描述</th> -->
				<th  class="sort-column updateDate">制卡时间</th>
				<!-- <th  class="sort-column remarks">备注信息</th> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmCard">
			<tr>
				<%-- <td> <input type="checkbox" id="${dmCard.id}" class="i-checks"></td> --%>
				<!-- <td>
					${dmCard.name}
				</td> -->
				<td><a  href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','47%', '90%')">
					${dmCard.cardid}
				</a></td>
				<td>
					${dmCard.packageName}
				</td>
				<td>
					${dmCard.memberName}
				</td>
				<td>
					${dmCard.numuse}
				</td>
				<td>
					${dmCard.numshengyu}
				</td>
				<td>
					${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
				</td>
				<!-- <td>
					${dmCard.contents}
				</td> -->
				<td>
					<fmt:formatDate value="${dmCard.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%-- <td>
					${dmCard.remarks}
				</td> --%>
				<%-- <td>
					<shiro:hasPermission name="dmcard:dmCard:view">
						<a href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','47%', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmcard:dmCard:edit">
    					<a href="#" onclick="openDialog('编辑套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','47%', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="dmcard:dmCard:del">
						<a href="${ctx}/dmcard/dmCard/delete?id=${dmCard.id}" onclick="return confirmx('确认要删除该套餐卡管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td> --%>
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