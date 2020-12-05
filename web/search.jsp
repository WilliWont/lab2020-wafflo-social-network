<%-- 
    Document   : search
    Created on : Sep 17, 2020, 10:08:32 AM
    Author     : Will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/base.css">
        <link rel="stylesheet" href="css/search.css">
        <script src="js/jquery-latest-min.js"></script>
        <script src="js/user-notification.js"></script>
        <title>wafflo</title>
    </head>
    <body>
        
        <c:set var="searchValue" value="${fn:trim(param.txtSearch)}"/>
        <c:set var="articleTable" value="${requestScope.ARTICLE_RESULT}"/>
        <c:set var="pageNum" value="${param.thisPage}"/>
        <c:if test="${empty param.thisPage}">
            <c:set var="pageNum" value="${1}"/>
        </c:if>
        <c:set var="perPage" value="${20}"/>
        
        <div id="search-container">    
        <div id="search-header-container"> 
             <%--<c:if test="${empty articleTable}"> class ="search-header-full"</c:if>--%>

        <div id="search-header">
            <h1 id="welcome-container">
                <span id="welcome-msg">Hi</span> 
                <span id="welcome-name">${sessionScope.SES_USER.nameF}</span>
            </h1>
            <div id="btn-control-container">
                <form action="DispatchServlet" id="controlForm">
                    <input type="submit" value="Logout" name="btnAction"/>
                    <input type="submit" value="Make an Article" name="btnAction"/> 
                    <div id="notif-btn">
                    <div id='notif-amount'>0</div>
                    <input type="submit" value="Notification" name="btnAction">
                    </div>
                </form>
            </div>

        <form action="DispatchServlet" id="searchForm">
            <input type="text" name="txtSearch" value="${searchValue}" />
            <input type="submit" value="Search Article" name="btnAction"/>
        </form>
        
        </div>
        </div>

        <div id="search-content">
        <%--<c:if test="${not empty searchValue}">--%>
            <c:if test="${not empty articleTable}">
                <c:forEach var="dto" items="${articleTable}" varStatus="counter" end="${perPage-1}">
                    <div class="div-article">
                                                
                        <c:url value="DispatchServlet" var="addr">
                            <c:param name="txtArticleID" value="${dto.id}"/>
                            <c:param name="perPage" value="${perPage}"/>
                            <c:param name="txtSearch" value="${searchValue}"/>
                            <c:param name="thisPage" value="${pageNum}"/>
                            <c:param name="btnAction" value="View"/>
                        </c:url>
                        
                        <div class="article-img-container">
                            <a href="${addr}">
                                <img class="article-img" src="${dto.image}">
                            </a>
                        </div>
                        
                        <div class="article-info-container">
                            <h6 class="div-article-title">
                            <a href="${addr}">
                                ${dto.title}
                            </a>
                            </h6>

                            <div class="div-article-user">
                                ${dto.user.nameF} ${dto.user.nameL}
                            </div>

                            <div class="div-article-vote <c:if test="${dto.currentVote > 0}">vote-positive</c:if><c:if test="${dto.currentVote < 0}">vote-negative</c:if> ">
                                ${dto.currentVote}
                            </div>

                            <div class="div-article-date">
                                <fmt:formatDate value="${dto.postDate}" pattern="MMM dd, yyyy"/>
                            </div>
                        </div>

                        
                    </div> <!-- result block-->
                </c:forEach> <!--article code block-->
            <div class="btn-page-container">
                <form action="DispatchServlet">
                    <c:if test="${pageNum > 1}">
                        <input type="submit" value="Previous Page" name="btnAction"/>
                    </c:if>
                    
                    <c:if test="${fn:length(articleTable) > 20}">
                        <input type="submit" value="Next Page" name="btnAction"/>
                    </c:if>
                    <input type="hidden" name="perPage" value="${perPage}" />
                    <input type="hidden" name="txtSearch" value="${searchValue}" />
                    <input type="hidden" name="thisPage" value="${pageNum}" />
                </form> <!--paging code block-->
            </div>

            </c:if>
            <c:if test="${empty articleTable}">
                <h1 id="result-empty">no result</h1>
            </c:if>
        <%--</c:if>--%>
        </div>
        </div>
    </body>
</html>
