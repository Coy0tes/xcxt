<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>团购套餐管理管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/inputselect/jquery-ui.min.css">
    <script type="text/javascript" src="${ctxStatic}/inputselect/jquery-ui.min.js"></script>
	<script type="text/javascript">
	var validateForm;
	$(document).ready(function() {
		
		 var str = " ";
		// 获取复选框
		var name = document.getElementsByName("tiptype");
		// 获取后台传过来的值
		var dic = '${tuangouPackage.tiptype}';
		if(dic.length!=0){
			<c:forEach items='${tuangouPackage.tiptype}' var="item">
			    $('input[name=tiptype][value=${item}]').attr("checked","checked");
			    $('input[name=tiptype][value=${item}]').parent().addClass("checked");
			</c:forEach>
		}
	
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
			
			// 单选框默认选中
			if(!'${tuangouPackage.id}'){
				setRadioDefault();
			}
			
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
			
			
			 //编辑情况才有，根据从后台传进来的packages，团购套餐包含的套餐
			 if(row && row.packageid){	// row.packageid：套餐的id
				 //赋值名称规格	
				 <c:forEach items='${packages}' var='packages'>
				 		var value = '${packages.name} | ${fns:getDictLabel(packages.danshuang, 'danshuang', '')}';
				 		var packageid = '${packages.id}';
				 		var xcsl = '${packages.xcsl}';
				 		var pscs = '${packages.pscs}';
				 		if(row.packageid == packageid){
					 		$(list+idx).find("#project").val(value);
					 		$(list+idx).find($(list+idx+"_xcsl")).val(xcsl);	//赋值选菜数量
				        	$(list+idx).find($(list+idx+"_pscs")).val(pscs);	//赋值配送次数
					 	}
				 </c:forEach>
			 } 	
		}
		
		
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			var idd = id.val();		// 子表id
			var packid = $("#id").val();	// 主表id 效验主表团购套餐是否下架
			
			if (id.val() == ""){	// 如果子表id为空，直接删除
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				
				//--------------------效验套餐是否下架 冻结库存是否为0，套餐下架并且库存为零才能删除，如果不能删除，页面重新赋值数据库最新的库存跟冻结库存- BEGIN-------------
				 $.ajax({
					url:ctx + '/tuangoupackage/tuangouPackage/checkDelete',
					data:{idd:idd,packid:packid},
					type:'post',
					dataType:'json',
					success:function(data){
						if(data.code!="00"){
							var m = data.msg4; // 区分套餐单双
							var n ='${fns:getDictLabel(data.msg4, 'danshuang', '')}';
							top.layer.msg(data.msg1+data.msg2+$("#project").val()+data.msg3,{icon:2,time: 5000});
						}else{
							delFlag.val("1");
							$(obj).html("&divide;").attr("title", "撤销删除");
							$(obj).parent().parent().addClass("error");
							id.closest('tr').hide();
						}
					}
					
				}); 
				//--------------------效验套餐是否下架 冻结库存是否为0，套餐下架并且库存为零才能删除，如果不能删除，页面重新赋值数据库最新的库存跟冻结库存- END-------------
				
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body class="hideScroll">
<%-- <div id="dmpackage" hidden="hidden">${dmPackage}</div> --%>
	<form:form id="inputForm" modelAttribute="tuangouPackage" action="${ctx}/tuangoupackage/tuangouPackage/save" method="post" class="form-horizontal">
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
						<form:textarea path="guizecontent" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否上架：</label></td>
					<td class="width-35">
						<form:radiobuttons path="ison" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布时间：</label></td>
					<td class="width-35">
						<form:input path="fbsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置开团提醒：</label></td>
					<td class="width-35">
						<form:radiobuttons path="istip" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开始时间：</label></td>
					<td class="width-35">
						<form:input path="beginsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置倒计时：</label></td>
					<td class="width-35">
						<form:radiobuttons path="istimetip" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>结束时间：</label></td>
					<td class="width-35">
						<form:input path="endsj" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required"  onfocus="this.blur()"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开团提醒方式：</label></td>
					<td class="width-35">
						<form:checkboxes path="tiptype" items="${fns:getDictList('tiptype')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>起团数量：</label></td>
					<td class="width-35">
						<form:input path="minnum" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>初始销售量：</label></td>
					<td class="width-35">
						<form:input path="initsellnum" htmlEscape="false"    class="form-control required digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>最大团购数量：</label></td>
					<td class="width-35">
						<form:input path="zdtgsl" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr> --%>
				<tr>
					<td class="width-15 active"><label class="pull-right">价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>每人限购数量：</label></td>
					<td class="width-35">
						<form:input path="maxnumperson" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">图片：</label></td>
					<td class="width-35">
						<form:hidden id="imgurl" path="imgurl" htmlEscape="false" maxlength="2000" class="form-control"/>
						<sys:ckfinder input="imgurl" type="images" uploadPath="/tuangoupackage/tuangouPackage" selectMultiple="true" />
					</td>
		  		</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">团购套餐规格：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-primary btn-outline btn-sm " onclick="addRow('#tuangouPackageGuigeList', tuangouPackageGuigeRowIdx, tuangouPackageGuigeTpl);tuangouPackageGuigeRowIdx = tuangouPackageGuigeRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>套餐信息</th>
						<th>配送次数</th>
						<th>选菜数量</th>
						<th><font color="red">*</font>团购价格</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="tuangouPackageGuigeList">
				</tbody>
			</table>
			<script type="text/template" id="tuangouPackageGuigeTpl">//<!--
				<tr id="tuangouPackageGuigeList{{idx}}">
					<td class="hide">
						<input id="tuangouPackageGuigeList{{idx}}_id" name="tuangouPackageGuigeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tuangouPackageGuigeList{{idx}}_delFlag" name="tuangouPackageGuigeList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<!-- <td hidden="hidden">
						<input id="tuangouPackageGuigeList{{idx}}_packageid" name="tuangouPackageGuigeList[{{idx}}].packageid" type="text" value="{{row.packageid}}"    class="form-control "/>
					</td> -->
					<td >
						<input type="hidden" id="packageid" name="tuangouPackageGuigeList[{{idx}}].packageid" class="form-control" value="{{row.packageid}}">
  						<input id="project" class="form-control" value="">
						<input type="hidden" id="project-value">
					</td>
						<!-- 配送次数 pscs-->
					<td>
						<input id="tuangouPackageGuigeList{{idx}}_pscs" name="tuangouPackageGuigeList[{{idx}}].pscs" type="text" value="{{row.pscs}}"  readonly="true"  class="form-control required number"/>
					</td>
						<!-- 选菜数量xcsl -->
					<td>
						<input id="tuangouPackageGuigeList{{idx}}_xcsl" name="tuangouPackageGuigeList[{{idx}}].xcsl" type="text" value="{{row.xcsl}}"  readonly="true"  class="form-control required number"/>
					</td>
					<td>
						<input id="tuangouPackageGuigeList{{idx}}_price" name="tuangouPackageGuigeList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control required number"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tuangouPackageGuigeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var tuangouPackageGuigeRowIdx = 0, tuangouPackageGuigeTpl = $("#tuangouPackageGuigeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tuangouPackage.tuangouPackageGuigeList)};
					for (var i=0; i<data.length; i++){
						addRow('#tuangouPackageGuigeList', tuangouPackageGuigeRowIdx, tuangouPackageGuigeTpl, data[i]);
						tuangouPackageGuigeRowIdx = tuangouPackageGuigeRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>