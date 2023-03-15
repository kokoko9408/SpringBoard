<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Upload with Ajax</h1>

	<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
}

.uploadResult ul li img {
	width: 100px;
}

.bigPictureWrapper {
	position: absolute;
	display: none;
	justify-content: center;
	align-items: center;
	top: 0%;
	width: 100%;
	height: 100%;
	background-color: gray;
	z-index: 100;
}

.bigPicture {
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
}

.bigPicture img {
	width: 600px;
}
</style>

	<div class='bigPictureWrapper'>
		<div class='bigPicture'></div>
	</div>


	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>

	<div class='uploadResult'>
		<ul></ul>
	</div>

	<button id='uploadBtn'>Upload</button>

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>






	<script>
		function showImage(fileCallPath) {
			alert(fileCallPath);

			$(".bigPictureWrapper").css("display", "flex").show();

			$(".bigPicture").html(
					"<img src='/display?fileName=" + encodeURI(fileCallPath)
							+ "'>").animate({ width : '100%', height : '100%'}, 1000);
		}

		$(".bigPictureWrapper").on("click", function(e) {
			$(".bigPicture").animate({ width : '0%', height : '0%'}, 1000);
			setTimeout(function() {
			$('.bigPictureWrapper').hide();},1000);	
			});
		
		$(".uploadResult").on("click","span", function(e){
			   
			  var targetFile = $(this).data("file");
			  var type = $(this).data("type");
			  console.log(targetFile);
			  
			  $.ajax({
			    url: '/deleteFile',
			    data: {fileName: targetFile, type:type},
			    dataType:'text',
			    type: 'POST',
			      success: function(result){
			         alert(result);
			       }
			  }); //$.ajax
			  
			});
			
			
		$(document)
				.ready(
						function() {
							$("#uploadBtn").on("click", function(e) {

								var formData = new FormData();
								//FormData : 가상의 <form>태그
								//Ajax를 이용하는 파일 업로드는 FormData를 이용해 필요한 파라미터를 담아서 전송하는 방식

								var inputFile = $("input[name='uploadFile']");

								var files = inputFile[0].files;

								console.log(files);

								//fileData를 forData에 추가한 뒤 Ajax를 통해 formData자체를 전송
								for (var i = 0; i < files.length; i++) {

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
										alert("업로드완료");
										console.log(result)

										showUploadedFile(result);

										$(".uploadDiv").html(cloneObj.html());

									}
								}); //$.ajax
							});

							/* 특정 크기 이상의 파일은 업로드할 수 없도록 제한하는 처리를 javaScript로 */
							var regex = new RegExp(
									"(.*?)\.(exe|sh|zip|alz|pptx)$");
							var maxSize = 5242880; //5MB

							function checkExtension(fileName, fileSize) {

								if (fileSize >= maxSize) {
									alert("파일 사이즈 초과");
									return false;
								}

								if (regex.test(fileName)) {
									alert("해당 종류의 파일은 업로드할 수 없습니다.");
									return false;
								}
								return true;
							}

							var cloneObj = $(".uploadDiv").clone();

							$("#uploadBtn")
									.on(
											"click",
											function(e) {

												var formData = new FormData();

												var formData = new FormData();

												var inputFile = $("input[name='uploadFile']");

												var files = inputFile[0].files;

												//console.log(files);

												for (var i = 0; i < files.length; i++) {

													if (!checkExtension(
															files[i].name,
															files[i].size)) {
														return false;
													}

													formData.append(
															"uploadFile",
															files[i]);

												}

											});

							var uploadResult = $(".uploadResult ul");

							function showUploadedFile(uploadResultArr) {

								var str = "";

								$(uploadResultArr)
										.each(
												function(i, obj) {

													if (!obj.image) { //이미지가 아닌 경우 아이콘을 보여주는 형태

														var fileCallPath = encodeURIComponent(obj.uploadPath+ "/"+ obj.uuid
																+ "_"+ obj.fileName);
													
														var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
														
														str += "<li><a href ='/download?fileName="
																+ fileCallPath
																+ "'>"
																+ "<img src = 'resources/img/59.png'>"
																+ obj.fileName
																+ "</a>"+"<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"+"<div></li>"
													} else {
														//str += "<li>" + obj.fileName + "</li>";
														var fileCallPath = encodeURIComponent( //URI호출에 적합한 문자열로 인코딩하기 위해
														obj.uploadPath + "/s_"
																+ obj.uuid
																+ "_"
																+ obj.fileName);

														var originPath = obj.uploadPath
																+ "\\"
																+ obj.uuid
																+ "_"
																+ obj.fileName;
														//업로드 경로와 UUID가 붙은 파일의 이름이 필요 -> originPath라는 변수 통해 하나의 문자열 생성 
														originPath = originPath
																.replace(
																		new RegExp(
																				/\\/g),
																		"/");

														str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+
											              "<img src='display?fileName="+fileCallPath+"'></a>"+
											              "<span data-file=\'"+fileCallPath+"\' data-type='image'> x </span>"+
											              "<li>";
											     }

												});
								uploadResult.append(str);
							}
						});
	</script>
</body>
</html>
