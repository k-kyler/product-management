package com.qlbanhang.dao;

import com.qlbanhang.model.Sanpham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanphamDAO {
    private String jdbcURL = "jdbc:postgresql://localhost:5432/QLBanhang";
	private String jdbcUsername = "postgres";
	private String jdbcPassword = "postgres";
	private String jdbcDriver = "org.postgresql.Driver";

    private static final String SELECT_PRODUCTS = "select * from \"Sanpham\"";
	private static final String INSERT_PRODUCT = "insert into \"Sanpham\"" + " (masp, tensp, giaban) values " + "(?, ?, ?)";
	private static final String UPDATE_STUDENT = "update students set name = ?, gradeid = ? where studentid = ?";
	private static final String DELETE_STUDENT = "delete from students where studentid = ?";

    public SanphamDAO() {

    }

    // Method to connect db
    protected Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return connection;
	}

    // Method to get all products
    public List<Sanpham> getAllProducts() {
		List<Sanpham> products = new ArrayList<>();

		try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCTS);
		) {
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String productId = resultSet.getString("masp");
				String productName = resultSet.getString("tensp");
				int productPrice = resultSet.getInt("giaban");

				products.add(new Sanpham(productId, productName, productPrice));
			}
		} catch (SQLException ex) {
			System.err.println(ex);
		}

		return products;
	}

	// Method to add a new product
	public void addNewProduct(Sanpham product) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT);

			preparedStatement.setString(1, product.getMasp());
			preparedStatement.setString(2, product.getTensp());
			preparedStatement.setInt(3, product.getGiaban());
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex);
		}
	}
}
