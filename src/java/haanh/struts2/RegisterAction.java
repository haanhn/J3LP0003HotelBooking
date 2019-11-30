/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionSupport;
import haanh.user.UserDAO;
import haanh.user.UserDTO;
import haanh.userprofile.UserProfileDAO;
import haanh.userprofile.UserProfileDTO;
import haanh.utils.DBUtils;
import haanh.utils.UrlConstants;
import java.sql.Date;
import java.sql.SQLException;
import java.util.UUID;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class RegisterAction extends ActionSupport {
    
    private String email;
    private String password;
    private String confirm;
    private String fullname;
    private String phone;
    private String address;
    
    public RegisterAction() {
    }
    
    @Override
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_FAIL; 
        
        UserDAO userDAO = new UserDAO();
        UserProfileDAO profileDAO = new UserProfileDAO();
        
        String roleId = DBUtils.ROLE_CUSTOMER;
        String userId = createRandomUserId();
        Date createdDate = new Date(System.currentTimeMillis());
        
        UserDTO user = new UserDTO(userId, email, password, true, roleId);
        UserProfileDTO profile = new UserProfileDTO(userId, fullname, phone, address, createdDate);
        
        boolean resultUser = userDAO.insertUser(user);
        if (resultUser) {
            profileDAO.insertUser(profile);
            result = UrlConstants.STRUTS2_RESULT_SUCCESS;
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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    private String createRandomUserId() throws NamingException, SQLException {
        String userId = UUID.randomUUID().toString();
        UserDAO dao = new UserDAO();
        boolean existed = true;
        
        while (existed) {
            existed = dao.checkUserIdExist(userId);
            if (existed) {
                userId = UUID.randomUUID().toString();
            }
        }
        return userId;
    }
}
