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
        <s:url  var="homeLink" action="pageHome"></s:url>
        <h1>
            <s:a href="%{homeLink}">Hotel Booking</s:a>
        </h1>
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
            <s:if test="%{#session.currentUser != null}">
                <li>
                    <s:url  var="getOrderLink" action="getMyOrders"></s:url>
                    <s:a href="%{getOrderLink}">Booking history</s:a>
                </li>
                <li>
                    <s:url  var="logOutLink" action="logOut"></s:url>
                    <s:a href="%{logOutLink}">Log Out</s:a>
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
                </li>
            </s:if>
        </ul>
    </body>
</html>
