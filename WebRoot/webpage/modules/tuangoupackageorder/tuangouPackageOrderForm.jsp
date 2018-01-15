<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购套餐订单管理管理</title>
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
	<form:form id="inputForm" modelAttribute="tuangouPackageOrder" action="${ctx}/tuangoupackageorder/tuangouPackageOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单编号：</label></td>
					<td class="width-35">
						${tuangouPackageOrder.ddh}
					</td>
					<td class="width-15 active"><label class="pull-right">购买会员：</label></td>
					<td class="width-35">
						${tuangouPackageOrder.memberName}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">购买总数量：</label></td>
					<td class="width-35">
						${tuangouPackageOrder.num}
					</td>
					<td class="width-15 active"><label class="pull-right">支付总价格：</label></td>
					<td class="width-35">
						${tuangouPackageOrder.sfprice}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tuangouPackageOrder.status, 'orderStatus','')}
					</td>
				<tr>
					<td class="width-15 active"><label class="pull-right">取消状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tuangouPackageOrder.qxzt, 'qxzt','')}
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>作废原因：</label></td>
					<td class="width-35">
						<form:input path="zuofeireason" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>作废时间：</label></td>
					<td class="width-35">
						<form:input path="zuofeitime" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否退款：</label></td>
					<td class="width-35">
						<form:select path="sftk" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>退款金额：</label></td>
					<td class="width-35">
						<form:input path="tkje" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>作废处理人：</label></td>
					<td class="width-35">
						<form:input path="principal1" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">团购订单明细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-primary btn-outline btn-sm " onclick="addRow('#tuangouPackageOrderDetailList', tuangouPackageOrderDetailRowIdx, tuangouPackageOrderDetailTpl);tuangouPackageOrderDetailRowIdx = tuangouPackageOrderDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>套餐</th>
						<th>套餐卡卡号</th>
					</tr>
				</thead>
				<tbody id="tuangouPackageOrderDetailList">
				</tbody>
			</table>
			<script type="text/template" id="tuangouPackageOrderDetailTpl">//<!--
				<tr id="tuangouPackageOrderDetailList{{idx}}">
					<td class="hide">
						<input id="tuangouPackageOrderDetailList{{idx}}_id" name="tuangouPackageOrderDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tuangouPackageOrderDetailList{{idx}}_delFlag" name="tuangouPackageOrderDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="tuangouPackageOrderDetailList{{idx}}_packageid" name="tuangouPackageOrderDetailList[{{idx}}].packageid" type="text" value="{{row.packageName}}"  readonly="readonly"  class="form-control "/>
					</td>
					
					<td>
						<input id="tuangouPackageOrderDetailList{{idx}}_cardid" name="tuangouPackageOrderDetailList[{{idx}}].cardid" type="text" value="{{row.cardName}}"  readonly="readonly"  class="form-control "/>
					</td>					

				</tr>//-->
			</script>
			<script type="text/javascript">
				var tuangouPackageOrderDetailRowIdx = 0, tuangouPackageOrderDetailTpl = $("#tuangouPackageOrderDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tuangouPackageOrder.tuangouPackageOrderDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#tuangouPackageOrderDetailList', tuangouPackageOrderDetailRowIdx, tuangouPackageOrderDetailTpl, data[i]);
						tuangouPackageOrderDetailRowIdx = tuangouPackageOrderDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>