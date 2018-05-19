/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import static interfaces.Home.jDesktopPane1;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANURUDDHA
 */
public class UpdateStock extends javax.swing.JInternalFrame {

    static DecimalFormat df = new DecimalFormat("00.00");
    public static String bank = "";
    public static String cheque_no;
    public static String date;

    /**
     * Creates new form UpdateStock
     */
    public UpdateStock() {
        initComponents();
        itemCombo();
        supplier();
    }

    void supplier() {
        try {
            ResultSet rs = JDBC.getdata("SELECT * FROM supplier WHERE status='Active'");
            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("supplier_combo"));
            }
            jCGRN_supplier.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
        }
    }

    void itemCombo() {
        try {
            ResultSet rs = JDBC.getdata("SELECT * FROM items WHERE status='Active'");
            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("item_combo"));
            }
            jCGRN_item.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addTable() {
        String iid = null;
        String iname = null;
        try {
            ResultSet rs = JDBC.getdata("SELECT * FROM items WHERE item_combo='" + jCGRN_item.getSelectedItem().toString() + "'");
            while (rs.next()) {
                iid = rs.getString("iid");
                iname = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
        Vector v = new Vector();
        v.add(iid);
        v.add(iname);
        v.add(jTGRN_up.getText());
        v.add(jTGRN_qty.getText());
        v.add(jTGRN_net.getText());
        d.addRow(v);
        jTGRN_qty.setText("");
        jTGRN_up.setText("");
        jTGRN_net.setText("");
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

    void GRN_item_count() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            double item_count = 0;
            double sub_total = 0;
            for (int x = 0; x < d.getRowCount(); x++) {
                item_count = item_count + Double.parseDouble(jTable3.getValueAt(x, 3).toString());
                sub_total = sub_total + Double.parseDouble(jTable3.getValueAt(x, 4).toString());
            }
            jTItem_count.setText(df.format(item_count));
            jTSub_total.setText(df.format(sub_total));
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
                } else if (jCGRN_supplier.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select supplier", "Input error", JOptionPane.ERROR_MESSAGE);
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
                            ResultSet rs = JDBC.getdata("SELECT * FROM stock WHERE iid='" + jTable3.getValueAt(x, 0) + "'");
                            if (rs.next()) {
                                JDBC.putdata("UPDATE stock SET "
                                        + "qty=qty+'" + jTable3.getValueAt(x, 3) + "',"
                                        + "unit_price='" + jTable3.getValueAt(x, 2) + "'"
                                        + "WHERE iid='" + jTable3.getValueAt(x, 0) + "'");
                            } else {
                                JDBC.putdata("INSERT INTO stock VALUES("
                                        + "0,"
                                        + "'" + jTable3.getValueAt(x, 0) + "',"
                                        + "'" + jTable3.getValueAt(x, 1) + "',"
                                        + "'" + jTable3.getValueAt(x, 2) + "',"
                                        + "'" + jTable3.getValueAt(x, 3) + "',"
                                        + "'" + jTable3.getValueAt(x, 0) + "-" + jTable3.getValueAt(x, 1) + "')");
                            }
                        }

                        for (int x = 0; x < d.getRowCount(); x++) {
                            JDBC.putdata("INSERT INTO grn VALUES("
                                    + "'" + jTinvoice_no.getText() + "',"
                                    + "'" + jTable3.getValueAt(x, 0) + "',"
                                    + "'" + jTable3.getValueAt(x, 3) + "',"
                                    + "'" + jTable3.getValueAt(x, 4) + "')");
                        }

                        String sid = null;
                        ResultSet rs = JDBC.getdata("SELECT * FROM supplier WHERE supplier_combo='" + jCGRN_supplier.getSelectedItem() + "'");
                        while (rs.next()) {
                            sid = rs.getString("sid");
                        }

                        JDBC.putdata("INSERT INTO grn_info VALUES("
                                + "'" + jTinvoice_no.getText() + "',"
                                + "'" + QuickDateTime.date() + "',"
                                + "'" + jTItem_count.getText() + "',"
                                + "'" + jTNet_amount.getText() + "',"
                                + "'" + sid + "')");

                        String cash_account = "DSKH15000001";
                        String stock_account = "DSKT15000001";

                        double na = Double.parseDouble(jTNet_amount.getText().trim());
                        double ca = Double.parseDouble(jTCash_amount.getText().trim());
                        double cha = Double.parseDouble(jTCheque_amount.getText().trim());
                        double ba = Double.parseDouble(jTBalance.getText().trim());

                        if (jCCash.isSelected() && !(jCCheque.isSelected()) && !(jCCredit.isSelected())) {
                            //only cash
                            Account.debit(stock_account, jTinvoice_no.getText(), na, QuickDateTime.date());
                            Account.credit(cash_account, jTinvoice_no.getText(), na, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), na, jCCash.getText());

                        } else if (jCCheque.isSelected() && !(jCCash.isSelected()) && !(jCCredit.isSelected())) {
                            //only cheque
                            Account.debit(stock_account, jTinvoice_no.getText(), na, QuickDateTime.date());
                            Account.credit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), cha, jCCheque.getText());

                        } else if (jCCredit.isSelected() && !(jCCheque.isSelected()) && !(jCCash.isSelected())) {
                            //only credit
                            Account.debit(stock_account, jTinvoice_no.getText(), na, QuickDateTime.date());
                            Account.credit(sid, jTinvoice_no.getText(), na, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), 0, jCCash.getText());
                        } else if (jCCash.isSelected() && jCCredit.isSelected() && !(jCCheque.isSelected())) {
                            //cash
                            Account.debit(stock_account, jTinvoice_no.getText(), ca, QuickDateTime.date());
                            Account.credit(cash_account, jTinvoice_no.getText(), ca, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), ca, jCCash.getText());
                            //credit
                            Account.debit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(sid, jTinvoice_no.getText(), ba, QuickDateTime.date());

                        } else if (jCCash.isSelected() && jCCheque.isSelected() && !(jCCredit.isSelected())) {
                            //cash
                            Account.debit(stock_account, jTinvoice_no.getText(), (na - cha - ba), QuickDateTime.date());
                            Account.credit(cash_account, jTinvoice_no.getText(), (na - cha - ba), QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), (na - cha - ba), jCCash.getText());
                            //cheque
                            Account.debit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                        } else if (jCCredit.isSelected() && jCCheque.isSelected() && !(jCCash.isSelected())) {
                            //cheque
                            Account.debit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            //credit
                            Account.debit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(sid, jTinvoice_no.getText(), ba, QuickDateTime.date());
                        } else if (jCCredit.isSelected() && jCCheque.isSelected() && jCCash.isSelected()) {
                            //cash
                            Account.debit(stock_account, jTinvoice_no.getText(), ca, QuickDateTime.date());
                            Account.credit(cash_account, jTinvoice_no.getText(), ca, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), ca, jCCash.getText());
                            //cheque
                            Account.debit(stock_account, jTinvoice_no.getText(), cha, QuickDateTime.date());
                            Account.credit(bank, jTinvoice_no.getText(), cha, QuickDateTime.date());

                            Account.supplierPayments(jTinvoice_no.getText(), cha, jCCheque.getText());
                            //credit
                            Account.debit(stock_account, jTinvoice_no.getText(), ba, QuickDateTime.date());
                            Account.credit(sid, jTinvoice_no.getText(), ba, QuickDateTime.date());
                        }

                        d.setRowCount(0);
                        jTItem_count.setText("00.00");
                        jTSub_total.setText("00.00");
                        jTCash_settle.setText("00.00");
                        jTNet_amount.setText("00.00");
                        jTCash_amount.setText("00.00");
                        jTCheque_amount.setText("00.00");
                        jTBalance.setText("00.00");
                        jTinvoice_no.setText("");

                        jCCash.setSelected(false);
                        jCCheque.setSelected(false);
                        jCCredit.setSelected(false);
                    }
                }
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
        jPanel6 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        jTItem_count = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTGRN_qty = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jCGRN_item = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jTGRN_up = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTGRN_net = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jTinvoice_no = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jCGRN_supplier = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTNet_amount = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTCash_settle = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTSub_total = new javax.swing.JTextField();
        jCCredit = new javax.swing.JCheckBox();
        jCCash = new javax.swing.JCheckBox();
        jCCheque = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jTBalance = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTCash_amount = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTCheque_amount = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jButton23 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Update Stock");

        NewInvoice1.setBackground(new java.awt.Color(255, 255, 255));
        NewInvoice1.setMaximumSize(new java.awt.Dimension(1364, 655));
        NewInvoice1.setMinimumSize(new java.awt.Dimension(1364, 655));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMaximumSize(new java.awt.Dimension(1350, 212));
        jPanel6.setMinimumSize(new java.awt.Dimension(1350, 212));
        jPanel6.setOpaque(false);

        jButton17.setText("DELETE");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Name", "Unit Price", "Qty", "Net Amount(Rs)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setText("Item Count");

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(46, 46, 46)
                        .addComponent(jTItem_count, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1069, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTItem_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1350, 48));
        jPanel2.setMinimumSize(new java.awt.Dimension(1350, 48));
        jPanel2.setOpaque(false);

        jTGRN_qty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTGRN_qty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTGRN_qtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTGRN_qtyFocusLost(evt);
            }
        });
        jTGRN_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTGRN_qtyKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Qty");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setText("Item");

        jCGRN_item.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCGRN_item.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCGRN_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCGRN_itemActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Unit Price(Rs)");

        jTGRN_up.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTGRN_up.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTGRN_upFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTGRN_upFocusLost(evt);
            }
        });
        jTGRN_up.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTGRN_upKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTGRN_upKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Net Amount (Rs)");

        jTGRN_net.setEditable(false);
        jTGRN_net.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTGRN_net.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTGRN_netFocusLost(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton7.setText("Add");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(45, 45, 45)
                .addComponent(jCGRN_item, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTGRN_up, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTGRN_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTGRN_net, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCGRN_item, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTGRN_up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTGRN_qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTGRN_net, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setMaximumSize(new java.awt.Dimension(280, 129));
        jPanel30.setMinimumSize(new java.awt.Dimension(280, 129));
        jPanel30.setName(""); // NOI18N
        jPanel30.setOpaque(false);

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel67.setText("Invoice No");

        jTinvoice_no.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel69.setText("Supplier ");

        jCGRN_supplier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCGRN_supplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCGRN_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCGRN_supplierActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel69)
                .addGap(43, 43, 43)
                .addComponent(jCGRN_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(402, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel69)
                    .addComponent(jTinvoice_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(jCGRN_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setOpaque(false);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Net Amount(Rs)");

        jTNet_amount.setEditable(false);
        jTNet_amount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTNet_amount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTNet_amount.setText("00.00");
        jTNet_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTNet_amountActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Cash Settled (Rs)");

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

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Sub Total (Rs)");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTSub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTCash_settle, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTNet_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jCCash)
                .addGap(18, 18, 18)
                .addComponent(jCCredit)
                .addGap(10, 10, 10)
                .addComponent(jCCheque)
                .addGap(17, 17, 17))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTSub_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTCash_settle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTNet_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCCredit)
                    .addComponent(jCCash)
                    .addComponent(jCCheque))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setText("Cash Amount(Rs)");

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

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setText("Balance(Rs)");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setText("Cheque Amount(Rs)");

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
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTCash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTCheque_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(290, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTCash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel24)
                        .addComponent(jTBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTCheque_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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
                    .addGroup(NewInvoice1Layout.createSequentialGroup()
                        .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(NewInvoice1Layout.createSequentialGroup()
                        .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 1083, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NewInvoice1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        NewInvoice1Layout.setVerticalGroup(
            NewInvoice1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewInvoice1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NewInvoice1, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NewInvoice1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
            int ii = Integer.parseInt(jTable3.getValueAt(jTable3.getSelectedRow(), 3).toString().trim());
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            if (d.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No data entered", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected item?", "", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    d.removeRow(jTable3.getSelectedRow());
                    GRN_item_count();
                    setZero();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No item selected", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTGRN_qtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTGRN_qtyFocusGained
        try {
            jTGRN_net.setText("");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTGRN_qtyFocusGained

    private void jTGRN_qtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTGRN_qtyFocusLost
        try {
            double up = Double.parseDouble(jTGRN_up.getText());
            double qty = Double.parseDouble(jTGRN_qty.getText());
            jTGRN_net.setText(df.format(up * qty));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTGRN_qtyFocusLost

    private void jTGRN_qtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTGRN_qtyKeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.')) {
        } else {
            evt.consume();
        }
        String s = jTGRN_qty.getText();
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
    }//GEN-LAST:event_jTGRN_qtyKeyTyped

    private void jCGRN_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCGRN_itemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCGRN_itemActionPerformed

    private void jTGRN_upFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTGRN_upFocusGained
        try {
            jTGRN_net.setText("");
            jTGRN_qty.setText("");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTGRN_upFocusGained

    private void jTGRN_upFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTGRN_upFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTGRN_upFocusLost

    private void jTGRN_upKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTGRN_upKeyReleased
        //
    }//GEN-LAST:event_jTGRN_upKeyReleased

    private void jTGRN_upKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTGRN_upKeyTyped
        char c = evt.getKeyChar();
        if ((Character.isDigit(c)) || (c == '.')) {
        } else {
            evt.consume();
        }
        String s = jTGRN_up.getText();
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
    }//GEN-LAST:event_jTGRN_upKeyTyped

    private void jTGRN_netFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTGRN_netFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTGRN_netFocusLost

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            if (jTGRN_up.getText().isEmpty() || jTGRN_qty.getText().isEmpty() || jTGRN_net.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Some fields are empty", "Input error", JOptionPane.ERROR_MESSAGE);
            } else {
                ResultSet rs = JDBC.getdata("SELECT * FROM items WHERE item_combo='" + jCGRN_item.getSelectedItem().toString() + "'");
                String iid = "";
                while (rs.next()) {
                    iid = rs.getString("iid");
                }
                if (checkDuplicate(0, jTable3, iid)) {
                    JOptionPane.showMessageDialog(null, "This item is already entered", "Input error", JOptionPane.ERROR_MESSAGE);
                } else {
                    addTable();
                    GRN_item_count();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCGRN_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCGRN_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCGRN_supplierActionPerformed

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
                cal();
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

    private void jTItem_countFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTItem_countFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countFocusGained

    private void jTItem_countFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTItem_countFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countFocusLost

    private void jTItem_countKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTItem_countKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTItem_countKeyTyped

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        try {
            if (jCCheque.isSelected()) {
                if (jTinvoice_no.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter invoice number", "Input error", JOptionPane.ERROR_MESSAGE);
                } else if (jCGRN_supplier.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select supplier", "Input error", JOptionPane.ERROR_MESSAGE);
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
                    ChequeDetailsUpdateStock cdus = new ChequeDetailsUpdateStock();
                    cdus.jTAmount.setText(df.format(cha));
                    Dimension desktopSize = jDesktopPane1.getSize();
                    Dimension jInternalFrameSize = cdus.getSize();
                    cdus.setLocation((desktopSize.width - jInternalFrameSize.width) / 2, (desktopSize.height - jInternalFrameSize.height) / 2);
                    Home.jDesktopPane1.add(cdus).setVisible(true);
                }
            } else {
                toDB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton23ActionPerformed

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
                cal();
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

    private void jCCashItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCCashItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCCashItemStateChanged

    private void jCCreditItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCCreditItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCCreditItemStateChanged

    private void jCChequeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCChequeItemStateChanged
        textfieldVisible();
    }//GEN-LAST:event_jCChequeItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NewInvoice1;
    public static javax.swing.JButton jButton17;
    public static javax.swing.JButton jButton23;
    public static javax.swing.JButton jButton7;
    public static javax.swing.JCheckBox jCCash;
    public static javax.swing.JCheckBox jCCheque;
    public static javax.swing.JCheckBox jCCredit;
    public static javax.swing.JComboBox jCGRN_item;
    public static javax.swing.JComboBox jCGRN_supplier;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    public static javax.swing.JTextField jTBalance;
    public static javax.swing.JTextField jTCash_amount;
    public static javax.swing.JTextField jTCash_settle;
    public static javax.swing.JTextField jTCheque_amount;
    public static javax.swing.JTextField jTGRN_net;
    public static javax.swing.JTextField jTGRN_qty;
    public static javax.swing.JTextField jTGRN_up;
    public static javax.swing.JTextField jTItem_count;
    public static javax.swing.JTextField jTNet_amount;
    public static javax.swing.JTextField jTSub_total;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTextField jTinvoice_no;
    // End of variables declaration//GEN-END:variables
}
