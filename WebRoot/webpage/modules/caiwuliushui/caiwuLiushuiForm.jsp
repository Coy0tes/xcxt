<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>财务流水管理</title>
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
		<form:form id="inputForm" modelAttribute="caiwuLiushui" action="${ctx}/caiwuliushui/caiwuLiushui/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">交易类型：</label></td>
					<td class="width-35">
					${fns:getDictLabel(caiwuLiushui.jylx, 'jylx', '')}
						<%-- <form:input path="jylx" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单类型：</label></td>
					<td class="width-35">
					${fns:getDictLabel(caiwuLiushui.ordertype, 'ordertype', '')}
						<%-- <form:input path="jylx" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">付款方：</label></td>
					<td class="width-35">
					${caiwuLiushui.fukuanfang}
						<%-- <form:input path="fukuanfang" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收款方：</label></td>
					<td class="width-35">
					${caiwuLiushui.shoukuanfang }
						<%-- <form:input path="shoukuanfang" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">交易金额：</label></td>
					<td class="width-35">
					${caiwuLiushui.jyje }
						<%-- <form:input path="jyje" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单ID：</label></td>
					<td class="width-35">
					${caiwuLiushui.orderid }
						<%-- <form:input path="orderid" htmlEscape="false"    class="form-control "/> --%>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>