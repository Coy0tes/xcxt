<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			
			//取富文本的值
			if(validateForm.form()){
			  $("#contents").val(UM.getEditor('myEditor').getContent()); //富文本提交
			  
			  // 分销提成金额不能大于销售金额校验 分销提成比例不能大于100% BEGIN----------------------
			  var xsprice = parseFloat($("#xsprice").val());
			  var fxtcje = $("#fxtcje").val();
			  var fxtcbl = $("#fxtcbl").val();
			  if(fxtcje!=null){
				  if(xsprice<fxtcje){
					  top.layer.msg('分销提成金额不能大于销售金额，请查看！',{icon:2});
					  return false;
				  }
			  }
			  if(fxtcbl!=null){
				  if(100<fxtcbl){
					  top.layer.msg('分销提成比例不能大于100%，请查看！',{icon:2});
					  return false;
				  }
			  }
			  // 分销提成金额不能大于销售金额校验 分销提成比例不能大于100% END--------------------------
			  
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
			
			// 单选框默认选中
			if(!'${goods.id}'){
				setRadioDefault();
			}
			
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
		<form:form id="inputForm" modelAttribute="goods" action="${ctx}/goods/goods/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" hideType="1"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td style="width:20%" class="width-15 active"><label class="pull-right"><font color="red">*</font>菜品名称：</label></td>
					<td style="width:80%" class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否上架：</label></td>
					<td class="width-35" >
							<form:radiobuttons path="ison" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>菜品分类：</label></td>
					<td class="width-35" >
						<form:select path="category" class="form-control required">
							<form:option value="" label=""/>
							<form:options  id="category" items="${fenleiList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">菜品简述：</label></td>
					<td class="width-35">
						<form:textarea path="description" htmlEscape="false" rows="2"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>菜品图片：</label></td>
					<td class="width-35">
						<form:hidden id="imgurl" path="imgurl" htmlEscape="false" maxlength="5000" class="form-control"/>
						<sys:ckfinder input="imgurl" type="images" uploadPath="/goods/goods" selectMultiple="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">菜品规格：</label></td>
					<td class="width-35">
						<form:input path="guige" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>库存数量：</label></td>
					<td class="width-35">
						<form:input path="kcsl" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">冻结库存：</label></td>
					<td class="width-35">
						<form:input path="djkc" htmlEscape="false"    class="form-control" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">原价：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">销售价格：</label></td>
					<td class="width-35">
						<form:input path="xsprice" htmlEscape="false"    class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">检验报告：</label></td>
					<td class="width-35">
						<form:hidden id="checkreport" path="checkreport" htmlEscape="false" maxlength="2000" class="form-control"/>
						<sys:ckfinder input="checkreport" type="images" uploadPath="/goods/goods" selectMultiple="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">菜品描述：</label></td>
					<td class="width-35">
						<form:hidden path="contents"/>
						<div id="hiddendiv" style="display: none;">渲染umeditor元素</div>
						<!--style给定宽度可以影响编辑器的最终宽度-->
						<!-- 富文本编辑器 -->
						<script type="text/plain" id="myEditor" style="width:100%;height:200px;">
						</script>
					</td>
				</tr>
				<tr hidden="hidden">
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35" id="sort">
						<form:input path="sort" htmlEscape="false" id="sort"  name="sort" class="form-control required digits"/>
					</td>
				</tr>
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
					<td class="width-15 active"><label class="pull-right">分销提成比例：</label></td>
					<td class="width-35">
						<form:input path="fxtcbl" htmlEscape="false"    class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成金额：</label></td>
					<td class="width-35">
						<form:input path="fxtcje" htmlEscape="false"    class="form-control number"/>
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