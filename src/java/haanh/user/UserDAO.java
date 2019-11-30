/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.user;

import haanh.utils.DBUtils;
import haanh.utils.DataUtils;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class UserDAO {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    public UserDTO login(String email, String password) throws NoSuchAlgorithmException, SQLException, NamingException {
        UserDTO dto = null;
        try {
            con = DBUtils.getConnection();
            String sql = "select UserId, RoleId from [User] "
                    + "where [User].Email=? and [User].Password=? and [User].Active=?";
            
            String hashedPassword = DataUtils.getSHA256HashedString(password);
            stm = con.prepareStatement(sql);
            stm.setString(1, email);
            stm.setString(2, hashedPassword);
            stm.setBoolean(3, true);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                dto = new UserDTO();
                dto.setUserId(rs.getString("UserId"));
                dto.setEmail(email);
                dto.setActive(true);
                dto.setRoleId(rs.getString("RoleId"));
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public boolean insertUser(UserDTO dto) throws NoSuchAlgorithmException, SQLException, NamingException {
        boolean result = false;
        try {
            con = DBUtils.getConnection();
            String sql = "insert into [User](UserId, Email, Password, Active, RoleId) "
                    + "values(?,?,?,?,?)";
            
            String hashedPassword = DataUtils.getSHA256HashedString(dto.getPassword());
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getUserId());
            stm.setString(2, dto.getEmail());
            stm.setString(3, hashedPassword);
            stm.setBoolean(4, dto.getActive());
            stm.setString(5, dto.getRoleId());
            
            int row = stm.executeUpdate();
            if (row > 0) {
                result = true;
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    
    public boolean checkUserIdExist(String userId) throws NamingException, SQLException {
        boolean existed = false;
        try {
            String sql = "select UserId from [User] where UserId=?";
            con = DBUtils.getConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            rs = stm.executeQuery();
            if (rs.next()) {
                existed = true;
            }
        } finally {
            closeConnection();
        }
        return existed;
    }
    
    public boolean checkEmailExist(String email) throws NamingException, SQLException {
        boolean existed = false;
        try {
            String sql = "select UserId from [User] where Email=?";
            con = DBUtils.getConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, email);
            rs = stm.executeQuery();
            if (rs.next()) {
                existed = true;
            }
        } finally {
            closeConnection();
        }
        return existed;
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
