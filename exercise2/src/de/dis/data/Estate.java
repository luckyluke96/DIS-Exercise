package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Estate {
    private int id = -1;
    private String city;
    private int postalCode;
    private String street;
    private int streetNumber;
    private int squareArea;
    private int estateAgentID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumnber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getSquareArea() {
        return squareArea;
    }

    public void setSquareArea(int squareArea) {
        this.squareArea = squareArea;
    }

    public int getEstateAgentID() {
        return estateAgentID;
    }

    public void setEstateAgendID(int estateAgentID) {
        this.estateAgentID = estateAgentID;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.

            // Achtung, hier wird noch ein Parameter mitgegeben,
            // damit spC$ter generierte IDs zurC<ckgeliefert werden!
            String insertSQL = "INSERT INTO estate(city, postal_code, street, street_number, square_area, estate_agent_id) VALUES (?, ?, ?, ?, ?, 7);";
            // String insertSQL = "INSERT INTO estate(postal_code) VALUES (456)";
            // System.out.println(insertSQL);
            System.out.println(getEstateAgentID());

            PreparedStatement pstmt = con.prepareStatement(insertSQL,
                    Statement.RETURN_GENERATED_KEYS);

            // Setze Anfrageparameter und fC<hre Anfrage aus

            pstmt.setString(1, getCity());
            pstmt.setInt(2, getPostalCode());
            pstmt.setString(3, getStreet());
            pstmt.setInt(4, getStreetNumber());
            pstmt.setInt(5, getSquareArea());
            // pstmt.setInt(6, getEstateAgentID());
            System.out.println(getEstateAgentID());

            pstmt.executeUpdate();

            // Hole die Id des engefC<gten Datensatzes
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.print("next");
                setId(rs.getInt(1));
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.out.println("problem with adding estate");
            e.printStackTrace();
        }

    }

    public void delete() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {

            String deleteSQL = "DELETE FROM estate WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(deleteSQL,
                    Statement.RETURN_GENERATED_KEYS);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setInt(1, getId());

            pstmt.executeUpdate();

            pstmt.close();

        } catch (SQLException e) {
            System.out.println("Exception");
            e.printStackTrace();
        }

    }

    public void update() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {

            // Falls schon eine ID vorhanden ist, mache ein Update...
            String updateSQL = "UPDATE estate SET city = ?, postal_code = ?, street = ?, street_number = ?, " +
                    "square_area = ?, estate_agent_id = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setString(1, getCity());
            pstmt.setInt(2, getPostalCode());
            pstmt.setString(3, getStreet());
            pstmt.setInt(4, getStreetNumber());
            pstmt.setInt(5, getSquareArea());
            pstmt.setInt(6, getEstateAgentID());
            pstmt.setInt(7, getId());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Die Login-Daten sind nicht korrekt.");
            e.printStackTrace();
        }
    }
}
