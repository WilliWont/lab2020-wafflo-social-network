<%-- 
    Document   : register
    Created on : Sep 16, 2020, 8:06:26 PM
    Author     : Will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/base.css">
        <link rel="stylesheet" href="css/register.css">
        <title>wafflo</title>
    </head>
    <body>
    <div id='regi'>
    <div id='regiForm'>
        <h1>registration</h1>
        <c:set var="errorMsg" value="${requestScope.REG_ERROR}"/>
        <form action="DispatchServlet" method="POST">
                        
        <div class="input-container">
            
            <div class="input-name">
                <div class="input-group">
                    <input type="text" name="txtNameF" id="form-nameF" 
                    value="${fn:trim(param.txtNameF)}"/>
                    <label for="form-nameF">First Name</label>
                    <c:if test="${not empty errorMsg.firstNameError}">
                    <span class="msg-error">
                        ${errorMsg.firstNameError}
                    </span>
                    </c:if>
                </div>

                <div class="input-group">
                    <input type="text" name="txtNameL" id="form-nameL" 
                    value="${fn:trim(param.txtNameL)}"/>
                    <label for="form-nameL">Last Name</label>
                    <c:if test="${not empty errorMsg.lastNameError}">
                    <span class="msg-error">
                        ${errorMsg.lastNameError}
                    </span>
                    </c:if>
                </div>
            </div>
            
            <div class="input-group">
                <input type="text" name="txtEmail" id="form-email" 
                value="${fn:trim(param.txtEmail)}"/>
                <label for="form-email">Email Address</label>
                <c:if test="${not empty errorMsg.emailError}">
                <span class="msg-error">
                    ${errorMsg.emailError}
                </span>
                </c:if>
            </div>
                
            <div class="input-group">
                <input type="password" name="txtPassword" id="form-pw" 
                value=""/>
                <label for="form-pw">Password</label>
                <c:if test="${not empty errorMsg.passwordError}">
                <span class="msg-error">
                    ${errorMsg.passwordError}
                </span>
                </c:if>
            </div>
                
            <div class="input-group">
                <input type="password" name="txtPasswordConf" id="form-pwConf" 
                value=""/>
                <label for="form-pwConf">Repeat Password:</label>
                <c:if test="${not empty errorMsg.passwordConfError}">
                <span class="msg-error">
                    ${errorMsg.passwordConfError}
                </span>
                </c:if>
            </div>
                
        </div>
                <div class="form-btn-container">
                    <input type="submit" value="Register Account" name="btnAction" class="form-btn" id='btn-register'>
                    <a href="login.html">return to login</a>
                </div>
        </form>
    </div>    
    </div>
    </body>
</html>
