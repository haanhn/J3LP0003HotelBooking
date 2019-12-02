<%-- 
    Document   : list-room-infos-of-hotel
    Created on : 02-Dec-2019, 07:24:51
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
        
        <h2>Hotel Rooms</h2>
        <table border="0" cellpadding="3">
            <tr>
                <th>Hotel Name</th>
                <td>
                    <s:property value="hotel.name"/>
                </td>
            </tr>
            <tr>
                <th>Address</th>
                <td>
                    <s:property value="hotel.address"/>
                </td>
            </tr>
        </table>
        
        <s:if test="%{infos != null && infos.size()>0}">
            <table border="1" cellpadding="3">
                <thead>
                    <tr>
                        <th>Room Name</th>
                        <th>Description</th>
                        <th>Add to cart</th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator value="infos">
                        <tr>
                            <td>
                                <s:property value="name"/>
                            </td>
                            <td>
                                <s:property value="description"/>
                            </td>
                            <td>
                                Add to cart
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>

        </s:if>
    </body>
</html>
