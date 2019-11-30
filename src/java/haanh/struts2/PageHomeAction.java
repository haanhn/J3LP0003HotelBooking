/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import haanh.area.AreaDAO;
import haanh.roomtype.RoomTypeDAO;
import haanh.utils.UrlConstants;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author HaAnh
 */
public class PageHomeAction {
    
    private Map<Integer, String> areas = new HashMap<>();
    private Map<Integer, String> roomTypes = new HashMap<>();
    private Date fromDate;
    private Date toDate;
    private Integer roomAmount;
    
    public PageHomeAction() {
    }
    
    public String execute() throws Exception {
        String result = UrlConstants.STRUTS2_RESULT_SUCCESS;
        
        AreaDAO areaDAO = new AreaDAO();
        RoomTypeDAO typeDAO = new RoomTypeDAO();
        
        
        areas = areaDAO.getAllAreas();
        roomTypes = typeDAO.getAllRoomTypes();
        Date currentDate = new Date(System.currentTimeMillis());
        fromDate = currentDate;
        toDate = currentDate;
        roomAmount = 1;
        
        return result;
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

    public Integer getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
    }
}
