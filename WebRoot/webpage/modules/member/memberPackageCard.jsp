<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<!-- 会员--》查看套餐卡 页面 -->
<head>
	<title>查看会员套餐卡</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	    var iframe;
		$(document).ready(function() {
			iframe = window.frameElement && window.frameElement.name || '';
		});
		
		function editCs(title,url,width,height){
			var target = iframe;
			top.layer.open({
			    type: 2,  
			    area: [width, height],
			    title: title,
		        maxmin: true, //开启最大化最小化按钮
			    content: url ,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	 var body = top.layer.getChildFrame('body', index);
			         var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			         var inputForm = body.find('#inputForm');
			         var top_iframe;
			         if(target){
			        	 top_iframe = target;//如果指定了iframe，则在改frame中跳转
			         }else{
			        	 top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
			         }
			         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
			        if(iframeWin.contentWindow.doSubmit() ){
			        	  setTimeout(function(){top.layer.close(index)}, 100);//延时0.1秒，对应360 7.1版本bug
			         }
				  },
				  cancel: function(index){ 
			      }
			}); 	
		}
	</script>
	
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>查看会员套餐卡 </h5>
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
	<form:form id="searchForm" modelAttribute="member" action="${ctx}/member/member/memberPackageCard?id=${member.id }" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<%-- <span>手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>昵称：</span>
				<form:input path="nickname" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/> --%>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row" hidden="hidden">
	<div class="col-sm-12">
		<div class="pull-left">
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
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
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
				<th  class="sort-column member.id">所属会员</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column numshengyu">剩余次数</th>
				<th  class="sort-column numuse">使用次数</th>
				<!-- <th  class="sort-column contents">套餐卡描述</th> -->
				<th  class="sort-column updateDate">更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dmCard">
			<tr>
				<td> <input type="checkbox" id="${dmCard.id}" class="i-checks"></td>
				<!-- <td>
					${dmCard.name}
				</td> -->
				<td><a  href="#" onclick="openDialogView('订单记录', '${ctx}/member/member/cardXuancaiList?cardid=${dmCard.id}','77%', '80%')">
					${dmCard.cardid}
				</a></td>
				<td>
					${dmCard.packageName}
				</td>
				<td>
					${dmCard.memberName}
				</td>
				<td>
					${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
				</td>
				<td>
					${dmCard.numshengyu}
				</td>
				<td>
					${dmCard.numuse}
				</td>
				<!-- <td>
					${dmCard.contents}
				</td> -->
				<td>
					<fmt:formatDate value="${dmCard.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<th>
					<shiro:hasPermission name="member:member:edit">
	    				<a href="#" onclick="editCs('编辑套餐卡剩余次数', '${ctx}/dmcard/dmCard/memberCardNumForm?id=${dmCard.id}','60%', '95%');" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑剩余次数</a>
	    			</shiro:hasPermission>
				</th>
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