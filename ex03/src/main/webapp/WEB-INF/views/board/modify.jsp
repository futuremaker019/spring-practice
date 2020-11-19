<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    
<%@include file="../includes/header.jsp" %>

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
					<input type="hidden" name="type" value='<c:out value="${cri.type }"/>'>
					<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"/>'>
				
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

<div class="bigPictureWrapper">
	<div class="bigPicture">
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Files</div>
			<!-- end panel-heading  -->
			<div class="panel-body">
				<div class="form-group uploadDiv">
					<input type="file" name="uploadFile" multiple>
				</div>
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

<script type="text/javascript">
	$(document).ready(function(){
		var formObj = $("form");
		
		$('button').on("click", function(e){
			
			e.preventDefault();
			
			var operation = $(this).data("oper");
			
			/* consol.log(operation); */
			
			if (operation === 'remove') {
				formObj.attr("action", "/board/remove");
			} else if (operation === "list") {
				/* self.location = "/board/list"; */
				
				// move to list
				formObj.attr("action", "/board/list").attr("method", "get");
				var pageNumTag = $("input[name='pageNum']").clone();
				var amountTag = $("input[name='amount']").clone();
				var typeTag = $("input[name='type']").clone();
				var keywordTag = $("input[name='keyword']").clone();
				
				// form 내의 모든 태그를 삭제-전달하는 파라미터가 없기때문에
				formObj.empty();
				formObj.append(pageNumTag);
				formObj.append(amountTag);
				formObj.append(typeTag);
				formObj.append(keywordTag);
			} else if (operation == 'modify') {
				console.log("submit clicked");
				
				var str = "";
				
				$(".uploadResult ul li").each(function(index, object){
					console.log("object : " + object);
					
					var jobject = $(object);
					console.dir(jobject);
					
					str += "<input type='hidden' name='attachList["+ index +"].fileName' value='" + jobject.data("filename") + "'>";
					str += "<input type='hidden' name='attachList["+ index +"].uuid' value='" + jobject.data("uuid") + "'>";
					str += "<input type='hidden' name='attachList["+ index +"].uploadPath' value='" + jobject.data("path") + "'>";
					str += "<input type='hidden' name='attachList["+ index +"].fileType' value='" + jobject.data("type") + "'>";
				});
				formObj.append(str).submit();
			}
			formObj.submit();
		});
	});
</script>

<script>
$(document).ready(function(){
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880;
	
	// 게시물을 불러올때 첨부파일을 같이 불러오는 즉시 실행 함수 작성
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
						+ " data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div>";
					str += "<span>"+ attach.fileName + "</span>";
					str += "<button type='button' class='btn btn-warning btn-circle' "
						+ "data-file=\'" + fileCallPath + "\' data-type='image'>";
					str += "<i class='fa fa-times'></i></button><br>";
					str += "<img src='/display?fileName="+fileCallPath+"'></div></li>";
				} else {
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"'"
						+ " data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'>";
					str += "<div><span>"+attach.fileName+"</span><br/>";
					str += "<button type='button' class='btn btn-warning btn-circle' "
						+ "data-file=\'" + fileCallPath + "\' data-type='file'>";
					str += "<i class='fa fa-times'></i></button><br>";
					str += "<img src='/resources/document.png'>";
					str += "</div></li>";
				}
			});
			$(".uploadResult ul").html(str);
		}); // end getjson
	})(); //end 즉시 실행 함수 
	
	$(".uploadResult").on("click", "button", function(e){
		console.log("delete button clicked");
		
		if (confirm("첨부파일을 삭제하시겠습니까?")) {
			var targetLi = $(this).closest("li");
			targetLi.remove();
		}
	});
	
	// change() 메서드는 해당 input에 값의 변경이 있을때 이벤트를 발생시킨다.  	
	$("input[type='file']").change(function(e) {
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var maxFileAmount = 3;
		
		// files 가 배열의 형태로 들어온다.
		// 중요한것은 name, size, type
		var files = inputFile[0].files;
		console.log("files 호출이 아래에 뜰거야")
		console.log(files);
		
		for (var i = 0; i < files.length; i++){
			if (!checkExtension(files[i].name, files[i].size)) {
				return false;
			}
			
			if (files.length >= maxFileAmount){
				alert("3개의 파일을 업로드 할 수 없습니다.");
				return false;
			}
			// formData.append(key, value) 형태의 값이 들어와야 한다.
			// 파일 업로드 input 태크의 name은 key자리에, 사진의 데이터는 value 자리에 넣어준다. 
			formData.append("uploadFile", files[i]);
		} // validation, 업로드 파일을 
		
		// 받아준 form을 서버에 전송한다.
		// 전송된 데이터는 showUploadResult 메서드에서 호출하여 화면에 보여준다.
		$.ajax({
			url : '/uploadAjaxAction',
			processData : false,
			contentType : false,
			data : formData,
			type : 'POST',
			dataType : 'json',
			success : function (result) {
				console.log(result);
				showUploadResult(result);
			}
		}); // end ajax
	});
	
	function showUploadResult(uploadResultArr) {
		if (!uploadResultArr || uploadResultArr == 0) { return; }
		
		var uploadUL = $(".uploadResult ul");
		var str = "";
		
		$(uploadResultArr).each(function(i, object){
			if (object.image) {
				var fileCallPath 
					= encodeURIComponent(object.uploadPath + "/s_" + object.uuid + "_" + object.fileName);
				var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
				
				str += "<li data-path='" + object.uploadPath + "'"
					+ " data-uuid='" + object.uuid + "' data-filename='" + object.fileName + "'"
					+ " data-type='" + object.image + "'>";
				str += "<div>";
				str += "<span>"+ object.fileName + "</span>";
				str += "<button type='button' class='btn btn-warning btn-circle' "
					+ "data-file=\'" + fileCallPath + "\' data-type='image'>";
				str += "<i class='fa fa-times'></i></button><br>";
				str += "<img src='/display?fileName=" + fileCallPath + "'>";
				str += "</div></li>";
			} else {
				var fileCallPath
					= encodeURIComponent(object.uploadPath + "/" + object.uuid + "_" + object.fileName);
				var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
				
				str += "<li data-path='" + object.uploadPath + "' data-uuid='" + object.uuid + "'"
					+ " data-filename='" + object.filename + "' date-type='" + object.image + "'>";
				ste += "<div>"
				str += "<span>" + object.fileName + "</span>";
				str += "<button type='button' class='btn btn-warning btn-circle'"
					+ "data-file=\'" + fileCallPath + "\' data-type='file'>";
				str += "<i class='fa fa-times'></i></button><br>";
				str += "<img src='/resources/img/document.png'></a>";
				str += "</div></li>";
			}
		});
		uploadUL.append(str);
	} // end showUploadResult
	
	function checkExtension(fileName, fileSize) {
		if (regex.test(fileName)) {
			alert("해당 종류의 파일은 업로드 할 수 없습니다.")
			return false;
		}
		
		if (fileSize >= maxSize) {
			alert("파일 사이즈 초과");
			return false;
		}
		return true;
	} // file extension & size validation
});
</script>

<%@include file="../includes/footer.jsp" %>