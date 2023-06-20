package de.dis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;


public class Main {

    public void createWarehouse() throws SQLException, InterruptedException {
        // Setup
        Connection con = setup_new_connection();
        Statement cs = con.createStatement();

        cs.execute("DROP TABLE if exists sales_fact;" +
                "CREATE TABLE sales_fact (" +
                "sale_id SERIAL PRIMARY KEY," +
                "shop_id INT REFERENCES shop(shopid)," +
                "article_id INT REFERENCES article(articleid)," +
                "date_id INT," +
                "quantity INT," +
                "turnover DECIMAL(10, 2)" +
                ");"
        );

        con.close();
    }

    public void fillWarehouseFromCSV() throws SQLException {
        Connection con = setup_new_connection();
        Statement cs = con.createStatement();

        String csvFilePath = "";

        //FileReader fileReader = new FileReader(csvFilePath);
        //CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build()) {
    }


    public static void main(String[] args) throws SQLException, InterruptedException {
        Main mainObject = new Main();
        mainObject.createWarehouse();

    }


    public static Connection setup_new_connection() {

        try {
            // Holen der Einstellungen aus der db.properties Datei
            Properties properties = new Properties();
            FileInputStream stream = new FileInputStream(new File("db.properties"));
            properties.load(stream);
            stream.close();

            String jdbcUser = properties.getProperty("jdbc_user");
            //System.out.println("jdbc_user: " + jdbcUser);
            String jdbcPass = properties.getProperty("jdbc_pass");
            //System.out.println("jdbc_pass: " + jdbcPass);
            String jdbcUrl = properties.getProperty("jdbc_url");
            // Verbindung zur Datenbank herstellen
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
        } catch (Exception e) {
            //System.out.println("==== error =====");
            e.printStackTrace();
        }
        return null;
    }


}

