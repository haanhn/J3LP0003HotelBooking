<%-- 
    Document   : login
    Created on : 28-Nov-2019, 13:07:39
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
        
        <h2>Page Login</h2>
        <s:form action="login" method="POST">
            <s:textfield label="Email" name="email"/>
            <s:password label="Password" name="password"/>
            <s:submit value="Login"/>
        </s:form>
        
        <s:if test="%{#request.message != null}">
            <div class="message">
                ${requestScope.message}
            </div>
        </s:if>
    </body>
</html>
