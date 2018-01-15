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
	<script type="text/javascript">
	
		$(document).ready(function() {
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/memberCardNumFormSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">套餐卡号：</label></td>
					<td class="width-35">
						${dmCard.cardid}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属套餐：</label></td>
					<td class="width-35">
						${dmCard.packageName}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属会员：</label></td>
					<td class="width-35">
						${dmCard.memberName}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(dmCard.status, 'taoCanKaZT', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">套餐配送次数：</label></td>
					<td class="width-35">
						${dmCard.numpscs}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">使用次数：</label></td>
					<td class="width-35">
						${dmCard.numuse}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>剩余次数：</label></td>
					<td class="width-35">
						<form:input path="numshengyu" htmlEscape="false" class="form-control required digits" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编辑备注：</label></td>
					<td class="width-35">
						<form:textarea path="numremarks" htmlEscape="false" class="form-control required " />
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right">分销提成类型：</label></td>
					<td class="width-35">
						${fns:getDictLabel(dmCard.fxtclx, 'fxtclx', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成比例：</label></td>
					<td class="width-35">
						${dmCard.fxtcbl}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成金额：</label></td>
					<td class="width-35">
						${dmCard.fxtcje}
					</td>
				</tr> --%>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编辑人：</label></td>
					<td class="width-35">
						<form:input path="numadmin" htmlEscape="false" class="form-control required " />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编辑时间：</label></td>
					<td class="width-35">
						<form:input path="numtime" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required" readonly="true"/>
					</td>
				</tr> --%>
				
		 	</tbody>
		</table>
	</form:form>

</body>
</html>