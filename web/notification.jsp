<%-- 
    Document   : notification
    Created on : Sep 27, 2020, 12:29:04 AM
    Author     : Will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>wafflo</title>
        <link rel="stylesheet" href="css/base.css">
        <link rel="stylesheet" href="css/notification.css">
    </head>
    <body>
        <div id="notif-container">
        <h1>Notification</h1>
        <form action="DispatchServlet">
        <input type="submit" value="Return to Home" name="btnAction"/>
        </form>
        <div id="vote-cmt-container">
        <div id="vote-container">
        <h2>Votes</h2>
        <c:set var="voteList" value="${requestScope.VOTE_NOTIF}"/>
        <c:if test="${not empty voteList}">
            <c:forEach var="vote" items="${voteList}">
                <c:url var="postAddr" value="DispatchServlet">
                    <c:param name="btnAction" value="View"/>
                    <c:param name="txtArticleID" value="${vote.id}"/>
                </c:url>
                <div class="div-notif">
                <a href="${postAddr}" class="post-link">
                    ${vote.currentVote} people voted on your post 
                    <span class="post-title">${vote.title}</span>
                </a>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty voteList}">
            <span class="notif-empty">no new votes</span>
        </c:if>
        </div>
        <div id="cmt-container">
        <h2>Comment</h2>
        <c:set var="cmtList" value="${requestScope.CMT_NOTIF}"/>
        <c:if test="${not empty cmtList}">
            <c:forEach var="cmt" items="${cmtList}">
                <c:url var="postAddr" value="DispatchServlet">
                    <c:param name="btnAction" value="View"/>
                    <c:param name="txtArticleID" value="${cmt.article.id}"/>
                </c:url>
                <div class="div-notif">
                <a href="${postAddr}" class="post-link">
                    ${cmt.user.nameF} ${cmt.user.nameL} commented on your post 
                    <span class="post-title">${cmt.article.title}</span>
                    <br>
                    at <fmt:formatDate value="${cmt.postDate}" pattern="HH:mm 'on' MMM dd YYYY"/>
                <br>
                <span class="post-comment">${cmt.description}</span>
                </a>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty cmtList}">
            <span class="notif-empty">no new comments</span>
        </c:if>
        </div>
        </div>
        </div>
    </body>
</html>
