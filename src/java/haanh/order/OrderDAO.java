/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.order;

import haanh.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class OrderDAO {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    public List<OrderDTO> getOrdersByUserId(String userId) throws NamingException, SQLException {
        List<OrderDTO> list = new ArrayList<>();
        try {
            String sql = "select Id, BookingDate, FromDate, ToDate, TotalBill, Status from [Order] "
                    + "where UserId=? "
                    + "order by BookingDate desc";
            con = DBUtils.getConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                OrderDTO dto = new OrderDTO();
                dto.setId(rs.getInt("Id"));
                dto.setBookingDate(rs.getDate("BookingDate"));
                dto.setFromDate(rs.getDate("FromDate"));
                dto.setToDate(rs.getDate("ToDate"));
                dto.setTotalBill(rs.getDouble("TotalBill"));
                dto.setStatus(rs.getInt("Status"));
                dto.setUserId(userId);
                
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        
        return list;
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
