<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购订单管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		});
		
		function cantuan(){
			var tuangouid = $("#tuangouid").val();
			
			$.ajax({
				url:'${ctx}/tuangouorder/tuangouOrder/tuangouOrderRecord',
				data:{tuangouid:tuangouid},
				type:'post',
				dataType:'json',
				success:function(data){
    				top.layer.msg(data.msg,{icon:1});
    				
    				/* $('#contentTable thead tr th input.i-checks').iCheck('check');
    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck'); */
    				setTimeout(function(){window.location.reload();},200);
	    		}
			});
		}
		
		function piliangFahuoList(){
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
			confirmx("是否批量发货？",function(){
			 openDialog('批量发货', '${ctx}/tuangouorder/tuangouOrder/piliangFahuoList?ids='+checkedidArray,'60%', '95%');
			 });
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>团购订单管理列表 </h5>
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
	<%-- <div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="tuangouOrder" action="${ctx}/tuangouorder/tuangouOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>真实订单：</span>
		    <form:select path="flag" class="form-control">
		    	<form:option value=""></form:option>
		    	<form:option value="0">是</form:option>
		    	<form:option value="1">否</form:option>
		    </form:select>
		     &nbsp;&nbsp;
		    <span>订单状态：</span>
		    <form:select path="status" class="form-control">
		    	<form:option value=""></form:option>
		    	<form:options items="${fns:getDictList('orderStatus') }" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
			<div>
		    &nbsp;&nbsp;
			<span>订单编号：</span>
				<form:input path="ddh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			&nbsp;&nbsp;
		    <span>团购名称：</span>
				<form:input path="tuangou.guizename" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<div style="margin-top:10px;">
			    <span>注册手机号：</span>
					<form:input path="membermobile" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			    <span>收货人手机号：</span>
					<form:input path="lxdh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			    <span>收货人姓名：</span>
					<form:input path="shrname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			</div>
			<div style="padding:5px">
				<span>下单开始日期：</span>
					<form:input path="startTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onfocus="this.blur()" class=" form-control input-sm"/>
				<span>下单结束日期：</span>
					<form:input path="endTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onfocus="this.blur()" class=" form-control input-sm"/>
			</div>
				
			</div>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div> --%>
	
	<!-- 工具栏 -->
	<div class="row" hidden="hidden">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="tuangouorder:tuangouOrder:add">
				<table:addRow url="${ctx}/tuangouorder/tuangouOrder/form" title="团购订单管理"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="tuangouorder:tuangouOrder:edit">
				    <table:editRow url="${ctx}/tuangouorder/tuangouOrder/form" title="团购订单管理" id="contentTable"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
			</div>
			<shiro:hasPermission name="tuangouorder:tuangouOrder:del">
				<table:delRow url="${ctx}/tuangouorder/tuangouOrder/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangouorder:tuangouOrder:import">
				<table:importExcel url="${ctx}/tuangouorder/tuangouOrder/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="tuangouorder:tuangouOrder:export">
	       		<table:exportExcel url="${ctx}/tuangouorder/tuangouOrder/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       <a href="#" onclick="piliangFahuoList()" class="btn btn-primary btn-outline btn-sm " ><i class="fa fa-edit"></i> 批量发货</a>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 异步增加参团记录 -->
	<a href="#" onclick="cantuan()" class="btn btn-primary btn-outline btn-sm"> 参团记录管理</a>
	<%-- 	<a href="${ctx}/tuangouorder/tuangouOrder/tuangouOrderRecordList?id=${tuangou.id}" onclick="return confirmx('确认要增加参团记录吗？', this.href)" class="btn btn-primary btn-outline btn-sm"> 参团记录管理</a> --%>
	<input id="tuangouid" type="hidden" value="${tuangouid }">
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column ddh">订单编号</th>
				<th  class="sort-column tuangouname">团购名称</th>
				<th  class="sort-column memberid">购买会员</th>
				<th  class="sort-column membermobile">注册手机号</th>
				<th  class="sort-column num">选菜数量</th>
				<th  class="sort-column sfprice">实付价格</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shrname">收货人</th>
				<th  class="sort-column lxdh">收货人电话</th>
				<th  class="sort-column createDate">下单时间</th>
				<!-- <th>操作</th> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${lists}" var="tuangouOrder">
			<tr>
				<td> <input type="checkbox" id="${tuangouOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看团购订单管理', '${ctx}/tuangouorder/tuangouOrder/form?id=${tuangouOrder.id}&gongneng=3','45%', '90%')">
					${tuangouOrder.ddh}
				</a></td>
				<td>
					${tuangouOrder.tuangouname}
				</td>
				<td>
					${tuangouOrder.membername}
				</td>
				<td>
					${tuangouOrder.membermobile}
				</td>
				<td>
					${tuangouOrder.num}
				</td>
				<td>
					${tuangouOrder.sfprice}
				</td>
				<td>
					<%-- <div id="hid" hidden="hidden">${tuangouOrder.status}</div> --%>
					${fns:getDictLabel(tuangouOrder.status, 'orderStatus', '')}
				</td>
				<td>
					${tuangouOrder.shrname}
				</td>
				<td>
					${tuangouOrder.lxdh}
				</td>
				<td>
					<fmt:formatDate value="${tuangouOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%-- <td>
					<shiro:hasPermission name="tuangouorder:tuangouOrder:view" >
						<a href="#" onclick="openDialogView('查看团购订单管理', '${ctx}/tuangouorder/tuangouOrder/form?id=${tuangouOrder.id}','45%', '90%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="tuangouorder:tuangouOrder:edit">
    					<a href="#" onclick="openDialog('编辑团购订单管理', '${ctx}/tuangouorder/tuangouOrder/form?id=${tuangouOrder.id}','45%', '57%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="tuangouorder:tuangouOrder:del">
						<a href="${ctx}/tuangouorder/tuangouOrder/delete?id=${tuangouOrder.id}" onclick="return confirmx('确认要删除该团购订单管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<c:if test="${tuangouOrder.flag == '1' }">
						<a href="${ctx}/tuangouorder/tuangouOrder/delete?id=${tuangouOrder.id}" onclick="return confirmx('确认要删除该团购订单管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</c:if>
					<c:if test="${tuangouOrder.status == '0'} ">
						<shiro:hasPermission name="tuangouorder:tuangouOrder:edit" >
	    					<a href="#" onclick="openDialog('订单发货', '${ctx}/tuangouorder/tuangouOrder/form?id=${tuangouOrder.id}&gongneng=1','45%', '57%')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 发货</a>
	    				</shiro:hasPermission>
	    			</c:if>
	    			<c:if test="${tuangouOrder.status =='0' }">
						<shiro:hasPermission name="tuangouorder:tuangouOrder:edit">
	    					<a href="#" onclick="openDialog('订单发货', '${ctx}/tuangouorder/tuangouOrder/fahuoForm?id=${tuangouOrder.id}','45%', '90%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 发货</a>
	    				</shiro:hasPermission>
					</c:if>
					<c:if test="${tuangouOrder.status =='1' }">
						<a href="#" onclick="openDialogView('查看物流信息', '${ctx}/sf/showList?wldh=${tuangouOrder.wldh}','400px', '80%')" class="btn btn-primary btn-xs" ><i class="fa fa-search-plus"></i> 物流信息</a>
					</c:if>
    				<c:if test="${tuangouOrder.status != '1'  and tuangouOrder.status !='4' and tuangouOrder.status !='2' and tuangouOrder.flag != '1'}">
	    					<a href="#" onclick="openDialog('订单作废 警告：订单作废后相关信息无法恢复！', '${ctx}/tuangouorder/tuangouOrder/quxiaoForm?id=${tuangouOrder.id}','45%', '90%')"  class="btn btn-danger btn-xs" ><i class="fa fa-edit"></i> 取消</a>
    				</c:if>
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