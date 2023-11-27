<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    
<%@include file="../includes/header.jsp" %>

 <div class="row">
     <div class="col-lg-12">
         <h1 class="page-header">Tables</h1>
     </div>
     <!-- /.col-lg-12 -->
 </div>
 <!-- /.row -->
 
 <div class="row">
     <div class="col-lg-12">
         <div class="panel panel-default">
             <div class="panel-heading">
                 Board List Page
                 <button id="regBtn" type="button" class="btn btn-xs pull-right">Register New Board</button>
             </div>
             <!-- /.panel-heading -->
             <div class="panel-body">
                 <table width="100%" class="table table-striped table-bordered table-hover">
                     <thead>
                         <tr>
                             <th>#번호</th>
                             <th>제목</th>
                             <th>작성자</th>
                             <th>작성일</th>
                             <th>수정일</th>
                         </tr>
                     </thead>
                     <tbody>
                     	<c:forEach items="${list }" var="board">
                     		<tr>
                     			<td><c:out value="${board.bno }"/></td>
                     			<td>
                     				<a class="move" href='<c:out value="${board.bno }"/>'><c:out value="${board.title }"/></a>
									<b>[ <c:out value="${board.replycnt }" /> ]</b>                     			
                     			</td>
                     			<td><c:out value="${board.writer }"/></td>
                     			<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate }"/></td>
                     			<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updatedate }"/></td>
                     		</tr>
                     	</c:forEach>
                     </tbody>
                 </table>
                 <!-- /.table-responsive -->
                 
                 <form id="searchForm" action="/board/list" method="get">
                 	<select name="type">
                 		<option value="" <c:out value="${pageMaker.cri.type == null ? 'selected' : ''}"/>>---</option>
                 		<option value="T" <c:out value="${pageMaker.cri.type eq 'T' ? 'selected' : ''}"/>>제목</option>
                 		<option value="C" <c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : ''}"/>>내용</option>
                 		<option value="W" <c:out value="${pageMaker.cri.type eq 'W' ? 'selected' : ''}"/>>작성자</option>
                 		<option value="TC" <c:out value="${pageMaker.cri.type eq 'TC' ? 'selected' : ''}"/>>제목 or 내용</option>
                 		<option value="TW" <c:out value="${pageMaker.cri.type eq 'TW' ? 'selected' : ''}"/>>제목 or 작성자</option>
                 		<option value="TCW" <c:out value="${pageMaker.cri.type eq 'TCW' ? 'selected' : ''}"/>>제목 or 내용 or 작성자</option>
                 	</select>
                 	<input type="text" name="keyword" />
                 	<input type="hidden" name="pageNum" value='${pageMaker.cri.pageNum }'>
                 	<input type="hidden" name="amount" value='${pageMaker.cri.amount }'>
                 	<button class="btn btn-default">Search</button>
                 </form>
                 <!-- /.Search Box -->
                 
                 <div class="pull-right">
                 	<ul class="pagination">
                 		<c:if test="${pageMaker.prev }">
                 			<li class="paginate_button previous">
                 				<a href="${pageMaker.startPage - 1 }">Previous</a>
                 			</li>
                 		</c:if>
                 		
                 		<c:forEach var="num" begin="${pageMaker.startPage }" end="${pageMaker.endPage }">
                 			<li class="paginate_button ${pageMaker.cri.pageNum == num ? 'active' : '' } ">
                 				<a href="${num }">${num }</a>
                 			</li>
                 		</c:forEach>
                 		
                 		<c:if test="${pageMaker.next}">
                 			<li class="paginate_button next">
                 				<a href="${pageMaker.endPage + 1 }">Next</a>
                 			</li>
                 		</c:if>
                 	</ul>
                 </div>
                 <!-- /.end pagination -->
                 
                 <form id='actionForm' action="/board/list" method='get'>
          			<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum }'>
          			<input type='hidden' name='amount' value='${pageMaker.cri.amount }'>
          			<input type="hidden" name="type" value="${pageMaker.cri.type }">
          			<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
          		</form>
                 
                 <!--  -->
                <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                 	<div class="modal-dialog">
                 		<div class="modal-content">
                 			<div class="modal-header">
                 				<button type="button" class="close" data-dismiss="modal"
                 				aria-hidden="true">&times;</button>
                 				<h4 class="modal-title" id="myModalLabel">Modal title</h4>
                 			</div>
                 			<div class="modal-body">처리가 완료되었습니다</div>
                 			<div class="modal-footer">
                 				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                 				<button type="button" class="btn btn-primary" >Save changes</button>
                 			</div>
                 			<!-- /.modal-header -->
                 		</div>
                 		<!-- /.modal-content -->
                 	</div>
                 	<!-- /.modal-dialog -->
                 </div>
                 <!-- /.modal -->

             </div>
             <!-- /.panel-body -->
         </div>
         <!-- /.panel -->
     </div>
     <!-- /.col-lg-12 -->
 </div>
 <!-- /.row -->
            
<script type="text/javascript">
	$(document).ready(function(){
		var result = '<c:out value="${result}"/>';
		var actionForm = $("#actionForm");
		var searchForm = $("#searchForm");
		
		checkModal(result);
		
		/* 뒤로가기 및 앞으로 가기 클릭 시, 미리 일어난 이벤트를 반복적으로 처리 되는 것을 막는다. */
		/* history를 비워주는 역할을 하는 것이 아닐까한다. */
		history.replaceState({}, null, null);
		
		/* 게시글 등록 확인 모달 이벤트 처리 */
		function checkModal(result) {
			if (result === '' || history.state) {
				return;
			}
			if (parseInt(result) > 0) {
				$(".modal-body").html("게시글 " + parseInt(result) + " 번이 등록되었습니다.");
			}
			$("#myModal").modal("show");
		}
		
		//
		$("#regBtn").on("click", function(){
			self.location = "/board/register";
		});
		
		$(".paginate_button a").on("click", function(e) {
			
			e.preventDefault();
			
			/* consol.log 에러 발생 */
			/* consol.log('click'); */
			
			actionForm.find("input[name='pageNum']").val($(this).attr("href"));
			actionForm.submit();
		});
		
		// a 태그의 content를 클릭 시 발생하는 이벤트
		$(".move").on("click", function(e){
			e.preventDefault();
			
			// value에서 +의 정체를 알고 싶다... 
			//별거 아니다. jquery를 사용하여 string을 + 로 연결해준것일뿐.. 
			actionForm.append("<input type='hidden' name='bno' value='"+ $(this).attr("href") +"' >");
			actionForm.attr("action", "/board/get");
			actionForm.submit();
		});
		
		$("#searchForm button").on("click", function(e){
			
			if(!searchForm.find("option:selected").val()){
				alert("검색종류를 선택하세요.");
				return false;
			}
			
			if(!searchForm.find("input[name='keyword']").val()){
				alert("키워드를 입력하세요.");
				return false;
			}
			
			searchForm.find("input[name='pageNum']").val("1");
			e.preventDefault();
			
			searchForm.submit();
		});
	});
</script>

<%@include file="../includes/footer.jsp" %>