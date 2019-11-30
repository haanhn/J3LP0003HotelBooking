/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author HaAnh
 */
public class DBUtils {
    
    //Roles
    public static final String ROLE_ADMIN = "AD001";
    public static final String ROLE_CUSTOMER = "U001";
    
    //Order Status, Schedule Status
    public static final int ORDER_STATUS_BOOKED = 0;
    public static final int ORDER_STATUS_CANCELED = -1;
    
    public static Connection getConnection() throws NamingException, SQLException {
        Context context = new InitialContext();
        Context env = (Context) context.lookup("java:comp/env");
        DataSource ds = (DataSource) env.lookup("J3LP0003HotelBookingDS");
        Connection con = ds.getConnection();
        return con;
    }
    
}
