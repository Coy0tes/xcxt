<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐卡订单管理管理</title>
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
	<form:form id="inputForm" modelAttribute="dmCardOrder" action="${ctx}/dmcardorder/dmCardOrder/savewuliu" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单编号：</label></td>
					<td class="width-35" >
						${dmCardOrder.ddh }
					</td>
					<td class="width-15 active"><label class="pull-right">购卡数量：</label></td>
					<td class="width-35">
						${dmCardOrder.num }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">购卡类型：</label></td>
					<td class="width-35">
					    ${fns:getDictLabel(dmCardOrder.gclx,'cardType','')}
					</td>
					<td class="width-15 active"><label class="pull-right">所属套餐：</label></td>
					<td class="width-35">
						${dmCardOrder.packageName }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">购卡会员：</label></td>
					<td class="width-35">
						 ${dmCardOrder.member.name }
					</td>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(dmCardOrder.status,'orderStatus','')}
						
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人所在省：</label></td>
					<td class="width-35">
						${dmCardOrder.shrprovince }
					</td>
					<td class="width-15 active"><label class="pull-right">收货人所在市：</label></td>
					<td class="width-35">
						${dmCardOrder.shrcity }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人所在区：</label></td>
					<td class="width-35">
						${dmCardOrder.shrcounty }
					</td>
					<td class="width-15 active"><label class="pull-right">收货详细地址：</label></td>
					<td class="width-35">
						${dmCardOrder.shraddress }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货人姓名：</label></td>
					<td class="width-35">
						${dmCardOrder.shrname }
					</td>
					<td class="width-15 active"><label class="pull-right">收货人联系电话：</label></td>
					<td class="width-35">
						${dmCardOrder.lxdh }
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">快递公司：</label></td>
					<td class="width-35">
						<form:input path="kdgs" htmlEscape="false" value="顺丰速递" class="form-control" type="hidden"/>
						<label class="pull-left">顺丰速递</label>
					</td>
					<td class="width-15 active"><label class="pull-right">物流单号：</label></td>
					<td class="width-35">
						<form:input path="wldh" htmlEscape="false"    class="form-control digits "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">套餐卡管理：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#dmCardFuList', dmCardFuRowIdx, dmCardFuTpl);dmCardFuRowIdx = dmCardFuRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>套餐卡号</th>
						<th>所属套餐</th>
						<th>所属会员</th>
						<!-- <th>套餐名称</th> -->
						<th>状态</th>
					</tr>
				</thead>
				<tbody id="dmCardFuList">
				</tbody>
			</table>
			<script type="text/template" id="dmCardFuTpl">//<!--
				<tr id="dmCardFuList{{idx}}">
					<td class="hide">
						<input id="dmCardFuList{{idx}}_id" name="dmCardFuList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="dmCardFuList{{idx}}_delFlag" name="dmCardFuList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="dmCardFuList{{idx}}_cardid" name="dmCardFuList[{{idx}}].cardid" type="text" value="{{row.cardid}}"    class="form-control required number" readonly="true"/>
						
					</td>
					
					<td>
						<input id="dmCardFuList{{idx}}_packageid" name="dmCardFuList[{{idx}}].packageid" type="text" value="{{row.packageName}}"    class="form-control required" readonly="true"/>
 					</td>
					
					<td>
						<input id="dmCardFuList{{idx}}_name" name="dmCardFuList[{{idx}}].member.memberid" type="text" value="{{row.memberName}}"    class="form-control " readonly="true"/>
					</td>
					
					<!--<td>
						<input id="dmCardFuList{{idx}}_contents" name="dmCardFuList[{{idx}}].contents" type="text" value="{{row.contents}}"    class="form-control "/>
					</td>-->
					
					<td>
						<select id="dmCardFuList{{idx}}_status" name="dmCardFuList[{{idx}}].status" data-value="{{row.status}}" class="form-control m-b  required" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('taoCanKaZT')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
						<!--input id="dmCardFuList{{idx}}_status" name="dmCardFuList[{{idx}}].status" items="${fns:getDictList('taoCanKaZT')}" var="dict" type="text" value="{{${dict.value}}}"    class="form-control "/>-->
					</td>

				</tr>//-->
			</script>
			<script type="text/javascript">
				var dmCardFuRowIdx = 0, dmCardFuTpl = $("#dmCardFuTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(dmCardOrder.dmCardFuList)};
					for (var i=0; i<data.length; i++){
						addRow('#dmCardFuList', dmCardFuRowIdx, dmCardFuTpl, data[i]);
						dmCardFuRowIdx = dmCardFuRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>