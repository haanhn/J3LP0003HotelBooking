<%-- 
    Document   : index
    Created on : 28-Nov-2019, 09:19:59
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
        
        <h2>Home Page</h2>
        
        <s:form action="searchHotels" method="GET">
            <s:select name="area" label="Area" list="areas" listKey="key" listValue="value"/>
            <s:textfield name="hotelName" label="Hotel Name" />
            <s:textfield name="fromDate" label="From Date" value="%{currentDate}" />
            <s:textfield name="toDate" label="To Date" value="%{currentDate}" />
            <s:select name="roomType" label="Room Type" list="roomTypes" listKey="key" listValue="value"/>
            <s:textfield name="roomAmount" label="Room Amount" value="1"/>
            
            <s:hidden name="currentDate" value="%{currentDate}"/>
            <s:submit value="Search"/>
        </s:form>
    </body>
</html>
