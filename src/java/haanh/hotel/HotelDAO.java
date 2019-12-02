/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.hotel;

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
public class HotelDAO {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    public HotelDTO getHotelByHotelId(int hotelId) throws NamingException, SQLException {
        HotelDTO dto = new HotelDTO();
        try {
            con = DBUtils.getConnection();
            String sql = "select HotelId, HotelName, HotelPhoto, Address, AreaId from Hotel "
                    + "where HotelId = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, hotelId);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                dto.setId(rs.getInt("HotelId"));
                dto.setName(rs.getString("HotelName"));
                dto.setPhoto(rs.getString("HotelPhoto"));
                dto.setAddress(rs.getString("Address"));
                dto.setAreaId(rs.getInt("AreaId"));
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
    
    public List<HotelDTO> getHotelByHotelIds(List<Integer> hotelIds) throws NamingException, SQLException {
        List<HotelDTO> list = new ArrayList<>();
        try {
            con = DBUtils.getConnection();
            String sql = "select HotelId, HotelName, HotelPhoto, Address, AreaId from Hotel "
                    + "where HotelId ";
            sql = sql + getStringParamterInQuery(hotelIds.size());
            stm = con.prepareStatement(sql);
            int paramCount = 1;
            for (Integer id : hotelIds) {
                stm.setInt(paramCount, id);
                paramCount++;
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                HotelDTO dto = new HotelDTO();
                dto.setId(rs.getInt("HotelId"));
                dto.setName(rs.getString("HotelName"));
                dto.setPhoto(rs.getString("HotelPhoto"));
                dto.setAddress(rs.getString("Address"));
                dto.setAreaId(rs.getInt("AreaId"));
                
                list.add(dto);
                System.out.println("hotel Id " + dto.getId());
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
    
    private String getStringParamterInQuery(int totalParams) {
        StringBuilder s = new StringBuilder("");
        if (totalParams > 0) {
            s.append(" in (");
            boolean first = true;
            for (int i = 0; i < totalParams; i++) {
                if (first) {
                    s.append("?");
                    first = false;
                } else {
                    s.append(",?");
                }
            }
            s.append(")");
        }
        System.out.println(s.toString());
        return s.toString();
    }
}
