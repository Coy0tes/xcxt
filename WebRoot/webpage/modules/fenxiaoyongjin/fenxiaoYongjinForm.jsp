<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分销佣金记录管理</title>
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
		<form:form id="inputForm" modelAttribute="fenxiaoYongjin" action="${ctx}/fenxiaoyongjin/fenxiaoYongjin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">提成人微信Openid：</label></td>
					<td class="width-35">
						<%-- <form:input path="wxopenid" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.wxopenid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">提成人姓名：</label></td>
					<td class="width-35">
						<%-- <form:input path="name" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.name }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">提成人手机号：</label></td>
					<td class="width-35">
						<%-- <form:input path="mobile" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.mobile }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">提成人佣金：</label></td>
					<td class="width-35">
						<%-- <form:input path="jine" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.jine }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">佣金层级：</label></td>
					<td class="width-35">
						<%-- <form:input path="layer" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.layer }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">来源订单：</label></td>
					<td class="width-35">
						<%-- <form:input path="orderid" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.orderid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单类型：</label></td>
					<td class="width-35">
						<%-- <form:input path="ordertype" htmlEscape="false"    class="form-control "/> --%>
						<%-- <form:select path="ordertype" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ordertype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> --%>
						${fns:getDictLabel(fenxiaoYongjin.ordertype, 'ordertype', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">客户微信Openid：</label></td>
					<td class="width-35">
						<%-- <form:input path="xfzwxopenid" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.xfzwxopenid }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">客户姓名：</label></td>
					<td class="width-35">
						<%-- <form:input path="xfzname" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.xfzname }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">消费金额：</label></td>
					<td class="width-35">
						<%-- <form:input path="xfjine" htmlEscape="false"    class="form-control "/> --%>
						${fenxiaoYongjin.xfjine }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">发放状态：</label></td>
					<td class="width-35">
						<%-- <form:input path="ffzt" htmlEscape="false"    class="form-control "/> --%>
						<%-- <form:select path="ffzt" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ffzt')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> --%>
						${fns:getDictLabel(fenxiaoYongjin.ffzt, 'ffzt', '')}
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>