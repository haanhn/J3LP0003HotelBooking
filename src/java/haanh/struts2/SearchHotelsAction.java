/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionSupport;
import haanh.area.AreaDAO;
import haanh.roomtype.RoomTypeDAO;
import haanh.utils.UrlConstants;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author HaAnh
 */
public class SearchHotelsAction extends ActionSupport {
    
    private Map<Integer, String> areas = new HashMap<>();
    private Map<Integer, String> roomTypes = new HashMap<>();
    
    private Integer area;
    private String hotelName;
    private Date fromDate;
    private Date toDate;
    private Integer roomType;
    private Integer roomAmount;
    
    public SearchHotelsAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_SUCCESS;
        
        getAreasAndRoomTypes();
        System.out.println("SearchHotels");
        
        return result;
    }

    private void getAreasAndRoomTypes() throws SQLException, NamingException {
        AreaDAO areaDAO = new AreaDAO();
        RoomTypeDAO typeDAO = new RoomTypeDAO();
        
        areas = areaDAO.getAllAreas();
        roomTypes = typeDAO.getAllRoomTypes();
    }
    
    public Map<Integer, String> getAreas() {
        return areas;
    }

    public void setAreas(Map<Integer, String> areas) {
        this.areas = areas;
    }

    public Map<Integer, String> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Map<Integer, String> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
    }
        
}
