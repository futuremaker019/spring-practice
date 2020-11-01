<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upload with Ajax</title>
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
</style>
</head>
<body>
	<h1>Upload with Ajax</h1>
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	
	<button id="uploadBtn">Upload</button>
	
	<div class="uploadResult">
		<ul>
		</ul>
	</div>
	
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
<script>
	$(document).ready(function(){
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880; // 5MB
		var cloneObj = $(".uploadDiv").clone();
		var uploadResult = $(".uploadResult ul");
		
		function checkExtension(fileName, fileSize) {
			if(fileSize >= maxSize) {
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName)) {
				alert("해당 종류의 파일은 업로드 할 수 없습니다.");
				return false;
			}
			return true;
		}
		
		$("#uploadBtn").on("click", function(e){
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			
			//input의 file 타입은 files 메서드를 사요할 수 있는거 같다.
			var files = inputFile[0].files;
			
			console.log(files);
			
			//add filedata to formdata
			for(var i = 0; i < files.length; i++) {
				if (!checkExtension(files[i].name, files[i].size)) {
					return false;
				}
				
				formData.append("uploadFile", files[i]);
			}
			
			$.ajax({
				url : '/uploadAjaxAction',
				processData : false,
				contentType : false,
				data : formData,
				type : 'POST',
				dataType : 'json',
				success : function(result) {
					console.log(result);
					
					showUploadedFile(result);
					
					$(".uploadDiv").html(cloneObj.html());
				}
			});
			
			function showUploadedFile(uploadResultArr) {
				var str = "";
				
				$(uploadResultArr).each(function(i, obj){
					
					if (!obj.image) {
						str += "<li><img src='/resources/img/document.png'> " + obj.fileName + "</li>";
					} else {
						/* str += "<li>" + obj.fileName + "</li>"; */
						
						// 브라우저에서 GET 방식으로 첨부파일의 이름을 사용할 때에는 항상 파일 이름에 포함된 공백 문자나 한글 이름 등이 문제가 된다.
						// encoodeURIComponent()를 이용하여 URI 호출에 적합한 문자열로 인코딩 처리한다.
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
						str += "<li><img src='/display?fileName=" + fileCallPath + "'></li>";
					}
				});
				
				uploadResult.append(str);
			}
			
		});
	})
</script>
</body>
</html>