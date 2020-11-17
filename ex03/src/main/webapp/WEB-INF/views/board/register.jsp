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
	
	var formObj = $("form[role='form']");
	
	$("button[type='submit']").on("click", function(e){
		e.preventDefault();
		console.log("submit clicked");
		
		var str = "";
		
		$(".uploadResult ul li").each(function(index, object){
			var jobject = $(object);
			console.dir
		});
	});
	
	// change() 메서드는 해당 input에 값의 변경이 있을때 이벤트를 발생시킨다.  	
	$("input[type='file']").change(function(e) {
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		
		for (var i = 0; i < files.length; i++){
			if (!checkExtension(files[i].name, files[i].size)) {
				return false;
			}
			formData.append("uploadFile", files[i]);
		} // validation, 업로드 파일을 
		
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
	
	$(".uploadResult").on("click", "button", function(e){
		console.log("delete button clicked");
		
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		
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
	
	function showUploadResult(uploadResultArr) {
		if (!uploadResultArr || uploadResultArr == 0) { return; }
		
		var uploadUL = $(".uploadResult ul");
		var str = "";
		
		$(uploadResultArr).each(function(i, object){
			if (object.image) {
				var fileCallPath 
					= encodeURIComponent(object.uploadPath + "/s_" + object.uuid + "_" + object.fileName);
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
				var fileLink = fileCallPath.replace(new ReqExp(/\\/g), "/");
				
				str += "<li data-path='" + object.uploadPath + "' data-uuid='" + object.uuid + "'
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