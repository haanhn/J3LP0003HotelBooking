/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.roominformation;

import haanh.room.RoomDTO;
import haanh.utils.DBUtils;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class RoomInformationDAO {

    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;

    public Map<Integer, RoomInformationDTO> getMapRoomInfoByHotelAndRoomType(
            Integer areaId, String hotelName,
            Integer roomTypeId) throws NamingException, SQLException {
        Map<Integer, RoomInformationDTO> map = new HashMap<>();
        try {
            con = DBUtils.getConnection();
            
            String sql = "select ri.Id, ri.HotelId, ri.TotalRooms from RoomInformation as ri "
                    + "where "
                    + "ri.RoomTypeId=? "
                    + "and ri.HotelId in (select h.HotelId from Hotel as h where h.HotelName like ? and h.AreaId=?) ";
            
            stm = con.prepareStatement(sql);
            stm.setInt(1, roomTypeId);
            stm.setString(2, "%" + hotelName + "%");
            stm.setInt(3, areaId);
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                RoomInformationDTO dto = new RoomInformationDTO();
                dto.setId(rs.getInt("Id"));
                dto.setHotelId(rs.getInt("HotelId"));
                dto.setTotalRooms(rs.getInt("TotalRooms"));
                map.put(dto.getId(), dto);
            }
        } finally {
            closeConnection();
        }
        return map;
    } 
    
    public List<RoomInformationDTO> getListRoomInfosByHotelId(int hotelId, int roomTypeId,
                                    Date fromDate, Date toDate) throws SQLException, NamingException {
        int requiredAmount = 1;
        Map<Integer, RoomInformationDTO> map = new HashMap<>();
        List<RoomInformationDTO> infos = new ArrayList<>();
        List<RoomDTO> rooms = new ArrayList<>();
        List<RoomDTO> bookedRooms = new ArrayList<>();
        Map<Integer, Integer> bookedRoomInfos = new HashMap<>();
        //in step 5: contains hotelId and amount of [roomIds] booked room (of [roomTypeId]) fromDate - toDate
        Map<Integer, Integer> mapHotelTotalRooms = new HashMap<>();
        //contains K-V: hotelId-total amount of rooms of roomTypeId
        List<RoomInformationDTO> availableInfos = new ArrayList<>();
        try {
            con = DBUtils.getConnection();
            
            //Step 1 
            String sqlSelectRoomInfos = "select ri.Id, ri.Name, ri.Description, ri.TotalRooms from RoomInformation as ri " 
                    + "where ri.HotelId=? and ri.RoomTypeId=? ";
            stm = con.prepareStatement(sqlSelectRoomInfos);
            stm.setInt(1, hotelId);
            stm.setInt(2, roomTypeId);
            rs = stm.executeQuery();

            while (rs.next()) {
                RoomInformationDTO dto = new RoomInformationDTO();
                dto.setId(rs.getInt("Id"));
                dto.setName(rs.getString("Name"));
                dto.setDescription(rs.getString("Description"));
                dto.setHotelId(hotelId);
                dto.setTotalRooms(rs.getInt("TotalRooms"));
                dto.setRoomTypeId(roomTypeId);
                infos.add(dto);
            }

            System.out.println("info size = " + infos.size());
            if (infos.size() > 0) {
//                select r.Id, r.RoomInfoId from Room as r where r.RoomInfoId in (2,1)
                //Step 2
                String sqlSelectRooms = "select r.Id, r.RoomInfoId from Room as r where r.RoomInfoId ";

                sqlSelectRooms = sqlSelectRooms + getStringParamterInQuery(infos.size());
                System.out.println("sqlSelectRooms = " + sqlSelectRooms);
                stm = con.prepareStatement(sqlSelectRooms);
                int paramSqlRooms = 1;
                for (int i = 0; i < infos.size(); i++) {
                    stm.setInt(paramSqlRooms, infos.get(i).getId());
                    paramSqlRooms++;
                }
                rs = stm.executeQuery();

                while (rs.next()) {
                    RoomDTO dto = new RoomDTO();
                    dto.setId(rs.getInt("Id"));
                    dto.setRoomInfoId(rs.getInt("RoomInfoId"));
                    rooms.add(dto);
                    System.out.println("room " + dto.getId());
                }

                System.out.println("Room amount = " + rooms.size());
                //Step 4
                if (rooms.size() > 0) {

                    String sqlSelectSchedules = "select distinct(rs1.RoomId) from RoomSchedule as rs1 "
                            + "where rs1.Id not in "
                            + "(select rs2.Id from RoomSchedule as rs2 "
                            + "where rs2.FromDate > ? or rs2.ToDate < ?) "
                            + "and rs1.Status = ? "
                            + "and rs1.RoomId ";

                    sqlSelectSchedules = sqlSelectSchedules + getStringParamterInQuery(rooms.size());
                    System.out.println("sqlSelectSchedules = " + sqlSelectSchedules);
                    stm = con.prepareStatement(sqlSelectSchedules);
                    stm.setDate(1, toDate);
                    stm.setDate(2, fromDate);
                    stm.setInt(3, DBUtils.ORDER_STATUS_BOOKED);
                    int paramSqlSchedules = 4;
                    for (int i = 0; i < rooms.size(); i++) {
                        stm.setInt(paramSqlSchedules, rooms.get(i).getId());
                        paramSqlSchedules++;
                    }
                    rs = stm.executeQuery();

                    while (rs.next()) {
                        RoomDTO dto = new RoomDTO();
                        dto.setId(rs.getInt("RoomId"));
                        bookedRooms.add(dto);
                        System.out.println("bookedRoom " + dto.getId());
                    }

                    //Step 5
                    System.out.println("Booked room amount: " + bookedRooms.size());
                    if (bookedRooms.size() > 0) {
                        String sqlParamCountBookedRoom = getStringParamterInQuery(bookedRooms.size());
                        String sqlSelectCountBookedRoom
                                = "select r.RoomInfoId, count(distinct(r.Id)) as BookedAmount from Room as r "
                                + "where r.Id "
                                + sqlParamCountBookedRoom
                                + "group by r.RoomInfoId";
                        stm = con.prepareStatement(sqlSelectCountBookedRoom);
                        int paramSqlCountBookedRoom = 1;
                        for (int i = 0; i < bookedRooms.size(); i++) {
                            stm.setInt(paramSqlCountBookedRoom, bookedRooms.get(i).getId());
                            paramSqlCountBookedRoom++;
                        }
                        rs = stm.executeQuery();

                        while (rs.next()) {
                            bookedRoomInfos.put(rs.getInt("RoomInfoId"), rs.getInt("BookedAmount"));
                        }
                    }

                }

            }

            
            
            
            availableInfos = getListAvailableInfos(getMapRoomInfos(infos), bookedRoomInfos, requiredAmount);
        } finally {
            closeConnection();
        }
        return availableInfos;
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
    
    private Map<Integer, RoomInformationDTO> getMapRoomInfos(List<RoomInformationDTO> infos) {
        Map<Integer, RoomInformationDTO> map = new HashMap<>();
        for (int i = 0; i < infos.size(); i++) {
            RoomInformationDTO info = infos.get(i);
            map.put(info.getId(), info);
        }
        return map;
    }
    
    private List<RoomInformationDTO> getListAvailableInfos(
            Map<Integer, RoomInformationDTO> mapInfoTotalRoom,
            Map<Integer, Integer> infoBookedAmount,
            int requiredAmount) {

        List<RoomInformationDTO> infos = new ArrayList<>();
        //update mapInfoTotalRoom: K-V: hotelId - total amount of room of roomTypeId: 
        //--> contains hotelId with available rooms
        Set<Map.Entry<Integer, Integer>> setBooked = infoBookedAmount.entrySet();
        
        for (Entry<Integer, Integer> entry : setBooked) {
            int infoId = entry.getKey();
            RoomInformationDTO info = mapInfoTotalRoom.get(infoId);
            
            int bookedAmount = entry.getValue();
            int totalAmount = info.getTotalRooms();
            int availableAmount = totalAmount - bookedAmount;
            
            info.setTotalRooms(availableAmount);            
            mapInfoTotalRoom.put(infoId, info);
            System.out.println("info-available amount: " + infoId + ":" + availableAmount);
        }

        Set<Map.Entry<Integer, RoomInformationDTO>> set = mapInfoTotalRoom.entrySet();
        for (Map.Entry<Integer, RoomInformationDTO> entry : set) {
            RoomInformationDTO info = entry.getValue();
            int availableAmount = info.getTotalRooms();

            if (availableAmount >= requiredAmount) {
                infos.add(info);
                System.out.println("infoId available: " + entry.getKey() + ":" + availableAmount);
            }
        }
        return infos;
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
