<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐卡管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  
			  // 判断分销提成比例是否超100
			  var fxtcbl = $("#fxtcbl").val();
			  if(fxtcbl>100){
				  top.layer.msg('分销提成比例不能超过100%，请仔细检查!',{icon:2});
				  return false;
			  }
			  
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
	<!-- 富文本编辑器样式和函数 -->
	<link href="${ctxStatic}/umeditor1.2.3/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/umeditor1.2.3/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor1.2.3/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor1.2.3/umeditor.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/umeditor1.2.3/lang/zh-cn/zh-cn.js"></script>
    
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/fxtcSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="ids"  value="${ids}"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成类型：</label></td>
					<td class="width-35">
						<form:select path="fxtclx" class="form-control">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fxtclx')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成比例(%)：</label></td>
					<td class="width-35">
						<form:input path="fxtcbl" htmlEscape="false"  id="fxtcbl"  class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成金额(元)：</label></td>
					<td class="width-35">
						<form:input path="fxtcje" htmlEscape="false"    class="form-control number"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>