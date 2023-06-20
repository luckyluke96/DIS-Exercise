package src.de.dis;

import src.de.dis.data.DbConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		loadIntoWarehouse();

	}

	public static void loadIntoWarehouse() {
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String creatFactTableSQL = "CREATE TABLE sales_fact (\n" +
					"    sale_id SERIAL PRIMARY KEY,\n" +
					"    store_id INT,\n" +
					"    product_id INT,\n" +
					"    time_id INT,\n" +
					"    quantity INT,\n" +
					"    turnover DECIMAL(10, 2)\n" +
					");";

			PreparedStatement pstmt = con.prepareStatement(creatFactTableSQL);

			pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
