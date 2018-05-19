/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
public class ProfitLoss extends javax.swing.JInternalFrame {

    /**
     * Creates new form ProfitLoss
     */
    public ProfitLoss() {
        initComponents();
    }

    void cal() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            double sales = 0;
            double opening_stock = 0;
            double purchase = 0;
            double closing_stock = 0;
            double opening_stock_plus_purchase = 0;
            double opening_stock_plus_purchase_minus_closing_stock = 0;
            double gross_profit = 0;
            double expenses = 0;
            double net_profit = 0;

            Calendar f = dateChooserCombo1.getSelectedDate();
            int fmonth = f.get(Calendar.MONTH) + 1;
            int fdate = f.get(Calendar.DATE);
            int fyear = f.get(Calendar.YEAR);
            String from_date_string = (fyear + "-" + fmonth + "-" + fdate);
            Date from_date_date = df.parse(from_date_string);
            String from_date = df.format(from_date_date);
            System.out.println(from_date);

            Calendar t = dateChooserCombo2.getSelectedDate();
            int tmonth = t.get(Calendar.MONTH) + 1;
            int tdate = t.get(Calendar.DATE);
            int tyear = t.get(Calendar.YEAR);
            String to_date_string = (tyear + "-" + tmonth + "-" + tdate);
            Date to_date_date = df.parse(to_date_string);
            String to_date = df.format(to_date_date);
            System.out.println(to_date);

            //calculate sales for given period
            ResultSet rs_sales = JDBC.getdata("SELECT sum(amount) AS sales FROM account WHERE type='credit' AND aid='DSKT15000001' "
                    + "AND (date BETWEEN '" + from_date + "' AND '" + to_date + "')");
            while (rs_sales.next()) {
                sales = rs_sales.getDouble("sales");
            }
            System.out.println("sales = " + sales);

            //calculate opening stock for given period
            ResultSet rs_opening_stock = JDBC.getdata("SELECT * FROM current_stock WHERE date='" + from_date + "'");
            while (rs_opening_stock.next()) {
                opening_stock = rs_opening_stock.getDouble("amount");
            }
            System.out.println("opening stock = " + opening_stock);

            //calculate purchase for given period
            ResultSet rs_purchase = JDBC.getdata("SELECT sum(amount) AS sales FROM account WHERE type='debit' AND aid='DSKT15000001' "
                    + "AND (date BETWEEN '" + from_date + "' AND '" + to_date + "')");
            while (rs_purchase.next()) {
                purchase = rs_purchase.getDouble("sales");
            }
            System.out.println("purchase = " + purchase);

            //calculate closing stock for given period
            if (to_date.equals(QuickDateTime.date())) {
                ResultSet rs_stock = JDBC.getdata("select sum(qty*unit_price) AS total ,sum(qty) AS sumqty from stock");
                while (rs_stock.next()) {
                    closing_stock = rs_stock.getDouble("total");
                }
                System.out.println("closing stock = " + closing_stock);
            } else {
                ResultSet rs_closing_stock = JDBC.getdata("SELECT * FROM current_stock WHERE date='" + to_date + "'");
                while (rs_closing_stock.next()) {
                    closing_stock = rs_closing_stock.getDouble("amount");
                }
                System.out.println("closing stock = " + closing_stock);
            }
            opening_stock_plus_purchase = opening_stock + purchase;
            opening_stock_plus_purchase_minus_closing_stock = opening_stock_plus_purchase - closing_stock;
            gross_profit = sales - opening_stock_plus_purchase_minus_closing_stock;

            net_profit = gross_profit - expenses;
            System.out.println("gross = " + gross_profit);

            //print report
            ResultSet rs = JDBC.getdata("SELECT * FROM paths");
            String path = "";
            while (rs.next()) {
                path = rs.getString("path");
            }
            System.out.println(path);
//            JasperReport r = JasperCompileManager.compileReport("G:\\DOCUMENTS\\exploit\\PROJECTS\\DSK\\src\\reports\\profitloss.jrxml");
            JasperReport r = JasperCompileManager.compileReport(path + "/profitloss.jrxml");
            HashMap para = new HashMap();
            para.put("sales", sales);
            para.put("opening_stock", opening_stock);
            para.put("purchase", purchase);
            para.put("closing_stock", closing_stock);
            para.put("gross_profit", gross_profit);
            para.put("opening_stock_plus_purchase", opening_stock_plus_purchase);
            para.put("opening_stock_plus_purchase_minus_closing_stock", opening_stock_plus_purchase_minus_closing_stock);
            para.put("expenses", expenses);
            para.put("net_profit", net_profit);
            para.put("from_date", from_date);
            para.put("to_date", to_date);
            JasperPrint jp = JasperFillManager.fillReport(r, para, new JREmptyDataSource());
            JasperViewer.viewReport(jp, false);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel71 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Profit and Loss");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel70.setText("From");

        dateChooserCombo1.setNothingAllowed(false);
        dateChooserCombo1.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));

        dateChooserCombo2.setNothingAllowed(false);
        dateChooserCombo2.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel71.setText("To");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooserCombo2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cal();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
