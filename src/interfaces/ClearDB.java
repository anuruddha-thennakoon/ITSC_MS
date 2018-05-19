/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author ANURUDDHA
 */
public class ClearDB {
    public static void main(String[] args) {
        try {
            JDBC.putdata("DELETE FROM account");
            JDBC.putdata("DELETE FROM bank");
            JDBC.putdata("DELETE FROM cheque_info");
            JDBC.putdata("DELETE FROM current_stock");
            JDBC.putdata("DELETE FROM customer");
            JDBC.putdata("DELETE FROM customer_payments");
            JDBC.putdata("DELETE FROM grn_info");
            JDBC.putdata("DELETE FROM invoice_info");
            JDBC.putdata("DELETE FROM items");
            JDBC.putdata("DELETE FROM paths");
            JDBC.putdata("DELETE FROM return_ability");
            JDBC.putdata("DELETE FROM stock");
            JDBC.putdata("DELETE FROM supplier_payments");
            JDBC.putdata("DELETE FROM grn");
            JDBC.putdata("DELETE FROM invoice");
            JDBC.putdata("DELETE FROM supplier");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
