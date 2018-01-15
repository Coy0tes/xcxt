<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" >
	
	// 如果套餐卡不是一张，那么业务员不能修改，只有是一张套餐卡的时候才能修改
	/* $(function(){
		var yewuid = '${member.yewuid}';
		if(yewuid=="0"){
			$("select[name=yewuid]").attr("disabled","disabled");
		}
	}); */
	
	
	var validateForm;
	
	// 手机号码验证
	function isMobile(value){
		var length = value.length;
	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
	    return (length == 11 && mobile.test(value));
	}
	
	
	
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		
	  var t = isMobile($("#mobile").val());
	  if(!t){
		  top.layer.msg('手机号码有误，重新输入',{icon:2});
		  return false;
	  }
	  if(validateForm.form()){
		  $("#inputForm").submit();
		  return true;
	  }

	  return false;
	}
	$(document).ready(function() {
		validateForm = $("#inputForm").validate({
			messages: {
				querenPwd: {equalTo: "输入与上面相同的密码"}
			},
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
		if(!'${member.id}'){
			setRadioDefault();
		}
		
	});
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, 
			delBtn: true, 
			row: row
		}));
		//每行省市区级联
		var pz,cz,ctz;
		if(row == undefined){
			pz = "";
			cz = "";
			ctz = "";
		}else{
			pz = row.province;
			cz = row.city;
			ctz = row.county;
		}
		addressInit("memberAddressList"+idx+"_province","memberAddressList"+idx+"_city","memberAddressList"+idx+"_county",pz,cz,ctz);
		
		
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
		
		//每次新增行，绑定下radio的点击事件
		$("input[name='memberAddressList["+idx+"].isdefault'][value=1]").click(function(){
			if($(this).is(":checked")){
				// 被选中的name
				var name1 = $(this).prop("name");
				$.each($("#memberAddressList :radio[value=0]"), function(){
					if($(this).prop("name")!=name1){
						$(this).prop("checked", true);
					}
				});
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
	<script type="text/javascript" src="${ctxStatic}/area/jsAddress.js"></script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="member" action="${ctx}/member/member/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<%-- <td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td> --%>
					<td class="width-15 active"><label class="pull-right">昵称：</label></td>
					<td class="width-35">
						<form:input path="nickname" htmlEscape="false"  class="form-control " disabled="disabled"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机号：</label></td>
					<td class="width-35">
						<form:input path="mobile" htmlEscape="false"    class="form-control required number" readonly="1"/>
					</td>
				</tr>
				<tr>
					<%-- <td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送手机短信：</label></td>
					<td class="width-35" >
							<form:radiobuttons path="isPhone" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td> --%>
					<td class="width-15 active"><label class="pull-right">生日：</label></td>
					<td class="width-35">
						<form:input path="birthday" htmlEscape="false" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onfocus="this.blur()" class="form-control" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分销权限：</label></td>
					<td class="width-35" >
							<form:radiobuttons path="fenxiaoquanxian" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right">会员名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"   class="form-control" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送微信消息：</label></td>
					<td class="width-35" >
							<form:radiobuttons path="isWechat" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">业务员：</label></td>
					<td class="width-35">
						<form:select path="yewuid" class="form-control" >
							<form:option value="" label=""/>
							<form:options  items="${dmYewuLists}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送手机短信：</label></td>
					<td class="width-35" >
							<form:radiobuttons path="isPhone" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right">头像：</label></td>
					<td class="width-35">
						<form:input path="headimgurl" htmlEscape="false"     class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">昵称：</label></td>
					<td class="width-35">
						<form:input path="nickname" htmlEscape="false" disabled="disabled"  class="form-control "/>
					</td>
		  		</tr> --%>
		  		
				<tr>
					<td class="width-15 active"><label class="pull-right">微信Openid：</label></td>
					<td class="width-35">
						<form:input path="wxopenid" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">推荐人微信Openid：</label></td>
					<td class="width-35">
						<form:input path="tjrwxopenid" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">关注时间：</label></td>
					<td class="width-35">
						<form:input path="gzTime" htmlEscape="false"   readonly="true"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">取消关注时间：</label></td>
					<td class="width-35">
						<form:input path="qxgzTime" htmlEscape="false"   readonly="true"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">佣金：</label></td>
					<td class="width-35">
						<form:input path="yongjin" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">已提现佣金：</label></td>
					<td class="width-35">
						<form:input path="yongjinytx" htmlEscape="false"  readonly="true"   class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">会员地址：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-primary btn-outline btn-sm " id="add" onclick="addRow('#memberAddressList', memberAddressRowIdx, memberAddressTpl);memberAddressRowIdx = memberAddressRowIdx + 1;" title="新增收货地址"><i class="fa fa-plus"></i> 新增收货地址</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>收货人</th>
						<th>收货人电话</th>
						<th>省</th>
						<th>市</th>
						<th>区</th>
						<th>详细地址</th>
						<th>是否默认地址</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="memberAddressList">
				</tbody>
			</table>
			<script type="text/template" id="memberAddressTpl">//<!--
				<tr id="memberAddressList{{idx}}">
					<td class="hide">
						<input id="memberAddressList{{idx}}_id" name="memberAddressList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="memberAddressList{{idx}}_delFlag" name="memberAddressList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<input id="memberAddressList{{idx}}_shr" name="memberAddressList[{{idx}}].shr" type="text" value="{{row.shr}}"    class="form-control"/>
					</td>

					<td>
						<input id="memberAddressList{{idx}}_shrmobile" name="memberAddressList[{{idx}}].shrmobile" type="text" value="{{row.shrmobile}}"    class="form-control"/>
					</td>
		 
					<td>
						<select id="memberAddressList{{idx}}_province" name="memberAddressList[{{idx}}].province" data-value="{{row.province}}"    class="form-control m-b  required"/>
					</td>
					
					
					<td>
						<select id="memberAddressList{{idx}}_city" name="memberAddressList[{{idx}}].city"  data-value="{{row.city}}"    class="form-control m-b  required"/>
					</td>
					
					
					<td>
						<select id="memberAddressList{{idx}}_county" name="memberAddressList[{{idx}}].county"  data-value="{{row.county}}"    class="form-control m-b "/>
					</td>
			
					<td>
						<input id="memberAddressList{{idx}}_address" name="memberAddressList[{{idx}}].address" type="text" value="{{row.address}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<c:forEach items="${fns:getDictList('yes_no')}" var="dict" varStatus="dictStatus">
							<span><input  id="memberAddressList{{idx}}_isdefault${dictStatus.index}" name="memberAddressList[{{idx}}].isdefault" type="radio" class="i-checks required" value="${dict.value}" data-value="{{row.isdefault}}"><label for="memberAddressList{{idx}}_isdefault${dictStatus.index}">${dict.label}</label></span>
						</c:forEach>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#memberAddressList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
				</div>
			</script>
			<script type="text/javascript">
				var memberAddressRowIdx = 0, memberAddressTpl = $("#memberAddressTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(member.memberAddressList)};
					for (var i=0; i<data.length; i++){
						addRow('#memberAddressList', memberAddressRowIdx, memberAddressTpl, data[i]);
						memberAddressRowIdx = memberAddressRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>