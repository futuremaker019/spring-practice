<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>
	
	<div class='uploadResult'>
		<ul>
		</ul>
	</div>
	
	<div class='bigPictureWrapper'> 
		<div class='bigPicture'>
		</div>
	</div>
	
	<button id='uploadBtn'>Upload</button>

<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
<script>
	function showImage(originPath) {
		// alert(fileCallPath);
		console.log(originPath);
		
		$(".bigPictureWrapper").css("display", "flex").show();
		
		$(".bigPicture")
			.html("<img src='/display?fileName=" + encodeURI(originPath) +"'>")
			.show();
			
			/* .animate({width:'100%', height:'100%'}, 0); */
			
		$(".bigPictureWrapper").on("click", function(e){
			$(".bigPictureWrapper").hide();
			
			/* $(".bigPicture").animate({width : '0%', height : '0%'}, 0);
			setTimeout(function(){
				
			}, 0); */
		});
	}

	$(document).ready(function(){
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880;  // 5MB
		
		var cloneObj = $(".uploadDiv").clone();
		
		var uploadResult = $(".uploadResult ul");
		
		function checkExtension(fileName, fileSize) {
			if (fileSize >= maxSize) {
				alert("파일 사이즈 초과");
				return false;
			}
			
			if (regex.test(fileName)) {
				alert("해당 종류의 파일은 업로드 할 수 없습니다.");
				return false;
			}
			return true;
		}
		
		$("#uploadBtn").on("click", function(e){
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			console.log(files);
			
			// add filedata to formdata
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
				success : function(result){
					console.log(result);
					
					showUploadedFile(result);
					
					$(".uploadDiv").html(cloneObj.html());
				}
			}); // ajax end 
		}); // uploadBtn end
		
		$(".uploadResult").on("click", "span", function(e){
			var filePath = $(this).data("path");
			var type = $(this).data("type");
			console.log(filePath);
			
			$.ajax({
				url : '/deleteFile',
				data : {fileName: filePath, type : type},
				dateType : 'text',
				type : 'POST',
				success : function(result) {
					alert(result);
				}
			}); // delete file ajax end
		}); // uploadResult end
		
		function showUploadedFile(uploadResultArr) {
			var str = "";
			$(uploadResultArr).each(function(i, obj){
				if (!obj.image) {
					var fileCallPath 
						= encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
					
					str += "<li><div><a href='/download?fileName=" + fileCallPath + "'>"
						+ "<img src='/resources/img/document.png'>" + obj.fileName + "</a>"
						+ "<span data-path=\'" + fileCallPath + "\' data-type='file'> x </span></div></li>";
				} else {
					var fileCallPath 
						= encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
					originPath = originPath.replace(new RegExp(/\\/g), "/");
					
					console.log("originPath : " + originPath);
					
					str += "<li><a href=\"javascript:showImage(\'" + originPath +"\')\">"
						+ "<img src='/display?fileName=" + fileCallPath + "'></a>"
						+ "<span data-path=\'" + fileCallPath + "\' data-type='image'> x </span></li>";
				}
			});
			uploadResult.append(str);
		} // end showUploadedFile
	});
</script>

</body>
</html>