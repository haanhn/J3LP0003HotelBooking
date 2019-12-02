/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.struts2;

import com.opensymphony.xwork2.ActionSupport;
import haanh.area.AreaDAO;
import haanh.hotel.HotelDAO;
import haanh.hotel.HotelDTO;
import haanh.roomschedule.RoomScheduleDAO;
import haanh.roomtype.RoomTypeDAO;
import haanh.utils.DataUtils;
import haanh.utils.UrlConstants;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author HaAnh
 */
public class SearchHotelsAction extends ActionSupport {

    private Map<Integer, String> areas = new HashMap<>();
    private Map<Integer, String> roomTypes = new HashMap<>();
    private List<HotelDTO> hotels = new ArrayList<>();

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
        System.out.println("SearchHotels");
        
        if (validateFromAndToDate()) {
//            RoomInformationDAO infoDAO = new RoomInformationDAO();
//            Map<Integer, RoomInformationDTO> mapInfos = infoDAO.getMapRoomInfoByHotelAndRoomType(area, hotelName, roomType);
            RoomScheduleDAO scheduleDAO = new RoomScheduleDAO();
            HotelDAO hotelDAO = new HotelDAO();
            
            List<Integer> hotelIds = scheduleDAO.getRoomBookedInTimeRange2(area, hotelName, fromDate, toDate, roomType, roomAmount);
            
            if (hotelIds != null && hotelIds.size() > 0) {
                hotels = hotelDAO.getHotelByHotelIds(hotelIds);
            } else {
                ServletActionContext.getRequest().setAttribute(UrlConstants.ATTR_MESSAGE, "No hotels found for your requirements");
            }
        }
        getAreasAndRoomTypes();
        return result;
    }

    private void getAreasAndRoomTypes() throws SQLException, NamingException {
        AreaDAO areaDAO = new AreaDAO();
        RoomTypeDAO typeDAO = new RoomTypeDAO();

        areas = areaDAO.getAllAreas();
        roomTypes = typeDAO.getAllRoomTypes();
    }

    private boolean validateFromAndToDate() {
        Date currentDate = new Date(DataUtils.getCurrentDateInMillis());
        Date from = new Date(DataUtils.getDateInMillis(fromDate, 22));
        Date to = new Date(DataUtils.getDateInMillis(toDate, 23));
        
        System.out.println("currentDate " +currentDate.getTime());
        System.out.println("fromDate " +from.getTime());
        System.out.println("toDate " +to.getTime());
        System.out.println("fromDate " +from);
        boolean valid = true;
        if (from.compareTo(currentDate) < 0) {
            valid = false;
            ServletActionContext.getRequest().setAttribute(UrlConstants.ATTR_MESSAGE_DATE, "From Date only valid from today");
        } else {
            valid = from.compareTo(to) < 0;
            if (!valid) {
                ServletActionContext.getRequest().setAttribute(UrlConstants.ATTR_MESSAGE_DATE, "From Date >= To Date");
            }
        }
        return valid;
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

    public List<HotelDTO> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelDTO> hotels) {
        this.hotels = hotels;
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
