/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author ANURUDDHA
 */
public class Account {

    public static void credit(String account_id, String ref_id, double amount, String date) {
        try {
            JDBC.putdata("INSERT INTO account VALUES(0,'" + account_id + "','" + ref_id + "','" + amount + "','" + date + "','Credit')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debit(String account_id, String ref_id, double amount, String date) {
        try {
            JDBC.putdata("INSERT INTO account VALUES(0,'" + account_id + "','" + ref_id + "','" + amount + "','" + date + "','Debit')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void supplierPayments(String grn_no, double amount, String method) {
        try {
            JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'"+grn_no+"','"+amount+"','"+method+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void customerPayments(String invoice_no, double amount, String method) {
        try {
            JDBC.putdata("INSERT INTO customer_payments VALUES(0,'"+invoice_no+"','"+amount+"','"+method+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
