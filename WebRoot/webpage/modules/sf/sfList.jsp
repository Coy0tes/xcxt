<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>物流信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		});
		
	</script>
</head>
<body class="gray-bg"> 
	<div class="wrapper wrapper-content">
	<div class="ibox">
    <div class="ibox-content">
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<tbody>
		<c:forEach items="${list}" var="each">
			<tr>
				<td>
					${each.accept_time}<br>
					${each.remark}
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