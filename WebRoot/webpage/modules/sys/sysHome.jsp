<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		     WinMove();
		     
		     $this = $(".scrolls");
		     $this.hover(function() { 
		    	 	clearInterval(srcollTime); 
		    	 }, function() { 
		    		srcollTime = setInterval(function(){doscroll();}, 1500);
		   	 });
		});
		
	</script>
	<script type="text/javascript">
	    var srcollTime;
		var doscroll = function(){
		    var $parent = $('.scrolls');
		    var $first = $parent.find('li:first');
		    var height = $first.height();
		    $first.animate({
		        height: 0
		        }, 500, function() {// 动画结束后，把它插到最后，形成无缝
		        $first.css('height', height).appendTo($parent);
		    });
		};
		srcollTime = setInterval(function(){doscroll();}, 1500);
	</script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/css/1.css" />
	<style>
		.myvisual{
		    display: block;
		    float: left;
		    font-size: 35px;
		    height: 220px;
		    line-height: 35px;
		    margin-bottom: 15px;
		    padding-left: 15px;
		    padding-top: 10px;
		    width: 80px;
		}
		.details .mynumber {
		    font-size: 65px;
		    font-weight: 300;
		    letter-spacing: -1px;
		    line-height: 36px;
		    margin-bottom: 0;
		    padding-top: 95px;
		    text-align: center;
		    color: #fff;
		}
		
		.details .mydesc {
		float:left; width:100%;
		    font-size: 18px;
		    font-weight: 300;
		    letter-spacing: 0;
		    text-align: center;
		    color: #fff;
		    margin-top:5px;
		}
	</style>
</head>
<body class="gray-bg" style="overflow-x:hidden;">
  <c:if test="${showflag=='y'}">
	<div class="wrapper wrapper-content">
	<!-- 第一行，数量汇总行 -->
	<div class="row">
		<div class="ibox float-e-margins">
	        <div class="col-sm-3">
	            <div class="dashboard-stat blue">
				<div class="visual">
				<i class="fa fa-bar-chart-o"></i>
				</div>
				<div class="details">
				<div class="number">
				<span>${dayMap.number}/${dayMap.total}</span>
				</div>
				<div class="desc"> 日任务 </div>
				</div>
				<a class="more" onclick="top.openTab('${ctx}/shanghudepttaskday/shanghudepttaskday','日任务',false)" data-target="日任务">
				<i class="m-icon-swapright m-icon-white"></i>
				</a>
				</div>
	        </div>
	        <div class="col-sm-3">
	            <div class="dashboard-stat red">
				<div class="visual">
				<i class="fa fa-bar-chart-o"></i>
				</div>
				<div class="details">
				<div class="number">
				<span>${weekMap.number}/${weekMap.total}</span>
				</div>
				<div class="desc"> 周任务 </div>
				</div>
				<a class="more" onclick="top.openTab('${ctx}/shanghudepttaskweek/shanghudepttaskweek','周任务',false)" data-target="周任务">
				<i class="m-icon-swapright m-icon-white"></i>
				</a>
				</div>
	        </div>
	        <div class="col-sm-3">
	            <div class="dashboard-stat green">
				<div class="visual">
				<i class="fa fa-bar-chart-o"></i>
				</div>
				<div class="details">
				<div class="number">
				<span>${monthMap.number}/${monthMap.total}</span>
				</div>
				<div class="desc"> 月任务 </div>
				</div>
				<a class="more" onclick="top.openTab('${ctx}/shanghudepttaskmonth/shanghudepttaskmonth','月任务',false)" data-target="月任务">
				<i class="m-icon-swapright m-icon-white"></i>
				</a>
				</div>
	        </div>
	        <div class="col-sm-3">
	            <div class="dashboard-stat purple">
				<div class="visual">
				<i class="fa fa-bar-chart-o"></i>
				</div>
				<div class="details">
				<div class="number">
				<span>${tempMap.number}/${tempMap.total}</span>
				</div>
				<div class="desc">临时任务</div>
				</div>
				<a class="more" onclick="top.openTab('${ctx}/shanghudepttasktemp/shanghudepttasktemp','临时任务',false)" data-target="临时任务">
				<i class="m-icon-swapright m-icon-white"></i>
				</a>
				</div>
	        </div>
	    </div>
   </div> 
   
   <!-- 第二行 -->
   <div class="row">
   	 <div class="col-sm-8">
   	    <div class="row">
   	         <!-- 隐患趋势 -->
   	         <div class="col-sm-10">
   	         	<div class="ibox-content" style="padding:15px 10px 5px;">
                  <script src="${ctxStatic}/echarts/echarts.min.js" charset="utf-8" type="text/javascript"></script>
                  <div class="echarts" id="echarts-bar-chart"></div>
                  <script type="text/javascript">
						var xAxisData=[];
						var csyhData=[];
						var clyhData=[];
					
						<c:forEach items="${monthZhlist}" var="month">
							xAxisData.push("${month}");
						</c:forEach>
						
						<c:forEach items="${csyhlist}" var="cs">
							csyhData.push("${cs}");
						</c:forEach>
						
						<c:forEach items="${clyhlist}" var="cl">
							clyhData.push("${cl}");
						</c:forEach>
						
						// 基于准备好的dom，初始化echarts实例
					    var myChart = echarts.init(document.getElementById('echarts-bar-chart'));
				    	option = {
			    		    title : {
			    		        text: '年隐患趋势分析图'
			    		    },
			    		    tooltip : {
			    		        trigger: 'axis'
			    		    },
			    		    legend: {
			    		        data:['产生的隐患数','处理的隐患数']
			    		    },
			    		    calculable : true,
			    		    xAxis : [
			    		        {
			    		            type : 'category',
			    		            data : xAxisData
			    		        }
			    		    ],
			    		    yAxis : [
			    		        {
			    		            type : 'value'
			    		        }
			    		    ],
			    		    series : [
			    		        {
			    		            name:'产生的隐患数',
			    		            type:'bar',
			    		            data:csyhData
			    		        },
			    		        {
			    		            name:'处理的隐患数',
			    		            type:'bar',
			    		            data:clyhData
			    		        }
			    		    ]
			    		};
				 	
				    	// 使用刚指定的配置项和数据显示图表。
				    	myChart.setOption(option);
				    
				    	window.onresize = myChart.resize;
				</script>
               </div>
   	         </div>
   	         
   	         <!-- 未处理隐患 -->
   	         <div class="col-sm-2">
   	         		<div class="dashboard-stat blue" style="height:260px;">
					<div class="myvisual">
						<!-- <i class="fa fa-bar-chart-o"></i> -->
						</div>
						<div class="details" style="width:100%;right:0;padding-right:0;padding-top:20px;">
							<div class="mydesc">未处理隐患 </div>
							<div class="mynumber">
								<span>${wclnum}</span>
							</div>
							
						</div>
					    <a class="more" onclick="top.openTab('${ctx}/shanghudepttaskdianweiyh/shanghudepttaskdianweiyh?yhzt=wcl','未处理隐患',false)" data-target="未处理隐患">
						<i class="m-icon-swapright m-icon-white"></i>
					</a>
					</div>
   	         </div>
   	    </div>
   	    
   	    <div class="row">
   	       <div class="col-sm-12">
	    	<div class="ibox float-e-margins">
	            <div class="ibox-title">
	                <h5>${curmonth} 班组工作量统计</h5>
	            </div>
	            
	            <div class="ibox-content">
	            	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th>部门</th>
								<th>班组名称</th>
								<th>月任务数</th>
								<th>应检点位</th>
								<th>按时</th>
								<th>超时</th>
								<th>漏检</th>
								<th>完成率</th>
								<th>超时率</th>
								<th>漏检率</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${bzgzllist}" var="shanghudepttaskusertj">
							<tr>
								<td>
									${shanghudepttaskusertj.departname}
								</td>
								<td>
									${shanghudepttaskusertj.banzuname}
								</td>
								<td>
									${shanghudepttaskusertj.rws}
								</td>
								<td>
									${shanghudepttaskusertj.zsl}
								</td>
								<td>
									${shanghudepttaskusertj.zcsl}
								</td>
								<td>
									${shanghudepttaskusertj.cssl}
								</td>
								<td>
									${shanghudepttaskusertj.ljsl}
								</td>
								<td>
									<div class="col-sm-12">
								          <small id="jds"> ${shanghudepttaskusertj.zcl}%</small>
				                      	  <div class="progress progress-mini">
				                          <div style="width: ${shanghudepttaskusertj.zcl}%;"  class="progress-bar"></div>
				                      	  </div>
								     </div>
								</td>
								<td>
									<div class="col-sm-12">
								          <small id="jds"> ${shanghudepttaskusertj.csl}%</small>
				                      	  <div class="progress progress-mini">
				                          <div style="width: ${shanghudepttaskusertj.csl}%;"  class="progress-bar"></div>
				                      	  </div>
								     </div>
								</td>
								<td>
									<div class="col-sm-12">
								          <small id="jds"> ${shanghudepttaskusertj.ljl}%</small>
				                      	  <div class="progress progress-mini">
				                          <div style="width: ${shanghudepttaskusertj.ljl}%;"  class="progress-bar"></div>
				                      	  </div>
								     </div>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
	            </div>
	        </div>
           </div>
   	    </div>
   	</div>
   	
   	<!-- 巡检记录 -->
   	<div class="col-sm-4">
		<div class="ibox float-e-margins">
           <div class="ibox-title">
               <h5>实时巡检记录</h5>
           </div>
           <div class="ibox-content">
	           <ul class="scrolls" style="padding-left:0px;">
		           <c:forEach items="${xjlist}" var="xj">
		              <li style="list-style-type:none;">
		                 <table style="width:100%;border-collapse:separate; border-spacing:0px 5px;">
		                   <tr>
		                     <td style="width:30%;">${xj.taskname}</td>
		                     <td style="width:30%;">${xj.checkername }</td>
		                     <td style="width:40%;">
		                     	<c:if test="${xj.xjzt=='yh'}">
		                       		<span class="label label-danger pull-right" style="margin-right:10px;"><font style="font-size:12px;">${fns:getDictLabel(xj.xjzt,'dianwei_xjzt','')}</font></span>
		                       </c:if>
		                       <c:if test="${xj.xjzt=='cs'}">
		                       		<span class="label label-warning pull-right" style="margin-right:10px;"><font style="font-size:12px;">${fns:getDictLabel(xj.xjzt,'dianwei_xjzt','')}</font></span>
		                       </c:if>
		                       <c:if test="${xj.xjzt=='lj'}">
		                       		<span class="label label-primary pull-right" style="margin-right:10px;"><font style="font-size:12px;">${fns:getDictLabel(xj.xjzt,'dianwei_xjzt','')}</font></span>
		                       </c:if>
		                       <c:if test="${xj.xjzt=='zc'}">
		                       		<span class="label label-success pull-right" style="margin-right:10px;"><font style="font-size:12px;">${fns:getDictLabel(xj.xjzt,'dianwei_xjzt','')}</font></span>
		                       </c:if>
		                     </td>
		                   </tr>
		                   <tr>
		                     <td><h5>${xj.dianweiname }</h5></td>
		                     <td><h5>${xj.banzuname}</h5></td>
		                     <td><span class="pull-right" style="margin-right:10px;">${xj.checkdate}</span></td>
		                   </tr>
		                 </table>
		               </li>
		           </c:forEach>
	           </ul>
           </div>
           
       </div>
   	</div>
   </div>
    </div>
    
   </c:if>
</body>
</html>