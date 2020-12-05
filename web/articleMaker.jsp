<%-- 
    Document   : articleMaker
    Created on : Sep 17, 2020, 8:06:09 PM
    Author     : Will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>wafflo</title>
        <link rel="stylesheet" href="css/base.css">
        <link rel="stylesheet" href="css/post.css">
    </head>
    <body>
        <div id="body-container">
            <div id="header-container">
            <h1>ARTICLE MAKER</h1>
            <c:set var="postError" value="${requestScope.POST_ERROR}"/>
            <c:if test="${not empty postError.articleError}">
                <span class="msg-error">${postError.articleError}</span>
            </c:if>
            </div>
            <div id="form-container">
            <form action="DispatchServlet" method="POST" id="textForm" enctype='multipart/form-data'>
                <div id="title-container">
                Title
                <br>
                <input type="text" name="txtPostTitle" value="${fn:trim(param.txtPostTitle)}"/>
                <c:if test="${not empty postError.articleTitleError}">
                    <span class="msg-error">${postError.articleTitleError}</span>
                </c:if>
                </div>
                <div id="content-container">
                Content
                <br>
                <textarea name="txtPostContent" form="textForm">${fn:trim(param.txtPostContent)}</textarea>
                <c:if test="${not empty postError.articleContentError}">
                    <span class="msg-error">${postError.articleContentError}</span>
                </c:if>
                <input type="file" name="txtImage" accept="image/*"/>
                <c:if test="${not empty postError.articleImageError}">
                    <span class="msg-error">${postError.articleImageError}</span>
                </c:if>
                </div>
                <div id="btn-container">
                <input type="submit" value="Return to Home" name="btnAction"/>
                <input type="submit" value="Post Article" name="btnAction"/>
                </div>
            </form>
            </div>
        </div>
    </body>
</html>
