<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<!-- 会员--》查看会员套餐卡--》查看此卡下的订单 -->
<head>
	<title>订单记录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>订单记录 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column ddh">订单编号</th>
				<th  class="sort-column cardid">套餐卡卡号	</th>
				<th  class="sort-column memberid">购买会员</th>
				<th  class="sort-column num">选菜数量</th>
				<th  class="sort-column status">订单状态</th>
				<th  class="sort-column shrprovince">收货地址</th>
				<th  class="sort-column shrname">收货人</th>
				<th  class="sort-column lxdh">收货人电话</th>
				<th  class="sort-column kdgs">快递公司</th>
				<th  class="sort-column wldh">物流单号</th>
				<!-- <th>操作</th> -->
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${goodsOrder}" var="goodsOrder">
			<tr>
				<td> <input type="checkbox" id="${goodsOrder.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看菜品订单', '${ctx}/goodsorder/goodsOrder/form?id=${goodsOrder.id}','60%', '80%')">
					${goodsOrder.ddh}
				</a></td>
				<td>
					${goodsOrder.cardName}
				</td>
				<td>
					${goodsOrder.memberName}
				</td>
				<td>
					${goodsOrder.num}
				</td>
				<td>
					${fns:getDictLabel(goodsOrder.status, 'orderStatus', '')}
					<%-- ${goodsOrder.status} --%>
				</td>
				<td>
					${goodsOrder.shrprovince}
					${goodsOrder.shrcity}
					${goodsOrder.shrcounty}
					${goodsOrder.shraddress}
				</td>
				<td>
					${goodsOrder.shrname}
				</td>
				<td>
					${goodsOrder.lxdh}
				</td>
				<td>
					${goodsOrder.kdgs}
				</td>
				<td>
					${goodsOrder.wldh}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>