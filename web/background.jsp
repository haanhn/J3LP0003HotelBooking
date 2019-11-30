<%-- 
    Document   : background
    Created on : 28-Nov-2019, 14:52:25
    Author     : HaAnh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hotel Booking</title>
    </head>
    <body>
        <!--<s-->
        <h1><a href="/J3LP0002BookStore">Hotel Booking</a></h1>
        <s:if test="%{#session.currentUser != null}">
            <div class="box-welcome">
                Welcome <s:property value="%{#session.currentUser.fullname}"/>
            </div>
        </s:if>
        
        <ul class="list-top-navigation">
            <c:if test="${currentUser.roleId ne 'AD001'}">
                <li>
                    <a href="cart-detail.jsp">View Cart</a>
                </li>
            </c:if>
            <c:if test="${currentUser.roleId eq 'AD001'}">
                <li>
                    <c:url var="getAllBooksLink" value="/admin/getAllBooks">
                </c:url>
                <a href="${getAllBooksLink}">Admin Page</a>
                </li>
            </c:if>
            <s:if test="%{#session.currentUser != null}">
                <li>
                    <s:url  var="logOutLink" action="logOut"></s:url>
                    <s:a href="%{logOutLink}">Log Out</s:a>
                    <%--<s:iterator--%> 
                </li>
            </s:if>
            <s:if test="%{#session.currentUser == null}">
                <li>
                    <s:url var="pageLoginLink" value="login.jsp"></s:url>
                    <a href="${pageLoginLink}">Login</a>
                </li>
                <li>
                    <s:url var="registerLink" value="register.jsp"></s:url>
                    <s:a href="%{registerLink}">Register</s:a>
                    <!--???: Tai sao o day khong can #-->
                </li>
            </s:if>
        </ul>
    </body>
</html>
