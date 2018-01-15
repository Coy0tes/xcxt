<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购套餐管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
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
				url:'${ctx}/tuangoupackage/tuangouPackage/isonUp',
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
		// ------------------批量下架 BEGIN-------------------------
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
				url:'${ctx}/tuangoupackage/tuangouPackage/isonDown',
				data:{ids:JSON.stringify(arr)},
				type:'post',
				dataType:'json',
				success:function(data){
					top.layer.msg(data.msg,{icon:1});
					setTimeout(function(){window.location.reload();},200);
				}
			});
		}
		// ------------------批量下架 END-------------------------
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>团购套餐管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="tuangouPackage" action="${ctx}/tuangoupackage/tuangouPackage/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>规则名称：</span>
				<form:input path="guizename" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			<span>是否启用：</span>
				<form:select path="ison" class="form-control required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="tuangoupackage:tuangouPackage:add">
				<table:addRow url="${ctx}/tuangoupackage/tuangouPackage/xinzengForm" title="团购套餐管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackage:tuangouPackage:edit">
			    <table:editRow url="${ctx}/tuangoupackage/tuangouPackage/form" title="团购套餐管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackage:tuangouPackage:del">
				<table:delRow url="${ctx}/tuangoupackage/tuangouPackage/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackage:tuangouPackage:import">
				<table:importExcel url="${ctx}/tuangoupackage/tuangouPackage/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangoupackage:tuangouPackage:export">
	       		<table:exportExcel url="${ctx}/tuangoupackage/tuangouPackage/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column guizename">规则名称</th>
				<th  class="sort-column guizecontent">规则描述</th>
				<th  class="sort-column price">价格</th>
				<th  class="sort-column ison">是否上架</th>
				<th  class="sort-column fbsj">发布时间</th>
				<th  class="sort-column beginsj">开始时间</th>
				<th  class="sort-column endsj">结束时间</th>
				<th  class="sort-column istip">是否设置开团提醒</th>
				<!-- <th  class="sort-column tiptype">开团提醒方式</th> -->
				<th  class="sort-column minnum">起团数量</th>
				<th  class="sort-column maxnumperson">每人限购数量</th>
				<th  class="sort-column istimetip">是否设置倒计时</th>
				<!-- <th  class="sort-column initsellnum">初始销售量</th>
				<th  class="sort-column zdtgsl">最大团购数量</th> -->
				<th  class="sort-column createDate">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tuangouPackage">
			<tr>
				<td> <input type="checkbox" id="${tuangouPackage.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看团购套餐管理', '${ctx}/tuangoupackage/tuangouPackage/form?id=${tuangouPackage.id}','85%', '95%')">
					${tuangouPackage.guizename}
				</a></td>
				<td>
					${tuangouPackage.guizecontent}
				</td>
				<td>
					${tuangouPackage.price}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackage.ison, 'yes_no', '')}
				</td>
				<td>
					${tuangouPackage.fbsj}
				</td>
				<td>
					${tuangouPackage.beginsj}
				</td>
				<td>
					${tuangouPackage.endsj}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackage.istip, 'yes_no', '')}
				</td>
				<%-- <td>
					${fns:getDictLabel(tuangouPackage.tiptype, 'tiptype', '')}
				</td> --%>
				<td>
					${tuangouPackage.minnum}
				</td>
				<td>
					${tuangouPackage.maxnumperson}
				</td>
				<td>
					${fns:getDictLabel(tuangouPackage.istimetip, 'yes_no', '')}
				</td>
				<%-- <td>
					${tuangouPackage.initsellnum}
				</td>
				<td>
					${tuangouPackage.zdtgsl}
				</td> --%>
				<td>
					<fmt:formatDate value="${tuangouPackage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="tuangoupackage:tuangouPackage:view">
						<a href="#" onclick="openDialogView('查看团购套餐管理', '${ctx}/tuangoupackage/tuangouPackage/form?id=${tuangouPackage.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="tuangoupackage:tuangouPackage:edit">
    					<a href="#" onclick="openDialog('修改团购套餐管理', '${ctx}/tuangoupackage/tuangouPackage/form?id=${tuangouPackage.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="tuangoupackage:tuangouPackage:del">
						<a href="${ctx}/tuangoupackage/tuangouPackage/delete?id=${tuangouPackage.id}" onclick="return confirmx('确认要删除该团购套餐管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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