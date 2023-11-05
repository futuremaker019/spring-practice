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
	
	<div class="bigPictureWrapper">
		<div class="bigPicture">
		</div>
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
		});
		
		function showUploadedFile(uploadResultArr) {
			var str = "";
			
			$(uploadResultArr).each(function(i, obj){
				if (!obj.image) {
					var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);

					str += "<li><a href='/download?fileName=" + fileCallPath + "'>"
						+ "<img src='/resources/img/document.png'>" + obj.fileName + "</a></li>";
				} else {
					var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
					originPath = originPath.replace(new RegExp(/\\/g), "/")
					
					str += "<li><a href=\"javascript:showImage(\'" + originPath + "\')\"><img src='/display?fileName'"
							+ fileCallPath + "'></a><li>";

					
					str += "<li><a href='/download?fileName=" + fileCallPath + "'>" 
						+ "<img src='/resources/img/document.png'>" + obj.fileName + "</a></li>";
						
					
				} else {
					// 브라우저에서 GET 방식으로 첨부파일의 이름을 사용할 때에는 항상 파일 이름에 포함된 공백 문자나 한글 이름 등이 문제가 된다.
					// encoodeURIComponent()를 이용하여 URI 호출에 적합한 문자열로 인코딩 처리한다.
					var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					var originalPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
					
					//  결로 구분자(\\)를 "/"로 바꾸는 코드 (정규 표현식을 이용했다.) 
					originalPath = originalPath.replace(new RegExp(/\\/g), "/");
					
					str += "<li><a href=\"javascript:showImage(\'" + originalPath + "\')\"><img src='/display?fileName=" 
						+ fileCallPath + "'></a><li>";
				}
			});
			uploadResult.append(str);
		}
		
		function showImage(fileCallPath) {

			alert(fileCallPath);

			// alert(fileCallPath);
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture")
				.html("<img src='/display?fileName=" + encodeURI(fileCallPath) +"'>")
				.animate({width:'100%', height:'100%'}, 100)
				
			$(".bigPictureWrapper").on("click", function(e){
				$(".bigPicture").animate({width : '0%', height : '0%'}, 1000);
				setTimeout(function(){
					$(".bigPictureWrapper").hide();
				}, 100);
			});
		}
	})
	// document-ready-end
</script>
</body>
</html>