<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>cms-个人中心</title>
<script type="text/javascript" src="/js/jquery-3.2.1/jquery.js" ></script>
<link href="/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/localization/messages_zh.js"></script>

 <link rel="stylesheet" href="/kindeditor/themes/default/default.css" />
 <link rel="stylesheet" href="/kindeditor/plugins/code/prettify.css" />
 <script charset="utf-8" src="/kindeditor/plugins/code/prettify.js"></script>
 <script charset="utf-8" src="/kindeditor/kindeditor-all.js"></script>
 <script charset="utf-8" src="/kindeditor/lang/zh-CN.js"></script>




<style type="text/css">
	.menuselected {
		background:red;
	}
	.mymenuselected li:hover {
		background:blue;
	}
</style>
</head>
<body style="background:url(/images/4.jpg);">
<nav class="navbar navbar-expand-lg navbar-light bg-light" style="background:#F5F5F5">
  <div class="collapse navbar-collapse" id="navbarSupportedContent" style="background:#F5F5F5;width:30px;height:40px;">
    
    <ul class="navbar-nav mr-auto">
    	<li class="nav-item">
           <a class="nav-link" href="#"><img src="/images/logo.png"> </a>
      </li>
      
      <li class="nav-item active">
        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Link</a>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Dropdown
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="#">Action</a>
          <a class="dropdown-item" href="#">Another action</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="#">Something else here</a>
        </div>
      </li>
      <li class="nav-item">
        <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
      </li>
    </ul>
    <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>
    <div>
    	<ul class="nav">
    		<li class="nav-item nav-link"> <img width="35px" height="35px" src="/images/tx.jpg"> </li>
    	
    		<li class="nav-item nav-link">a</li>
    		<li class="nav-item nav-link">c</li>
    		<li class="nav-item nav-link">d</li>
    	</ul>
    </div>
  </div>
</nav><!--  头结束 -->
	<div class="container-fluid">
	<div class="row">
		<div class="col-md-2" style="margin-top:20px ; border-right:solid 2px"> 
			<!-- 左侧的菜单 -->
			<ul class="nav flex-column mymenuselected">
				  <li class="nav-item ">
				    <a  class="nav-link active" href="#" onclick="showWork($(this),'/user/articles')" >我的文章</a>
				  </li>
				  <li class="nav-item">
				    <a id="postLink" class="nav-link" href="#" onclick="showWork($(this),'/user/postArticle')">发表文章</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" href="#" onclick="showWork($(this),'/user/comments')" >我的评论</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">个人设置</a>
				  </li>
				</ul>	
		</div>
		
		<div class="col-md-10" id="workcontent"> 
		
		<!-- 	<div id="kindEditor" style="display: none"> 
				   引入kindEditor的样式
				      <textarea name="content1" cols="201" rows="200" style="width:700px;height:200px;visibility:hidden;"></textarea> 
              </div> -->
			</div>	
	</div>
	</div>
	
<!-- 尾开始 -->
<nav class="nav fixed-bottom justify-content-center "  style="background:#F5F5F5" height="50px"> 
	       此树是我栽  
</nav>

<script type="text/javascript">	
		
 	 KindEditor.ready(function(K) {
		window.editor1 = K.create('#contentId', {
			cssPath : '/kindeditor/plugins/code/prettify.css',
			//uploadJson : '/resource/kindeditor/jsp/upload_json.jsp',
			uploadJson:'/file/upload'// );
		   })
		prettyPrint();
	});
	
	
	function showWork(obj,url){
		$(".mymenuselected li").removeClass("menuselected");
		obj.parent().addClass("menuselected")		
		$("#workcontent").load(url);
		
	}
</script>


</body>

</html>