/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.userprofile;

import java.sql.Date;

/**
 *
 * @author HaAnh
 */
public class UserProfileDTO {
    
    private String userId;
    private String fullname;
    private String phone;
    private String address;
    private Date createdDate;

    public UserProfileDTO() {
    }

    public UserProfileDTO(String userId, String fullname, String phone, String address, Date createdDate) {
        this.userId = userId;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
}
