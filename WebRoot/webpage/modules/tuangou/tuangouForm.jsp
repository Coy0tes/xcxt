<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		$(document).ready(function() {
			// 获取“是否设置开团提醒”状态，如果为否，将“开团提醒方式”不设置值
			//alert($('input[name="istip"]:checked').val());
			//var num = $("input[name='istip']").val();
//			alert(num);
			
			
			 var str = " ";
			// 获取复选框
			var name = document.getElementsByName("tiptype");
			// 获取后台传过来的值
			var dic = "${tuangou.tiptype}";
			if(dic.length!=0){
				<c:forEach items="${tuangou.tiptype}" var="item">
				    $('input[name=tiptype][value=${item}]').attr("checked","checked");
				    $('input[name=tiptype][value=${item}]').parent().addClass("checked");
				</c:forEach>
			}
		
			// 单选框默认选中
			if(!'${tuangou.id}'){
				setRadioDefault();
			}
			
			// 初始化赋值菜品信息跟菜品销售价格 
			$("#price").val('${goods.price}');
			$("#goodsName").val('${tuangou.goodsName}');
			
			//--------------------grid框选择菜品 后自动把规格跟价格带过来 Begin-----------
			$("#goodsId").change(function(){
				var goodsId = $("#goodsId").val();
				$.ajax({
					url:'${ctx}/goods/goods/goodsPrice',
					data:{id:goodsId},
					type:'post',
					dataType:'json',
					success:function(data){
						$("#goodsName").val(data.name+" | "+data.guige);
						$("#price").val(data.price);
					}
				});
			});
			//--------------------grid框选择菜品 后自动把规格跟价格带过来 END-----------
			
			$("input[name=tiptype]").click(function(){
				alert();
			});
			
		});
		
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
		/* function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		} */
		
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="tuangou" action="${ctx}/tuangou/tuangou/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>规则名称：</label></td>
					<td class="width-35">
						<form:input path="guizename" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>规则描述：</label></td>
					<td class="width-35">
						<form:textarea path="guizecontent" htmlEscape="false" rows="2"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否启用：</label></td>
					<td class="width-35">
						<form:radiobuttons path="ison" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布时间：</label></td>
					<td class="width-35">
						<%-- <form:input path="fbsj" htmlEscape="false"    class="form-control required"/> --%>
						<form:input path="fbsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置倒计时：</label></td>
					<td class="width-35">
						<form:radiobuttons path="istimetip" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开始时间：</label></td>
					<td class="width-35">
						<%-- <form:input path="beginsj" htmlEscape="false"    class="form-control required"/> --%>
						<form:input path="beginsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置开团提醒：</label></td>
					<td class="width-35">
						<form:radiobuttons path="istip" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结束时间：</label></td>
					<td class="width-35">
						<%-- <form:input path="endsj" htmlEscape="false"    class="form-control required"/> --%>
						<form:input path="endsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开团提醒方式：</label></td>
					<td class="width-35">
						<form:checkboxes path="tiptype" items="${fns:getDictList('tiptype')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属菜品：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/tuangou/tuangou/selectgoods" id="goods" name="goods.id"  value="${tuangou.goods.id}"  title="选择所属菜品" labelName="goods.name" 
						 labelValue="${tuangou.goods.name}" cssClass="form-control required" fieldLabels="商品 | 规格 | 分类  | 原价 | 售价 " fieldKeys="name|guige|category|price|xsprice" searchLabel="商品" searchKey="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>起团数量：</label></td>
					<td class="width-35">
						<form:input path="minnum" htmlEscape="false"    class="form-control required digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>每人限购数量：</label></td>
					<td class="width-35">
						<form:input path="maxnumperson" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>初始销售量：</label></td>
					<td class="width-35">
						<form:input path="initsellnum" htmlEscape="false"    class="form-control required digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>团购最大数量：</label></td>
					<td class="width-35">
						<form:input path="tgzdsl" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr> --%>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>团购起团价格：</label></td>
					<td class="width-35">
						<form:input path="qituanprice" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right">价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"  readonly="true"  class="form-control number"/>
					</td>
				</tr>
		 	</tbody>
		</table>


	</form:form>
</body>
</html>