<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购订单管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		$(document).ready(function() {
			
		});
		
		function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if (validateForm.form()) {
				var g = $("#g").val();	
				// 如果是0，做退款效验处理
				if(g==0){
					//如果是否退款选是，则验证退款金额是否为空
					var b = $("input[name=sftk]:checked").val();
					if(!b){
						top.layer.msg('请选择是否退款',{icon:2});
						return false;
					}else if($("input[name=sftk]:checked").val()=="1"){
						if($("input[name=tkje]").val()==""){
							top.layer.msg('请输入退款金额',{icon:2});
							return false;
						}
					}
				}
				
				if(${tuangouOrder.sfprice}<$("#tkje").val()){
					top.layer.msg('退款的金额不能超过付款金额，请重新填写！',{icon:2});
					return false;
				}
				
				$("#inputForm").submit();
				return true;
			}
			return false;
		}
		$(document).ready(
				function() {
					validateForm = $("#inputForm").validate(
							{
								submitHandler : function(form) {
									loading('正在提交，请稍等...');
									form.submit();
								},
								errorContainer : "#messageBox",
								errorPlacement : function(error, element) {
									$("#messageBox").text("输入有误，请先更正。");
									if (element.is(":checkbox")
											|| element.is(":radio")
											|| element.parent().is(
													".input-append")) {
										error.appendTo(element.parent()
												.parent());
									} else {
										error.insertAfter(element);
									}
								}
							});

				});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="tuangouOrder" action="${ctx}/tuangouorder/tuangouOrder/quxiaoSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div hidden="hidden"><form:input path="gongneng" htmlEscape="false"  id="g"  class="form-control "/></div>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单编号：</label></td>
					<td class="width-35">
						${tuangouOrder.ddh}
					</td>
					<td class="width-15 active"><label class="pull-right">购买会员：</label></td>
					<td class="width-35">
						${tuangouOrder.membername}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">选菜数量：</label></td>
					<td class="width-35">
						${tuangouOrder.num}
					</td>
					<td class="width-15 active"><label class="pull-right">实付价格(元)：</label></td>
					<td class="width-35">
						${tuangouOrder.sfprice}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tuangouOrder.status, 'orderStatus', '')}
						
					</td>
					<td class="width-15 active"><label class="pull-right">收货人所在省：</label></td>
					<td class="width-35">
						${tuangouOrder.shrprovince}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人所在市：</label></td>
					<td class="width-35">
						${tuangouOrder.shrcity}
					</td>
					<td class="width-15 active"><label class="pull-right">收货人所在区：</label></td>
					<td class="width-35">
						${tuangouOrder.shrcounty}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货详细地址：</label></td>
					<td class="width-35">
						${tuangouOrder.shraddress}
					</td>
					<td class="width-15 active"><label class="pull-right">收货人姓名：</label></td>
					<td class="width-35">
						${tuangouOrder.shrname}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人联系电话：</label></td>
					<td class="width-35">
						${tuangouOrder.lxdh}
					</td>
				</tr>
				<tr id="tuikuan">
					<td class="width-15 active"><label class="pull-right" id="sftkxx" >是否退款：</label></td>
					<td class="width-35">
						<form:radiobuttons path="sftk" name="sftk" id="sftk" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"  htmlEscape="false"  class="i-checks"/>
					</td>
					<td class="width-15 active"><label class="pull-right" id="tkje1">退款金额：</label></td>
					<td class="width-35">
						<form:input path="tkje" htmlEscape="false" id="tkje" name="tkje"  class="form-control number"/>
					</td>
				</tr>
				<tr id="tuikuan1">
					<td class="width-15 active"><label class="pull-right" id="zuofeireason1">取消原因：</label></td>
					<td class="width-35">
						<form:textarea path="zuofeireason" htmlEscape="false" id="zuofeireason"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right" id="zuofeireason1">订单退款金额方式：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tuangouOrder.qxzt, 'qxzt', '')}
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>