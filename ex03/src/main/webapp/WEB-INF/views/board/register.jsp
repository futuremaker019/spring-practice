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
				<form role="form" action="/board/register" method="post">
					<div class="form-group">
						<label>Title</label><input class="form-control" name="title">
					</div>
					<div class="form-group">
						<label>Text Area</label>
						<textarea class="form-control" rows="3" name="content"></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label>
						<input class="form-control" name="writer">
					</div>
					<button type="submit" class="btn btn-default">Submit Button</button>
					<button type="reset" class="btn btn-default">Reset Button</button>
					<a href="/board/list" >리스트로 돌아가기</a>
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
			<div class="panel-heading">File Attach</div>
			<!-- end panel-heading -->
			<div class="panel-body">
				<div class="form-group uploadDiv">
					<label>3개의 사진파일 업로드 가능</label>
					<input type="file" name="uploadFile" multiple>
				</div>
				
				<div class="uploadResult">
					<ul>
					</ul>
				</div>
			</div>
			<!-- end panel body  -->
		</div>
		<!-- end panel  -->
	</div>
</div>
<!-- end row -->

<script>
$(document).ready(function(){
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880; //5MB
	
	var formObject = $("form[role='form']");
	
	// submit 버튼 이벤트, 
	$("button[type='submit']").on("click", function(e){
		e.preventDefault();
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
		formObject.append(str).submit();
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
	
	// 삭제 버튼 이벤트 - 첨부파일 삭제 버튼 클릭시, 태크에 담긴 data 정보를 읽어온 후 이벤트를 처리한다.
	$(".uploadResult").on("click", "button", function(e){
		console.log("delete button clicked");
		
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		
		// li 태그를 찾고 remove로 지워준다.
		var targetLi = $(this).closest("li");
		
		$.ajax({
			url : '/deleteFile',
			data : {fileName : targetFile, type : type},
			dataType : 'text',
			type : 'POST',
			success : function(result) {
				alert(result);
				targetLi.remove();
			}
		})
	});
	
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
	
	
	// 첨부파일 선택 시, register 페이지에서 업로드될 사진을 보여준다.
	// 서버에서 보내준 BoardAttachVO의 객체를 받아서 화면에 띄어주는 작업이다.
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
});
</script>

<%@include file="../includes/footer.jsp" %>