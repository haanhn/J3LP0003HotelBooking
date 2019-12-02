/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import haanh.order.OrderDAO;
import haanh.utils.UrlConstants;

/**
 *
 * @author HaAnh
 */
public class CancelOrderAction {
    
    private Integer orderId;
    
    public CancelOrderAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_SUCCESS;
        
        OrderDAO dao = new OrderDAO();
        dao.cancelOrder(orderId);        
        
        return result;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
