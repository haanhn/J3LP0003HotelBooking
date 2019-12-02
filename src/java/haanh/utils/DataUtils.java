/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haanh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;

/**
 *
 * @author HaAnh
 */
public class DataUtils {
    
    //-------------USER--------------
    public static String getSHA256HashedString(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(str.getBytes());
        String hashed = Base64.getEncoder().encodeToString(bytes);
        return hashed;
    }
    
    public static long getCurrentDateInMillis() {
        Calendar cal = getCurrentDate();        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        return cal.getTimeInMillis();
    }
    
    public static long getDateInMillis(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR, hour);        
        return cal.getTimeInMillis();
    }
    
    private static Calendar getCurrentDate() {
        Date current = new Date(System.currentTimeMillis());
        Calendar calCurrent = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        
        calCurrent.setTimeInMillis(current.getTime());
        cal.set(Calendar.YEAR, calCurrent.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calCurrent.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calCurrent.get(Calendar.DAY_OF_MONTH));
        
        return cal;
    }
}
