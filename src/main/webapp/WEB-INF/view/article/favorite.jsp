<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收藏</title>
<script type="text/javascript" src="/js/jquery-3.2.1/jquery.js" ></script>
<link href="/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/localization/messages_zh.js"></script>
</head>
<body>
	<div class="container">
	<form:form  modelAttribute="favorite" 
	action="/article/favorite" method="post" enctype="multipart/form-data">
		<div class="form-group">
		   <label >文章标题</label>${article.title}
		  </div>
		<input type="hidden" name="articleId" value="${article.id}">
		
		<div class="form-group">
		   <label >地址</label>
		    <form:input path="url" />
		    <form:errors path="url" cssStyle="color:red"></form:errors>
		 </div> 
		<div class="form-group">
		   <label >时间</label>
		    <form:input path="created" />
		    <form:errors path="created" cssStyle="color:red"></form:errors>
		 </div> 
		<button>提交</button>
	</form:form>	
	
	</div>
</body>
</html>