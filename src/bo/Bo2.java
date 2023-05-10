package bo;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bo2 extends JPanel {

    private JTable table;
    private JTextField textField;
    private JTextField textField_2;
    private JTextField textField_3;
    DefaultTableModel model;
    int i=0;
    private JTextField textField_7;

    private BoService bo2Service = new BoService(2);

    /**
     * Create the panel.
     */
    public Bo2() throws SQLException {
        setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(318, 71, 545, 518);
        add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        model = new DefaultTableModel();

        Object[] column= {"ID","Product","Date","QTY","Tax","sync","UPDATE","DELETE"};
        Object[] row =new Object[8];
        model.setColumnIdentifiers(column);
        table.setModel(model);
        ArrayList<Product> initialProducts = bo2Service.getAllProducts();
        for(Product product : initialProducts){
            row[0]=i;
            row[1]=product.product;
            row[2]=product.date;
            row[3]=product.qty;
            row[4]=product.cost;
            row[5]=product.isSynchronized;
            row[6]="Update";
            row[7]="Delete";
            model.addRow(row);
            i++;
        }
        JLabel lblNewLabel_1_2_1 = new JLabel("Product");
        lblNewLabel_1_2_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 25));
        lblNewLabel_1_2_1.setBounds(-1, 74, 134, 40);
        add(lblNewLabel_1_2_1);

        JLabel lblNewLabel_1_2_1_1 = new JLabel("QTY");
        lblNewLabel_1_2_1_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 25));
        lblNewLabel_1_2_1_1.setBounds(-1, 229, 141, 40);
        add(lblNewLabel_1_2_1_1);

        JLabel lblNewLabel_1_1 = new JLabel("Date");
        lblNewLabel_1_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 25));
        lblNewLabel_1_1.setBounds(-1, 151, 134, 40);
        add(lblNewLabel_1_1);

        textField = new JTextField();
        textField.setFont(new Font("Verdana Pro", Font.PLAIN, 22));
        textField.setColumns(10);
        textField.setBounds(115, 150, 192, 40);
        add(textField);

        textField_2 = new JTextField();
        textField_2.setFont(new Font("Verdana Pro", Font.PLAIN, 22));
        textField_2.setColumns(10);
        textField_2.setBounds(115, 74, 192, 40);
        add(textField_2);

        textField_3 = new JTextField();
        textField_3.setFont(new Font("Verdana Pro", Font.PLAIN, 22));
        textField_3.setColumns(10);
        textField_3.setBounds(116, 228, 192, 40);
        add(textField_3);

        JButton btnAdd = new JButton("add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Date=textField.getText();
                String Product=textField_2.getText();
                String Qty=textField_3.getText();
                String cost=textField_7.getText();
                Product newProduct = new Product(i,Date,Product,Integer.parseInt(Qty),Double.parseDouble(cost),false);
                try {
                    bo2Service.createDbRow(newProduct);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                row[0]=i;
                row[1]=Product;
                row[2]=Date;
                row[3]=Qty;
                row[4]=cost;
                row[5]="false";
                row[6]="Update";
                row[7]="Delete";
                model.addRow(row);
                i++;
            }}
        );
        btnAdd.setForeground(new Color(95, 158, 160));
        btnAdd.setFont(new Font("Segoe UI Black", Font.ITALIC, 20));
        btnAdd.setBackground(Color.CYAN);
        btnAdd.setBounds(0, 509, 308, 35);
        add(btnAdd);

        JButton btnClear = new JButton("clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText("");

                textField_2.setText("");
                textField_3.setText("");
                textField_7.setText("");
            }
        });
        btnClear.setForeground(new Color(95, 158, 160));
        btnClear.setFont(new Font("Segoe UI Black", Font.ITALIC, 20));
        btnClear.setBackground(Color.CYAN);
        btnClear.setBounds(0, 554, 308, 35);
        add(btnClear);


        JButton btnGetListe = new JButton("Synchronize");
        btnGetListe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bo2Service.synchronizeDb();
            }
        });
        btnGetListe.setForeground(new Color(95, 158, 160));
        btnGetListe.setFont(new Font("Segoe UI Black", Font.ITALIC, 20));
        btnGetListe.setBackground(Color.CYAN);
        btnGetListe.setBounds(607, 26, 231, 35);
        add(btnGetListe);

        textField_7 = new JTextField();
        textField_7.setFont(new Font("Verdana Pro", Font.PLAIN, 22));
        textField_7.setColumns(10);
        textField_7.setBounds(116, 312, 192, 40);
        add(textField_7);

        JLabel lblNewLabel_1_2_2_1 = new JLabel("TAX");
        lblNewLabel_1_2_2_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 25));
        lblNewLabel_1_2_2_1.setBounds(0, 311, 118, 40);
        add(lblNewLabel_1_2_2_1);

        JButton btnGetList = new JButton("Get List");
        btnGetList.setForeground(new Color(95, 158, 160));
        btnGetList.setFont(new Font("Segoe UI Black", Font.ITALIC, 20));
        btnGetList.setBackground(Color.CYAN);
        btnGetList.setBounds(335, 26, 231, 35);
        btnGetList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Product> initialProducts = null;
                try {
                    initialProducts = bo2Service.getAllProducts();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                model.setRowCount(0);
                for(Product product : initialProducts){
                    row[0]=product.id;
                    row[1]=product.product;
                    row[2]=product.date;
                    row[3]=product.qty;
                    row[4]=product.cost;
                    row[5]=product.isSynchronized;
                    row[6]="Update";
                    row[7]="Delete";
                    model.addRow(row);
                }
            }
        });
        add(btnGetList);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(67);
        table.getColumnModel().getColumn(2).setPreferredWidth(85);
        table.getColumnModel().getColumn(3).setPreferredWidth(88);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 6) {
                    try {
                        System.out.println(" "+ model.getValueAt(row,0)+
                         model.getValueAt(row,1)+
                         model.getValueAt(row,2)+
                         model.getValueAt(row,3)+
                         model.getValueAt(row,4)+
                        false);
                        bo2Service.updateDb(new Product(
                                Integer.parseInt( model.getValueAt(row,0).toString()),
                                (String) model.getValueAt(row,1).toString(),
                                (String) model.getValueAt(row,2).toString(),
                                Integer.parseInt(model.getValueAt(row,3).toString()),
                                Double.parseDouble((model.getValueAt(row,4)).toString()),
                                false
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (col == 7) {
                    int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this row?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);

                    
                    if (confirmed == JOptionPane.YES_OPTION) {
                        try {
                            bo2Service.deleteFromDb(row);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        model.removeRow(row);
                    }
                }
            }
        });
    }
    public static void main(String [] args) throws SQLException {
        JFrame frame = new JFrame("Bo 2");
        frame.setContentPane(new Bo2());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}