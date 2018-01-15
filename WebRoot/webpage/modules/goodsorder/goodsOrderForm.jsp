<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜品订单管理</title>
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
		function addRow(list, idx, tpl, row){
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
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="goodsOrder" action="${ctx}/goodsorder/goodsOrder/saveFa" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单编号：</label></td>
					<td class="width-35">
						${goodsOrder.ddh }
					</td>
					<td class="width-15 active"><label class="pull-right">套餐卡卡号：</label></td>
					<td class="width-35">
						${goodsOrder.cardName }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">购买会员：</label></td>
					<td class="width-35">
						${goodsOrder.memberName }
					</td>
					<td class="width-15 active"><label class="pull-right">选菜数量：</label></td>
					<td class="width-35">
						${goodsOrder.num }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(goodsOrder.status, 'orderStatus', '')}
					</td>
					<td class="width-15 active"><label class="pull-right">收货人所在省：</label></td>
					<td class="width-35">
						${goodsOrder.shrprovince }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人所在市：</label></td>
					<td class="width-35">
						${goodsOrder.shrcity }
					</td>
					<td class="width-15 active"><label class="pull-right">收货人所在区：</label></td>
					<td class="width-35">
						${goodsOrder.shrcounty }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货详细地址：</label></td>
					<td class="width-35">
						${goodsOrder.shraddress }
					</td>
					<td class="width-15 active"><label class="pull-right">收货人姓名：</label></td>
					<td class="width-35">
						${goodsOrder.shrname }
					</td>
				</tr>
				<tr>
					<td class="width-15 active" ><label class="pull-right">收货人联系电话：</label></td>
					<td class="width-35">
						${goodsOrder.lxdh }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">快递公司：</label></td>
					<td class="width-35">
						 <form:input path="kdgs" htmlEscape="false" value="顺丰速递" class="form-control" type="hidden"/>
						<label class="pull-left">顺丰速递</label>
						<%-- ${goodsOrder.kdgs } --%>
					</td>
					<td class="width-15 active"><label class="pull-right">物流单号：</label></td>
					<td class="width-35">
						<form:input path="wldh" htmlEscape="false"    class="form-control  digits " />
						<%-- ${goodsOrder.wldh } --%>
					</td>
		  		</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">菜品订单明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#goodsOrderDetailList', goodsOrderDetailRowIdx, goodsOrderDetailTpl);goodsOrderDetailRowIdx = goodsOrderDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>产品名称</th>
						<th>菜品规格</th>
						<th>选菜数量</th>
					</tr>
				</thead>
				<tbody id="goodsOrderDetailList">
				</tbody>
			</table>
			<script type="text/template" id="goodsOrderDetailTpl">//<!--
				<tr id="goodsOrderDetailList{{idx}}">
					<td class="hide">
						<input id="goodsOrderDetailList{{idx}}_id" name="goodsOrderDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="goodsOrderDetailList{{idx}}_delFlag" name="goodsOrderDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="goodsOrderDetailList{{idx}}_goodsname" name="goodsOrderDetailList[{{idx}}].goodsname" type="text" value="{{row.goodsname}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="goodsOrderDetailList{{idx}}_goodsguige" name="goodsOrderDetailList[{{idx}}].goodsguige" type="text" value="{{row.goodsguige}}"    class="form-control " readonly="true"/>
					</td>
					
					
					<td>
						<input id="goodsOrderDetailList{{idx}}_num" name="goodsOrderDetailList[{{idx}}].num" type="text" value="{{row.num}}"    class="form-control " readonly="true"/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var goodsOrderDetailRowIdx = 0, goodsOrderDetailTpl = $("#goodsOrderDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(goodsOrder.goodsOrderDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#goodsOrderDetailList', goodsOrderDetailRowIdx, goodsOrderDetailTpl, data[i]);
						goodsOrderDetailRowIdx = goodsOrderDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>