<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>套餐管理管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/inputselect/jquery-ui.min.css">
    <script type="text/javascript" src="${ctxStatic}/inputselect/jquery-ui.min.js"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  
			// 判断分销提成比例是否超100
			parseFloat($("#xsprice").val());
			var fxtcbl = $("#fxtcbl").val();
			var fxtcje = $("#fxtcje").val();
			var xsprice = parseFloat($("#xsprice").val());
			if(fxtcbl != null){
				if(fxtcbl>100){
					  top.layer.msg('分销提成比例不能超过100%，请仔细检查!',{icon:2});
					  return false;
				}
			}
			// 判断提成金额是否超过销售价格
			if(fxtcje != null){
				if(xsprice<fxtcje){
					top.layer.msg('分销提成金额不能超过销售价格，请仔细检查!',{icon:2});
					return false;
				  }
			  }
			
			  //$("#contents").val(UM.getEditor('myEditor').getContent()); //富文本提交
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
			if(!'${dmPackage.id}'){
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
			//编辑情况才有，根据goodsid获取名称和规格，并赋值到子表记录中
			 if(row && row.goodsid){
				 //赋值名称规格
				 <c:forEach items='${goods}' var='good'>
				 	var eachid = '${good.id}';
				 	if(row.goodsid == eachid){
				 		var value = '${good.name} | ${good.guige}';
				 		$(list+idx).find("#project").val(value);
				 	}
				 </c:forEach>
			 } 

	// -------------------------autocomplete自动补全 BEGIN------------------------------
				// 处理菜品
				var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
			    var pro = "";
			    // 循环得到${goods}对象的属性
			    <c:forEach items='${goodson}' var='good'>
				    var value = '${good.name} | ${good.guige}'; //获取单个text
		            var label = '${good.name} | ${good.guige}'; //获取单个text
		            var id = '${good.id}'; //获取单个value，也就是id
		            
			    	pro={id:id,label:label, value:value};
		            arr.push(pro);
				</c:forEach>

		        var projects =arr;
		        
		        $(list+idx).find( "#project" ).autocomplete({
		          minLength: 0,
		          source: projects,
		          // 鼠标移到选项后的触发事件
		          focus: function( event, ui ) {
		        	 //$(list+idx).find( "#project" ).val( ui.item.label );
		            // return false;
		          },
		          // 给input赋值
		          select: function(event, ui) {
		        	  // 效验菜品是否重复
		        	 var isexists = false; //true=页面上已经存在新添加的菜品,false=页面上没有新添加的菜品
		        	 $("#dmPackageGoodsZengsongList tr").find("input[id='goodsid']").each(function(){	// 定位存在id的行
							var ids = $(this).val();	// 获取当前的id
							if(ui.item.id == ids){
								isexists = true;
								return false;
							}
					});
		        	 
		        	if(!isexists){ //如果页面上没有，则可以新增
		        		    $(list+idx).find("#goodsid").val(ui.item.id);	// id
				        	$(list+idx).find("#project").val(ui.item.label);	// value
				        	$(list+idx).find("#project-value").val(ui.item.value);		// value
				            return false;
		        	 }else{
		        		 top.layer.msg('该菜品已经选择，不能重复选择',{icon:2});
		        		 $(list+idx).remove();
		        	 }
		        	  
		          }
		        })
		        //下拉赋值
		        .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
		          return $( "<li>" )
		            .append( "<a>" + item.label + "<br>" + "</a>" )
		            .appendTo( ul );
		        };
				arr=""; 
	// -------------------------autocomplete自动补全 END------------------------------
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				/* delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error"); */
				
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
				id.closest('tr').hide();
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
	<!-- 富文本编辑器样式和函数 -->
	<link href="${ctxStatic}/umeditor1.2.3/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/umeditor1.2.3/third-party/template.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor1.2.3/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/umeditor1.2.3/umeditor.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/umeditor1.2.3/lang/zh-cn/zh-cn.js"></script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="dmPackage" action="${ctx}/dmpackage/dmPackage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>频次：</label></td>
					<td class="width-35">
						<form:radiobuttons path="danshuang" items="${fns:getDictList('danshuang')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="status" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>销售状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="xszt" items="${fns:getDictList('xszt')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>配送次数(次)：</label></td>
					<td class="width-35">
						<form:input path="pscs" htmlEscape="false"    class="form-control required digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>选菜数量(种)：</label></td>
					<td class="width-35">
						<form:input path="xcsl" htmlEscape="false"    class="form-control required digits"/>
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否上架：</label></td>
					<td class="width-35">
						<form:radiobuttons path="ison" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr> --%>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>原价(元)：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>销售价格(元)：</label></td>
					<td class="width-35">
						<form:input path="xsprice" htmlEscape="false" id="xsprice"   class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>套餐图片：</label></td>
					<td class="width-35">
						<form:hidden id="imgurl" path="imgurl" htmlEscape="false" maxlength="5000" class="form-control"/>
						<sys:ckfinder input="imgurl" type="images" uploadPath="/dmpackage/dmPackage" selectMultiple="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">套餐描述：</label></td>
					
					<td class="width-35">
						<%-- <form:hidden path="contents"/>
						<div id="hiddendiv" style="display: none;">渲染umeditor元素</div>
						<!--style给定宽度可以影响编辑器的最终宽度-->
						<!-- 富文本编辑器 -->
						<script type="text/plain" id="myEditor" style="width:100%;height:200px;">
    						
						</script> --%>
						<form:textarea path="contents" htmlEscape="false" rows="4"    class="form-control "/>
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
					<td class="width-15 active"><label class="pull-right">分销提成比例(%)：</label></td>
					<td class="width-35">
						<form:input path="fxtcbl" htmlEscape="false" id="fxtcbl" class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分销提成金额(元)：</label></td>
					<td class="width-35">
						<form:input path="fxtcje" htmlEscape="false" id="fxtcje"   class="form-control number"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">套餐赠送菜品：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-primary btn-outline btn-sm " onclick="addRow('#dmPackageGoodsZengsongList', dmPackageGoodsZengsongRowIdx, dmPackageGoodsZengsongTpl);dmPackageGoodsZengsongRowIdx = dmPackageGoodsZengsongRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>选择菜品</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="dmPackageGoodsZengsongList">
				</tbody>
			</table>
			<script type="text/template" id="dmPackageGoodsZengsongTpl">//<!--
				<tr id="dmPackageGoodsZengsongList{{idx}}">
					<td class="hide">
						<input id="dmPackageGoodsZengsongList{{idx}}_id" name="dmPackageGoodsZengsongList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="dmPackageGoodsZengsongList{{idx}}_delFlag" name="dmPackageGoodsZengsongList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td style="width:100%">

						<input type="hidden" id="goodsid" name="dmPackageGoodsZengsongList[{{idx}}].goodsid" class="form-control" value="{{row.goodsid}}">
  						<input id="project" class="form-control" value="">
						<input type="hidden" id="project-value">
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#dmPackageGoodsZengsongList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var dmPackageGoodsZengsongRowIdx = 0, dmPackageGoodsZengsongTpl = $("#dmPackageGoodsZengsongTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(dmPackage.dmPackageGoodsZengsongList)};
					for (var i=0; i<data.length; i++){
						addRow('#dmPackageGoodsZengsongList', dmPackageGoodsZengsongRowIdx, dmPackageGoodsZengsongTpl, data[i]);
						dmPackageGoodsZengsongRowIdx = dmPackageGoodsZengsongRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>