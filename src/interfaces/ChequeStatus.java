/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANURUDDHA
 */
public class ChequeStatus extends javax.swing.JInternalFrame {

    /**
     * Creates new form ChequeStatus
     */
    public ChequeStatus() {
        initComponents();
        chequeDetails();
    }

    void chequeDetails() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            d.setRowCount(0);
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
        } catch (Exception e) {
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Cheque Status");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Return");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("View Details");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            String status = d.getValueAt(jTable1.getSelectedRow(), 5).toString();

            String id = d.getValueAt(jTable1.getSelectedRow(), 0).toString();
            String bank = d.getValueAt(jTable1.getSelectedRow(), 1).toString();
            String ino = d.getValueAt(jTable1.getSelectedRow(), 4).toString();
            double amount = Double.parseDouble(d.getValueAt(jTable1.getSelectedRow(), 3).toString());
            ResultSet rs_check = JDBC.getdata("SELECT * FROM return_ability WHERE cheque_no='" + id + "'");
            if (rs_check.next()) {
                JOptionPane.showMessageDialog(null, "You cant return this cheque,some items reguarding this cheque has returned", "Input eror", JOptionPane.ERROR_MESSAGE);
            } else if (status.equals("Received")) {
                ResultSet rs_invoice_info = JDBC.getdata("SELECT * FROM invoice_info WHERE invoice_no='" + ino + "'");
                String cid = "";
                while (rs_invoice_info.next()) {
                    cid = rs_invoice_info.getString("cid");
                }
                //return entry
                Account.debit(cid, ino, amount, QuickDateTime.date());
                Account.credit(bank, ino, amount, QuickDateTime.date());

                JDBC.putdata("UPDATE cheque_info SET status='Return' WHERE cheque_no='" + id + "'");
                chequeDetails();
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
                chequeDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No item selected", "Input eror", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
