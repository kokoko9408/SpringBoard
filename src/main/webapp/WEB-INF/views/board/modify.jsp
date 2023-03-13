<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../includes/header.jsp"%>

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
			<div class="panel-heading">Board Modify Page</div>
			<!-- /.panel-heading -->
			<div class="panel-body">

				<form role="form" action="/board/modify" method="post">

					<input type="hidden" name="pageNum" value="${cri.pageNum} "/>
					<input type="hidden" name="amount" value="${cri.amount} "/>



					<div class="form-group">
						<label>Bno</label> <input name="bno" class="form-control"
							value="${board.bno}" readonly="readonly">
					</div>

					<div class="form-group">
						<label>Title</label> <input name="title" class="form-control"
							value="${board.title}">
					</div>

					<div class="form-group">
						<label>Text Area</label>
						<textarea rows="3" cols="" name="content" class="form-control">
                           ${board.content}
                        </textarea>
					</div>

					<div class="form-group">
						<label>Writer</label> <input name="writer" class="form-control"
							value="${board.writer}" readonly="readonly">
					</div>

					<div class="form-group">
						<!-- <label>Update Date</label> -->
						<input type="hidden" name="updateDate" class="form-control"
							value='<fmt:formatDate pattern = "yyyy/MM/dd" value="${board.updateDate}"/>'
							readonly="readonly">
					</div>


					<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
					<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
					<button type="submit" data-oper='list' class="btn btn-info">List</button>



				</form>

			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<script>
	$(document).ready(function() {
		var formObj = $("form");

		$("button").on("click", function(e) {
			e.preventDefault();
			var operation = $(this).data("oper"); //data-oper(modify,remove,list 중에서)

			if (operation === 'remove') {
				formObj.attr("action", "/board/remove");
			} else if (operation === 'list') {
				//self.location = "/board/list"; //post방식
				formObj.attr("action", "/board/list").attr("method", "get");
				
				var pageNumTag = $("input[name='pageNum']").clone();
				var amountTag = $("input[name='amount']").clone();
				
				formObj.empty();
				formObj.append(pageNumTag);
				formObj.append(amountTag);
				
			}
			formObj.submit();
		});
		//alert("수정완료"); "수정완료"라고 창이 뜸

	});
</script>


<%@ include file="../includes/footer.jsp"%>