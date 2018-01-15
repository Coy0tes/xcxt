<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		$(function() {
			$("#mainpic").find("img").addClass("zoomify");
			$('.zoomify').zoomify();
		});
		
		//  批量编辑分销佣金信息
		function fxtcForm(){
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
			  confirmx("是否批量编辑套餐卡分佣提成信息？",function(){
				  openDialog('批量编辑套餐分佣信息', '${ctx}/goods/goods/fxtcForm?ids='+checkedidArray,'60%', '60%');
			   });
		}
		
		//批量上架
		function plUp(){
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
			  confirmx("是否批量上架？",function(){
			    	$.ajax({
			    		url:'${ctx}/goods/goods/plUp',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
		    				top.layer.msg('批量上架成功!',{icon:1});
		    				
		    				$('#contentTable thead tr th input.i-checks').iCheck('check');
		    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
		    				setTimeout(function(){window.location.reload();},200);
			    		}
			    	});
			   });
		}
		
		//批量下架
		function plDown(){
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
			  
			  confirmx("是否批量下架？",function(){
			    	$.ajax({
			    		url:'${ctx}/goods/goods/plDown',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
			    				top.layer.msg('批量下架成功!',{icon:1});
			    				
			    				$('#contentTable thead tr th input.i-checks').iCheck('check');
			    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
			    				setTimeout(function(){window.location.reload();},200);
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
		<h5>菜品管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="goods" action="${ctx}/goods/goods/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>菜品名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span>是否上架：</span>
				<form:select path="ison" class="form-control">
					<form:option value=""></form:option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"/>
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
			<shiro:hasPermission name="goods:goods:add">
				<table:addRow url="${ctx}/goods/goods/form" title="菜品管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="goods:goods:edit">
				    <table:editRow url="${ctx}/goods/goods/form" title="菜品管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="goods:goods:del">
				<table:delRow url="${ctx}/goods/goods/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:goods:import">
				<table:importExcel url="${ctx}/goods/goods/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
	       		<table:exportExcel url="${ctx}/goods/goods/export"></table:exportExcel><!-- 导出按钮 -->
			<shiro:hasPermission name="goods:goods:export">
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			<a href="#" onclick="plUp()"   class="btn btn-primary btn-outline btn-sm"><i class="fa fa-edit"></i> 批量上架</a>
			<a href="#" onclick="plDown()" class="btn btn-primary btn-outline btn-sm"><i class="fa fa-edit"></i> 批量下架</a>
	       	<button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="fxtcForm()" title="批量编辑佣金提成"><i class="fa fa-edit"></i> 批量编辑佣金提成信息</button>
		
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
				<th  class="sort-column name">菜品名称</th>
				<th  class="sort-column guige">菜品规格</th>
				<th  class="sort-column category">菜品分类</th>
				<th  class="sort-column category">是否上架</th>
				<th  class="sort-column price">原价</th>
				<th  class="sort-column xsprice">销售价</th>
				<th  class="sort-column kcsl">库存</th>
				<th  class="sort-column djkc">冻结库存</th>
				<th  class="sort-column fxtclx">分销提成类型</th>
				<th  class="sort-column fxjebl">分销提成比例(%)</th>
				<th  class="sort-column fxtcje">分销提成金额(元)</th>
				<th  class="sort-column checkreport">检验报告</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="goods">
			<tr>
				<td> <input type="checkbox" name="i-checks" id="${goods.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看菜品管理', '${ctx}/goods/goods/form?id=${goods.id}','85%', '95%')">
					${goods.name}
				</a></td>
				<td>
					${goods.guige}
				</td>
				<td>
					${goods.category}
				</td>
				<td>
					${fns:getDictLabel(goods.ison, 'yes_no', '')}
				</td>
				<td>
					${goods.price}
				</td>
				<td>
					${goods.xsprice}
				</td>
				<td>
					${goods.kcsl}
				</td>
				<td>
					${goods.djkc}
				</td>
				<td>
					${fns:getDictLabel(goods.fxtclx, 'fxtclx', '')}
				</td>
				<td>
					${goods.fxtcbl}
				</td>
				<td>
					${goods.fxtcje}
				</td>	
				<td>
					<%-- <div style="text-align:center;">
					   	<img src="${goods.checkreport}" class="zoomify" style="max-height: 100px;max-width: 100px;">
					</div> --%>
					<c:if test="${goods.ischeckreport != 1 }">未上传</c:if>
					<c:if test="${goods.ischeckreport == 1}">已上传</c:if>
					
				</td>
				<td>
					<shiro:hasPermission name="goods:goods:view">
						<a href="#" onclick="openDialogView('查看菜品管理', '${ctx}/goods/goods/form?id=${goods.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="goods:goods:edit">
    					<a href="#" onclick="openDialog('编辑菜品管理', '${ctx}/goods/goods/form?id=${goods.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="goods:goods:del">
						<a href="${ctx}/goods/goods/delete?id=${goods.id}" onclick="return confirmx('确认要删除该菜品管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
<script src="${ctxStatic}/zoomify/js/zoomify.min.js"></script>
<script>
	$(function() {
		$('.zoomify').zoomify();
	});
 </script>
</body>
</html>