<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="referrer" content="never">
		<title></title>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/wxmedia/css/css17718.css" />
		<script type="text/javascript" src="${ctxStatic}/wxmedia/js/jquery1.8.3.min.js"></script>
		<script>
			function doSubmit(){
				var rtn = "未选中任何素材@";
				var num = 0;
				var mediaid = "";
				$(".em2").each(function(){
					if($(this).attr("style")==undefined || $(this).attr("style")==""){
						mediaid = $(this).closest("li").find("input[name=media_id]").val();
						num = num + 1;
					};
				});
				
				if(num>1){
					rtn = "只能选中一个素材@";
				}else if(num==1){
					rtn = "获取成功@"+mediaid;
				}
				
				return rtn;
			}
		</script>
	</head>
	<body>
		<div class="newlistbox">
			<div class="newlistbody">
				<ul>
				   	<c:forEach items="${listmap}" var="map">
			   			<li>
							<div class="newlistonly">
								<span>更新于 ${map.update_time}</span>
								<img width="100%" height="50%" src="${map.thumb_url}">
								<div class="newlistcont">
									<p>${map.title}</p>
									<input type="hidden" name="media_id" value="${map.media_id}"/>
								</div>
								<i><a href="##" class="roundedsvg">
									<img width="100px" height="100px" src="${ctxStatic}/wxmedia/img/rounded.svg">
									<img width="100px" height="100px" style="display:none;" src="${ctxStatic}/wxmedia/img/checked.svg">
									<em class="em1">点击选中</em>
									<em class="em2" style="display:none;">点击取消</em>
								</a></i>
							</div>
						</li>
				   	</c:forEach>
				
				</ul>
				<script type="text/javascript">
		            $(function(){
		               $(".roundedsvg").click(function(){
		            	    $(this).find("img").toggle();
		            	    $(this).find("em").toggle();
					   });
		            });  
		       </script>   
			</div>
		</div>
	</body>
</html>
