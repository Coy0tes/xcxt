<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#tip").click(function(){
				var checked = $(this).prop("checked");
				if(checked){
					$(this).prop("checked",true);
					showWxColumns();
				}else{
					$(this).removeAttr("checked");
					hideWxColumns();
				}
			});
			
			//默认不显示微信信息
			$("#tip").prop("checked",false);
			hideWxColumns();
		});
		
		// 批量修改短信提醒
		function duanxinForm(){
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
			  confirmx("是否批量修改短信提醒？",function(){
				  openDialog('批量修改短信提醒设置', '${ctx}/member/member/duanxinForm?ids='+checkedidArray,'40%', '40%');
			   });
		}
		// 批量修改微信提醒
		function wechatForm(){
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
			  confirmx("是否批量修改微信提醒？",function(){
				  openDialog('批量修改微信提醒设置', '${ctx}/member/member/wechatForm?ids='+checkedidArray,'40%', '40%');
			   });
		}
		//批量发送短信消息
		function sendSms(){
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
			  confirmx("是否批量发送短信？",function(){
				  openDialog('批量发送短信', '${ctx}/member/member/smsForm?ids='+checkedidArray,'40%', '40%');
			  });
		}
		
		function showWxColumns(){
			$("#contentTable tr").each(function(){
				$(this).find("th.headimgurl").show();
				$(this).find("td.headimgurl").show();
				
				$(this).find("th.nickname").show();
				$(this).find("td.nickname").show();
				
				$(this).find("th.wxopenid").show();
				$(this).find("td.wxopenid").show();
				
				$(this).find("th.gzTime").show();
				$(this).find("td.gzTime").show();
				
				$(this).find("th.qxgzTime").show();
				$(this).find("td.qxgzTime").show();
			});
		}
		function hideWxColumns(){
			$("#contentTable tr").each(function(){
				$(this).find("th.headimgurl").hide();
				$(this).find("td.headimgurl").hide();
				
				$(this).find("th.nickname").hide();
				$(this).find("td.nickname").hide();
				
				$(this).find("th.wxopenid").hide();
				$(this).find("td.wxopenid").hide();
				
				$(this).find("th.gzTime").hide();
				$(this).find("td.gzTime").hide();
				
				$(this).find("th.qxgzTime").hide();
				$(this).find("td.qxgzTime").hide();
			});
		}
		function openQuanxian(){
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
			  /* confirmx("是否批量开通分销权限？",function(){
				  openDialog('是否批量开通分销权限', '${ctx}/member/member/openQuanxian?ids='+checkedidArray,'40%', '40%');
			   }); */
			  confirmx("是否批量开通分销权限？",function(){
			    	$.ajax({
			    		url:'${ctx}/member/member/openQuanxian',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
		    				top.layer.msg('批量开通分销权限成功!',{icon:1});
		    				
		    				$('#contentTable thead tr th input.i-checks').iCheck('check');
		    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
		    				setTimeout(function(){window.location.reload();},200);
			    		}
			    	});
			   });
		}
		function closeQuanxian(){
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
			  /* confirmx("是否批量关闭分销权限？",function(){
				  openDialog('是否批量关闭分销权限', '${ctx}/member/member/closeQuanxian?ids='+checkedidArray,'40%', '40%');
			   }); */
			  confirmx("是否批量关闭分销权限？",function(){
			    	$.ajax({
			    		url:'${ctx}/member/member/closeQuanxian',
			    		data:{ids:checkedidArray.join()},
			    		type:'post',
			    		success:function(data){
		    				top.layer.msg('批量关闭分销权限成功!',{icon:1});
		    				
		    				$('#contentTable thead tr th input.i-checks').iCheck('check');
		    				$('#contentTable thead tr th input.i-checks').iCheck('uncheck');
		    				setTimeout(function(){window.location.reload();},200);
			    		}
			    	});
			   });
		}
		function err(){
			top.layer.msg("演示界面，此功能不允许操作！", {icon:2});
		}
	</script>
	
	<link rel="stylesheet" href="${ctxStatic}/zoomify/css/zoomify.min.css">
	
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>会员信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="member" action="${ctx}/member/member/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>昵称：</span>
				<form:input path="nickname" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>短信提醒：</span>	
			<form:select path="isPhone" class="form-control">
				<form:option value=""></form:option>
				<form:options items="${fns:getDictList('yes_no') }" itemLabel="label" itemValue="value"/>
			</form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>微信提醒：</span>	
			<form:select path="isWechat" class="form-control">
				<form:option value=""></form:option>
				<form:options items="${fns:getDictList('yes_no') }" itemLabel="label" itemValue="value"/>
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
			<shiro:hasPermission name="member:member:add">
				<table:addRow url="${ctx}/member/member/form" title="会员信息" width="85%" height="95%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:member:edit">
			    <table:editRow url="${ctx}/member/member/form" title="会员信息" id="contentTable" width="85%" height="95%"></table:editRow><!-- 编辑按钮 -->
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
	       <a href="#" onclick="duanxinForm()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-gear"></i> 批量设置短信提醒</a>
	       <a href="#" onclick="wechatForm()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-gear"></i> 批量设置微信提醒</a>
	       <a href="#" onclick="openQuanxian()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-gear"></i> 批量开通分销权限</a>
	       <a href="#" onclick="closeQuanxian()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-gear"></i> 批量关闭分销权限</a>
	       <a href="#" onclick="err()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-envelope-o"></i> 批量发送短信</a>
<!-- 	       <a href="#" onclick="sendSms()"   class="btn btn-primary btn-outline btn-sm "><i class="fa fa-envelope-o"></i> 批量发送短信</a> -->
		   &nbsp;&nbsp;
		   <input type="checkbox" id="tip">&nbsp;显示微信信息
		   
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
				<th  class="sort-column headimgurl">头像</th>
				<th  class="sort-column nickname">昵称</th>
				<th  class="sort-column mobile">手机号</th>
				<th  class="sort-column onlyname">姓名</th>
				<th  class="sort-column wxopenid" style="width:180px;">微信Openid</th>
				<th  class="sort-column islook">是否关注</th>
				<th  class="sort-column packagemember">套餐会员</th>
				<th  class="sort-column yewuid">业务员</th>
				<th  class="sort-column isPhone">短信提醒</th>
				<th  class="sort-column isWechat">微信提醒</th>
				<th  class="sort-column fenxiaoquanxian">分销权限</th>
				<th  class="sort-column birthday">生日</th>
				<th  class="sort-column gzTime">关注时间</th>
				<th  class="sort-column qxgzTime">取消关注时间</th>
				<th  class="sort-column createDate">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="member">
			<tr>
				<td> <input type="checkbox" id="${member.id}" class="i-checks"></td>
				<td class="headimgurl">
					<div style="text-align:center;">
					   	<img src="${member.headimgurl}" class="zoomify" style="max-height: 50px;max-width: 50px;">
					</div>
				</td>
				<td class="nickname">
					${member.nickname}
				</td>
				<td>
					<script type="text/javascript">
						var phone = '${member.mobile}';
						var p = phone.substr(0, 3) + '****' + phone.substr(7);
						document.write(p);
					</script>
				</td>
				<td>
					${member.onlyname}
				</td>
				<td class="wxopenid">
					${member.wxopenid}
				</td>
				<td>
					${fns:getDictLabel(member.islook, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(member.packagemember, 'yes_no', '')}
				</td>
				<td>
					<%-- <c:if test="${member.yewuIsShow == 1}">
					</c:if> --%>
						${member.yewuname}	
				</td>
				<td>
					${fns:getDictLabel(member.isPhone, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(member.isWechat, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(member.fenxiaoquanxian, 'yes_no', '')}
				</td>
				<th>
					${member.birthday}
				</th>
				<td class="gzTime">
					${member.gzTime}
				</td>
				<td  class="qxgzTime">
					${member.qxgzTime}
				</td>
				<td>
					<fmt:formatDate value="${member.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<div style="padding:1px">
						<shiro:hasPermission name="member:member:view">
							<a href="#" onclick="openDialogView('查看会员信息', '${ctx}/member/member/form?id=${member.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="member:member:edit">
	    					<a href="#" onclick="openDialog('编辑会员信息', '${ctx}/member/member/form?id=${member.id}','85%', '95%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="member:member:del">
							<a href="${ctx}/member/member/delete?id=${member.id}" onclick="return confirmx('确认要删除该会员信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="member:member:view">
							<a href="#" onclick="openDialogView('查看会员套餐卡', '${ctx}/member/member/memberPackageCard?id=${member.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看套餐卡</a>
						</shiro:hasPermission>
					</div>
					<div style="margin-top:1px;">
						<shiro:hasPermission name="member:member:edit">
							<a href="${ctx}/member/member/resetPassword?id=${member.id}" onclick="return confirmx('确认重置该会员信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-edit"></i> 密码重置</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="member:member:edit">
	    					<a href="#" onclick="openDialog('分配套餐卡', '${ctx}/member/member/fenpeiCardList?memberid=${member.id}','85%', '95%')" style="background-color: green" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 分配套餐卡</a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="member:member:view">
							<a href="#" onclick="openDialogView('查看下级会员', '${ctx}/member/member/parentsForm?id=${member.id}','85%', '95%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看下级会员</a>
						</shiro:hasPermission>
					</div>
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