<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@  taglib prefix="form" uri="http://www.springframework.org/tags/form" %>   
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="/js/jquery-3.2.1/jquery.js" ></script>
<link href="/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/localization/messages_zh.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<select id="state">
		<option value="0" >请选择</option>
		<option value="1" ${complainType==1?"selected":""}>涉嫌黄色</option>
		<option value="2" ${complainType==2?"selected":""}>涉及暴力</option>
		<option value="3" ${complainType==3?"selected":""}>涉及违宗教政策</option>
		<option value="4" ${complainType==4?"selected":""}>涉及国家安全</option>
		<option value="5" ${complainType==5?"selected":""}>抄袭内容</option>
		<option value="6" ${complainType==6?"selected":""}>其他</option>
	</select>
	次数大于<input type="text" name="complainCnt">
	次数小于<input type="text" name="complainCnt">
	<button>查询</button>
	
	<table class="table">
		<tr>
			<td>投诉编号</td>
			<td>标题</td>
			<td>文章内容</td>
			<td>投诉类型</td>
			<td>投诉次数</td>
			<td>投诉详情</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${articlePages.list }" var="article">
			<tr>
				<td>${article.id }</td>
				<td>${article.title}</td>
				<td>${article.content}</td>
				<td>${articlechannelId }</td>
				<td>${article.complainCnt }</td>
				<td><input type="button" value="详情"  class="btn btn-warning" onclick="complainList(${article.id})" ></td>
				<td>
					<c:if test="${article.complainCnt >= 50}">
						<input type="button" value="禁止"  class="btn btn-primary" onclick="setStatus(2)">
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<nav aria-label="Page navigation example">
		  <ul class="pagination justify-content-center">
		    <li class="page-item disabled">
		      <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
		    </li>
		   	<c:forEach begin="1" end="${articlePages.pages}" varStatus="i">
		   		<li class="page-item"><a class="page-link" href="javascript:void()" onclick="gopage(${i.index})">${i.index}</a></li>
		   	</c:forEach>
		    
		   
		    <li class="page-item">
		      <a class="page-link" href="#" onclick="gopage(${articlePages.pageNum+1})">Next</a>
		    </li>
		  </ul>
		</nav>
	
</body>
</html>
<div class="modal fade"   id="complainModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
	<div class="modal-dialog" role="document" style="margin-left:100px;">
		<div class="modal-content" style="width:1200px;" >
			<div class="modal-body" id="complainListDiv">
		         
		         		
			</div>
			<div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
      		</div>
		</div>
	</div>
</div>
<script>
/**
* 查看文章的投诉
*/
function complainList(id){
	global_article_id=id;
	$("#complainModal").modal('show')
	$("#complainListDiv").load("/article/complains?articleId="+id);
	
}

function gopage(page){
	$("#workcontent").load("/admin/articles?page="+page + "&status="+${status});
}

$(function(){
	$("#state").change(function(){
		var statu= $("#state ").val();
		alert(statu)
		$("#workcontent").load("/admin/articles?page=" + '${pg.pageNum}' + "&status="+$("#state").val());
	})
})

function setStatus(status){
		var id = global_article_id;
		$.post("/admin/setArticeStatus",{id:id,status:status},function(msg){
			if(msg.code==1){
				alert('操作成功')
				//隐藏当前的模态框
				$('#articleContent').modal('hide')
				$('#complainModal').modal('hide')
				//刷新当前的页面
				//refreshPage();
				return;	
			}
			alert(msg.error);
		},
		"json")
	}
</script>