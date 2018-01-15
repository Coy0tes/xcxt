<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>选菜管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
	});
	var ctx = '${ctx}';
	// ------------------批量上架 BEGIN-------------------------
	function isonUp(){
		var size = $("#contentTable tbody tr td input.i-checks:checked").size();
		if(size == 0 ){
			 top.layer.alert('请至少选择一条记录!', {icon: 0, title:'警告'});
			 return false;
		}
		
		var checkedDom =  $("#contentTable tbody tr td input.i-checks:checkbox:checked");
		var arr = []; //存放所有的选中记录，每条记录中包含了id,name,guige
		$.each(checkedDom,function(i,v){
			var id0 = $(this).attr("id");
			var each = {}; //存放每条记录,以键值对的形式
			each.id = id0; //key-value
			arr.push(each); //存放的所有记录
		});
		$.ajax({
			url:'${ctx}/xuancai/xuancai/isonUp',
			data:{ids:JSON.stringify(arr)},
			type:'post',
			dataType:'json',
			success:function(data){
				top.layer.msg(data.msg,{icon:1});
				setTimeout(function(){window.location.reload();},200);
				
			}
		});
	}
	// ------------------批量上架 END-------------------------
	// ------------------批量上架 BEGIN-------------------------
	function isonDown(){
		var size = $("#contentTable tbody tr td input.i-checks:checked").size();
		if(size == 0 ){
			 top.layer.alert('请至少选择一条记录!', {icon: 0, title:'警告'});
			 return false;
		}
		
		var checkedDom =  $("#contentTable tbody tr td input.i-checks:checkbox:checked");
		var arr = []; //存放所有的选中记录，每条记录中包含了id,name,guige
		$.each(checkedDom,function(i,v){
			var id0 = $(this).attr("id");
			var each = {}; //存放每条记录,以键值对的形式
			each.id = id0; //key-value
			arr.push(each); //存放的所有记录
		});
		$.ajax({
			url:'${ctx}/xuancai/xuancai/isonDown',
			data:{ids:JSON.stringify(arr)},
			type:'post',
			dataType:'json',
			success:function(data){
				top.layer.msg(data.msg,{icon:1});
				setTimeout(function(){window.location.reload();},200);
			}
		});
	}
	// ------------------批量上架 END-------------------------
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>选菜管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="xuancai" action="${ctx}/xuancai/xuancai/" method="post" class="form-inline">
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
			<shiro:hasPermission name="xuancai:xuancai:add">
				<table:addRow url="${ctx}/xuancai/xuancai/form" title="选菜管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="xuancai:xuancai:edit">
			    <table:editRow url="${ctx}/xuancai/xuancai/form" title="选菜管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="xuancai:xuancai:del">
				<table:delRow url="${ctx}/xuancai/xuancai/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="xuancai:xuancai:import">
				<table:importExcel url="${ctx}/xuancai/xuancai/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="xuancai:xuancai:export">
	       		<table:exportExcel url="${ctx}/xuancai/xuancai/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="isonUp()" title="菜品批量上架"> 批量上架</button>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="isonDown()" title="菜品批量下架"> 批量下架</button>
	       
		
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
				
				<th  class="sort-column title">菜单名称</th>
				<th  class="sort-column packageid">所属套餐</th>
				<th  class="sort-column ison">是否上架</th>
				<th  class="sort-column tuijian">推荐显示</th>
				<th  class="sort-column begindate">选菜开始时间</th>
				<th  class="sort-column enddate">选菜截止时间</th>
				<th  class="sort-column updateDate">生成时间</th>
				<!-- <th  class="sort-column remarks">备注信息</th> -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="xuancai">
			<tr>
				<td> <input type="checkbox" id="${xuancai.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看选菜管理', '${ctx}/xuancai/xuancai/form?id=${xuancai.id}','85%', '95%')">
					${xuancai.title}
				</a></td>
				<td>
					${xuancai.packageName}
				</td>
				<td>
					${fns:getDictLabel(xuancai.ison, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(xuancai.tuijian, 'yes_no', '')}
				</td>
				<td>
					${xuancai.begindate }
				</td>
				<td>
					${xuancai.enddate }
				</td>
				<td>
					<fmt:formatDate value="${xuancai.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%-- <td>
					${xuancai.remarks}
				</td> --%>
				<td>
					<shiro:hasPermission name="xuancai:xuancai:view">
						<a href="#" onclick="openDialogView('查看选菜管理', '${ctx}/xuancai/xuancai/form?id=${xuancai.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="xuancai:xuancai:edit">
    					<a href="#" onclick="openDialog('编辑选菜管理', '${ctx}/xuancai/xuancai/form?id=${xuancai.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="xuancai:xuancai:del">
						<a href="${ctx}/xuancai/xuancai/delete?id=${xuancai.id}" onclick="return confirmx('确认要删除该选菜管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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