<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    
<%@include file="../includes/header.jsp" %>

<div class="bigPictureWrapper">
	<div class="bigPicture">
	</div>
</div>

<style type="text/css">
	.uploadResult {
		width : 100%;
		background-color : grey;
	}
	
	.uploadResult ul {
		display : flex;
		flex-flow : row;
		justify-contenct : center;
		align-items : center;
	}
	
	.uploadResult ul li {
		list-style : none;
		padding : 10px;
	}
	
	.uploadResult ul li img {
		width : 150px;
	}
	
	.bigPictureWrapper {
		position : absolute;
		display : none;
		justify-content : center;
		align-items : center;
		top : 0%;
		width : 100%;
		height : 100%;
		background-color : gray;
		z-index: 100;
		background : rgba(255, 255, 255, 0.5);
	}
	
	.bigPicture{
		position : relative;
		display : flex;
		justify-content : center;
		align-items : center;
	}
	
	.bigPicture img {
		width : 600px;
	}
</style>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Register</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group">
					<label>Bno</label>
					<input class="form-control" name="bno" 
					value='<c:out value="${board.bno}"/>' readonly="readonly">
				</div>
				<div class="form-group">
					<label>Title</label>
					<input class="form-control" name="title"
					value='<c:out value="${board.title}"/>' readonly="readonly">
				</div>
				<div class="form-group">
					<label>Text Area</label>
					<textarea class="form-control" rows="3" 
					name="content" readonly="readonly"><c:out value="${board.content}"/></textarea>
				</div>
				<div class="form-group">
					<label>Writer</label>
					<input class="form-control" name="writer"
					value='<c:out value="${board.writer}"/>' readonly="readonly">
				</div>
				<button data-oper='modify' class="btn btn-default">Modify</button>
				<button data-oper='list' class="btn btn-info">List</button>
					
				<form id="operForm" action="/board/modify" method="get">
					<input type="hidden" id="bno" name="bno" value='<c:out value="${board.bno }"/>'>
					<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"/>'>
					<input type="hidden" name="amount" value='<c:out value="${cri.amount }"/>'>
					<input type="hidden" name="type" value='<c:out value="${cri.type }"/>'>
					<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"/>'>
				</form>
			</div>
			<!-- end panel body -->
		</div>
		<!-- end panel heading -->
	</div>
	<!-- end panel -->
</div>
<!-- end row -->

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Files</div>
			<div class="panel-body">
				<div class='uploadResult'>
					<ul>
					</ul>
				</div>
			</div>
			<!-- end panel-body -->
		</div>
		<!-- end panel -->
	</div>
</div>
<!-- end row -->

<div class="row">
	<div class="col-lg-12">
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i> Reply
				<button id='addReplyBtn' class='btn btn-primary btn-xs pull-right'>New Reply</button>
			</div>
			<!-- /.panel-heading -->
			
			<div class="panel-body">
				
				<ul class="chat">
					<!-- start reply -->
					<li class="left clearfix" data-rno='12'>
						<div>
							<div class="header">
								<strong class="primary-font">user00</strong>
								<small class="pull-right text-muted">2018-01-01 13:13</small>
							</div>
							<p>Good job</p>
						</div>
					</li>
					<!-- end reply -->
				</ul>
				<!-- /. end ul -->
			</div>
			<!-- /.end chat panel-body -->
			
			<div class="panel-footer">
				
			</div>
			<!-- /.end chat panel-footer -->
			
		</div>
	</div>
</div>
<!-- end row -->

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
    aria-labelledby="myModalLabel" aria-hidden="true">
 	<div class="modal-dialog">
 		<div class="modal-content">
 		
 			<div class="modal-header">
 				<button type="button" class="close" data-dismiss="modal"
 				aria-hidden="true">&times;</button>
 				<h4 class="modal-title" id="myModalLabel">Reply Modal</h4>
 			</div>
 			
 			<div class="modal-body">
 				<div class="form-group">
 					<label>Reply</label>
 					<input class="form-control" name="reply" value="New Reply!!!">
 				</div>
 				<div class="form-group">
 					<label>Replyer</label>
 					<input class="form-control" name="replyer" value="replyer">
 				</div>
 				<div class="form-group">
 					<label>Reply Date</label>
 					<input class="form-control" name="replyDate" value="">
 				</div>
 			</div>
 			
 			<div class="modal-footer">
 				<button id="modalModBtn" type="button" class="btn btn-warning" >Modify</button>
 				<button id="modalRemoveBtn" type="button" class="btn btn-danger" >Remove</button>
 				<button id="modalRegisterBtn" type="button" class="btn btn-primary" >Register</button>
 				<button id="modalCloseBtn" type="button" class="btn btn-default" >Close</button>
 			</div>
 			
 		</div>
 		<!-- /.modal-content -->
 	</div>
 	<!-- /.modal-dialog -->
 </div>
 <!-- /.modal -->



<script type="text/javascript" src="/resources/js/reply.js"></script>

<script>
	$(document).ready(function(){
	
		var bnoValue = '<c:out value = "${board.bno}"/>';
		var replyUL = $(".chat");
		
		var modal = $(".modal");
		var modalInputReply = modal.find("input[name='reply']");
		var modalInputReplyer = modal.find("input[name='replyer']");
		var modalInputReplyDate = modal.find("input[name='replyDate']");
		
		var modalModBtn = $("#modalModBtn");
		var modalRemoveBtn = $("#modalRemoveBtn");
		var modalRegisterBtn = $("#modalRegisterBtn");
		var modalCloseBtn = $("#modalCloseBtn");
		
		var pageNum = 1;
		var replyPageFooter = $(".panel-footer");
		
		showList(1);
		
		// content 클릭 시, 첫 페이지의 댓글 목록 가져오는 이벤트 처리
		function showList(page) {
			replyService.getList({bno:bnoValue, page:page || 1}, function(replyCnt, list){

				console.log("replyCnt : " + replyCnt);
				console.log("list : " + list);

				var str = "";

				if (page == -1) {
					// 가장 마지막 페이지를 보여준다.
					// 나머지를 제외한 반올림(전체 댓글수 / 한 페이지의 댓글수) = 마지막 페이지 번호
					pageNum = Math.ceil(replyCnt/10.0);
					showList(pageNum);
					return;
				}
				
				if(list == null || list.length == 0) {
					replyUL.html("");
					return;
				}
				
				// 댓글 목록을 itaration으로 가져온다.
				for(var i = 0, len = list.length || 0; i < len; i++) {
					str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
					str += "<div><div class='header'><strong class='primary-font'>" + list[i].replyer + "</strong>";
					str += "<small class='pull-right text-muted'>" + replyService.displayTime(list[i].replyDate) + "</small></div>";
					str += "<p>" + list[i].reply + "</p></div></li>";
				}
				
				replyUL.html(str);
				
				showReplyPage(replyCnt);
			}); //end replyService function
		} // end showList
		
		function showReplyPage(replyCnt) {
			var endNum = Math.ceil(pageNum / 10.0) * 10;
			var startNum = endNum - 9;
			
			var prev = (startNum != 1);
			var next = false;
			
			// 마지막 페이지 * 10(한 페이지의 탯글수) = 10페이지 까지의 총 댓글수
			if(endNum * 10 >= replyCnt) {
				endNum = Math.ceil(replyCnt / 10.0);
			}
			
			if(endNum * 10 < replyCnt) {
				next = true;
			}
			
			var str = "<ul class='pagination pull-right'>";
			
			if(prev) {
				str += "<li class='page-item'><a class='page-link' href='"+ (startNum - 1) +"'>Previous</a></li>"; 
			}
			
			for (var i = startNum; i <= endNum; i++) {
				/* var active = pageNum == 1 ? "active" : ""; */
				
				str += "<li class='page-item'><a class='page-link' href='"+ i +"'>"+ i +"</a></li>";
			}
			
			if(next) {
				str += "<li class='page-item'><a class='page-link' href='"+ (endNum + 1) +"'>Next</a></li>";
			}
			
			str += "</ul>";
			
			console.log(str);
			
			replyPageFooter.html(str);
		}
		
		// 'New Reply' 버튼 클릭 이벤트 처리
		$("#addReplyBtn").on("click", function(e){
			
			modal.find("input").val("");
			modalInputReplyDate.closest("div").hide();
			modal.find("button[id != 'modalCloseBtn']").hide();
			
			modalRegisterBtn.show();
			
			$(".modal").modal("show");			
		});
		
		// 댓글 클릭 이벤트 처리
		$(".chat").on("click", "li", function(e){
			
			var rno = $(this).data("rno");
			
			replyService.get(rno, function(reply){
				/* console.log(reply); */
				
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.replyer);
				modalInputReplyDate.val(replyService.displayTime(reply.replyDate))
						.attr("readonly", "readonly");
				
				// 이건 뭘 의미하나...
				// modal의 rno를 reply 객체에서 전달받은 rno로 초기화 한다??
				/*console.log(modal.data("rno")); */
				modal.data("rno", reply.rno);
				/* console.log(modal.data("rno", reply.rno)); */
				
				modal.find("button[id != 'modalCloseBtn']").hide();
				modalModBtn.show();
				modalRemoveBtn.show();
				
				$(".modal").modal("show");
			});
		});
		
		// Register button 이벤트 처리
		// 새 댓글을 등록하면 가장 마지막 페이지(page = -1)를 보여준다.
		modalRegisterBtn.on("click", function(e){
			
			var reply = {
				reply : modalInputReply.val(),
				replyer : modalInputReplyer.val(),
				bno : bnoValue
			};
			
			replyService.add(reply, function(result){
				
				alert(result);
				
				modal.find("input").val("");
				modal.modal("hide");
				
				/* showList(1); */
				showList(-1);
			});
		});
		
		modalModBtn.on("click", function(e){
			
			var reply = {rno:modal.data("rno"), reply:modalInputReply.val()};
			
			replyService.update(reply, function(result){
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		modalRemoveBtn.on("click", function(e){
			
			var rno = modal.data("rno");
			
			replyService.remove(rno, function(result){
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			});
		});
		
		modalCloseBtn.on("click", function(e){
			modal.modal("hide");
		});
		
		// 댓글 페이지 번호 클릭 시 해당 댓글 목록 페이지를 보여주는 이벤트 처리
		replyPageFooter.on("click","li a", function(e){
			e.preventDefault();
			console.log("page click");
			
			var targetPageNum = $(this).attr("href");
			
			console.log("targetPageNum : " + targetPageNum);
			
			pageNum = targetPageNum;
			
			showList(pageNum);
		});
	});
</script>

<script type="text/javascript">
	$(document).ready(function(){
		var operForm = $("#operForm");
		
		$("button[data-oper='modify']").on("click", function(e){
			operForm.attr("action", "/board/modify").submit();
		});
		
		$("button[data-oper='list']").on("click", function(e){
			operForm.find("#bno").remove();
			operForm.attr("action", "/board/list");
			operForm.submit();
		});
	});
</script>

<script>
	$(document).ready(function(){
		(function(){
			var bno = '<c:out value="${board.bno}"/>';
			
			$.getJSON("/board/getAttachList", {bno : bno}, function(arr){
				console.log(arr);
				
				var str = "";
				
				$(arr).each(function(i, attach){
					if (attach.fileType) {
						var fileCallPath 
							= encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
						
						str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"'"
							+ " data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'>";
						str += "<div><img src='/display?fileName="+fileCallPath+"'></div></li>";
					} else {
						str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"'"
							+ " data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'>";
						str += "<div><span>"+attach.fileName+"</span><br/>";
						str += "<img src='/resources/document.png'>";
						str += "</div></li>";
					}
				});
				$(".uploadResult ul").html(str);
			}); // end getjson
		})();
		
		$(".uploadResult").on("click", "li", function(e){
			console.log("clicked");
			
			var liObj = $(this);
			var filePath = liObj.data("path")+"/"+liObj.data("uuid")+"_"+liObj.data("filename");
			console.log("filePath before encoding : " + filePath);
			
			/* var path = encodeURIComponent(filePath); */
			
			if (liObj.data("type")) {
				showImage(filePath.replace(new RegExp(/\\/g), "/"));
			} else {
				// download
				self.location = "/download?fileName=" + path
			}
		});
		
		function showImage(fileCallPath) {
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture")
				.html("<img src='/display?fileName=" + encodeURI(fileCallPath) +"'>")
				.show();
				
			$(".bigPictureWrapper").on("click", function(e){
				$(".bigPictureWrapper").hide();
			});
		}
	});
</script>

<%@include file="../includes/footer.jsp" %>