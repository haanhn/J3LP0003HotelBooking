/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import haanh.hotel.HotelDAO;
import haanh.hotel.HotelDTO;
import haanh.roominformation.RoomInformationDAO;
import haanh.roominformation.RoomInformationDTO;
import haanh.utils.UrlConstants;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HaAnh
 */
public class GetListRoomInfoOfHotelAction {
    
    private Integer hotelId;
    private Date fromDate;
    private Date toDate;
    private Integer roomType;
    
    private HotelDTO hotel;
    private List<RoomInformationDTO> infos = new ArrayList<>();
    
    public GetListRoomInfoOfHotelAction() {
    }
    
    public String execute() throws Exception {
        infos = new ArrayList<>();
        
        HotelDAO hotelDAO = new HotelDAO();
        RoomInformationDAO infoDAO = new RoomInformationDAO();
        
        hotel = hotelDAO.getHotelByHotelId(hotelId);
        infos = infoDAO.getListRoomInfosByHotelId(hotelId, roomType, fromDate, toDate);
        
        return UrlConstants.STRUTS2_RESULT_SUCCESS;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
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

    public HotelDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelDTO hotel) {
        this.hotel = hotel;
    }

    public List<RoomInformationDTO> getInfos() {
        return infos;
    }

    public void setInfos(List<RoomInformationDTO> infos) {
        this.infos = infos;
    }
}
