<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>

<div class="container">

    <c:forEach var="board" items="${boards}">
        <div class="card m-2">
            <div class="card-body">
                <h4 class="card-title">${board.title}</h4>
                <p class="font-italic" style="text-align:right">작성자: ${board.user.username}</p>
                <hr />
                <p class="card-text">${board.content}</p>
                <a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
            </div>
        </div>
    </c:forEach>

</div>

<%@ include file="layout/footer.jsp"%>