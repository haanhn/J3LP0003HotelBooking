/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.viewmodel;

import haanh.user.UserDTO;
import haanh.userprofile.UserProfileDTO;
import java.io.Serializable;

/**
 *
 * @author HaAnh
 */
public class UserVM implements Serializable {
    
    private String userId;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private Boolean active;
    private String roleId;

    public UserVM() {
    }
    
    public UserVM(UserDTO user, UserProfileDTO profile) {
        if (user != null && profile != null) {
            this.userId = user.getUserId();
            this.email = user.getEmail();
            this.fullname = profile.getFullname();
            this.phone = profile.getPhone();
            this.address = profile.getAddress();
            this.active = user.getActive();
            this.roleId = user.getRoleId();
        }
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
            
}
