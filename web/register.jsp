<%-- 
    Document   : register
    Created on : 29-Nov-2019, 09:48:26
    Author     : HaAnh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hotel Booking</title>
        <link href="css/myStyle.css" rel="stylesheet" type="text/css">
        <s:head/>
    </head>
    <body>
        <jsp:include page="background.jsp"/>
        
        <h2>Register</h2>
        <s:form action="register" method="POST">
            <s:if test="%{exception.message.indexOf('U_Email') > -1}">
                <div class="show-input-err">Email existed, please choose another!</div>
            </s:if>
            <s:textfield name="email" label="Email" />
            <s:password name="password" label="Password" />
            <s:password name="confirm" label="Confirm" />
            <s:textfield name="fullname" label="Fullname" />
            <s:textfield name="phone" label="Phone" />
            <s:textfield name="address" label="Address" />
            <s:submit value="Register"/>
        </s:form>
    </body>
</html>
