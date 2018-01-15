<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>佣金提现管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="yjtx" action="${ctx}/yongjintixian/yjtx/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 微信OPENID：</label></td>
					<td class="width-35">
						<%-- <form:input path="wxopenid" htmlEscape="false"    class="form-control  "/> --%>
						${yjtx.wxopenid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 提现金额：</label></td>
					<td class="width-35">
						<%-- <form:input path="jine" htmlEscape="false"    class="form-control   number"/> --%>
						${yjtx.wxopenid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 领取状态：</label></td>
					<td class="width-35">
						<%-- <form:select path="status" class="form-control  ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('lqzt')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> --%>
						${fns:getDictLabel(yjtx.status, 'lqzt', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 申请时间：</label></td>
					<td class="width-35">
						<%-- <form:input path="sdate" htmlEscape="false"    class="form-control  "/> --%>
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"  value="${yjtx.sdate}" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 后台处理状态：</label></td>
					<td class="width-35">
						<%-- <form:select path="clzt" class="form-control  ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('htclzt')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> --%>
						${fns:getDictLabel(yjtx.clzt, 'htclzt', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 处理备注：</label></td>
					<td class="width-35">
						<%-- <form:input path="clremark" htmlEscape="false"    class="form-control  "/> --%>
						${yjtx.clremark }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 处理日期：</label></td>
					<td class="width-35">
						<%-- <form:input path="cldate" htmlEscape="false"    class="form-control  "/> --%>
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"  value="${yjtx.cldate}" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 开户银行：</label></td>
					<td class="width-35">
						<%-- <form:input path="bank" htmlEscape="false"    class="form-control  "/> --%>
						${yjtx.bank }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 银行卡号：</label></td>
					<td class="width-35">
						<%-- <form:input path="cardid" htmlEscape="false"    class="form-control   digits"/> --%>
						${yjtx.cardid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 银行户名：</label></td>
					<td class="width-35">
						<%-- <form:input path="username" htmlEscape="false"    class="form-control  "/> --%>
						${yjtx.username }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"> 手机号：</label></td>
					<td class="width-35">
						<%-- <form:input path="mobile" htmlEscape="false"    class="form-control   digits"/> --%>
						${yjtx.mobile }
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>