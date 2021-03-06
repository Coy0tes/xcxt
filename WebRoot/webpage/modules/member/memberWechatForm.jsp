<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" >
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
			messages: {
				querenPwd: {equalTo: "输入与上面相同的密码"}
			},
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
		
		// 初始化下拉框，默认是（1）
		$("#isWechat").val(1);
		
	});
	</script>
	<script type="text/javascript" src="${ctxStatic}/area/jsAddress.js"></script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="member" action="${ctx}/member/member/wechatSave" method="post" class="form-horizontal">
		<input type="hidden" name="ids"  value="${ids}"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送微信消息：</label></td>
					<td class="width-35" >
						<form:select path="isWechat" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否给全部会员：</label></td>
					<td class="width-35" >
						<input type="radio" name="wechat" class="i-checks " value="1">是
						<input type="radio" name="wechat" class="i-checks " checked="checked" value="0">否
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>