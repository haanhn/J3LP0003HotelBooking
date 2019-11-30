/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionContext;
import haanh.order.OrderDAO;
import haanh.order.OrderDTO;
import haanh.utils.UrlConstants;
import haanh.viewmodel.UserVM;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HaAnh
 */
public class GetMyOrdersAction {
    
    private List<OrderDTO> orders;
    
    public GetMyOrdersAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_SUCCESS;
        
        OrderDAO dao = new OrderDAO();
        orders = dao.getOrdersByUserId(getCurrentUser().getUserId());
        
        return result;        
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }
    
    public static UserVM getCurrentUser() {
        Map session = ActionContext.getContext().getSession();
        UserVM user = (UserVM) session.get(UrlConstants.ATTR_CURRENT_USER);
        return user;
    }
}
