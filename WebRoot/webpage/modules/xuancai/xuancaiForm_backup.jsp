<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>选菜管理管理</title>
	<meta name="decorator" content="default"/>
	
	<link rel="stylesheet" href="${ctxStatic}/inputselect/jquery-ui.min.css">
    <script type="text/javascript" src="${ctxStatic}/inputselect/jquery-ui.min.js"></script>
    <%-- <link rel="stylesheet" href="${ctxStatic}/inputselect/style.css"> --%>
    <%-- <script type="text/javascript" src="${ctxStatic}/inputselect/inputselect.js"></script> --%>
	
	<%-- <script type="text/javascript" src="${ctxStatic}/styleimg/js/jquery.flexslider-min.js"></script> --%>
	
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		
	</script>

	<script type="text/javascript">
	var ctx = '${ctx}';
    Array.prototype.remove = function(val) {  
        var index = this.indexOf(val);  
        while(index>-1){  
            this.splice(index, 1);  
            index = this.indexOf(val);  
        }  
    };  

    
    //var candelete = []; //存放所有不能删除的id
	var validateForm;
    var goodsidarr = []; //页面上所有菜品的id
		
    
    //------------------------ 售罄菜品
    function shouqing(id,xuancaiListNum){
    	$.ajax({
    		url:ctx + '/xuancai/xuancai/shouqing',
    		data:{zbid:id},
    		dataType:'json',
    		type:'post',
    		success:function(data){
    			top.layer.msg(data.msg,{icon:1});
    			// 得到当前要操作的行
    			$("#xuancaiGoodsList").find("tr input[id="+xuancaiListNum+"_kcsl]").val(data.code);
    			$("#xuancaiGoodsList").find("tr input[id="+xuancaiListNum+"_kcsl]").attr("readonly","readonly");
    		}
    	});
    }
    
    
	var id;
	$(document).ready(function(){
	    $("#select_id").change(function(){
			// 多个相同的套餐只能有一个上架效验
	    	id = $(this).val();
	    	$.ajax({
	    		url:ctx + '/xuancai/xuancai/isPackage',
	    		data:{packageid:id},
	    		dataType:'json',
	    		type:'post',
	    		async:false,
	    		success:function(data){
	    			if(data.code=="11"){
	    				top.layer.msg(data.msg,{icon:2});
	    			}
	    		}
	    	});
	    	// 套餐子表选菜验证 
	   	 	// 菜品是否下架，是否存在
	    	$.ajax({
				url:ctx + '/xuancai/xuancai/isonList',
				data:{ids : arr.join()},
				type:'post',
				dataType:'json',
				async:false,
				success:function(data){
					if(data.code!="00"){
						top.layer.msg(data.msg,{icon:2});
						return false;
					}else{
						res = true;
					}
				}
			});
	    });
	});
		
    // 子表批量添加菜品规格
	function plzj(){
		top.layer.open({
		    type:2,  
		    area: ["70%", "80%"],
		    title: "批量添加选菜规格",
	        maxmin: true, 
		    content: "${ctx}/xuancai/xuancai/plList",
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		         var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		         var rtn = iframeWin.contentWindow.doSubmit(); //返回的所有选中的 菜品记录
		         
		         var ids = [];	// 存放传过来的id
		         //1.取出已有的所有菜品Id，连接成字符串，ids
		         $("#xuancaiGoodsList tr").find("select").each(function(){
					var eachid = $(this).attr("data-value");
					ids.push(eachid);
				 });
		         
		         //2.循环选中的菜品，判定是否能添加到页面。只要有一个选中的 菜品在ids中，就不能添加
		         var isin = false;
		         $.each(rtn,function(i,v){
		        	 var goodsid = v.goodsid;
		        	 if(ids.join().indexOf(goodsid) != -1){
		        		 isin = true;
			        	 return isin;
		        	 }
		         });
		         
		         if(isin){
		        	 top.layer.msg('选项已存在,不能重复添加！',{icon:2});
		         }else{
		        	 $.each(rtn,function(i,v){ //循环所有的菜品
			        	var id = v.goodsid;
			        	v.djsl = 0;	// 循环曾加时冻结库存默认为 0 
			        	//增加行
			        	addRow('#xuancaiGoodsList', xuancaiGoodsRowIdx, xuancaiGoodsTpl, v);
						xuancaiGoodsRowIdx = xuancaiGoodsRowIdx + 1;
			        });
		        	 
		        	top.layer.close(index);//关闭对话框。 */
		         }
		         
			  },
			  cancel: function(index){ 
		      }
		});
	}

	
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。11
		
		//1.---------------------【菜品是否下架或者菜品是否存在】 验证 BEGIN------------------------------------------
		var arr = [];
		//循环添加选中的id
		$("#xuancaiGoodsList tr").find("input[id='goodsid']").each(function(){	// 定位存在id的行
			var ids = $(this).val();	// 获取当前的id
			arr.push(ids);
		});
		var res = false;  //res=false,验证不通过；res=true,验证通过
		//发请求到后台，将arr数组传给后台
		$.ajax({
			url:ctx + '/xuancai/xuancai/isonList',
			data:{ids : arr.join()},
			type:'post',
			dataType:'json',
			async:false,
			success:function(data){
				if(data.code!="00"){
					top.layer.msg(data.msg,{icon:2});
					return false;
				}else{
					res = true;
				}
			}
		});
		if(!res){
			return false;
		}
		//1.---------------------【菜品是否下架或者菜品是否存在】 验证 END------------------------------------------

		if(validateForm.form()){
		  //2.冻结库存不为0时，不能删除 
		  
		
		  //3.-----------------------------校验子表列表中的库存数量必须大于等于冻结库存 BEGIN-----------------------------------
			 /*  var isok = true;
			  var arr0 = []; // 子表id
			  $("#xuancaiGoodsList tr").each(function(){
				    //2.校验子表列表中的库存数量必须大于等于冻结库存
					var id = $(this).find("td").eq(0).find("input").eq(0).val();
					var kcsl = $(this).find("td").eq(2).find("input").val();
					var each = {};
					each.id = id;
					each.kcsl = kcsl;
					arr0.push(each);
			  });
			  $.ajax({
					url:ctx + '/xuancai/xuancai/checkDjck',
					data:{ids : JSON.stringify(arr0)},
					type:'post',
					dataType:'json',
					async:false,
					success:function(data){
						if(data.code!="00"){
							isok = false;
							top.layer.msg(data.msg,{icon:2});
							//更新最新的冻结库存
							$("#xuancaiGoodsList tr").each(function(){
								var id = $(this).find("input").eq(0).val();
								if(id == data.zbid){
									$(this).find("td").eq(3).find("input").val(data.newdjsl);
								}
							});
							return false;
						}
					}
				}); */
		  
		  //3.------------------------------校验子表列表中的库存数量必须大于等于冻结库存 END-----------------------------------
		  /* if(isok){
			  $("#inputForm").submit();
			  return true;
		  }else{
			  return false;
		  } */
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

// -------------------------autocomplete自动不全------------------------------
			// 处理菜品
			var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		    var pro = "";
		    // 循环得到${goods}对象的属性
		    <c:forEach items='${goods}' var='good'>
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
	        	  console.info(event);
	        	  console.info(ui);
	        	  
	        	  // 效验菜品是否重复
	        	 var isexists = false; //true=页面上已经存在新添加的菜品,false=页面上没有新添加的菜品
	        	 $("#xuancaiGoodsList tr").find("input[id='goodsid']").each(function(){	// 定位存在id的行
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
// -------------------------autocomplete自动不全------------------------------

	}
	
	function delRow(obj, prefix){
		var id = $(prefix+"_id");	// id是子表的id
		var delFlag = $(prefix+"_delFlag");		// delFlag的值：0 是叉号	1是除号
		var idd = id.val();		// 子表id
		var packid = $("#id").val();	// 主表id 
		if (id.val() == ""){
			$(obj).parent().parent().remove();
		}else if(delFlag.val() == "0"){
			
			//--------------------效验套餐是否下架 冻结库存是否为0，套餐下架并且库存为零才能删除，如果不能删除，页面重新赋值数据库最新的库存跟冻结库存- BEGIN-------------
			/* $.ajax({
				url:ctx + '/xuancai/xuancai/checkDelete',
				data:{idd:idd,packid:packid},
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.code!="00"){
						top.layer.msg(data.msg1+data.msg2,{icon:2,time: 5000});
						$(prefix+"_djsl").val(data.djsl);
						$(prefix+"_kcsl").val(data.kcsl);;
					}else{
						delFlag.val("1");
						$(obj).html("&divide;").attr("title", "撤销删除");
						$(obj).parent().parent().addClass("error");
						id.closest('tr').hide();
					}
				}
				
			}); */
			//--------------------效验套餐是否下架 冻结库存是否为0，套餐下架并且库存为零才能删除，如果不能删除，页面重新赋值数据库最新的库存跟冻结库存- END-------------
			
			
			
			
			
		}else if(delFlag.val() == "1"){
			delFlag.val("0");
			$(obj).html("&times;").attr("title", "删除");
			$(obj).parent().parent().removeClass("error");
			
			// 有关冻结
			/* var djsl = $(prefix+"_djsl").val(); //冻结数量
			if(parseInt(djsl) > 0){
				//candelete.remove(id.val());
			} */
		}
	}
	</script>
	<script>
		 
		
	
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="xuancai" action="${ctx}/xuancai/xuancai/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>菜单名称：</label></td>
					<td class="width-35">
						<form:input path="title" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属套餐：</label></td>
					<td class="width-35">
						<form:select id="select_id" path="packageid" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${dmPackage }" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否上架：</label></td>
					<td class="width-35">
						<form:radiobuttons path="ison" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>选菜开始时间：</label></td>
					<td class="width-35">
						<form:input path="begindate" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>选菜截止时间：</label></td>
					<td class="width-35">
						<form:input path="enddate" htmlEscape="false"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  class="form-control required" readonly="true"/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">选菜规格：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-primary btn-outline btn-sm " onclick="addRow('#xuancaiGoodsList', xuancaiGoodsRowIdx, xuancaiGoodsTpl);xuancaiGoodsRowIdx = xuancaiGoodsRowIdx + 1;" title="新增选菜规格"><i class="fa fa-plus"></i> 新增选菜规格</a>
			<!-- <a class="btn btn-primary btn-outline btn-sm " onclick="plzj()" title="批量增加选菜规格"><i class="fa fa-plus"></i> 批量增加选菜规格</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>菜品</th>
						<!-- <th>库存数量</th>
						<th>冻结库存</th> 
						<th></th>-->
						<th width="10px">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="xuancaiGoodsList">
				
				</tbody>
			</table>
			<div hidden="hidden" id="good">
				${goods}
			</div>
			<%-- <div hidden="hidden">
							<select id="combobox"  id="goodsid" name="goodsid" data-value="{{row.goodsid}}" class="form-control m-b  required">
								<!-- <option value=""></option> -->
								<c:forEach items="${goods}" var="dict">
									<option id="d" value="${dict.id}">${dict.name} | (规格：${dict.guige})</option>
								</c:forEach>
							</select>
			</div> --%>
			<script type="text/template" id="xuancaiGoodsTpl">//<!--
				<tr id="xuancaiGoodsList{{idx}}">
					<td class="hide">
						<input id="xuancaiGoodsList{{idx}}_id" name="xuancaiGoodsList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="xuancaiGoodsList{{idx}}_delFlag" name="xuancaiGoodsList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td style="width:100%">

						<input type="hidden" id="goodsid" name="xuancaiGoodsList[{{idx}}].goodsid" class="form-control" value="{{row.goodsid}}">
  						<input id="project" class="form-control" value="">
						<input type="hidden" id="project-value">
					</td>
<!--
					<td>
						<input id="xuancaiGoodsList{{idx}}_kcsl" name="xuancaiGoodsList[{{idx}}].kcsl" type="text" value="{{row.kcsl}}"    class="form-control required digits"/>
					</td>


					<td>
						<input id="xuancaiGoodsList{{idx}}_djsl" name="xuancaiGoodsList[{{idx}}].djsl" type="text" value="{{row.djsl}}"   class="form-control " readonly="false"/>
					</td>

					<td>
						<a href="#" onclick="shouqing('{{row.id}}','xuancaiGoodsList{{idx}}')"  class="btn btn-danger btn-xs"> 售罄按钮</a>
					</td>
-->

					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#xuancaiGoodsList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var xuancaiGoodsRowIdx = 0, xuancaiGoodsTpl = $("#xuancaiGoodsTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(xuancai.xuancaiGoodsList)};
					for (var i=0; i<data.length; i++){
						addRow('#xuancaiGoodsList', xuancaiGoodsRowIdx, xuancaiGoodsTpl, data[i]);
						xuancaiGoodsRowIdx = xuancaiGoodsRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>