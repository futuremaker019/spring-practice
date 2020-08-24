<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    
<%@include file="../includes/header.jsp" %>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Modify</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Modify</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form role="form" action="/board/modify" method="post">
				
					<!-- pageNum과 amount를 form에 추가한다. -->
					<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"/>'>
					<input type="hidden" name="amount" value='<c:out value="${cri.amount }"/>'>
				
					<div class="form-group">
						<label>Bno</label>
						<input class="form-control" name='bno' 
						value='<c:out value="${board.bno}"/>' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>Title</label>
						<input class="form-control" name='title' 
						value='<c:out value="${board.title}"/>'>
					</div>
					
					<div class="form-group">
						<label>Text Area</label>
						<textarea class="form-control" rows="3" 
						name='content'><c:out value="${board.content}"/></textarea>
					</div>
					
					<div class="form-group">
						<label>Writer</label>
						<input class="form-control" name='writer'
						value='<c:out value="${board.writer}"/>' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>Register Date</label>
						<input class="form-control" name='regdate'
						value='<fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}"/>' readonly="readonly"/>
					</div>
					
					<div class="form-group">
						<label>Update Date</label>
						<input class="form-control" name='updatedate'
						value='<fmt:formatDate pattern="yyyy-MM-dd" value="${board.updatedate}"/>' readonly="readonly"/>
					</div>
					
					<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
					<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
					<button type="submit" data-oper='list' class="btn btn-info">List</button>
				</form>
			</div>
			<!-- end panel body -->
		</div>
		<!-- end panel heading -->
	</div>
	<!-- end panel -->
</div>
<!-- end row -->

<script type="text/javascript">
	$(document).ready(function(){
		var formObj = $("form");
		
		$('button').on("click", function(e){
			
			e.preventDefault();
			
			var operation = $(this).data("oper");
			
			/* consol.log(operation); */
			
			if(operation === 'remove') {
				formObj.attr("action", "/board/remove");
			} else if (operation === "list") {
				/* self.location = "/board/list"; */
				
				// move to list
				formObj.attr("action", "/board/list").attr("method", "get");
				var pageNumTag = $("input[name='pageNum']").clone();
				var amountTag = $("input[name='amount']").clone();
				
				// form 내의 모든 태그를 삭제-전달하는 파라미터가 없기때문에
				formObj.empty();
				formObj.append(pageNumTag);
				formObj.append(amountTag);
			}
			formObj.submit();
		});
	});
</script>

<%@include file="../includes/footer.jsp" %>