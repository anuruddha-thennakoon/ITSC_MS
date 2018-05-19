/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANURUDDHA
 */
public class Return extends javax.swing.JInternalFrame {

    /**
     * Creates new form Return
     */
    public Return() {
        initComponents();
    }

    void displayReturn() {
        try {
            if (jComboBox2.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "First select a customer/supplier and then select invoice no", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                DefaultTableModel d = (DefaultTableModel) jTable2.getModel();
                d.setRowCount(0);
                if (jRcustomer.isSelected()) {
                    ResultSet rs_invoice = JDBC.getdata("SELECT * FROM invoice WHERE invoice_no='" + jComboBox2.getSelectedItem().toString() + "' AND qty>0");
                    while (rs_invoice.next()) {
                        Vector v = new Vector();
                        v.add(rs_invoice.getString("iid"));
                        v.add(rs_invoice.getString("qty"));
                        v.add(rs_invoice.getString("amount"));
                        d.addRow(v);
                    }
                } else if (jRsupplier.isSelected()) {
                    ResultSet rs_grn = JDBC.getdata("SELECT * FROM grn WHERE grn_no='" + jComboBox2.getSelectedItem().toString() + "' AND qty>0");
                    while (rs_grn.next()) {
                        Vector v = new Vector();
                        v.add(rs_grn.getString("iid"));
                        v.add(rs_grn.getString("qty"));
                        v.add(rs_grn.getString("amount"));
                        d.addRow(v);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "First select a customer/supplier and then select invoice no", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void customerReturn() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();

            String iid = d.getValueAt(jTable2.getSelectedRow(), 0).toString();
            double qty = Double.parseDouble(d.getValueAt(jTable2.getSelectedRow(), 1).toString());
            double return_amount = Double.parseDouble(d.getValueAt(jTable2.getSelectedRow(), 2).toString());

            String ino = jComboBox2.getSelectedItem().toString();
            String cno = jComboBox1.getSelectedItem().toString();

            String bno = "";
            String chno = "";
            ResultSet rs_bank = JDBC.getdata("SELECT * FROM cheque_info WHERE invoice_no='" + ino + "'");
            while (rs_bank.next()) {
                bno = rs_bank.getString("bank_bid");
                chno = rs_bank.getString("cheque_no");
            }
            System.out.println(bno);
            System.out.println(chno);

            ResultSet rs_cash_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");
            ResultSet rs_credit_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");
            ResultSet rs_cheque_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");

            String cash_account = "DSKH15000001";
            String stock_account = "DSKT15000001";

            if (rs_credit_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.debit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.credit(cno, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + return_amount + "' WHERE invoice_no='" + ino + "'");
                //update invoice
                JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else if (rs_cheque_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.debit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.credit(bno, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + return_amount + "' WHERE invoice_no='" + ino + "'");
                //customer_payments
                double mreturn_amount = return_amount * -1;
                JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                //return_ability
                JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");
                //update invoice
                JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else if (rs_cash_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.debit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.credit(cash_account, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + return_amount + "' WHERE invoice_no='" + ino + "'");
                //customer_payments
                double mreturn_amount = return_amount * -1;
                JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cash')");
                //update invoice
                JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else {
                double dup_return_amount = return_amount;
                int count = 0;
                double cash_amount = 0;
                double cheque_amount = 0;
                double credit_amount = 0;

                ResultSet rs_cash_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND aid='DSKT15000001'");
                ResultSet rs_credit_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND aid='DSKT15000001'");
                ResultSet rs_cheque_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND aid='DSKT15000001'");

                if (rs_credit_check.next()) {
                    ResultSet rs_credit_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND aid='DSKT15000001'");
                    while (rs_credit_amount.next()) {
                        credit_amount = rs_credit_amount.getDouble("amount");
                    }
                } else {
                    count = count + 1;
                }

                if (rs_cheque_check.next()) {
                    ResultSet rs_cheque_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND aid='DSKT15000001'");
                    while (rs_cheque_amount.next()) {
                        cheque_amount = rs_cheque_amount.getDouble("amount");
                    }
                } else {
                    count = count + 2;
                }

                if (rs_cash_check.next()) {
                    ResultSet rs_cash_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND aid='DSKH15000001'");
                    while (rs_cash_amount.next()) {
                        cash_amount = rs_cash_amount.getDouble("amount");
                    }
                } else {
                    count = count + 3;
                }

                if (count == 3) {
                    //credit & cheque
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.debit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.credit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + credit_amount + "' WHERE invoice_no='" + ino + "'");

                    //cheque
                    double cheque_am = return_amount - credit_amount;
                    //account
                    Account.credit(bno, ino, cheque_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cheque_am + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_am * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //update invoice
                    JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 4) {
                    //credit & cash
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.debit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.credit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + credit_amount + "' WHERE invoice_no='" + ino + "'");

                    //cash
                    double cash_am = return_amount - credit_amount;
                    //account
                    Account.credit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cash_am + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cash_am * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 5) {
                    //cheque & cash
                    //cheque
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.debit(stock_account, ino, cheque_amount, QuickDateTime.date());
                    Account.credit(bno, ino, cheque_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cheque_amount + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_amount * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //cash
                    double cash_am = return_amount - cheque_amount;
                    //account
                    Account.credit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cash_am + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount_cash = cash_am * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount_cash + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 6) {
                    //credit,cheque & cash
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty+'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.debit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.credit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + credit_amount + "' WHERE invoice_no='" + ino + "'");

                    //cheque
                    double cheque_am = return_amount - credit_amount;
                    //account
                    Account.credit(bno, ino, cheque_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cheque_am + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_am * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //cash
                    double cash_am = return_amount - cheque_amount - credit_amount;
                    //account
                    Account.credit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE invoice_info SET total_amount=total_amount-'" + cash_am + "' WHERE invoice_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount_cash = cash_am * -1;
                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + mreturn_amount_cash + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE invoice SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void supplierReturn() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();

            String iid = d.getValueAt(jTable2.getSelectedRow(), 0).toString();
            double qty = Double.parseDouble(d.getValueAt(jTable2.getSelectedRow(), 1).toString());
            double return_amount = Double.parseDouble(d.getValueAt(jTable2.getSelectedRow(), 2).toString());

            String ino = jComboBox2.getSelectedItem().toString();
            String cno = jComboBox1.getSelectedItem().toString();

            String bno = "";
            String chno = "";
            ResultSet rs_bank = JDBC.getdata("SELECT * FROM cheque_info WHERE invoice_no='" + ino + "'");
            while (rs_bank.next()) {
                bno = rs_bank.getString("bank_bid");
                chno = rs_bank.getString("cheque_no");
            }
            System.out.println(bno);
            System.out.println(chno);

            ResultSet rs_cash_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");
            ResultSet rs_credit_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");
            ResultSet rs_cheque_amount_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND(aid='DSKT15000001' AND amount>='" + return_amount + "') ");

            String cash_account = "DSKH15000001";
            String stock_account = "DSKT15000001";

            if (rs_credit_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.credit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.debit(cno, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + return_amount + "' WHERE grn_no='" + ino + "'");
                //update invoice
                JDBC.putdata("UPDATE grn SET qty=0 WHERE invoice_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else if (rs_cheque_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.credit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.debit(bno, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + return_amount + "' WHERE grn_no='" + ino + "'");
                //customer_payments
                double mreturn_amount = return_amount * -1;
                JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                //return_ability
                JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");
                //update invoice
                JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else if (rs_cash_amount_check.next()) {
                //add item to stock
                JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                //account
                Account.credit(stock_account, ino, return_amount, QuickDateTime.date());
                Account.debit(cash_account, ino, return_amount, QuickDateTime.date());
                //invoice
                JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + return_amount + "' WHERE grn_no='" + ino + "'");
                //customer_payments
                double mreturn_amount = return_amount * -1;
                JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cash')");
                //update invoice
                JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                displayReturn();
            } else {
                int count = 0;
                double cash_amount = 0;
                double cheque_amount = 0;
                double credit_amount = 0;

                ResultSet rs_cash_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND aid='DSKT15000001'");
                ResultSet rs_credit_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND aid='DSKT15000001'");
                ResultSet rs_cheque_check = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND aid='DSKT15000001'");

                if (rs_credit_check.next()) {
                    ResultSet rs_credit_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + cno + "' AND aid='DSKT15000001'");
                    while (rs_credit_amount.next()) {
                        credit_amount = rs_credit_amount.getDouble("amount");
                    }
                } else {
                    count = count + 1;
                }

                if (rs_cheque_check.next()) {
                    ResultSet rs_cheque_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + bno + "' AND aid='DSKH15000001'");
                    while (rs_cheque_amount.next()) {
                        cheque_amount = rs_cheque_amount.getDouble("amount");
                    }
                } else {
                    count = count + 2;
                }

                if (rs_cash_check.next()) {
                    ResultSet rs_cash_amount = JDBC.getdata("SELECT * FROM account WHERE ref_id='" + ino + "' AND aid='DSKH15000001'");
                    while (rs_cash_amount.next()) {
                        cash_amount = rs_cash_amount.getDouble("amount");
                    }
                } else {
                    count = count + 3;
                }

                if (count == 3) {
                    //credit & cheque
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.credit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.debit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + credit_amount + "' WHERE grn_no='" + ino + "'");

                    //cheque
                    double cheque_am = return_amount - credit_amount;
                    //account
                    Account.debit(bno, ino, cheque_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cheque_am + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_am * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //update invoice
                    JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 4) {
                    //credit & cash
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.credit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.debit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + credit_amount + "' WHERE grn_no='" + ino + "'");

                    //cash
                    double cash_am = return_amount - credit_amount;
                    //account
                    Account.debit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cash_am + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cash_am * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 5) {
                    //cheque & cash
                    //cheque
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.credit(stock_account, ino, cheque_amount, QuickDateTime.date());
                    Account.debit(bno, ino, cheque_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cheque_amount + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_amount * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //cash
                    double cash_am = return_amount - cheque_amount;
                    //account
                    Account.debit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cash_am + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount_cash = cash_am * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount_cash + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                } else if (count == 6) {
                    //credit,cheque & cash
                    //credit
                    //add item to stock
                    JDBC.putdata("UPDATE stock SET qty=qty-'" + qty + "' WHERE iid='" + iid + "'");
                    //account
                    Account.credit(stock_account, ino, credit_amount, QuickDateTime.date());
                    Account.debit(cno, ino, credit_amount, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + credit_amount + "' WHERE grn_no='" + ino + "'");

                    //cheque
                    double cheque_am = return_amount - credit_amount;
                    //account
                    Account.debit(bno, ino, cheque_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cheque_am + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount = cheque_am * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount + "','Cheque')");
                    //return_ability
                    JDBC.putdata("INSERT INTO return_ability VALUES(0,'" + chno + "')");

                    //cash
                    double cash_am = return_amount - cheque_amount - credit_amount;
                    //account
                    Account.debit(cash_account, ino, cash_am, QuickDateTime.date());
                    //invoice
                    JDBC.putdata("UPDATE grn_info SET total_amount=total_amount-'" + cash_am + "' WHERE grn_no='" + ino + "'");
                    //customer_payments
                    double mreturn_amount_cash = cash_am * -1;
                    JDBC.putdata("INSERT INTO supplier_payments VALUES(0,'" + ino + "','" + mreturn_amount_cash + "','Cash')");

                    //update invoice
                    JDBC.putdata("UPDATE grn SET qty=0 WHERE grn_no='" + ino + "' AND iid='" + iid + "'");
                    displayReturn();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel68 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jRcustomer = new javax.swing.JRadioButton();
        jRsupplier = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setClosable(true);
        setTitle("Return");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel67.setText("Customer No");

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel68.setText("Invoice No");

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        buttonGroup1.add(jRcustomer);
        jRcustomer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRcustomer.setText("Customer");
        jRcustomer.setContentAreaFilled(false);
        jRcustomer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRcustomerItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRsupplier);
        jRsupplier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRsupplier.setText("Supplier");
        jRsupplier.setContentAreaFilled(false);
        jRsupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRsupplierItemStateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Return");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Qty", "Amount"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRcustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRsupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jRcustomer)
                    .addComponent(jRsupplier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jButton2)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        displayReturn();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRcustomerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRcustomerItemStateChanged
        try {
            jLabel67.setText("Customer No");
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();
            d.setRowCount(0);
            Vector vnull = new Vector();
            jComboBox2.setModel(new DefaultComboBoxModel(vnull));
            ResultSet rs_check = JDBC.getdata("SELECT DISTINCT cid FROM invoice_info");
            if (rs_check.next()) {
                ResultSet rs_customer = JDBC.getdata("SELECT DISTINCT cid FROM invoice_info");
                Vector v = new Vector();
                while (rs_customer.next()) {
                    v.add(rs_customer.getString("cid"));
                }
                jComboBox1.setModel(new DefaultComboBoxModel(v));
            } else {
                JOptionPane.showMessageDialog(null, "No customers found", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jRcustomerItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        try {
            if (jRcustomer.isSelected()) {
                ResultSet rs_check = JDBC.getdata("SELECT * FROM invoice_info");
                if (rs_check.next()) {
                    ResultSet rs_customer = JDBC.getdata("SELECT * FROM invoice_info");
                    Vector v = new Vector();
                    while (rs_customer.next()) {
                        v.add(rs_customer.getString("invoice_no"));
                    }
                    jComboBox2.setModel(new DefaultComboBoxModel(v));
                } else {
                    JOptionPane.showMessageDialog(null, "No invoices found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (jRsupplier.isSelected()) {
                ResultSet rs_check = JDBC.getdata("SELECT * FROM grn_info");
                if (rs_check.next()) {
                    ResultSet rs_customer = JDBC.getdata("SELECT * FROM grn_info");
                    Vector v = new Vector();
                    while (rs_customer.next()) {
                        v.add(rs_customer.getString("grn_no"));
                    }
                    jComboBox2.setModel(new DefaultComboBoxModel(v));
                } else {
                    JOptionPane.showMessageDialog(null, "No invoices found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jRsupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRsupplierItemStateChanged
        try {
            jLabel67.setText("Supplier No");
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();
            d.setRowCount(0);
            Vector vnull = new Vector();
            jComboBox2.setModel(new DefaultComboBoxModel(vnull));
            ResultSet rs_check = JDBC.getdata("SELECT DISTINCT sid FROM grn_info");
            if (rs_check.next()) {
                ResultSet rs_supplier = JDBC.getdata("SELECT DISTINCT sid FROM grn_info");
                Vector v = new Vector();
                while (rs_supplier.next()) {
                    v.add(rs_supplier.getString("sid"));
                }
                jComboBox1.setModel(new DefaultComboBoxModel(v));
            } else {
                JOptionPane.showMessageDialog(null, "No suppliers found", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jRsupplierItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jRcustomer.isSelected()) {
            customerReturn();
        }
        if (jRsupplier.isSelected()) {
            supplierReturn();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRcustomer;
    private javax.swing.JRadioButton jRsupplier;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
