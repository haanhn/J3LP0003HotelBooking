/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.userprofile;

import haanh.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class UserProfileDAO {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    public UserProfileDTO findByUserId(String userId) throws NamingException, SQLException {
        UserProfileDTO dto = null;
        try {
            con = DBUtils.getConnection();
            String sql = "select Fullname, Phone, Address from UserProfile "
                    + "where UserProfile.UserId=?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                dto = new UserProfileDTO();
                dto.setUserId(userId);
                dto.setFullname(rs.getString("Fullname"));
                dto.setPhone(rs.getString("Phone"));
                dto.setAddress(rs.getString("Address"));
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public boolean insertUser(UserProfileDTO dto) throws SQLException, NamingException {
        boolean result = false;
        try {
            con = DBUtils.getConnection();
            String sql = "insert into UserProfile(UserId, Fullname, Phone, Address, CreatedDate) "
                    + "values(?,?,?,?,?)";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getUserId());
            stm.setString(2, dto.getFullname());
            stm.setString(3, dto.getPhone());
            stm.setString(4, dto.getAddress());
            stm.setDate(5, dto.getCreatedDate());
            
            int row = stm.executeUpdate();
            if (row > 0) {
                result = true;
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    
    private void closeConnection() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stm != null) {
            stm.close();
        }
        if (con != null) {
            con.close();
        }
    }
}
