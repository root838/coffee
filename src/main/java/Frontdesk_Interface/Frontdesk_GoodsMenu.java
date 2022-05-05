/*
 * Created by JFormDesigner on Wed Apr 27 15:05:55 CST 2022
 */

package Frontdesk_Interface;

import bean.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Connection.*;

/**
 * @author 1
 */
public class Frontdesk_GoodsMenu extends JFrame {
    public Frontdesk_GoodsMenu() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        //======== this ========
        final Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setPreferredSize(new Dimension(800,600));
        setTitle("点单界面");

        final DefaultTableModel tableModel = new DefaultTableModel(queryData(), head) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(tableModel);
        // 设置表格中的数据居中显示
        DefaultTableCellRenderer r=new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table1.setDefaultRenderer(Object.class,r);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        //scrollPane1.setBounds(30, 40, 515, 295);
        scrollPane1.setBounds(20, 40, 760, 300);

        //---- button1 -加入购物车---
        button1.setText("\u52a0\u5165\u8d2d\u7269\u8f66");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(55, 350), button1.getPreferredSize()));
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //获取当前选中的行
               int selectedRow = table1.getSelectedRow();

               Connection conn = ConnectionHandler.getConnection();
               try{
                   Object data2[][]=queryData();
                   String pname =(String)data2[selectedRow][1];
                   int pid =Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
                   /* JDBC查询当前 shoppingCar 中有没有该商品
                    * 1.有，则 num=num+1
                    * 2.没有，则查询 product 表，把对应的商品信息查询出来放到购物车
                    */

                   String sql = "select name from shoppingCar where name=?";
                   String sql1 = "select * from stock where pid=? and stock=0";
                   PreparedStatement pstmt = conn.prepareStatement(sql);
                   PreparedStatement pstmt1=conn.prepareStatement(sql1);
                   pstmt.setString(1,pname);
                   pstmt1.setInt(1,pid);
                   ResultSet rs = pstmt.executeQuery();
                   ResultSet rs2=pstmt1.executeQuery();
                   if(rs2.next()){
                       JOptionPane.showMessageDialog(contentPane,"库存为零，无法加入购物车");
                   }else {
                       if(rs.next()){
                           String sql2 = "update shoppingCar set num=num+1 where name=?";
                           PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                           pstmt2.setString(1,pname);
                           pstmt2.executeUpdate();
                           JOptionPane.showMessageDialog(contentPane,"数量加1");
                       }
                       else{
                           float price=0;
                           String sql3 = "select * from product where name=?";
                           String sql4 = "insert into shoppingCar(name,num,price) " +
                                   "values(?,?,?)";
                           PreparedStatement pstmt3 = conn.prepareStatement(sql3);
                           pstmt3.setString(1,pname);
                           ResultSet rs1 = pstmt3.executeQuery();
                           while (rs1.next()){
                               price=rs1.getFloat("price");
                           }

                           PreparedStatement pstmt4 = conn.prepareStatement(sql4);
                           pstmt4.setString(1,pname);
                           pstmt4.setInt(2,1);
                           pstmt4.setFloat(3,price);
                           pstmt4.executeUpdate();
                           JOptionPane.showMessageDialog(contentPane,"加入购物车成功！");
                       }
                   }

               }catch (SQLException r){
                   r.printStackTrace();
               }catch (ArrayIndexOutOfBoundsException m){
                   JOptionPane.showMessageDialog(contentPane,"请选择需要加入购物车的商品！");
                   m.printStackTrace();
               }

            }
        });

        //---- button2 -购物车---
        button2.setText("\u8d2d\u7269\u8f66");
        contentPane.add(button2);
        button2.setBounds(new Rectangle(new Point(660, 355), button2.getPreferredSize()));
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Frontdesk_shoppingCar();
                dispose();
            }
        });

        //---- button3 -返回---
        button3.setText("\u8FD4\u56DE");
        contentPane.add(button3);
        button3.setBounds(new Rectangle(new Point(0, 0), button2.getPreferredSize()));
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Frontdesk_Mainmenu();
            }
        });

        pack();
        setLocationRelativeTo(getOwner());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public Object[][] queryData() {
        Object[][] data = null;
        java.util.List<Product> list = new ArrayList<Product>();
        Connection conn = null;

        Statement stmt = null;
        String sql = "SELECT * FROM product";

        ResultSet rs = null;
        try {
            conn = ConnectionHandler.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {

                Product product = new Product();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getFloat(3));
                product.setCates(rs.getInt(4));

                list.add(product);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

            try {
                rs.close();
                stmt.close();
                conn.close();//??????
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        data = new Object[list.size()][head.length];

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < head.length; j++) {
                data[i][0] = list.get(i).getId();
                data[i][1] = list.get(i).getName();
                data[i][2] = list.get(i).getPrice();
                data[i][3] = list.get(i).getCates();
            }
        }
        return data;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private String head[] = {"id", "商品名称", "单价", "商品分类"};
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        new Frontdesk_GoodsMenu();
    }
}
