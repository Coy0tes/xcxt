<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var ctx = '${ctx}';
		$(document).ready(function() {
		});
		$(function() {
			$("#mainpic").find("img").addClass("zoomify");
			$('.zoomify').zoomify();
		});
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
				 top.layer.alert('请至少选择一条记录!', {icon: 0, title:'警告'});
				 return false;
			}
			
			var checkedDom =  $("#contentTable tbody tr td input.i-checks:checkbox:checked");
			var arr = []; //存放所有的选中记录，每条记录中包含了id,name,guige
			$.each(checkedDom,function(i,v){
				var id0 = $(this).attr("id");
/* 				var name0 = $(this).closest('tr').find('td').eq(1).text().replace(/\t|\n/g,'');
				var guige0 = $(this).closest('tr').find('td').eq(2).text().replace(/\t|\n/g,''); */
				var each = {}; //存放每条记录,以键值对的形式
				each.goodsid = id0; //key-value
/* 				each.name = name0;
				each.guige = guige0; */
				
				arr.push(each); //存放的所有记录
			});
			return arr;
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
	<sys:message content="${message}" hideType="1"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="goods" action="${ctx}/xuancai/xuancai/plList" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>菜品名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
	 	<div class="pull-left" hidden="hidden">
			<shiro:hasPermission name="goods:goods:add">
				<table:addRow url="${ctx}/goods/goods/form" title="菜品管理" width="50%" height="730px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:goods:edit">
			    <table:editRow url="${ctx}/goods/goods/form" title="菜品管理" id="contentTable" width="50%" height="730px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:goods:del">
				<table:delRow url="${ctx}/goods/goods/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:goods:import">
				<table:importExcel url="${ctx}/goods/goods/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="goods:goods:export">
	       		<table:exportExcel url="${ctx}/goods/goods/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column name">菜品名称</th>
				<th  class="sort-column guige">菜品规格</th>
				<th  class="sort-column category">菜品分类</th>
				<th  class="sort-column category">是否上架</th>
				<!-- <th  class="sort-column imgurl">菜品图片</th> -->
				<!-- <th  class="sort-column contents">菜品描述</th> -->
				<th  class="sort-column price">原价</th>
				<th  class="sort-column xsprice">销售价格</th>
				<th  class="sort-column description">菜品简述</th>
				<th  class="sort-column checkreport">检验报告</th>
				<!-- <th  class="sort-column sort">排序</th> -->
				<!-- <th  class="sort-column updateDate">创建时间</th> -->
				<!-- <th  class="sort-column remarks">备注信息</th> -->
				<!-- <th>操作</th> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="goods">
			<tr>
				<td> <input type="checkbox" id="${goods.id}" class="i-checks"></td>
				<td>
					${goods.name}
				</td>
				<td>
					${goods.guige}
				</td>
				<td>
					${goods.category}
				</td>
				<td>
					${fns:getDictLabel(goods.ison, 'yes_no', '')}
				</td>
				<!-- <td>
					${goods.imgurl}
				</td> -->
				<!-- <td>
					${goods.contents}
				</td> -->
				<td>
					${goods.price}
				</td>
				<td>
					${goods.xsprice}
				</td>
				<td>
					${goods.description}
				</td>
				<td>
					<div style="text-align:center;">
					   	<img src="${goods.checkreport}" class="zoomify" style="max-height: 100px;max-width: 100px;">
					</div>
				</td>
				<!-- <td>
					${goods.sort}
				</td> -->
				<!-- <td>
					<fmt:formatDate value="${goods.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td> -->
				<!-- <td>
					${goods.remarks}
				</td> -->
				<%-- <td>
					<shiro:hasPermission name="goods:goods:view">
						<a href="#" onclick="openDialogView('查看菜品管理', '${ctx}/goods/goods/form?id=${goods.id}','50%', '730px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="goods:goods:edit">
    					<a href="#" onclick="openDialog('编辑菜品管理', '${ctx}/goods/goods/form?id=${goods.id}','50%', '730px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="goods:goods:del">
						<a href="${ctx}/goods/goods/delete?id=${goods.id}" onclick="return confirmx('确认要删除该菜品管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
<script src="${ctxStatic}/zoomify/js/zoomify.min.js"></script>
<script>
	$(function() {
		$('.zoomify').zoomify();
	});
 </script>
</body>
</html>