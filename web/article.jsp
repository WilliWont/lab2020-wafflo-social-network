<%-- 
    Document   : article
    Created on : Sep 17, 2020, 8:05:48 PM
    Author     : Will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="tiennh.util.AccountHelper" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/base.css">
        <link rel="stylesheet" href="css/article.css">
        <script src="js/jquery-latest-min.js"></script>
        <c:set var="articleVote" value="${requestScope.ARTICLE_VOTE}"/>
        <c:set var="article" value="${requestScope.ARTICLE_REQ}"/>
        <c:set var="commentTable" value="${requestScope.ARTICLE_COMMENT}"/>
        <c:set var="adminRole" value="${AccountHelper.ADMIN_ROLE}"/>
        <c:set var="curUserRole" value="${sessionScope.SES_USER.role}"/>
        <c:set var="curUserID" value="${sessionScope.SES_USER.email}"/>
        <c:set var="deleteArticleError" value="${requestScope.DELETE_ARTICLE_ERROR}"/>
        <c:set var="deleteCmtError" value="${requestScope.DELETE_CMT_ERROR}"/>

        <script>
            let articleID = ${requestScope.ARTICLE_REQ.id};
            let articleVote = ${articleVote.articleVote};
            let pageVote = articleVote;
            let baseUpVote = ${articleVote.articleUpvoteAmount};
            let baseDownVote = ${articleVote.articleDownvoteAmount};
            if(articleVote === -1){
                baseDownVote++;
            } else if(articleVote === 1){
                baseUpVote--;
            }
        </script>
        <script src="js/article-vote.js"></script>
        <script src="js/article-cmt.js"></script>

        <title>wafflo - ${article.title}</title>
    </head>
    <body>
    <div id="body-container">
        <div id="btn-control-container">
            <form action="DispatchServlet">            
                <input type="submit" value="Return to Home" name="btnAction"/>

                <input type="hidden" name="perPage" value="${param.perPage}" />
                <input type="hidden" name="txtSearch" value="${param.txtSearch}" />
                <input type="hidden" name="thisPage" value="${param.thisPage}" />
                
            </form>
        </div>

        <div id="div-article-container">    
        <c:if test="${not empty article}">
        <div id="div-article-content-container">
        
        <div id="div-article-title-container">
        <h1>${article.title}</h1>
        </div>
        <div id="div-article-header-container">
            <div id="div-article-user-container">
                <p>posted by <span id="span-user">${article.user.nameF}</span> on 
                <span id="span-date">
                    <fmt:formatDate  
                    value="${article.postDate}" 
                    pattern="MMM dd, yyyy"/>
                </span>

                <c:if test="${(article.user.email == curUserID) || (curUserRole < article.user.role)}">
                    <c:url var="delete" value="DispatchServlet">
                        <c:param name="btnAction" value="Delete Article"/>
                        <c:param name="txtArticleID" value="${requestScope.ARTICLE_REQ.id}"/>
                    </c:url>
                    <a href="${delete}" class="delete-conf">delete</a>
                </c:if>
                    
                <c:if test="${not empty deleteArticleError}">
                    <div class="error-msg">${deleteArticleError.deleteError}</div>
                </c:if>
                
                </p>
            </div>
            <div id="div-article-vote-container">
            <button id="btn-Upvote" class="btn-vote 
                    <c:if test="${articleVote.articleVote > 0}">btn-upvote-clicked</c:if>">
                <img src="img/vote.svg" alt="">
            </button>
            <h5 id="upVoteValue" 
                <c:if test="${articleVote.articleVote > 0}">
                    class="upvote-clicked"
                </c:if>>${articleVote.articleUpvoteAmount}</h5>
            <h5 id="downVoteValue" 
                <c:if test="${articleVote.articleVote < 0}">
                    class="downvote-clicked"
                </c:if>>${articleVote.articleDownvoteAmount}</h5>

            <button id="btn-Downvote" class="btn-vote 
                <c:if test="${articleVote.articleVote < 0}">btn-downvote-clicked</c:if>">
                <img src="img/vote.svg" alt="">
            </button>
            <p id="msg-error"></p>
            </div>
        </div>
        <div id="div-article-desc-container">${article.description}</div>
        <div id="image-container">
            <c:set var="imageAddr" value="${requestScope.IMAGE_ADDR}"/>
            <c:if test="${article.imageStatus}">
                <img id="article-img" src="${article.image}">
            </c:if>
        </div>
        </div>
        <div id="div-article-cmt-container">

        <h3>Comment: </h3>
        <div id="div-article-cmt-form-container">
        <form action="DispatchServlet" method="POST" id="commentForm">
            <textarea name="txtComment" 
                form="commentForm" id="commentTextArea"></textarea>
            
            <input type="hidden" 
                   value="${requestScope.ARTICLE_REQ.id}" 
                   name="txtArticleID">
            
            <input type="submit" 
                   value="Post Comment" 
                   name="btnAction"/>
        </form> <!--post comment code block-->
        </div>
        <div id="div-comment">
            <c:url var="deleteTemplate" value="DispatchServlet">
                <c:param name="btnAction" value="Delete Comment"/>
                <c:param name="txtArticleID" value="${requestScope.ARTICLE_REQ.id}"/>
            </c:url>
            <div class="div-cmt-single" id="cmt-template">
                <div class="div-cmt-head">
                        <strong class="cmt cmt-owner"></strong>
                        <a href="${deleteTemplate}" class="delete-conf" 
                        onclick="return confirm('Are you sure you want to delete?');">delete</a>
                    </div>
                <div class="div-cmt-body"></div>
            </div>
            
            <c:if test="${not empty deleteCmtError}">
                <div class="error-msg">${deleteCmtError.deleteError}</div>
            </c:if>

            <c:if test="${not empty commentTable}">
                <c:forEach var="comment" items="${commentTable}" varStatus="counter">
                <div class="div-cmt-single">
                    <div class="div-cmt-head">
                    <c:if test="${comment.user.email == curUserID}">
                        <strong class="cmt cmt-owner">${comment.user.nameF} ${comment.user.nameL}: </strong>
                    </c:if>
                    
                    <c:if test="${comment.user.email != curUserID}">
                        <strong class="cmt">${comment.user.nameF} ${comment.user.nameL}: </strong>
                    </c:if>  
                    
                    <c:if test="${(comment.user.email == curUserID) || (curUserRole < comment.user.role)}">
                        <c:url var="delete" value="DispatchServlet">
                            <c:param name="btnAction" value="Delete Comment"/>
                            <c:param name="txtCmtID" value="${comment.id}"/>
                            <c:param name="txtArticleID" value="${requestScope.ARTICLE_REQ.id}"/>
                        </c:url>
                        <a href="${delete}" class="delete-conf">delete</a>
                    </c:if>
                    </div>
                    
                    <div class="div-cmt-body">
                        ${comment.description}
                    </div>

                </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty commentTable}">
                <h4 id="cmt-empty">no comments!</h4>
            </c:if>
        </div> <!--comment table code block-->
        </div>
        </c:if>
        <c:if test="${empty article}">
            > no article found
        </c:if>
        </div>
        </div>
                
        <script src="js/delete-conf.js"></script>
    
    </body>
</html>
