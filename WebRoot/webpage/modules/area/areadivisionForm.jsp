<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>行政区划管理</title>
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
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
	<script type="text/javascript" src="${ctxStatic}/area/jsAddress.js"></script>
	<script>
		$(function(){
			if("${areadivision.id}"){
			}else{
				var layer = "${areadivision.parent.layer}";
				if(layer == ""){
					$("#layer").val("1");
					$("#name").empty();
					var pos = $("#province option");
					$.each(pos,function(i,v){
						var val = $(v).val();
						var option = "<option value='"+val+"'>"+val+"</option>";
						$("#name").append(option);
					});
				}
				if(layer == "1"){
					$("#layer").val("2");
					$("#name").empty();
					$('#province').val("${areadivision.parent.name}");
					$('#province').trigger("change");
					var cos = $("#city option");
					$.each(cos,function(i,v){
						var val = $(v).val();
						var option = "<option value='"+val+"'>"+val+"</option>";
						$("#name").append(option);
					});
				}
				if(layer == "2"){
					$("#layer").val("3");
					$("#name").empty();
					var pname = "${areadivision.parent.name}";
					//获取升级节点
					$.ajax({
						url:'${ctx}/area/areadivision/getByName',
						data:{name:pname},
						type:'post',
						success:function(data){
							var p = data;
							var c = pname;
							$("#province").val(p);
							$("#province").trigger("change");
							$("#city").val(c);
							$("#city").trigger("change");
							var cos = $("#county option");
							$.each(cos,function(i,v){
								var val = $(v).val();
								var option = "<option value='"+val+"'>"+val+"</option>";
								$("#name").append(option);
							});
						}
					});
					
				}
			}
		});
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="areadivision" action="${ctx}/area/areadivision/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="layer"/>
		<div style="display: none;">
			 <select id="province"></select>
			 <select id="city"></select>
			 <select id="county"></select>
		</div>
		<sys:message content="${message}" hideType="1"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
			<tr>
				<td class="width-15 active"><label class="pull-right">上级父级编号:</label></td>
				<td class="width-35">
					<sys:treeselect id="parent" name="parent.id" value="${areadivision.parent.id}" labelName="parent.name" labelValue="${areadivision.parent.name}"
						title="父级编号" url="/area/areadivision/treeData" extId="${areadivision.id}" cssClass="form-control " allowClear="true"/>
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
				<td class="width-35">
				    <c:if test="${not empty areadivision.id}">
				    	${areadivision.name}
				    </c:if>
				    <c:if test="${empty areadivision.id}">
				    	<form:select path="name" htmlEscape="false"    class="form-control required">
						</form:select>
				    </c:if>
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
				<td class="width-35">
					<form:textarea path="remarks" htmlEscape="false" rows="2"    class="form-control "/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
				<td class="width-35">
					<form:input path="sort" htmlEscape="false"    class="form-control required"/>
				</td>
			</tr>
		</tbody>
	</table>
	</form:form>
	<script type="text/javascript" defer>
		addressInit("province","city","county","","","");
	</script>
</body>
</html>