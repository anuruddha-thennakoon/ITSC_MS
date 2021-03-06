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
public class ViewStock extends javax.swing.JInternalFrame {

    /**
     * Creates new form ViewStock
     */
    public ViewStock() {
        initComponents();
    }

    void viewAllStock() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            d.setRowCount(0);
            ResultSet rs_check = JDBC.getdata("SELECT * FROM stock WHERE qty>0");
            if (rs_check.next()) {
                ResultSet rs_stock = JDBC.getdata("SELECT * FROM stock WHERE qty>0");
                while (rs_stock.next()) {
                    Vector v = new Vector();
                    v.add(rs_stock.getString("iid"));
                    v.add(rs_stock.getString("name"));
                    v.add(rs_stock.getString("qty"));
                    v.add(rs_stock.getString("unit_price"));
                    d.addRow(v);
                }
                jTir_iid.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No item found", "Input error", JOptionPane.ERROR_MESSAGE);
                jTir_iid.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No item found", "Input error", JOptionPane.ERROR_MESSAGE);
            jTir_iid.setText("");
        }
    }

    void viewSelectedStock(String s) {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable3.getModel();
            d.setRowCount(0);
            ResultSet rs_check = JDBC.getdata("SELECT * FROM stock WHERE qty>0 AND " + s + " LIKE '%" + jTir_iid.getText().trim() + "%'");
            if (rs_check.next()) {
                ResultSet rs_stock = JDBC.getdata("SELECT * FROM stock WHERE qty>0 AND " + s + " LIKE '%" + jTir_iid.getText().trim() + "%'");
                while (rs_stock.next()) {
                    Vector v = new Vector();
                    v.add(rs_stock.getString("iid"));
                    v.add(rs_stock.getString("name"));
                    v.add(rs_stock.getString("qty"));
                    v.add(rs_stock.getString("unit_price"));
                    d.addRow(v);
                }
                jTir_iid.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No item found", "Input error", JOptionPane.ERROR_MESSAGE);
                jTir_iid.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No item found", "Input error", JOptionPane.ERROR_MESSAGE);
            jTir_iid.setText("");
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTir_iid = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jRiid = new javax.swing.JRadioButton();
        jRname = new javax.swing.JRadioButton();

        setClosable(true);
        setTitle("View Stock");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Item Name", "Qty", "Unit Price(Rs)"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jTir_iid.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("View all");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("View");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRiid);
        jRiid.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRiid.setSelected(true);
        jRiid.setText("Item No");
        jRiid.setContentAreaFilled(false);

        buttonGroup1.add(jRname);
        jRname.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRname.setText("Name");
        jRname.setContentAreaFilled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRiid)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTir_iid, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTir_iid)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jRiid)
                    .addComponent(jRname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            viewAllStock();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTir_iid.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some fields are empty", "Input error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (jRiid.isSelected()) {
                viewSelectedStock("iid");
            } else {
                viewSelectedStock("name");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRiid;
    private javax.swing.JRadioButton jRname;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTir_iid;
    // End of variables declaration//GEN-END:variables
}
