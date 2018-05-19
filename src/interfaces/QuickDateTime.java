/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ANURUDDHA
 */
public class QuickDateTime {

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static String datewithtime() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int hr = cal.get(Calendar.HOUR);
        String s = (year + "-" + month + "-" + date + "_" + hr + "-" + min + "-" + sec);
        return s;
    }

    public static String date() throws ParseException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        String s = (year + "-" + month + "-" + date);
        Date d = df.parse(s);
        String st=df.format(d);
        return st;
    }
}
