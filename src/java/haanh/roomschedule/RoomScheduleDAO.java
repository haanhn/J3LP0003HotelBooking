/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.roomschedule;

import haanh.room.RoomDTO;
import haanh.roominformation.RoomInformationDTO;
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
public class RoomScheduleDAO {

    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;

    public List<Integer> getRoomBookedInTimeRange(
            Integer areaId, String hotelName,
            Date fromDate, Date toDate,
            Integer roomTypeId, Integer requiredAmount,
            Map<Integer, RoomInformationDTO> mapRoomInfo) throws SQLException, NamingException {

        List<RoomDTO> rooms = new ArrayList<>();
        List<RoomDTO> bookedRooms = new ArrayList<>();

        List<Integer> hotelIds = new ArrayList<>();
        try {
            con = DBUtils.getConnection();

            String sql = "select r.Id, r.RoomInfoId from Room as r where r.RoomInfoId in ( "
                    + "select ri.Id from RoomInformation as ri "
                    + "where "
                    + "ri.RoomTypeId=? "
                    + "and ri.HotelId in (select h.HotelId from Hotel as h where h.HotelName like ? and h.AreaId=?) "
                    + ")";

            stm = con.prepareStatement(sql);
            stm.setInt(1, roomTypeId);
            stm.setString(2, "%" + hotelName + "%");
            stm.setInt(3, areaId);

            rs = stm.executeQuery();
            while (rs.next()) {
                RoomDTO dto = new RoomDTO();
                dto.setId(rs.getInt("Id"));
                dto.setRoomInfoId(rs.getInt("RoomInfoId"));
                rooms.add(dto);
            }

            if (rooms.size() > 0) {
//                String sqlSelectRoomsInSchedule = "select r.Id, r.RoomInfoId from Room as r where r.Id in ( "
//                        + "select distinct(rs1.RoomId) from RoomSchedule as rs1 where rs1.Id not in "
//                        + "(select rs2.Id from RoomSchedule as rs2 "
//                        + "where rs2.FromDate > ? or rs2.ToDate < ? ) "
//                        + "and "
//                        + "rs1.RoomId "
//                        + ")";
                String sqlSelectRoomsInSchedule = "select distinct(rs1.RoomId) from RoomSchedule as rs1 where rs1.Id not in "
                        + "(select rs2.Id from RoomSchedule as rs2 "
                        + "where rs2.FromDate > ? or rs2.ToDate < ? ) "
                        + "and "
                        + "rs1.RoomId ";
                sqlSelectRoomsInSchedule = sqlSelectRoomsInSchedule + getStringParamterInQuery(rooms.size());
                sqlSelectRoomsInSchedule = "select r.Id, r.RoomInfoId from Room as r where r.Id in ( "
                        + sqlSelectRoomsInSchedule
                        + " )";

                stm = con.prepareStatement(sql);
                stm.setDate(1, fromDate);
                stm.setDate(2, toDate);
                int paramCount = 2;
                for (int i = 0; i < rooms.size(); i++) {
                    paramCount++;
                    stm.setInt(paramCount, rooms.get(i).getId());
                }
                rs = stm.executeQuery();
                while (rs.next()) {
                    RoomDTO dto = new RoomDTO();
                    dto.setId(rs.getInt("Id"));
                    dto.setRoomInfoId(rs.getInt("RoomInfoId"));
                    bookedRooms.add(dto);
                }

                hotelIds = getListHotelsHasRoom(mapRoomInfo, getMapRoomInfoWithBookedAmount(bookedRooms), requiredAmount);
            }

        } finally {
            closeConnection();
        }
        return hotelIds;
    }

    public List<Integer> getRoomBookedInTimeRange1(
            Integer areaId, String hotelName,
            Date fromDate, Date toDate,
            Integer roomTypeId, Integer requiredAmount) throws SQLException, NamingException {

        try {
            con = DBUtils.getConnection();
            String sql
                    = "declare @queryHotelName nvarchar(50) "
                    + "set @queryHotelName = ? "
                    + "declare @queryAreaId int "
                    + "set @queryAreaId = ? "
                    + "declare @queryRoomTypeId int "
                    + "set @queryRoomTypeId = ? "
                    + "declare @queryFromDate date "
                    + "set @queryFromDate = ? "
                    + "declare @queryToDate date "
                    + "set @queryToDate = ? "
                    + "declare @queryRoomAmount int "
                    + "set @queryRoomAmount = ? "
                    //                    + "--1. select những hotels in area and name = 'abx' "
                    //                    + "--> hotelIds "

                    + "declare @hotel table (HotelId int) "
                    + "insert into @hotel "
                    + "select HotelId as HotelId from Hotel "
                    + "where AreaId = @queryAreaId and HotelName like '%' + @queryHotelName + '%' "
                    //                    + "--2. select roomInfo as ri mà ri.hotelId in (hotelIds) and ri.roomType=roomTypeId "
                    //                    + "--> roomInfoIds "
                    + "declare @roomInfoId table (RoomInfoId int) "
                    + "insert into @roomInfoId "
                    + "select ri.Id as RoomInfoId "
                    + "from @hotel as h "
                    + "inner join RoomInformation as ri on h.HotelId = ri.HotelId "
                    + "where ri.RoomTypeId = @queryRoomTypeId "
                    //                    + "--3. select room where id in (roomInfoIds) "
                    //                    + "--> roomIds "
                    + "declare @roomId table (RoomId int) "
                    + " "
                    + "insert into @roomId "
                    + "select r.Id as RoomId from @roomInfoId as ri inner join Room as r on ri.RoomInfoId = r.RoomInfoId "
                    + " "
                    //                    + "--4. select schedules "
                    //                    + "--where s.fromDate - s.toDate in range inclusive of from-to query condition "
                    //                    + "--and s.roomId in (roomIds) "
                    + "declare @schedule table (RoomId int) "
                    + " "
                    + "insert into @schedule "
                    + "select rs.RoomId as RoomId "
                    + "from @roomId as r "
                    + "         inner join RoomSchedule as rs on r.RoomId = rs.RoomId "
                    + "where not (@queryFromDate > rs.ToDate or @queryToDate < rs.FromDate) "
                    + " "
                    //                    + "--5. count unique s.roomId of schedule group by (RoomInfoId) "
                    + "declare @scheduleByRoomInfo table (RoomInfoId int, NumOfBookedRooms int) "
                    //                    + " "
                    + "insert into @scheduleByRoomInfo "
                    + "select r.RoomInfoId, count(distinct s.RoomId) as NumOfBookedRooms "
                    + "from @schedule as s "
                    + "         inner join Room as r on s.RoomId = r.Id "
                    + "group by r.RoomInfoId "
                    + " "
                    //                    + "--6. select hotels h where h.totalRooms - s.count(unique(roomId)) >= queriedRoomAmount "
                    + "declare @roomInfoByHotel table (HotelId int, RoomInfoId int) "
                    //                    + " "
                    + "insert into @roomInfoByHotel "
                    + "select ri.HotelId as HotelId, ri.Id as RoomInfoId "
                    + "from @scheduleByRoomInfo as s "
                    + "         inner join RoomInformation as ri on s.RoomInfoId = ri.Id "
                    + "where ri.TotalRooms - s.NumOfBookedRooms >= @queryRoomAmount "
                    + " "
                    + "select * from @roomInfoByHotel";
            stm = con.prepareStatement(sql);
            stm.setString(1, hotelName);
            stm.setInt(2, areaId);
            stm.setInt(3, roomTypeId);
            stm.setDate(4, fromDate);
            stm.setDate(5, toDate);
            stm.setInt(6, requiredAmount);

            rs = stm.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("RoomInfoId") + ":" + rs.getInt("HotelId"));
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    public List<Integer> getRoomBookedInTimeRange2(
            Integer areaId, String hotelName,
            Date fromDate, Date toDate,
            Integer roomTypeId, Integer requiredAmount) throws SQLException, NamingException {
//        List<HotelDTO> hotels = new ArrayList<>();
        List<RoomInformationDTO> infos = new ArrayList<>();
        List<RoomDTO> rooms = new ArrayList<>();
        List<RoomDTO> bookedRooms = new ArrayList<>();
        List<RoomInformationDTO> bookedRoomInfos = new ArrayList<>();
        //in step 5: contains hotelId and amount of [roomIds] booked room (of [roomTypeId]) fromDate - toDate
        Map<Integer, Integer> mapHotelTotalRooms = new HashMap<>();
        //contains K-V: hotelId-total amount of rooms of roomTypeId
        List<Integer> hotelIds = new ArrayList<>();
        try {
            con = DBUtils.getConnection();
            //Step 1
//            String sqlSelectHotels = "select HotelId from Hotel "
//                    + "where AreaId = ? and HotelName like ?";
//            stm = con.prepareStatement(sqlSelectHotels);
//            stm.setInt(1, areaId);
//            stm.setString(2, "%" + hotelName + "%");
//            rs = stm.executeQuery();
//
//            while (rs.next()) {
//                HotelDTO dto = new HotelDTO();
//                dto.setId(rs.getInt("HotelId"));
//
//                hotels.add(dto);
//            }

            //Step 1 & 2
            String sqlSelectRoomInfos = "select h.HotelId, ri.Id, ri.TotalRooms from Hotel as h "
                    + "inner join "
                    + "RoomInformation as ri "
                    + "on h.AreaId = ? and h.HotelName like ? and h.HotelId=ri.HotelId and ri.RoomTypeId = ?";
            stm = con.prepareStatement(sqlSelectRoomInfos);
            stm.setInt(1, areaId);
            stm.setString(2, "%" + hotelName + "%");
            stm.setInt(3, roomTypeId);
            rs = stm.executeQuery();

            while (rs.next()) {
                RoomInformationDTO dto = new RoomInformationDTO();
                dto.setHotelId(rs.getInt("HotelId"));
                dto.setId(rs.getInt("Id"));
                dto.setTotalRooms(rs.getInt("TotalRooms"));
                infos.add(dto);
            }

            System.out.println("info size = " + infos.size());
            if (infos.size() > 0) {
//                select r.Id, r.RoomInfoId from Room as r where r.RoomInfoId in (2,1)
                //Step 3
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
                                = "select ri.HotelId, count(distinct(r.Id)) as BookedAmount from Room as r "
                                + "inner join "
                                + "RoomInformation as ri on "
                                + "r.RoomInfoId = ri.Id "
                                + "and "
                                + "r.Id "
                                + sqlParamCountBookedRoom
                                + " group by ri.HotelId ";
                        stm = con.prepareStatement(sqlSelectCountBookedRoom);
                        int paramSqlCountBookedRoom = 1;
                        for (int i = 0; i < bookedRooms.size(); i++) {
                            stm.setInt(paramSqlCountBookedRoom, bookedRooms.get(i).getId());
                            paramSqlCountBookedRoom++;
                        }
                        rs = stm.executeQuery();

                        while (rs.next()) {
                            RoomInformationDTO dto = new RoomInformationDTO();
                            dto.setHotelId(rs.getInt("HotelId"));
                            dto.setTotalRooms(rs.getInt("BookedAmount"));
                            bookedRoomInfos.add(dto);
                            System.out.println("HotelId-bookedAmount: " + dto.getHotelId() + ":" + dto.getTotalRooms());
                        }
                    }

                }

            }

//            String sqlSelectCountRoomByHotelId
//                    = "select ri.HotelId, sum(ri.TotalRooms) as TotalRooms "
//                    + "from RoomInformation as ri "
//                    + "where ri.RoomTypeId = ? "
//                    + "group by ri.HotelId";
            
            String sqlSelectCountRoomByHotelId = 
                    "select h.HotelId, sum(ri.TotalRooms) as TotalRooms from Hotel as h "
                    + "inner join "
                    + "RoomInformation as ri "
                    + "on h.AreaId = ? and h.HotelName like ? and h.HotelId=ri.HotelId and ri.RoomTypeId = ? "
                    + "group by h.HotelId";

            stm = con.prepareStatement(sqlSelectCountRoomByHotelId);
            stm = con.prepareStatement(sqlSelectCountRoomByHotelId);
            stm.setInt(1, areaId);
            stm.setString(2, "%" + hotelName + "%");
            stm.setInt(3, roomTypeId);
            rs = stm.executeQuery();
            while (rs.next()) {
                mapHotelTotalRooms.put(rs.getInt("HotelId"), rs.getInt("TotalRooms"));
            }

            hotelIds = getListHotelIds(mapHotelTotalRooms, bookedRoomInfos, requiredAmount);
        } finally {
            closeConnection();
        }
        return hotelIds;
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

    //Get map contains 
    //  + key: roomInfoId
    //  + value: amount of rooms books of roomInfoId
    private Map<Integer, Integer> getMapRoomInfoWithBookedAmount(List<RoomDTO> bookedRooms) {
        Map<Integer, Integer> mapBookedAmount = new HashMap<>();

        for (int i = 0; i < bookedRooms.size(); i++) {
            int roomInfoId = bookedRooms.get(i).getRoomInfoId();
            Integer amount = mapBookedAmount.get(roomInfoId);
            if (amount == null) {
                amount = 1;
            } else {
                amount = amount + 1;
            }
            mapBookedAmount.put(roomInfoId, amount);
        }
        return mapBookedAmount;
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

    private List<Integer> getListHotelsHasRoom(
            Map<Integer, RoomInformationDTO> mapRoomInfos,
            Map<Integer, Integer> mapBookedAmount,
            int requiredAmount) {
        Map<Integer, Integer> mapHotelsWithAvailableAmount = new HashMap<>();

        Set<Entry<Integer, Integer>> setBookedAmount = mapBookedAmount.entrySet();

        //loop through mapBookedAmount --> get map contains roomInfo with available room amount
        for (Entry<Integer, Integer> entry : setBookedAmount) {
            int roomInfo = entry.getKey();
            int bookedAmount = entry.getValue();

            RoomInformationDTO info = mapRoomInfos.get(roomInfo);
            int totalAmount = info.getTotalRooms();
            int availableAmount = totalAmount - bookedAmount;

            info.setTotalRooms(availableAmount);
            mapRoomInfos.put(roomInfo, info);
        }

        //loop through map contains roomInfo and available roomAmount
        //add available amount of room group by hotelId
        Set<Entry<Integer, RoomInformationDTO>> setRoomInfos = mapRoomInfos.entrySet();
        for (Entry<Integer, RoomInformationDTO> entry : setRoomInfos) {
            int hotelId = entry.getValue().getHotelId();
            int roomInfo = entry.getKey();
            int availableAmount = entry.getValue().getTotalRooms();

            Integer totalAvailableAmount = mapHotelsWithAvailableAmount.get(hotelId);
            if (totalAvailableAmount == null) {
                totalAvailableAmount = availableAmount;
            } else {
                totalAvailableAmount = totalAvailableAmount + availableAmount;
            }

            mapHotelsWithAvailableAmount.put(hotelId, totalAvailableAmount);
        }

        List<Integer> hotelIds = new ArrayList<>();
        //loop through mapHotelsWithAvailableAmount contains hotelId and available roomAmount
        //if hotel.roomAvailableAmount >= requiredAmount --> add hotels to list
        Set<Entry<Integer, Integer>> setHotelsAvailableAmount = mapHotelsWithAvailableAmount.entrySet();
        for (Entry<Integer, Integer> entry : setHotelsAvailableAmount) {
            int hotelId = entry.getKey();
            int availableAmount = entry.getValue();

            if (availableAmount >= requiredAmount) {
                hotelIds.add(hotelId);
                System.out.println("hotelId " + hotelId);
            }
        }

        return hotelIds;
    }

    //**********************
    //get map K-V: hotelId-roomInfoDTO
    //from list roomInfoDTO (contains: hotelId, roomInfoId, totalRooms)
    private Map<Integer, RoomInformationDTO> getMapRoomInfo(List<RoomInformationDTO> infos) {
        Map<Integer, RoomInformationDTO> map = new HashMap<>();
        for (int i = 0; i < infos.size(); i++) {
            RoomInformationDTO info = infos.get(i);
            map.put(info.getHotelId(), info);
        }
        return map;
    }

    //mapInfos: K-V: hotelId-roomInfoDTO: contains hotelId with total rooms
    //infoBookedAmount: contains hotelId and amount of [roomIds] booked room (of [roomTypeId]) fromDate - toDate
    private List<Integer> getListHotelIds(
            Map<Integer, Integer> mapHotelTotalRoom,
            List<RoomInformationDTO> infoBookedAmount,
            int requiredAmount) {

        List<Integer> hotelIds = new ArrayList<>();
        //update mapHotelTotalRoom: K-V: hotelId - total amount of room of roomTypeId: 
        //--> contains hotelId with available rooms
        for (int i = 0; i < infoBookedAmount.size(); i++) {
            RoomInformationDTO infoBooked = infoBookedAmount.get(i);
            int hotelId = infoBooked.getHotelId();            

            int bookedAmount = infoBooked.getTotalRooms();
            int totalAmount = mapHotelTotalRoom.get(hotelId);
            int availableAmount = totalAmount - bookedAmount;
            
            mapHotelTotalRoom.put(hotelId, availableAmount);
            System.out.println("hotelId-available amount: " + hotelId + ":" + availableAmount);
        }

        Set<Entry<Integer, Integer>> set = mapHotelTotalRoom.entrySet();
        //add hotelId to list if available amount >= requiredAmount    
        for (Entry<Integer, Integer> entry : set) {
            int availableAmount = entry.getValue();

            if (availableAmount >= requiredAmount) {
                hotelIds.add(entry.getKey());
                System.out.println("hotelId available: " + entry.getKey() + ":" + availableAmount);
            }
        }
        return hotelIds;
    }
}
