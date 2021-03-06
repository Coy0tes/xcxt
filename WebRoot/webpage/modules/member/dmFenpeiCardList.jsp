<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐卡管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function doSubmit(){
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
				 top.layer.msg('请至少选择一条记录!', {icon: 0, title:'警告'});
				 return false;
			}
			var checkedDom =  $("#contentTable tbody tr td input.i-checks:checkbox:checked");
			var arr = []; //存放所有的选中记录，每条记录中包含了id,name,guige
			$.each(checkedDom,function(i,v){
				var id0 = $(this).attr("id");
				arr.push(id0); //存放的所有记录
				
			});
			var memberid = $("#h").html();
			$('#id').val(memberid);
			$('#ids').val(arr.join());
			$('#inputForm').submit();
			
			return true;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>套餐卡管理列表 </h5>
		<!-- 隐藏域id，存放会员的 id -->
		<div id = "h" name="h" hidden="hidden" >${memberid}</div>	
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
	
	
	<form:form id="inputForm" action="${ctx}/member/member/fenpeiSave" method="post" class="form-inline">
		<input type="hidden" id="ids" name="ids"/>
		<input type="hidden" id="id" name="id"/>
	</form:form>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="dmCard" action="${ctx}/member/member/fenpeiCardList" method="post" class="form-inline">
		<input type="hidden" name="memberid" value="${memberid}"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>套餐：</span>
            <form:select path="packageid" class="form-control">
				<form:option value="" label=""/>
				<form:options items="${dmPackageName}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			</form:select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span>频次：</span>
            <form:select path="packagedanshuang" class="form-control">
				<form:option value="" label=""/>
				<form:options items="${fns:getDictList('danshuang')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span>卡号：</span>
			<form:input path="cardid" class="form-control"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left" hidden="hidden">
			<shiro:hasPermission name="dmcard:dmCard:add">
				<table:addRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" width="47%" height="600px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="dmcard:dmCard:edit">
			    <table:editRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" id="contentTable" width="47%" height="600px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission> --%>
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
				<!-- <th  class="sort-column name">套餐名称</th> -->
				<th  class="sort-column cardid">套餐卡号</th>
				<th  class="sort-column packageid">所属套餐</th>
				<!-- <th  class="sort-column member.id">所属会员</th> -->
				<th  class="sort-column status">状态</th>
				<th  class="sort-column status">套餐配送次数</th>
				<!-- <th  class="sort-column status">使用次数</th>
				<th  class="sort-column status">剩余次数</th>
				<th  class="sort-column status">编辑备注</th>
				<th  class="sort-column status">编辑人</th>
				<th  class="sort-column status">编辑时间</th> -->
				<!-- <th  class="sort-column contents">套餐卡描述</th> -->
				<!-- <th  class="sort-column updateDate">更新时间</th>
				<th  class="sort-column remarks">备注信息</th> -->
				<!-- <th>操作</th> -->
			</tr>
		</thead>
		<tbody id="sub">
		<c:forEach items="${dmCardlist}" var="dmCard">
			<tr>
				<td> <input type="checkbox" id="${dmCard.id}" class="i-checks"></td>
				<!-- <td>
					${dmCard.name}
				</td> -->
				<td><a  href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','47%', '60%')">
					${dmCard.cardid}
				</a></td>
				<td>
					${dmCard.packageName} | ${fns:getDictLabel(dmCard.packagedanshuang, 'danshuang', '')}
				</td>
				<%-- <td>
					${dmCard.memberName}
				</td> --%>
				<td>
					${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
				</td>
				<td>
					${dmCard.numpscs}
				</td>
				<%-- <td>
					${dmCard.numuse}
				</td>
				<td>
					${dmCard.numshengyu}
				</td>
				<td>
					${dmCard.numremarks}
				</td>
				<td>
					${dmCard.numadminname}
				</td>
				<td>
					${dmCard.numtime}
				</td> --%>
				<!-- <td>
					${dmCard.contents}
				</td> -->
				<%-- <td>
					<fmt:formatDate value="${dmCard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
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
					<shiro:hasPermission name="dmcard:dmCard:edit">
    					<a href="#" onclick="openDialog('编辑套餐卡剩余次数', '${ctx}/dmcard/dmCard/numForm?id=${dmCard.id}','688px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑剩余次数</a>
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