/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionContext;
import haanh.utils.UrlConstants;
import java.util.Map;

/**
 *
 * @author HaAnh
 */
public class LogOutAction {
    
    public LogOutAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_SUCCESS;
        
//        SessionMap
        Map session = ActionContext.getContext().getSession();
        if (session != null) {
            session.remove(UrlConstants.ATTR_CURRENT_USER);
        }
        
        return result;
    }
    
}
