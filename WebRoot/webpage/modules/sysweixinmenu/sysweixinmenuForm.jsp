<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
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
		
		//获取媒体素材
		function getMediaID(){
			top.layer.open({
			    type: 2,  
			    area: ["80%", "70%"],
			    title: "选择媒体素材",
		        maxmin: true, //开启最大化最小化按钮
			    content: '${ctx}/sysweixinmenu/sysweixinmenu/getMediaList' ,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			         var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			         var rtn = iframeWin.contentWindow.doSubmit();
			         var msg = rtn.split("@")[0];
			         var mediaid = rtn.split("@")[1];
			         if(mediaid){
			        	  setTimeout(function(){top.layer.close(index);}, 100);//延时0.1秒，对应360 7.1版本bug
			        	  $('#mediaid').val(mediaid);
			         }else{
			        	 top.layer.msg(msg,{icon:2});
			         }
				  },
				  cancel: function(index){ 
			      }
			}); 
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="sysweixinmenu" action="${ctx}/sysweixinmenu/sysweixinmenu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
			<tr>
				<td class="width-15 active"><label class="pull-right">上级父级编号:</label></td>
				<td class="width-35">
					<sys:treeselect id="parent" name="parent.id" value="${sysweixinmenu.parent.id}" labelName="parent.name" labelValue="${sysweixinmenu.parent.name}"
						title="父级编号" url="/sysweixinmenu/sysweixinmenu/treeData" extId="${sysweixinmenu.id}" cssClass="form-control " allowClear="true"/>
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>菜单标题：</label></td>
				<td class="width-35">
					<form:input path="name" htmlEscape="false"    class="form-control required"/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
				<td class="width-35">
					<form:input path="sort" htmlEscape="false"  class="form-control required"/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>动作类型：</label></td>
				<td class="width-35">
					<form:radiobuttons path="type" items="${fns:getDictList('wxmenutype')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">网页链接：</label></td>
				<td class="width-35">
					<form:input path="url" htmlEscape="false"    class="form-control "/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">媒体ID：</label></td>
				<td class="width-35">
					<form:input path="mediaid" htmlEscape="false"  onclick="getMediaID();" readonly="true"  class="form-control "/>
				
				</td>
			</tr>
		</tbody>
	</table>
	</form:form>
</body>
</html>