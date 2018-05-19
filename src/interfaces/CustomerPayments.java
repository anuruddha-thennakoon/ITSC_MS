/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANURUDDHA
 */
public class CustomerPayments extends javax.swing.JInternalFrame {

    /**
     * Creates new form CustomerPayments
     */
    public CustomerPayments() {
        initComponents();
        creditDetails();
    }

    void viewChequeDetails() {
        if (jRall.isSelected()) {
            try {
                DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
                d.setRowCount(0);
                ResultSet rs_check = JDBC.getdata("SELECT * FROM cheque_info WHERE body='Customer'");
                if (rs_check.next()) {
                    ResultSet rs = JDBC.getdata("SELECT * FROM cheque_info WHERE body='Customer'");
                    while (rs.next()) {
                        Vector v = new Vector();
                        v.add(rs.getString("cheque_no"));
                        v.add(rs.getString("bank_bid"));
                        v.add(rs.getString("date"));
                        v.add(rs.getString("amount"));
                        v.add(rs.getString("invoice_no"));
                        v.add(rs.getString("status"));
                        d.addRow(v);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (jRreturn.isSelected()) {
            try {
                DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
                d.setRowCount(0);
                ResultSet rs_check = JDBC.getdata("SELECT * FROM cheque_info WHERE status='Return' AND body='Customer'");
                if (rs_check.next()) {
                    ResultSet rs = JDBC.getdata("SELECT * FROM cheque_info WHERE status='Return' AND body='Customer'");
                    while (rs.next()) {
                        Vector v = new Vector();
                        v.add(rs.getString("cheque_no"));
                        v.add(rs.getString("bank_bid"));
                        v.add(rs.getString("date"));
                        v.add(rs.getString("amount"));
                        v.add(rs.getString("invoice_no"));
                        v.add(rs.getString("status"));
                        d.addRow(v);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (jRreceived.isSelected()) {
            try {
                DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
                d.setRowCount(0);
                ResultSet rs_check = JDBC.getdata("SELECT * FROM cheque_info WHERE status='Received' AND body='Customer'");
                if (rs_check.next()) {
                    ResultSet rs = JDBC.getdata("SELECT * FROM cheque_info WHERE status='Received' AND body='Customer'");
                    while (rs.next()) {
                        Vector v = new Vector();
                        v.add(rs.getString("cheque_no"));
                        v.add(rs.getString("bank_bid"));
                        v.add(rs.getString("date"));
                        v.add(rs.getString("amount"));
                        v.add(rs.getString("invoice_no"));
                        v.add(rs.getString("status"));
                        d.addRow(v);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No cheques found", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    void creditDetails() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();
            d.setRowCount(0);
//            double invo_amount = 0;
//            double cash_amount = 0;
//            double credit_amount = 0;
//            String invo_no = "";
//            ResultSet rs_cash_amount = JDBC.getdata("select invoice_no,sum(amount) as sum from customer_payments where method='cash' group by invoice_no");
//            ResultSet rs_credit_amount = JDBC.getdata("select invoice_no,sum(amount) as sum from customer_payments where method='credit' group by invoice_no");

            ResultSet rs_credits = JDBC.getdata("SELECT invoice_info.invoice_no, invoice_info.total_amount-Sum(customer_payments.amount) AS balance "
                    + "FROM (customer_payments INNER JOIN invoice_info ON customer_payments.invoice_no=invoice_info.invoice_no) "
                    + "GROUP BY invoice_no "
                    + "HAVING balance>0");

            while (rs_credits.next()) {
                Vector v = new Vector();
                v.add(rs_credits.getString("invoice_no"));
                v.add(rs_credits.getString("balance"));
                d.addRow(v);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jRreceived = new javax.swing.JRadioButton();
        jRreturn = new javax.swing.JRadioButton();
        jRall = new javax.swing.JRadioButton();

        setClosable(true);
        setTitle("Customer Payments");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Amount"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Pay");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setText("View Details");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Credit Details", jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cheque Number", "Bank ", "Date", "Amount", "Invoice No", "Status"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("View Details");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Return");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRreceived);
        jRreceived.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRreceived.setText("Received");
        jRreceived.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRreceivedActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRreturn);
        jRreturn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRreturn.setText("Return");
        jRreturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRreturnActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRall);
        jRall.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRall.setText("All");
        jRall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRallActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jRall)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRreturn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRreceived)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRreceived)
                    .addComponent(jRreturn)
                    .addComponent(jRall))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cheque Details", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            String status = d.getValueAt(jTable1.getSelectedRow(), 5).toString();
            if (status.equals("Received")) {
                jButton1.setText("Return");
            } else {
                jButton1.setText("Received");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //        try {
        //            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
        //            String status = d.getValueAt(jTable1.getSelectedRow(), 5).toString();
        //            String id = d.getValueAt(jTable1.getSelectedRow(), 0).toString();
        //            String aid = d.getValueAt(jTable1.getSelectedRow(), 1).toString();
        //            String ino = d.getValueAt(jTable1.getSelectedRow(), 4).toString();
        //            if (status.equals("Received")) {
        //                JDBC.putdata("UPDATE cheque_info SET status='Return' WHERE cheque_no='" + id + "'");
        //                ResultSet rs_account = JDBC.getdata("SELECT * FROM account WHERE aid='" + aid + "' AND ref_id='" + ino + "'");
        //
        //                String account_id = "";
        //                String account_aid = aid;
        //
        //                while (rs_account.next()) {
        //                    account_id = rs_account.getString("id");
        //                }
        //
        //                JDBC.putdata("INSERT INTO account_ref VALUES(0,'" + account_id + "','" + account_aid + "','" + ino + "')");
        //
        //                ResultSet rs_invoice_info = JDBC.getdata("SELECT * FROM invoice_info WHERE invoice_no='" + ino + "'");
        //
        //                String cid = "";
        //                while (rs_invoice_info.next()) {
        //                    cid = rs_invoice_info.getString("cid");
        //                }
        //                System.out.println(cid);
        //                JDBC.putdata("UPDATE account SET aid='" + cid + "' WHERE id='" + account_id + "'");
        //                chequeDetails();
        //            } else {
        //                JDBC.putdata("UPDATE cheque_info SET status='Received' WHERE cheque_no='" + id + "'");
        //                String account_id = "";
        //                ResultSet rs_account = JDBC.getdata("SELECT * FROM account_ref WHERE ino='" + ino + "'");
        //                while (rs_account.next()) {
        //                    account_id = rs_account.getString("account_id");
        //                }
        //
        //                String cash_account = "DSKH15000001";
        //                JDBC.putdata("UPDATE account SET aid='" + cash_account + "' WHERE id='" + account_id + "'");
        //                chequeDetails();
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //            JOptionPane.showMessageDialog(null, "No item selected", "Input eror", JOptionPane.ERROR_MESSAGE);
        //        }

        try {
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            if (d.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No cheques available", "Input eror", JOptionPane.ERROR_MESSAGE);
            } else {
                String status = d.getValueAt(jTable1.getSelectedRow(), 5).toString();

                String id = d.getValueAt(jTable1.getSelectedRow(), 0).toString();
                String bank = d.getValueAt(jTable1.getSelectedRow(), 1).toString();
                String ino = d.getValueAt(jTable1.getSelectedRow(), 4).toString();
                double amount = Double.parseDouble(d.getValueAt(jTable1.getSelectedRow(), 3).toString());
                if (status.equals("Received")) {
                    ResultSet rs_invoice_info = JDBC.getdata("SELECT * FROM invoice_info WHERE invoice_no='" + ino + "'");
                    String cid = "";
                    while (rs_invoice_info.next()) {
                        cid = rs_invoice_info.getString("cid");
                    }
                    //return entry
                    Account.debit(cid, ino, amount, QuickDateTime.date());
                    Account.credit(bank, ino, amount, QuickDateTime.date());

                    JDBC.putdata("UPDATE cheque_info SET status='Return' WHERE cheque_no='" + id + "'");
                    viewChequeDetails();
                } else {
                    ResultSet rs_invoice_info = JDBC.getdata("SELECT * FROM invoice_info WHERE invoice_no='" + ino + "'");
                    String cid = "";
                    while (rs_invoice_info.next()) {
                        cid = rs_invoice_info.getString("cid");
                    }

                    String cash_account = "DSKH15000001";
                    //received cash
                    Account.debit(cash_account, ino, amount, QuickDateTime.date());
                    Account.credit(cid, ino, amount, QuickDateTime.date());

                    JDBC.putdata("UPDATE cheque_info SET status='Received' WHERE cheque_no='" + id + "'");
                    viewChequeDetails();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No item selected", "Input eror", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRallActionPerformed
        viewChequeDetails();
    }//GEN-LAST:event_jRallActionPerformed

    private void jRreturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRreturnActionPerformed
        viewChequeDetails();
    }//GEN-LAST:event_jRreturnActionPerformed

    private void jRreceivedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRreceivedActionPerformed
        viewChequeDetails();
    }//GEN-LAST:event_jRreceivedActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            DefaultTableModel d = (DefaultTableModel) jTable2.getModel();
            if (d.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No records available", "Input eror", JOptionPane.ERROR_MESSAGE);
            } else {
                double correct_amount = 0;
                String spamount = JOptionPane.showInputDialog(null, "Enter amount", "");
                if (spamount.equals(null)) {
                    JOptionPane.showMessageDialog(null, "Enter amount in correct format", "Input eror", JOptionPane.ERROR_MESSAGE);
                } else {
                    double dpmaount = Double.parseDouble(spamount);
                    double amount = Double.parseDouble(d.getValueAt(jTable2.getSelectedRow(), 1).toString());

                    if (dpmaount < amount) {
                        correct_amount = dpmaount;
                    } else {
                        correct_amount = amount;
                    }

                    String ino = d.getValueAt(jTable2.getSelectedRow(), 0).toString();

                    String cash_account = "DSKH15000001";
                    ResultSet rs_invoice_info = JDBC.getdata("SELECT * FROM invoice_info WHERE invoice_no='" + ino + "'");
                    String cid = "";
                    while (rs_invoice_info.next()) {
                        cid = rs_invoice_info.getString("cid");
                    }

                    //pay credits
                    Account.debit(cash_account, ino, correct_amount, QuickDateTime.date());
                    Account.credit(cid, ino, correct_amount, QuickDateTime.date());

//                    ResultSet rs_customer_payment = JDBC.getdata("SELECT * FROM customer_payments WHERE invoice_no='" + ino + "'");
//                    String id = "";
//                    while (rs_customer_payment.next()) {
//                        cid = rs_invoice_info.getString("cid");
//                    }

                    JDBC.putdata("INSERT INTO customer_payments VALUES(0,'" + ino + "','" + correct_amount + "','Cash')");
                    creditDetails();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Enter amount in correct format", "Input eror", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
//            JOptionPane.showMessageDialog(null, "Enter a amount", "Input eror", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(CustomerPayments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRall;
    private javax.swing.JRadioButton jRreceived;
    private javax.swing.JRadioButton jRreturn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
