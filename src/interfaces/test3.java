/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANURUDDHA
 */
public class test3 {

    public static void main(String[] args) {
        try {
            //        String d="2016-5-22";
            //        System.out.println(d);
            //        String k=QuickDateTime.date();
            //        System.out.println(k);
            //        System.out.println(QuickDateTime.date());
            //        if(k.equals(d)){
            //            System.out.println("elaaaa");
            //        }
            //        try {
            //            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //            int i = 10;
            //            String s = "2016-5-1";
            //            Date d = df.parse(s);
            //            System.out.println(d);
            //            System.out.println(df.format(d));
            //        } catch (Exception e) {
            //            e.printStackTrace();
            //        }
            //        DecimalFormat df = new DecimalFormat("00.00");
            //        double d=56.456;
            //        String s="256.892";
            //        double k=Double.parseDouble(df.format(Double.parseDouble(s)));
            //        System.out.println(k);
//            System.out.println("body");
//            ResultSet rs = JDBC.getdata("select * from customer where cid='DSKC15000003'");
//            System.out.println("1");
//            String a="";
//            while (rs.next()) {
//                System.out.println("2");
//                a = rs.getString(1);
//            }
//            System.out.println(a);
//            System.out.println("3");
            String db_path=JOptionPane.showInputDialog(null,"Input database path","");
            String p="C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysql";
            String s=db_path.replace("\\", "/");
            System.out.println(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
