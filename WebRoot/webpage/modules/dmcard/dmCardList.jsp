<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐卡管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
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
				  openDialog('批量编辑套餐分佣信息', '${ctx}/dmcard/dmCard/fxtcForm?ids='+checkedidArray,'85%', '75%');
			   });
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>套餐卡管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>套餐卡号：</span>
				<form:input path="cardid" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>套餐名称：</span>
				<form:select path="packageid" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${dmPackageName}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('taoCanKaZT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>会员：</span>	
				<form:input path="memberName" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>注册手机号：</span>
				<form:input path="membermobile" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<div style="padding:5px">
				<span>开始日期：</span>
					<form:input path="startTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onfocus="this.blur()" class=" form-control input-sm"/>
				<span>结束日期：</span>
					<form:input path="endTime" htmlEscape="false" maxlength="64" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onfocus="this.blur()" class=" form-control input-sm"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>套餐时长类型：</span>
				<form:select path="tcsclx"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('tcsclx')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="dmcard:dmCard:add">
				<table:addRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<div hidden="hidden">
				<shiro:hasPermission name="dmcard:dmCard:edit">
			    	<table:editRow url="${ctx}/dmcard/dmCard/form" title="套餐卡管理" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
			</div>
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
	       
			<button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="openDialog('批量生成套餐卡', '${ctx}/dmcard/dmCard/zdForm?id=${dmCard.id}','60%', '60%')" title="批量生成套餐卡"><i class="fa fa-plus"></i> 批量生成套餐卡</button>
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
				<th  class="sort-column cardid">套餐卡号</th>
				<th  class="sort-column memberName">会员</th>
				<th  class="sort-column mobile">手机号</th>
				<th  class="sort-column dmyewuname">业务员</th>
				<th  class="sort-column packageid">套餐</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column tcsclx">套餐时长类型</th>
				<th  class="sort-column price">价格</th>
				<th  class="sort-column numpscs">套餐配送次数</th>
				<th  class="sort-column numuse">使用次数</th>
				<th  class="sort-column numshengyu">剩余次数</th>
				<!-- <th  class="sort-column numremarks">编辑备注</th>
				<th  class="sort-column numadminname">编辑人</th>
				<th  class="sort-column numtime">编辑时间</th> -->
				<th  class="sort-column fxtclx">分销提成类型</th>
				<th  class="sort-column fxjebl">分销提成比例(%)</th>
				<th  class="sort-column fxtcje">分销提成金额(元)</th>
				<th  class="sort-column activetime">激活时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmCard">
			<tr>
				<td> <input type="checkbox" id="${dmCard.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','85%', '95%')">
					${dmCard.cardid}
				</a></td>
				<td>
					${dmCard.memberName}
				</td>
				<td>
					${dmCard.membermobile}
				</td>
				<td>
					${dmCard.dmyewuname}
				</td>
				<td>
					${dmCard.packageName}
				</td>
				<td>
					${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
				</td>
				<td>
					${fns:getDictLabel(dmCard.tcsclx, 'tcsclx', '')}
				</td>
				<td>
					${dmCard.price}
				</td>
				<td>
					${dmCard.numpscs}
				</td>
				<td>
					${dmCard.numuse}
				</td>
				<td>
					${dmCard.numshengyu}
				</td>
				
				<%-- <td>
					${dmCard.numremarks}
				</td>
				<td>
					${dmCard.numadminname}
				</td>
				<td>
					${dmCard.numtime}
				</td> --%>
				
				<td>
					${fns:getDictLabel(dmCard.fxtclx, 'fxtclx', '')}
				</td>
				<td>
					${dmCard.fxtcbl}
				</td>
				<td>
					${dmCard.fxtcje}
				</td>
				<td>
					${dmCard.activetime}
				</td>
				<td>
					<shiro:hasPermission name="dmcard:dmCard:view">
						<a href="#" onclick="openDialogView('查看套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmcard:dmCard:edit">
    					<a href="#" onclick="openDialog('编辑套餐卡管理', '${ctx}/dmcard/dmCard/form?id=${dmCard.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="dmcard:dmCard:del">
						<a href="${ctx}/dmcard/dmCard/delete?id=${dmCard.id}" onclick="return confirmx('确认要删除该套餐卡管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="dmcard:dmCard:edit">
    					<a href="#" onclick="openDialog('编辑套餐卡剩余次数', '${ctx}/dmcard/dmCard/numForm?id=${dmCard.id}','60%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑剩余次数</a>
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