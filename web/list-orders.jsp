<%-- 
    Document   : list-orders
    Created on : 01-Dec-2019, 01:31:16
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

        <h2>My Orders</h2>
        <s:if test="orders != null && orders.isEmpty()==false">
            <table border="1" cellpadding="3">
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Booking Date</th>
                        <th>Booking Time</th>
                        <th>Total Bill</th>
                        <th>Status</th>
                        <th>Cancel Order</th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator value="orders">
                        <tr>
                            <td>
                                <s:property value="id"/>
                            </td>
                            <td><s:date name="bookingDate" format="MM/dd/yyyy"/></td>
                            <td><s:date name="fromDate" format="MM/dd/yyyy"/> - <s:date name="toDate" format="MM/dd/yyyy"/></td>
                            <td>$<s:property value="totalBill"/></td>
                            <td>
                                <s:if test="status == 0">
                                    <div class="active">Booked</div>
                                </s:if>
                                <s:elseif test="status == -1">
                                    <div class="inactive">Canceled</div>
                                </s:elseif>
                            </td>
                            <td>
                                <s:if test="status == 0">
                                    <s:url  var="cancelOrderLink" action="cancelOrder">
                                        <s:param name="orderId" value="id"/>
                                    </s:url>
                                    <s:a href="%{cancelOrderLink}">Cancel Order</s:a>
                                </s:if>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>

        </s:if>
    </body>
</html>
