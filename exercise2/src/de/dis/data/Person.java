package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {
    private int id = -1;
    private String firstName;
    private String name;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {

            // Achtung, hier wird noch ein Parameter mitgegeben,
            // damit spC$ter generierte IDs zurC<ckgeliefert werden!
            String insertSQL = "INSERT INTO person(first_name, name, address) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertSQL,
                    Statement.RETURN_GENERATED_KEYS);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setString(1, getFirstName());
            pstmt.setString(2, getName());
            pstmt.setString(3, getAddress());
            pstmt.executeUpdate();

            // Hole die Id des engefC<gten Datensatzes
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                setId(rs.getInt(1));
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
