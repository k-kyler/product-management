package com.qlbanhang.form;

import com.qlbanhang.dao.SanphamDAO;
import com.qlbanhang.model.Sanpham;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class frmSanpham {
    private JPanel MainPanel;
    private JPanel Header;
    private JPanel InputsContainer;
    private JLabel Title;
    private JTextField ProductNameField;
    private JLabel ProductNameLabel;
    private JLabel ProductIdLabel;
    private JTextField ProductIdField;
    private JTextField ProductPriceField;
    private JLabel ProductPriceLabel;
    private JTable ProductsTable;
    private JPanel ButtonsContainer;
    private JButton AddButton;
    private JButton EditButton;
    private JButton DeleteButton;
    private JButton SaveButton;
    private JButton UnsaveButton;
    private JButton ExitButton;
    private JScrollPane TableInnerContainer;
    private JPanel TableContainer;

    private SanphamDAO sanphamDAO;
    private List<Sanpham> products;
    private ProductsTableModel productsTableModel;
    private Sanpham selectedProduct;
    private String actionMethod = "";

    // Constructor to initial data into form and implement event listener methods
    public frmSanpham() {
        // Fetch all products
        sanphamDAO = new SanphamDAO();
        products = new ArrayList<>();
        products.addAll(sanphamDAO.getAllProducts());
        productsTableModel = new ProductsTableModel(products);
        ProductsTable.setModel(productsTableModel);

        // Auto sort products table rows
        ProductsTable.setAutoCreateRowSorter(true);

        // Set default states of buttons
        AddButton.setEnabled(true);
        EditButton.setEnabled(false);
        DeleteButton.setEnabled(false);
        SaveButton.setEnabled(false);
        UnsaveButton.setEnabled(false);

        // Event listener to select and display selected row data on text fields
        ProductsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!ProductsTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRowIndex = ProductsTable.convertRowIndexToModel(ProductsTable.getSelectedRow());

                selectedProduct = products.get(selectedRowIndex);

                if (selectedProduct != null) {
                    ProductIdField.setText(selectedProduct.getMasp());
                    ProductNameField.setText(selectedProduct.getTensp());
                    ProductPriceField.setText(String.valueOf(selectedProduct.getGiaban()));

                    ProductIdField.setEnabled(false);
                    AddButton.setEnabled(true);
                    EditButton.setEnabled(true);
                    DeleteButton.setEnabled(true);
                    SaveButton.setEnabled(false);
                    UnsaveButton.setEnabled(false);
                }
            }
        });

        // Event listener to set add method
        AddButton.addActionListener(e -> {
            ProductIdField.setEnabled(true);

            actionMethod = "add";

            if (actionMethod == "edit" || actionMethod == "delete") {
                ProductIdField.setText("");
                ProductNameField.setText("");
                ProductPriceField.setText("");
            }

            SaveButton.setEnabled(true);
            UnsaveButton.setEnabled(true);
            AddButton.setEnabled(false);
            EditButton.setEnabled(false);
            DeleteButton.setEnabled(false);
        });

        // Event listener to set edit method
        EditButton.addActionListener(e -> {
            actionMethod = "edit";

            SaveButton.setEnabled(true);
            UnsaveButton.setEnabled(true);
            AddButton.setEnabled(false);
            EditButton.setEnabled(false);
            DeleteButton.setEnabled(false);
        });

        // Event listener to set delete method
        DeleteButton.addActionListener(e -> {
            actionMethod = "delete";

            SaveButton.setEnabled(true);
            UnsaveButton.setEnabled(true);
            AddButton.setEnabled(false);
            EditButton.setEnabled(false);
            DeleteButton.setEnabled(false);
        });

        // Event listener to process form action based on the action method variable
        SaveButton.addActionListener(e -> {
            // Handle add new product process
            if (actionMethod == "add") {
                if (ProductIdField.getText().trim().isEmpty() || ProductNameField.getText().trim().isEmpty() || ProductPriceField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainPanel, "Bạn chưa nhập đủ tất cả thông tin để thêm sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        if (ProductIdField.getText().trim().length() > 6) {
                            JOptionPane.showMessageDialog(MainPanel, "Mã sản phẩm không được quá 6 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else if (ProductNameField.getText().length() > 40) {
                            JOptionPane.showMessageDialog(MainPanel, "Tên sản phẩm không được quá 40 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                        // check product price here...

                        else {
                            // Execute add new product method from DAO
                            sanphamDAO.addNewProduct(new Sanpham(ProductIdField.getText(), ProductNameField.getText(), Integer.valueOf(ProductPriceField.getText())));

                            // Update products list
                            products.clear();
                            products.addAll(sanphamDAO.getAllProducts());
                            productsTableModel.fireTableDataChanged();

                            // Clear form fields
                            ProductIdField.setText("");
                            ProductNameField.setText("");
                            ProductPriceField.setText("");

                            // Set default states of buttons
                            AddButton.setEnabled(true);
                            EditButton.setEnabled(false);
                            DeleteButton.setEnabled(false);
                            SaveButton.setEnabled(false);
                            UnsaveButton.setEnabled(false);

                            // Display successful message
                            JOptionPane.showMessageDialog(MainPanel, "Bạn đã thêm sản phẩm mới", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // Handle edit product process
            else if (actionMethod == "edit") {
                if (ProductNameField.getText().trim().isEmpty() || ProductPriceField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainPanel, "Bạn chưa nhập tên sản phẩm và giá bán để chính sửa sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                     try {
                        if (ProductNameField.getText().length() > 40) {
                            JOptionPane.showMessageDialog(MainPanel, "Tên sản phẩm không được quá 40 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                        // check product price here...

                        else {
                            // Execute edit product method from DAO
                            sanphamDAO.editProduct(new Sanpham(ProductIdField.getText(), ProductNameField.getText(), Integer.valueOf(ProductPriceField.getText())));

                            // Update products list
                            products.clear();
                            products.addAll(sanphamDAO.getAllProducts());
                            productsTableModel.fireTableDataChanged();

                            // Set back buttons state
                             AddButton.setEnabled(true);
                            EditButton.setEnabled(true);
                            DeleteButton.setEnabled(true);
                            SaveButton.setEnabled(false);
                            UnsaveButton.setEnabled(false);

                            // Display successful message
                            JOptionPane.showMessageDialog(MainPanel, "Bạn đã chỉnh sửa sản phẩm", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // Handle delete product process
            else {
                try {
                    // Execute edit product method from DAO
                    sanphamDAO.deleteProduct(ProductIdField.getText());

                    // Update products list
                    products.clear();
                    products.addAll(sanphamDAO.getAllProducts());
                    productsTableModel.fireTableDataChanged();

                    // Enable product id field
                    ProductIdField.setEnabled(true);

                    // Clear form fields
                    ProductIdField.setText("");
                    ProductNameField.setText("");
                    ProductPriceField.setText("");

                    // Set default states of buttons
                    AddButton.setEnabled(true);
                    EditButton.setEnabled(false);
                    DeleteButton.setEnabled(false);
                    SaveButton.setEnabled(false);
                    UnsaveButton.setEnabled(false);

                    // Display successful message
                    JOptionPane.showMessageDialog(MainPanel, "Bạn đã xóa sản phẩm", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Event listener to undo the action method
        UnsaveButton.addActionListener(e -> {
            ProductIdField.setText("");
            ProductNameField.setText("");
            ProductPriceField.setText("");

            actionMethod = "";

            AddButton.setEnabled(true);
            EditButton.setEnabled(false);
            DeleteButton.setEnabled(false);
            SaveButton.setEnabled(false);
            UnsaveButton.setEnabled(false);
        });

        // Event listener to exit form application
        ExitButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    // Method to create custom JTable
    private static class ProductsTableModel extends AbstractTableModel {
        private List<Sanpham> products;
        private final String[] tableHeaders = { "Mã SP", "Tên sản phẩm", "Giá bán" };

        public ProductsTableModel(List<Sanpham> products) {
            this.products = products;
        }

        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public int getColumnCount() {
            return tableHeaders.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> products.get(rowIndex).getMasp();
                case 1 -> products.get(rowIndex).getTensp();
                case 2 -> products.get(rowIndex).getGiaban();
                default -> "";
            };
        }

        @Override
        public String getColumnName(int column) {
            return tableHeaders[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (getValueAt(0, columnIndex) != null) {
                return getValueAt(0, columnIndex).getClass();
            }

            return Object.class;
        }
    }

    // Main method to run form application
    public static void main(String[] args) {
        JFrame frame = new JFrame("frmSanpham");
        frame.setContentPane(new frmSanpham().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
