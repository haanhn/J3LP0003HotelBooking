/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import haanh.user.UserDAO;
import haanh.user.UserDTO;
import haanh.userprofile.UserProfileDAO;
import haanh.userprofile.UserProfileDTO;
import haanh.utils.UrlConstants;
import haanh.viewmodel.UserVM;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
/**
 *
 * @author HaAnh
 */
public class LoginAction extends ActionSupport{
    
    private String email;
    private String password;
    
    public LoginAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_FAIL;       
        
        UserDAO userDAO = new UserDAO();
        UserProfileDAO profileDAO = new UserProfileDAO();
        
        UserDTO user = userDAO.login(email, password);
        
        if (user != null) {
            UserProfileDTO profile = profileDAO.findByUserId(user.getUserId());
            if (profile != null) {
                UserVM userVM = new UserVM(user, profile);
                Map session = ActionContext.getContext().getSession();
                
                session.put(UrlConstants.ATTR_CURRENT_USER, userVM);                
                result = UrlConstants.STRUTS2_RESULT_SUCCESS;
            }
        } else {
            ServletActionContext.getRequest().setAttribute(UrlConstants.ATTR_MESSAGE, "Wrong email or password");
        }
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
