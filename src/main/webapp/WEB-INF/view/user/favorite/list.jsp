<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- <div class="container-fluid"> -->
	<table class="table">
		<!-- articlePage -->
	
	  <thead>
          <tr>
            <th>id</th>
            <th>标题</th>
            <th>发布时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
        	<c:forEach items="${favoriteList}" var="favorite">
        		<tr>
        			<td>${favorite.id}</td>
        			<td>
        				<a href="${favorite.url }">${favorite.text}</a>
        			</td>
        			<td><fmt:formatDate value="${favorite.created}" pattern="yyyy年MM月dd日"/></td>
        			<td width="200px">
        				<input type="button" value="删除"  class="btn btn-danger" onclick="del(${favorite.id})">
        			</td>
        		</tr>
        	</c:forEach>
        </tbody>
      </table>
      
      <nav aria-label="Page navigation example">
		  <ul class="pagination justify-content-center">
		    <li class="page-item disabled">
		      <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
		    </li>
		   	<c:forEach begin="1" end="${articlePage.pages}" varStatus="i">
		   		<li class="page-item"><a class="page-link" href="#" onclick="gopage(${i.index})">${i.index}</a></li>
		   	</c:forEach>
		    
		   
		    <li class="page-item">
		      <a class="page-link" href="#">Next</a>
		    </li>
		  </ul>
		</nav>
	
<!-- </div>     -->
<script>
	function del(id){
		if(!confirm("您确认删除么？"))
			return;
		
		$.post('/user/deletefavorite',{id:id},
				function(data){
					if(data==true){
						alert("刪除成功")
						//location.href="#"
						$("#workcontent").load("/user/favorite");
					}else{
						alert("刪除失敗")
					}
					
		},"json"				
		)
	}
	
	
	/**
	* 翻页
	*/
	/* function gopage(page){
		$("#workcontent").load("/user/articles?page="+page);
	} */
	
</script>

    
    