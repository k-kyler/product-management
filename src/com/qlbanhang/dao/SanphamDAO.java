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
	private static final String UPDATE_PRODUCT = "update \"Sanpham\" set tensp = ?, giaban = ? where masp = ?";
	private static final String DELETE_PRODUCT = "delete from \"Sanpham\" where masp = ?";

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

	// Method to edit product
	public boolean editProduct(Sanpham product) throws SQLException {
		boolean isUpdatedRow;

		try (
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT);
		) {
			preparedStatement.setString(1, product.getTensp());
			preparedStatement.setInt(2, product.getGiaban());
			preparedStatement.setString(3, product.getMasp());

			isUpdatedRow = preparedStatement.executeUpdate() > 0;
		}

		return isUpdatedRow;
	}

	// Method to delete product
	public boolean deleteProduct(String id) throws SQLException {
		boolean isDeletedRow;

		try (
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT);
		) {
			preparedStatement.setString(1, id);

			isDeletedRow = preparedStatement.executeUpdate() > 0;
		}

		return isDeletedRow;
	}
}
