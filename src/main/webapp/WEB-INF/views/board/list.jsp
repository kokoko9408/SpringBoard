<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="../includes/header.jsp"%>


<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">게시판</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				노래게시판
				<button id="regBtn" type="button"
					class="btn btn-info btn-xs pull-right">등록하기</button>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<table width="100%"
					class="table table-striped table-bordered table-hover">
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
                     <tr class="odd gradeX">
                        <td><c:out value="${board.bno }" /></td>
                        <%-- <td><a href='/board/get?bno=<c:out value="${board.bno}"/>'><c:out value="${board.title}"/></a></td> --%>
                        <%-- <td><a class='move' href='<c:out value="${board.bno}"/>'><c:out value="${board.title}" /></a></td> --%>
                        <td><a class='move' href='<c:out value="${board.bno}"/>'><c:out value="${board.title}" /> <b>[ <c:out value="${board.replyCnt }" /> ]</b></a></td>
                        <td><c:out value="${board.writer }" /></td>
                        <td class="center"><fmt:formatDate pattern="yyyy-MM-dd"
                              value="${board.regdate }" /></td>
                        <td class="center"><fmt:formatDate pattern="yyyy-MM-dd"
                              value="${board.updateDate }" /></td>
                     </tr>
                  </c:forEach>
               </tbody>
				</table>

				<!-- 검색 조건 시작 -->


				<div class='row'>
					<div class="col-lg-12">

						<form id='searchForm' action="/board/list" method='get'>
							<select name='type'>
								<option value=""
									<c:out value="${pageMaker.cri.type == null?'selected':''}"/>>--</option>
								<option value="T"
									<c:out value="${pageMaker.cri.type eq 'T'?'selected':''}"/>>제목</option>
								<option value="C"
									<c:out value="${pageMaker.cri.type eq 'C'?'selected':''}"/>>내용</option>
								<option value="W"
									<c:out value="${pageMaker.cri.type eq 'W'?'selected':''}"/>>작성자</option>
								<option value="TC"
									<c:out value="${pageMaker.cri.type eq 'TC'?'selected':''}"/>>제목
									or 내용</option>
								<option value="TW"
									<c:out value="${pageMaker.cri.type eq 'TW'?'selected':''}"/>>제목
									or 작성자</option>
								<option value="TWC"
									<c:out value="${pageMaker.cri.type eq 'TWC'?'selected':''}"/>>제목
									or 내용 or 작성자</option>
							</select> <input type='text' name='keyword'
								value='<c:out value="${pageMaker.cri.keyword}"/>' /> <input
								type='hidden' name='pageNum'
								value='<c:out value="${pageMaker.cri.pageNum}"/>' /> <input
								type='hidden' name='amount'
								value='<c:out value="${pageMaker.cri.amount}"/>' />
							<!-- BoardController의 list에서 Criteria cri의 변수가 받아줌. -->
							<button class='btn btn-default'>Search</button>
						</form>
					</div>
				</div>

				<!-- 검색 조건 끝 -->


				<!-- 페이지 처리 시작  -->
				<div class='pull-right'>
					<ul class="pagination">

						<c:if test="${pageMaker.prev}">
							<li class="paginate_button previous"><a
								href="${pageMaker.startPage-1}">Previous</a></li>
						</c:if>

						<c:forEach var="num" begin="${pageMaker.startPage}"
							end="${pageMaker.endPage}">
							<li class="paginate_button ${pageMaker.cri.pageNum == num?"active" :""} ">
								<!-- 삼항연산자 --> <a href="${num}">${num}</a>
							</li>
						</c:forEach>

						<c:if test="${pageMaker.next}">
							<li class="paginate_button next"><a
								href="${pageMaker.endPage+1}">Next</a></li>
						</c:if>


					</ul>
				</div>


				<%--   var actionForm = $("#actionForm");

                  $(".paginate_button a").on("click",   function(e) {

                           e.preventDefault(); //동작하지마

                           console.log('click');

                           actionForm.find("input[name='pageNum']").val($(this).attr("href"));
                           actionForm.submit();                      "${pageMaker.cri.pageNum} }"
                        });
             --%>


				<!-- 페이지 처리 끝 -->
				<form id="actionForm" action="/board/list" method="get">
					<input type="hidden" name="pageNum"
						value="${pageMaker.cri.pageNum} "> <input type="hidden"
						name="amount" value="${pageMaker.cri.amount} ">
				</form>
				<!-- 페이지번호를 클릭을 하면 링크를 통해가는게 아니라 폼태그를 submit을 날려서 처리할려고 함. 선택하는 번호에 따라서 pageNum부분이 바껴야함 -->
				<!-- Modal -->
				<div class="modal fade" id="myModal" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">Modal Header</h4>
							</div>
							<div class="modal-body">
								<p>Some text in the modal.</p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary">Save
									Changes</button>
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
							</div>
						</div>

					</div>
				</div>

			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var result = '<c:out value="${result}"/>';

						checkModal(result);

						history.replaceState({}, null, null); //주소창 클리어

						function checkModal(result) {

							if (result === '' || history.state) {
								return;
							}

							if (parseInt(result) > 0) {
								$(".modal-body").html(
										"게시글 " + parseInt(result)
												+ " 번이 등록되었습니다.");
							}

							$("#myModal").modal("show");
						}

						$("#regBtn").on("click", function() {

							self.location = "/board/register";

						});

						var actionForm = $("#actionForm");

						$(".paginate_button a").on(
								"click",
								function(e) {

									e.preventDefault();

									console.log('click');
									actionForm.attr("action", "/board/list");
									actionForm.find("input[name='pageNum']")
											.val($(this).attr("href"));
									actionForm.submit();
								});
						$(".move")
								.on(
										"click",
										function(e) {
											e.preventDefault();
											actionForm
													.find("input[name='bno']")
													.remove();
											actionForm
													.append("<input type='hidden' name='bno' value='"
															+ $(this).attr(
																	"href")
															+ "'>");
											actionForm.attr("action",
													"/board/get");
											actionForm.submit();
										});
						
						
						var searchForm = $("#searchForm");

		                  $("#searchForm button").on("click",   function(e) {

		                           if (!searchForm.find("option:selected").val()) {
		                              alert("검색종류를 선택하세요");
		                              return false;
		                           }

		                           if (!searchForm.find("input[name='keyword']").val()) {
		                              alert("키워드를 입력하세요");
		                              return false;
		                           }

		                           searchForm.find("input[name='pageNum']").val("1");
		                           e.preventDefault();

		                           searchForm.submit();

		                  });
					});

</script>

<%@include file="../includes/footer.jsp"%>
