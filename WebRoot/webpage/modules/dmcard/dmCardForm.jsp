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
			  
			  // 判断分销提成比例是否超100
			  var fxtcbl = $("#fxtcbl").val();
			  if(fxtcbl>100){
				  top.layer.msg('分销提成比例不能超过100%，请仔细检查!',{icon:2});
				  return false;
			  }
			  
			  /* 
			  if('${dmCard.status}' == "1"){ //原状态是已激活
				  if($("#status").val()==0){ //改成未激活
					  confirmx("是否将此套餐卡设置成未激活状态？此操作将会清空已绑定的会员，请谨慎操作！",function(){
						  $("#contents").val(UM.getEditor('myEditor').getContent()); //富文本提交
						  $("#inputForm").submit();
						  return true; 
					  });
				  }
			  }else{
				  $("#contents").val(UM.getEditor('myEditor').getContent()); //富文本提交
				  $("#inputForm").submit();
				  return true; 
			  } 
			  */
			  
			  $("#contents").val(UM.getEditor('myEditor').getContent()); //富文本提交
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
			
			if('${dmCard.id}'){
				$("#pscs").hide();
			}
			
		});
		$(document).ready(function() {
			// 监听套餐选择，如果是新增，将套餐的价格附给套餐卡的价格，如果是编辑，不操作
			$("table tbody tr td").find("select[name=packageid]").change(function(){
			packageid = $(this).val();
				if(!'${dmCard.id}'){
	 				$("#price").val("");
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
		<form:form id="inputForm" modelAttribute="dmCard" action="${ctx}/dmcard/dmCard/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<!-- <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr> -->
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐卡号：</label></td>
					<td class="width-35">
						<form:input path="cardid" htmlEscape="false"  class="form-control required" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属会员：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/dmcard/dmCard/selectmember" id="member" name="member.id"  value="${dmCard.member.id}"  title="选择所属会员" labelName="member.mobile" 
						 labelValue="${dmCard.memberName} | ${dmCard.membermobile }" cssClass="form-control" fieldLabels="会员姓名|手机号|昵称" fieldKeys="name|mobile|nickname" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
				</tr>
				<c:if test="${empty dmCard.id}">
					<tr>
						<td class="width-15 active"><label class="pull-right">所属业务员：</label></td>
						<td class="width-35">
							<%-- <sys:gridselect url="${ctx}/dmcard/dmCard/selectyewuyuan" id="dmYewu" name="dmYewu.id"  value="${dmCard.dmYewu.id}"  title="选择业务员" labelName="dmYewu.mobile" 
							 labelValue="${dmCard.dmyewuname}" cssClass="form-control" fieldLabels="业务员|编号" fieldKeys="name|num" searchLabel="手机号" searchKey="num" ></sys:gridselect> --%>
							<form:select path="dmYewu" class="form-control">
								<form:option value="" label=""/>
								<form:options items="${dmYewuLists}" itemLabel="name" itemValue="id" htmlEscape="false"/>
							</form:select>
						</td>
					</tr>
				</c:if>
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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐时长类型：</label></td>
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
						<form:input path="price" htmlEscape="false"  class="form-control required number" />
					</td>
				</tr>
				<tr id="pscs">
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>配送次数：</label></td>
					<td class="width-35">
						<form:input path="numpscs" htmlEscape="false"  class="form-control required number" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('taoCanKaZT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right">套餐卡描述：</label></td>
					<td class="width-35">
						<form:hidden path="contents"/>
						<div id="hiddendiv" style="display: none;">渲染umeditor元素</div>
						<!--style给定宽度可以影响编辑器的最终宽度-->
						<!-- 富文本编辑器 -->
						<script type="text/plain" id="myEditor" style="width:100%;height:200px;">
    						
						</script>
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
					<td class="width-15 active"><label class="pull-right">分销提成比例(%)：</label></td>
					<td class="width-35">
						<form:input path="fxtcbl" htmlEscape="false"  id="fxtcbl"  class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成金额(元)：</label></td>
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