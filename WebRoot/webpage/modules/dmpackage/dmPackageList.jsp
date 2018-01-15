<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function enabledPackage(){
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
			  confirmx("是否批量启用选中套餐？",function(){
				  $.ajax({
			    		url:'${ctx}/dmpackage/dmPackage/enabledPackage',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
			    				top.layer.msg('批量启用成功!',{icon:1});
			    				
			    				$('#contentTable thead tr th input.i-checks').iCheck('check');
			    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
			    				setTimeout(function(){window.location.reload();},200);
			    		}
			    	});
			   });
		}
		function disabledPackage(){
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
			  confirmx("是否批量禁用选中套餐？",function(){
				  $.ajax({
			    		url:'${ctx}/dmpackage/dmPackage/disabledPackage',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
			    			if(data.msg== 1){
			    				top.layer.msg('批量禁用成功!',{icon:1});
			    				
			    				$('#contentTable thead tr th input.i-checks').iCheck('check');
			    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
			    				setTimeout(function(){window.location.reload();},200);
			    			}else{
			    				top.layer.msg('批量禁用失败，有未失效的套餐卡，请查看!',{icon:2});
			    			}
			    		}
			    	});
			   });
		}
	</script>
	<link rel="stylesheet" href="${ctxStatic}/zoomify/css/zoomify.min.css">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>套餐管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="dmPackage" action="${ctx}/dmpackage/dmPackage/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>套餐名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
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
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="dmpackage:dmPackage:add">
				<table:addRow url="${ctx}/dmpackage/dmPackage/form" title="套餐管理 " width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmpackage:dmPackage:edit">
			    <table:editRow url="${ctx}/dmpackage/dmPackage/form" title="套餐管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="dmpackage:dmPackage:del">
					<table:delRow url="${ctx}/dmpackage/dmPackage/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
				</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="dmpackage:dmPackage:import">
				<table:importExcel url="${ctx}/dmpackage/dmPackage/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="dmpackage:dmPackage:export">
	       		<table:exportExcel url="${ctx}/dmpackage/dmPackage/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       
	       <a href="#" onclick="enabledPackage()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-edit"></i> 批量启用</a>
	       <a href="#" onclick="disabledPackage()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-edit"></i> 批量禁用</a>
		
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
				<th  class="sort-column name">套餐名称</th>
				<th  class="sort-column name">频次</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column xszt">销售状态</th>
				<!-- <th  class="sort-column ison">是否上架</th> -->
				<!-- <th  class="sort-column imgurl">套餐图片</th> -->
				<th  class="sort-column pscs">配送次数</th>
				<th  class="sort-column xcsl">选菜数量(种)</th>
				<th  class="sort-column price">原价(元)</th>
				<th  class="sort-column xsprice">销售价格(元)</th>
				<th  class="sort-column fxjebl">分销提成类型</th>
				<th  class="sort-column fxjebl">分销提成比例(%)</th>
				<th  class="sort-column fxtcje">分销提成金额(元)</th>
				<!-- <th  class="sort-column contents">套餐描述</th> -->
				<th  class="sort-column updateDate">创建时间</th>
				<!-- <th  class="sort-column remarks">备注信息</th> -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmPackage">
			<tr>
				<td> <input type="checkbox" id="${dmPackage.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看套餐管理', '${ctx}/dmpackage/dmPackage/form?id=${dmPackage.id}','85%', '95%')">
					${dmPackage.name}
				</a></td>
				<td>
					${fns:getDictLabel(dmPackage.danshuang, 'danshuang', '')}
				</td>
				<td>
					${fns:getDictLabel(dmPackage.status, 'status', '')}
				</td>
				<td>
					${fns:getDictLabel(dmPackage.xszt, 'xszt', '')}
				</td>
				<%-- <td>
					${fns:getDictLabel(dmPackage.ison, 'yes_no', '')}
				</td> --%>
				<!-- <td>
					${dmPackage.imgurl}
				</td> -->
				<td>
					${dmPackage.pscs}
				</td>
				<td>
					${dmPackage.xcsl}
				</td>
				<td>
					${dmPackage.price}
				</td>
				<td>
					${dmPackage.xsprice}
				</td>
				<td>
					${fns:getDictLabel(dmPackage.fxtclx, 'fxtclx', '')}
				</td>
				<td>
					${dmPackage.fxtcbl}
				</td>
				<td>
					${dmPackage.fxtcje}
				</td>
				<!-- <td>
					${dmPackage.contents}
				</td> -->
				<td>
					<fmt:formatDate value="${dmPackage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%-- <td>
					${dmPackage.remarks}
				</td> --%>
				<td>
					<shiro:hasPermission name="dmpackage:dmPackage:view">
						<a href="#" onclick="openDialogView('查看套餐管理', '${ctx}/dmpackage/dmPackage/form?id=${dmPackage.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmpackage:dmPackage:edit">
    					<a href="#" onclick="openDialog('编辑套餐管理', '${ctx}/dmpackage/dmPackage/form?id=${dmPackage.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="dmpackage:dmPackage:del">
						<a href="${ctx}/dmpackage/dmPackage/delete?id=${dmPackage.id}" onclick="return confirmx('确认要删除该套餐管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmpackage:dmPackage:view">
						<a href="#" onclick="openDialogView('查看套餐卡', '${ctx}/dmpackage/dmPackage/dmPackageCard?id=${dmPackage.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看套餐卡</a>
					</shiro:hasPermission>
					<%-- <a href="${ctx}/dmpackage/dmPackage/activation?id=${dmPackage.id}" onclick="return confirmx('确认 ${fns:getDictLabel(dmPackage.status, 'statusFan', '')} 套餐？', this.href)"   class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 启用/禁用</a> --%>
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
<!-- <script>
	$(function() {
		$('.zoomify').zoomify();
	});
	</script> -->
</body>
</html>