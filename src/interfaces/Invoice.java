/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import static interfaces.Home.jDesktopPane1;
import static interfaces.Invo.invoiceNo;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ANURUDDHA
 */
public class Invoice extends javax.swing.JInternalFrame {

    static DecimalFormat df = new DecimalFormat("00.00");
    public static String bank = "";
    public static String cheque_no;
    public static String date;

    /**
     * Creates new form Invoice
     */
    public Invoice() {
        initComponents();
        invoiceNo();
        itemCombo();
        customer();
    }

    void customer() {
        try {
            ResultSet rs = JDBC.getdata("SELECT * FROM customer WHERE status='Active'");
            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("customer_combo"));
            }
//            v.add("Default customer");
            jCInvo_customer.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
        }
    }

    public static void invoiceNo() {
        try {
            ResultSet rs = JDBC.getdata("select MAX(invoice_no) FROM invoice_info");
            while (rs.next()) {
                String batch1 = rs.getString("max(invoice_no)");
                int batch2 = Integer.parseInt(batch1.substring(4));
                int batch3 = (batch2 + 1);
                jTinvoice_no.setText("ITSI" + batch3);
            }
        } catch (Exception e) {
            jTinvoice_no.setText("ITSI" + 15000001);
        }
    }

    void itemCombo() {
        try {
            ResultSet rs = JDBC.getdata("SELECT DISTINCT stock_combo FROM stock WHERE qty>0");
            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("stock_combo"));
            }
            jCInvo_item.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void invoiceTable() {
        String iid = null;
        String iname = null;
        try {
            ResultSet rs = JDBC.getdata("SELECT * FROM items WHERE item_combo='" + jCInvo_item.getSelectedItem().toString() + "'");
            while (rs.next()) {
                iid = rs.getString("iid");
                iname = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double qty = Double.parseDouble(jTextField3.getText().trim().toString());
        double price = Double.parseDouble(jTextField5.getText().trim().toString());
        DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
        Vector v = new Vector();
        v.add(iid);
        v.add(iname);
        v.add(jTextField5.getText().trim());
        v.add(jTextField3.getText().trim());
        v.add(df.format(qty * price));
        v.add(jTextField4.getText().trim());
        d.addRow(v);
    }

    public boolean checkDuplicate(int columnIndex, JTable jt, String iid) {
        DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
        int rowCount = d.getRowCount();
        boolean b = false;
        for (int x = 0; x < rowCount; x++) {
            String tiid = jt.getValueAt(x, columnIndex).toString();
            if (iid.equals(tiid)) {
                b = true;
            }
        }
        return b;
    }

    void toatalAmount() {
        try {
            DefaultTableModel d1 = (DefaultTableModel) jTable3.getModel();
            double sub_total = 0;
            double qty = 0;
            for (int x = 0; x < d1.getRowCount(); x++) {
                sub_total = sub_total + Double.parseDouble(jTable3.getValueAt(x, 4).toString());
                qty = qty + Double.parseDouble(jTable3.getValueAt(x, 3).toString());
            }
            jTSub_total.setText(df.format(sub_total));
            jTItem_count.setText(df.format(qty));
            jTNet_amount.setText(df.format(sub_total));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void textfieldVisible() {
        if (jCCash.isSelected()) {
            jTCash_amount.setEnabled(true);
            setZero();
        } else {
            jTCash_amount.setEnabled(false);
            setZero();
        }
        if (jCCheque.isSelected()) {
            jTCheque_amount.setEnabled(true);
            setZero();
        } else {
            jTCheque_amount.setEnabled(false);
            setZero();
        }
    }

    public static boolean cal() {
        boolean bool = false;
        double na = Double.parseDouble(jTNet_amount.getText().trim());
        double ca = Double.parseDouble(jTCash_amount.getText().trim());
        double cha = Double.parseDouble(jTCheque_amount.getText().trim());

        if (jCCash.isSelected() && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {
            if (ca < na) {
                JOptionPane.showMessageDialog(null, "You have selected the cash option,can not enter cash amount lesser than net amount", "Input error", JOptionPane.ERROR_MESSAGE);
                bool = true;
                setZero();
            } else {
                jTBalance.setForeground(Color.black);
                jTBalance.setText(df.format(ca - na));
            }
        } else if (jCCheque.isSelected() && !(jCCash.isSelected()) && !(jCCredit.isSelected())) {
            if (!(cha == na)) {
                JOptionPane.showMessageDialog(null, "You have selected the cheque option,enter cheque amount equal to net amount", "Input error", JOptionPane.ERROR_MESSAGE);
                bool = true;
                setZero();
            } else {
                jTBalance.setForeground(Color.black);
                jTBalance.setText(df.format(cha - na));
            }
        } else if (jCCredit.isSelected() && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {
            //
        } else if (jCCash.isSelected() && jCCredit.isSelected() && !(jCCheque.isSelected())) {
            jTBalance.setForeground(Color.red);
            jTBalance.setText(df.format(-1 * (ca - na)));
        } else if (jCCash.isSelected() && jCCheque.isSelected() && !(jCCredit.isSelected())) {
            if (!((cha + ca) == na)) {
                if (na < cha) {
                    JOptionPane.showMessageDialog(null, "You have selected the cash and cheque option,enter correct amounts ", "Input error", JOptionPane.ERROR_MESSAGE);
                    bool = true;
                    setZero();
                } else if ((cha + ca) < na) {
                    JOptionPane.showMessageDialog(null, "You have selected the cash and cheque option,enter correct amounts ", "Input error", JOptionPane.ERROR_MESSAGE);
                    bool = true;
                    setZero();
                } else {
                    jTBalance.setForeground(Color.black);
                    double d = ca - (na - cha);
                    System.out.println("asd");
                    jTBalance.setText(df.format(d));
                    System.out.println("cvb");
                }
            } else {
                jTBalance.setForeground(Color.black);
                double d = ca - (na - cha);
                System.out.println("asd");
                jTBalance.setText(df.format(d));
                System.out.println("cvb");
            }
        } else if (jCCredit.isSelected() && jCCheque.isSelected() && !(jCCash.isSelected())) {
            if (na <= cha) {
                JOptionPane.showMessageDialog(null, "You have selected the credit and cheque option,enter correct amounts ", "Input error", JOptionPane.ERROR_MESSAGE);
                bool = true;
                setZero();
            } else {
                jTBalance.setForeground(Color.red);
                jTBalance.setText(df.format(-1 * (cha - na)));
            }
        } else if (jCCredit.isSelected() && jCCheque.isSelected() && jCCash.isSelected()) {
            jTBalance.setForeground(Color.red);
            jTBalance.setText(df.format(-1 * ((ca + cha) - na)));
        } else if (!(jCCash.isSelected()) && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {
            jTBalance.setForeground(Color.black);
            jTBalance.setText("00.00");
        }
        return bool;
    }

    static void setZero() {
        jTBalance.setForeground(Color.black);
        jTCash_amount.setText("00.00");
        jTCheque_amount.setText("00.00");
        jTBalance.setText("00.00");
    }

    public static void toDB() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            if (d.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No items added to save", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (jTinvoice_no.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter invoice number", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCInvo_customer.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select customer", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (!(jCCash.isSelected() || jCCheque.isSelected() || jCCredit.isSelected())) {
                    JOptionPane.showMessageDialog(null, "Please select payment method", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCCash.isSelected() && jTCash_amount.getText().equals("00.00")) {
                    JOptionPane.showMessageDialog(null, "Please enter cash amount", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCCheque.isSelected() && jTCheque_amount.getText().equals("00.00")) {
                    JOptionPane.showMessageDialog(null, "Please enter cheque amount", "Input error", JOptionPane.ERROR_MESSAGE);
                } else {
                    cal();
                    int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to save?", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        for (int x = 0; x < d.getRowCount(); x++) {
                            JDBC.putdata("UPDATE stock SET "
                                    + "qty=qty-'" + jTable3.getValueAt(x, 3) + "' "
                                    + "WHERE iid='" + jTable3.getValueAt(x, 0) + "'");
                        }

                        for (int x = 0; x < d.getRowCount(); x++) {
                            JDBC.putdata("INSERT INTO invoice VALUES("
                                    + "'" + jTinvoice_no.getText() + "',"
                                    + "'" + jTable3.getValueAt(x, 0) + "',"
                                    + "'" + jTable3.getValueAt(x, 3) + "',"
                                    + "'" + jTable3.getValueAt(x, 4) + "')");
                        }

                        String cid = null;
                        ResultSet rs = JDBC.getdata("SELECT * FROM customer WHERE customer_combo='" + jCInvo_customer.getSelectedItem() + "'");
                        while (rs.next()) {
                            cid = rs.getString("cid");
                        }

                        JDBC.putdata("INSERT INTO invoice_info VALUES("
                                + "'" + jTinvoice_no.getText() + "',"
                                + "'" + QuickDateTime.date() + "',"
                                + "'" + jTItem_count.getText() + "',"
                                + "'" + jTNet_amount.getText() + "',"
                                + "'" + cid + "')");

                        String cash_account = "DSKH15000001";
                        String stock_account = "DSKT15000001";


                        double na = Double.parseDouble(df.format(Double.parseDouble(jTNet_amount.getText().trim())));
                        double caa = Double.parseDouble(df.format(Double.parseDouble(jTCash_amount.getText().trim())));
                        double cha = Double.parseDouble(df.format(Double.parseDouble(jTCheque_amount.getText().trim())));
                        double ba = Double.parseDouble(df.format(Double.parseDouble(jTBalance.getText().trim())));
                        double cra = na - (caa + cha);
                        System.out.println("5");
                        if (jCCash.isSelected() && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {
                            System.out.println("66");
                            //only cash
                            Account.debit(cash_account, jTinvoice_no.getText(), na, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), na, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), na, jCCash.getText());
                            printInvoice(na, caa, 0.0, 0.0, ba);
                        } else if (jCCheque.isSelected() && !(jCCash.isSelected()) && !(jCCredit.isSelected())) {
                            System.out.println("65");
                            //only cheque
                            Account.debit(bank, jTinvoice_no.getText(), na, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            printInvoice(na, 0.0, cha, 0.0, ba);
                        } else if (jCCredit.isSelected() && !(jCCheque.isSelected()) && !(jCCash.isSelected())) {
                            System.out.println("1");
                            //only credit
                            Account.debit(cid, jTinvoice_no.getText(), na, QuickDateTime.date());
                            System.out.println("12");
                            Account.credit(stock_account, jTinvoice_no.getText(), na, QuickDateTime.date());
                            System.out.println("13");
                            Account.customerPayments(jTinvoice_no.getText(), 0, jCCash.getText());
                            System.out.println("13");
                            printInvoice(na, 0.0, 0.0, cra, 0.0);
                        } else if (jCCash.isSelected() && jCCredit.isSelected() && !(jCCheque.isSelected())) {
                            System.out.println("64");
                            //cash
                            Account.debit(cash_account, jTinvoice_no.getText(), caa, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), caa, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), caa, jCCash.getText());
                            //credit
                            Account.debit(cid, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            printInvoice(na, caa, 0.0, cra, 0.0);
                        } else if (jCCash.isSelected() && jCCheque.isSelected() && !(jCCredit.isSelected())) {
                            System.out.println("64");
                            //cash
                            Account.debit(cash_account, jTinvoice_no.getText(), (na - cha - ba), QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), (na - cha - ba), QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), (na - cha - ba), jCCash.getText());
                            //cheque
                            Account.debit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            printInvoice(na, caa, cha, 0.0, ba);
                        } else if (jCCredit.isSelected() && jCCheque.isSelected() && !(jCCash.isSelected())) {
                            System.out.println("63");
                            //cheque
                            Account.debit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            //credit
                            Account.debit(cid, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            printInvoice(na, 0.0, cha, cra, 0.0);
                        } else if (jCCredit.isSelected() && jCCheque.isSelected() && jCCash.isSelected()) {
                            System.out.println("61");
                            //cash
                            Account.debit(cash_account, jTinvoice_no.getText(), caa, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), caa, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), caa, jCCash.getText());
                            //cheque
                            Account.debit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.customerPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            //credit
                            Account.debit(cid, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            printInvoice(na, caa, cha, cra, 0.0);
                        }

                        d.setRowCount(0);
                        jTItem_count.setText("00.00");
                        jTSub_total.setText("00.00");
                        jTCash_settle.setText("00.00");
                        jTNet_amount.setText("00.00");
                        jTCash_amount.setText("00.00");
                        jTCheque_amount.setText("00.00");
                        jTBalance.setText("00.00");

                        jCCash.setSelected(false);
                        jCCheque.setSelected(false);
                        jCCredit.setSelected(false);
                        invoiceNo();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int chequeDetails() {
        double cha = Double.parseDouble(jTCheque_amount.getText().trim());
        ChequeDetailsInvoice cd = new ChequeDetailsInvoice();
        cd.jTAmount.setText(df.format(cha));
        cd.setLocation(280, 120);
        Home.jDesktopPane1.add(cd).setVisible(true);
        return 0;
    }

    public static void printInvoice(double na, double caa, double cha, double cra, double ba) {
        try {
            String fname = "";
            String lname = "";
            String name = "";
            String contact_no = "";
            String email = "";
            ResultSet rs_customer = JDBC.getdata("SELECT * FROM customer WHERE customer_combo='" + jCInvo_customer.getSelectedItem().toString() + "'");
            while (rs_customer.next()) {
                fname = rs_customer.getString("fname");
                lname = rs_customer.getString("lname");
                contact_no = rs_customer.getString("contact_no");
                email = rs_customer.getString("email");
            }
            name = fname + " " + lname;


            HashMap para = new HashMap();

            para.put("ino", jTinvoice_no.getText().trim());//            
            para.put("name", name);//
            para.put("contact_no", contact_no);//
            para.put("email", email);//

            ResultSet rs = JDBC.getdata("SELECT * FROM paths");
            String path = "";
            while (rs.next()) {
                path = rs.getString("path");
            }

            if (jCCash.isSelected() && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {

                //cash
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_cash.jrxml");
                para.put("net_amount", na);
                para.put("cash_amount", caa);
                para.put("balance_amount", ba);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCheque.isSelected() && !(jCCash.isSelected()) && !(jCCredit.isSelected())) {
                //cheque
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_cheque.jrxml");
                para.put("net_amount", na);
                para.put("cheque_amount", cha);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCredit.isSelected() && !(jCCheque.isSelected()) && !(jCCash.isSelected())) {
                //credit
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_credit.jrxml");
                para.put("net_amount", na);
                para.put("credit_amount", cra);


                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCash.isSelected() && jCCredit.isSelected() && !(jCCheque.isSelected())) {
                //cash & credit
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_cash_credit.jrxml");
                para.put("net_amount", na);
                para.put("cash_amount", caa);
                para.put("credit_amount", cra);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCash.isSelected() && jCCheque.isSelected() && !(jCCredit.isSelected())) {
                //cash & cheque
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_cash_cheque.jrxml");
                para.put("net_amount", na);
                para.put("cash_amount", caa);
                para.put("cheque_amount", cha);
                para.put("balance_amount", ba);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCredit.isSelected() && jCCheque.isSelected() && !(jCCash.isSelected())) {
                //credit & cheque
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice_cheque_credit.jrxml");
                para.put("net_amount", na);
                para.put("credit_amount", cra);
                para.put("cheque_amount", cha);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            } else if (jCCredit.isSelected() && jCCheque.isSelected() && jCCash.isSelected()) {
                //credit & cheque & cheque
                JasperReport r = JasperCompileManager.compileReport(path + "/invoice.jrxml");
                para.put("net_amount", na);
                para.put("cash_amount", caa);
                para.put("cheque_amount", cha);
                para.put("credit_amount", cra);
                para.put("balance_amount", ba);

                JasperPrint jp = JasperFillManager.fillReport(r, para, new JRTableModelDataSource(jTable3.getModel()));
                JasperViewer.viewReport(jp, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        NewInvoice1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTNet_amount = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTCash_settle = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTSub_total = new javax.swing.JTextField();
        jCCredit = new javax.swing.JCheckBox();
        jCCash = new javax.swing.JCheckBox();
        jCCheque = new javax.swing.JCheckBox();
        jPanel30 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jTinvoice_no = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jCInvo_customer = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jCInvo_item = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        jTItem_count = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jTBalance = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTCash_amount = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTCheque_amount = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton23 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Invoice");

        NewInvoice1.setBackground(new java.awt.Color(255, 255, 255));
        NewInvoice1.setMaximumSize(new java.awt.Dimension(1364, 655));
        NewInvoice1.setMinimumSize(new java.awt.Dimension(1364, 655));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setOpaque(false);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Net Amount(Rs)");

        jTNet_amount.setEditable(false);
        jTNet_amount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTNet_amount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTNet_amount.setText("00.00");
        jTNet_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTNet_amountActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Cash Settled (Rs)");

        jTCash_settle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTCash_settle.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTCash_settle.setText("00.00");
        jTCash_settle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTCash_settleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTCash_settleFocusLost(evt);
            }
        });
        jTCash_settle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTCash_settleKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Sub Total (Rs)");

        jTSub_total.setEditable(false);
        jTSub_total.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTSub_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTSub_total.setText("00.00");

        jCCredit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCCredit.setText("Credit");
        jCCredit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCCreditItemStateChanged(evt);
            }
        });

        jCCash.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCCash.setText("Cash");
        jCCash.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCCashItemStateChanged(evt);
            }
        });

        jCCheque.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCCheque.setText("Cheque");
        jCCheque.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCChequeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTSub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTCash_settle, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTNet_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jCCash)
                .addGap(18, 18, 18)
                .addComponent(jCCredit)
                .addGap(10, 10, 10)
                .addComponent(jCCheque)
                .addGap(30, 30, 30))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTSub_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTCash_settle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTNet_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCCredit)
                    .addComponent(jCCash)
                    .addComponent(jCCheque))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setMaximumSize(new java.awt.Dimension(280, 129));
        jPanel30.setMinimumSize(new java.awt.Dimension(280, 129));
        jPanel30.setName(""); // NOI18N
        jPanel30.setOpaque(false);

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel67.setText("Invoice No");

        jTinvoice_no.setEditable(false);
        jTinvoice_no.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel69.setText("Customer");

        jCInvo_customer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCInvo_customer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCInvo_customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCInvo_customerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTinvoice_no, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCInvo_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(400, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel67)
                    .addComponent(jTinvoice_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(jCInvo_customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jCInvo_item.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCInvo_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCInvo_itemActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Qty");

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setText("Item");

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Unit Price(Rs)");

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton7.setText("Add");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Warrenty");

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(45, 45, 45)
                .addComponent(jCInvo_item, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCInvo_item, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setMaximumSize(new java.awt.Dimension(1350, 212));
        jPanel9.setMinimumSize(new java.awt.Dimension(1350, 212));
        jPanel9.setOpaque(false);

        jButton18.setText("DELETE");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Name", "Unit price(Rs)", "Qty", "Net amount(Rs)", "Warrenty"
            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setText("Item Count");

        jTItem_count.setEditable(false);
        jTItem_count.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTItem_count.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTItem_count.setText("00.00");
        jTItem_count.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTItem_countFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTItem_countFocusLost(evt);
            }
        });
        jTItem_count.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTItem_countKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(46, 46, 46)
                        .addComponent(jTItem_count, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTItem_count, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTBalance.setEditable(false);
        jTBalance.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTBalance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTBalance.setText("00.00");
        jTBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTBalanceActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Cash Amount(Rs)");

        jTCash_amount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTCash_amount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTCash_amount.setText("00.00");
        jTCash_amount.setEnabled(false);
        jTCash_amount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTCash_amountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTCash_amountFocusLost(evt);
            }
        });
        jTCash_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTCash_amountKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Balance(Rs)");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Cheque Amount(Rs)");

        jTCheque_amount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTCheque_amount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTCheque_amount.setText("00.00");
        jTCheque_amount.setEnabled(false);
        jTCheque_amount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTCheque_amountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTCheque_amountFocusLost(evt);
            }
        });
        jTCheque_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTCheque_amountKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTCash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTCheque_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTCash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTCheque_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22)
                        .addComponent(jTBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        jButton23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton23.setText("Print");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NewInvoice1Layout = new javax.swing.GroupLayout(NewInvoice1);
        NewInvoice1.setLayout(NewInvoice1Layout);
        NewInvoice1Layout.setHorizontalGroup(
            NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewInvoice1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 1121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NewInvoice1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        NewInvoice1Layout.setVerticalGroup(
            NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewInvoice1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(NewInvoice1, javax.swing.GroupLayout.PREFERRED_SIZE, 1136, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NewInvoice1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTNet_amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTNet_amountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTNet_amountActionPerformed

    private void jTCash_settleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCash_settleFocusGained
        try {
            jTBalance.setForeground(Color.black);
            jTCash_settle.setText("");
            jTNet_amount.setText("00.00");
            jTBalance.setText("00.00");
            jTCash_amount.setText("00.00");
            jTCheque_amount.setText("00.00");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCash_settleFocusGained

    private void jTCash_settleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCash_settleFocusLost
        try {
            if (jTCash_settle.getText().equals("")) {
                jTCash_settle.setText("00.00");
                jTNet_amount.setText(jTSub_total.getText());
            } else {
                double st = Double.parseDouble(jTSub_total.getText().trim());
                double cs = Double.parseDouble(jTCash_settle.getText().trim());
                jTNet_amount.setText(df.format(st + cs));
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCash_settleFocusLost

    private void jTCash_settleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCash_settleKeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.') || (c == '-') || (c == '+')) {
        } else {
            evt.consume();
        }
        String s = jTCash_settle.getText();
        if (s.length() >= 10) {
            evt.consume();
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (count >= 1) {
                evt.consume();
            }
            if (ch == '.') {
                count++;
            }
        }
    }//GEN-LAST:event_jTCash_settleKeyTyped

    private void jCCreditItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCCreditItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCCreditItemStateChanged

    private void jCCashItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCCashItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCCashItemStateChanged

    private void jCChequeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCChequeItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCChequeItemStateChanged

    private void jCInvo_customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCInvo_customerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCInvo_customerActionPerformed

    private void jCInvo_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCInvo_itemActionPerformed
        //
    }//GEN-LAST:event_jCInvo_itemActionPerformed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        try {
            double qty = Double.parseDouble(jTextField3.getText().toString().trim());
            ResultSet rs = JDBC.getdata("SELECT * FROM stock WHERE qty>='" + qty + "' AND stock_combo='" + jCInvo_item.getSelectedItem() + "'");
            if (rs.next()) {
            } else {
                JOptionPane.showMessageDialog(null, "Not enough qty", "Input error", JOptionPane.ERROR_MESSAGE);
                jTextField3.setText("");
                jTextField3.requestFocus();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.')) {
        } else {
            evt.consume();
        }
        String s = jTextField3.getText();
        if (s.length() >= 10) {
            evt.consume();
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (count >= 1) {
                evt.consume();
            }
            if (ch == '.') {
                count++;
            }
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        //        try {
        //            double up = Double.parseDouble(jTextField5.getText().toString().trim());
        //            ResultSet rs = JDBC.getdata("SELECT * FROM stock WHERE unit_price<='" + up + "' AND item_combo='" + jCInvo_item.getSelectedItem() + "'");
        //            if (rs.next()) {
        //            } else {
        //                JOptionPane.showMessageDialog(null, "Unit price is low", "Input error", JOptionPane.ERROR_MESSAGE);
        //                jTextField5.setText("");
        //                jTextField5.requestFocus();
        //            }
        //        } catch (Exception e) {
        //        }
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        try {
            char c = evt.getKeyChar();
            if ((Character.isDigit(c)) || (c == '.')) {
            } else {
                evt.consume();
            }
            String s = jTextField5.getText();
            if (s.length() >= 10) {
                evt.consume();
            }
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (count >= 1) {
                    evt.consume();
                }
                if (ch == '.') {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            if (jTextField5.getText().isEmpty() || jTextField3.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Some fields are empty", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                ResultSet rs = JDBC.getdata("SELECT * FROM items WHERE item_combo='" + jCInvo_item.getSelectedItem().toString() + "'");
                String iid = "";
                while (rs.next()) {
                    iid = rs.getString("iid");
                }
                if (checkDuplicate(0, jTable3, iid)) {
                    JOptionPane.showMessageDialog(null, "This item is already entered", "Input error", JOptionPane.ERROR_MESSAGE);
                } else {
                    invoiceTable();
                    toatalAmount();
                    jTextField5.setText("");
                    jTextField3.setText("");
                    jTextField4.setText("");
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            int ii = Integer.parseInt(jTable3.getValueAt(jTable3.getSelectedRow(), 3).toString().trim());
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            if (d.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No data entered", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected item?", "", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    d.removeRow(jTable3.getSelectedRow());
                    toatalAmount();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No item selected", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTItem_countFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTItem_countFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countFocusGained

    private void jTItem_countFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTItem_countFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countFocusLost

    private void jTItem_countKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTItem_countKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countKeyTyped

    private void jTBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTBalanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTBalanceActionPerformed

    private void jTCash_amountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCash_amountFocusGained
        try {
            jTBalance.setForeground(Color.black);
            jTBalance.setText("00.00");
            jTCash_amount.setText("");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCash_amountFocusGained

    private void jTCash_amountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCash_amountFocusLost
        try {
            if (jTCash_amount.getText().equals("")) {
                jTCash_amount.setText("00.00");
            } else {
                //                cal();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCash_amountFocusLost

    private void jTCash_amountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCash_amountKeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.')) {
        } else {
            evt.consume();
        }
        String s = jTCash_amount.getText();
        if (s.length() >= 10) {
            evt.consume();
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (count >= 1) {
                evt.consume();
            }
            if (ch == '.') {
                count++;
            }
        }
    }//GEN-LAST:event_jTCash_amountKeyTyped

    private void jTCheque_amountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCheque_amountFocusGained
        try {
            jTBalance.setText("00.00");
            jTCheque_amount.setText("");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCheque_amountFocusGained

    private void jTCheque_amountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTCheque_amountFocusLost
        try {
            if (jTCheque_amount.getText().equals("")) {
                jTCheque_amount.setText("00.00");
            } else {
                //                cal();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTCheque_amountFocusLost

    private void jTCheque_amountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCheque_amountKeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.')) {
        } else {
            evt.consume();
        }
        String s = jTCheque_amount.getText();
        if (s.length() >= 10) {
            evt.consume();
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (count >= 1) {
                evt.consume();
            }
            if (ch == '.') {
                count++;
            }
        }
    }//GEN-LAST:event_jTCheque_amountKeyTyped

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        try {
            if (jCCheque.isSelected()) {
                if (jTinvoice_no.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter invoice number", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCInvo_customer.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select customer", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (!(jCCash.isSelected() || jCCheque.isSelected() || jCCredit.isSelected())) {
                    JOptionPane.showMessageDialog(null, "Please select payment method", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCCash.isSelected() && jTCash_amount.getText().equals("00.00")) {
                    JOptionPane.showMessageDialog(null, "Please enter cash amount", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCCheque.isSelected() && jTCheque_amount.getText().equals("00.00")) {
                    JOptionPane.showMessageDialog(null, "Please enter cheque amount", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (cal()) {
                    //
                } else {
                    double cha = Double.parseDouble(jTCheque_amount.getText().trim());
                    ChequeDetailsInvoice cd = new ChequeDetailsInvoice();
                    cd.jTAmount.setText(df.format(cha));
                    Dimension desktopSize = jDesktopPane1.getSize();
                    Dimension jInternalFrameSize = cd.getSize();
                    cd.setLocation((desktopSize.width - jInternalFrameSize.width) / 2, (desktopSize.height - jInternalFrameSize.height) / 2);
                    Home.jDesktopPane1.add(cd).setVisible(true);
                }
            } else {
                toDB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NewInvoice1;
    public static javax.swing.JButton jButton18;
    public static javax.swing.JButton jButton23;
    public static javax.swing.JButton jButton7;
    public static javax.swing.JCheckBox jCCash;
    public static javax.swing.JCheckBox jCCheque;
    public static javax.swing.JCheckBox jCCredit;
    public static javax.swing.JComboBox jCInvo_customer;
    public static javax.swing.JComboBox jCInvo_item;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    public static javax.swing.JTextField jTBalance;
    public static javax.swing.JTextField jTCash_amount;
    public static javax.swing.JTextField jTCash_settle;
    public static javax.swing.JTextField jTCheque_amount;
    public static javax.swing.JTextField jTItem_count;
    public static javax.swing.JTextField jTNet_amount;
    public static javax.swing.JTextField jTSub_total;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTextField jTextField3;
    public static javax.swing.JTextField jTextField4;
    public static javax.swing.JTextField jTextField5;
    public static javax.swing.JTextField jTinvoice_no;
    // End of variables declaration//GEN-END:variables
}
