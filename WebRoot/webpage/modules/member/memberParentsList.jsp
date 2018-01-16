<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<!-- 查看下级会员 -->
	<title>会员信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		    
		
	</script>
	
	<link rel="stylesheet" href="${ctxStatic}/zoomify/css/zoomify.min.css">
	
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>当前会员：
			<font color="blue">
				<c:if test="${member.name != null}">${member.name}</c:if>
				<c:if test="${member.name == null}">${member.nickname}</c:if>
			</font> &nbsp;&nbsp;&nbsp; 会员手机号：
												<script type="text/javascript">
													var phone = '${member.mobile}';
													var p = phone.substr(0, 3) + '****' + phone.substr(7);
													document.write(p);
												</script>
		</h5>
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
	<form:form id="searchForm" modelAttribute="mm" action="${ctx}/member/member/parentsForm" method="post" class="form-inline">
		<input type="hidden" name="wxopenid"  value="${tjrwxopenid}"/>
		<%-- <input type="text" name="id" value="推荐人id：${id}"/> --%>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>昵称：</span>
				<form:input path="nickname" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left" hidden="hidden">
			<shiro:hasPermission name="member:member:add">
				<table:addRow url="${ctx}/member/member/form" title="会员信息" width="65%" height="80%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:member:edit">
			    <table:editRow url="${ctx}/member/member/form" title="会员信息" id="contentTable" width="65%" height="80%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:member:del">
				<table:delRow url="${ctx}/member/member/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:member:import">
				<table:importExcel url="${ctx}/member/member/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:member:export">
	       		<table:exportExcel url="${ctx}/member/member/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<!-- <th  class="sort-column name">姓名</th> -->
				<th  class="sort-column headimgurl">头像</th>
				<th  class="sort-column nickname">昵称</th>
				<th  class="sort-column mobile">手机号</th>
				<th  class="sort-column isPhone">短信提醒</th>
				<th  class="sort-column isWechat">微信提醒</th>
				<!-- <th  class="sort-column password">密码</th> -->
				
				<!-- <th  class="sort-column wxopenid">微信Openid</th>
				<th  class="sort-column tjrwxopenid">推荐人微信Openid</th>
				<th  class="sort-column yongjin">佣金</th>
				<th  class="sort-column yongjinytx">已提现佣金</th> -->
				
				<th  class="sort-column updateDate">创建时间</th>
				<!-- <th  class="sort-column remarks">备注信息</th> -->
			</tr>
		</thead>
		
		<tbody>
		<c:forEach items="${page.list}" var="member">
			<c:if test="${member.id != pm.id }">
				<tr>
					<td> <input type="checkbox" id="${member.id}" class="i-checks"></td>
					<%-- <td>
						<a  href="#" onclick="openDialogView('查看会员信息', '${ctx}/member/member/form?id=${member.id}','60%', '80%')">${member.name}</a>
					</td> --%>
					<td>
						<div style="text-align:center;">
						   	<img src="${member.headimgurl}" class="zoomify" style="max-height: 50px;max-width: 50px;">
						</div>
					</td>
					<td>
						${member.nickname}
					</td>
					<td>
						${member.mobile}
					</td>
					<td>
						${fns:getDictLabel(member.isPhone, 'yes_no', '')}
					</td>
					<td>
						${fns:getDictLabel(member.isWechat, 'yes_no', '')}
					</td>
					<!-- <td>
						${member.password}
					</td> -->
					
					<%-- <td>
						${member.wxopenid}
					</td>
					<td>
						${member.tjrwxopenid}
					</td>
					<td>
						${member.yongjin}
					</td>
					<td>
						${member.yongjinytx}
					</td> --%>
					
					<td>
						<fmt:formatDate value="${member.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<!-- <td>
						${member.remarks}
					</td> -->
				</tr>
			</c:if>
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
<script src="${ctxStatic}/zoomify/js/zoomify.min.js"></script>
<script>
	$(function() {
		$('.zoomify').zoomify();
	});
 </script>
</body>
</html>