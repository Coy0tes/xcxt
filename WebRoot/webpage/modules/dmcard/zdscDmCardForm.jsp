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
			
			// 监听套餐改变事件
			$("table tbody tr td").find("select[name=packageid]").change(function(){
				packageid = $(this).val();
				if(packageid != ""){
					// 异步获取套餐价格
					$.ajax({
						url:'${ctx}/dmcard/dmCard/packageprice',
						data:{packageid:packageid},
						dataType:'json',
						type:'post',
						success:function(data){
		    				if(data.packageprice != null && data.packageprice != ""){
		    					var num = new Number(data.packageprice);
		    					$("#price").val(num.toFixed(2));
		    					$("#numpscs").val(data.pscs);
		    				}
			    		}
					});
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
		<form:form id="inputForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/zdscSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>生成数量：</label></td>
					<td class="width-35">
						<form:input path="cardNum" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属套餐：</label></td>
					<td class="width-35">
						<form:select path="packageid" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${dmPackageName}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐配送次数：</label></td>
					<td class="width-35">
						<form:input path="numpscs" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>配送频次：</label></td>
					<td class="width-35">
						<form:select path="tcsclx" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('tcsclx')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">业务员：</label></td>
					<td class="width-35">
						<form:select path="dmYewu" class="form-control" >
							<form:option value="" label=""/>
							<form:options  items="${dmYewuLists}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
		<script type="text/javascript">
		//实例化编辑器
	    var um = UM.getEditor('myEditor');
	     um.ready(function() {//编辑器初始化完成再赋值 
	        $('#hiddendiv').html($("#contents").val());
	        um.setContent($('#hiddendiv').text());
	    });
     </script>
</body>
</html>