package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contract {
    private int id = -1;
    private int contractNum;
    private String date;
    private String place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContractNum() {
        return contractNum;
    }

    public void setContractNum(int contractNum) {
        this.contractNum = contractNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {

            // Achtung, hier wird noch ein Parameter mitgegeben,
            // damit spC$ter generierte IDs zurC<ckgeliefert werden!
            String insertSQL = "INSERT INTO contract(contract_number, contract_date, place) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertSQL,
                    Statement.RETURN_GENERATED_KEYS);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setInt(1, getContractNum());
            pstmt.setString(2, getDate());
            pstmt.setString(3, getPlace());
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

    public void overview() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {

            // Achtung, hier wird noch ein Parameter mitgegeben,
            // damit spC$ter generierte IDs zurC<ckgeliefert werden!
            String fromSQL = "SELECT * FROM contract;";

            PreparedStatement pstmt = con.prepareStatement(fromSQL,
                    Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // System.out.println(rs.getInt("id"));
                System.out.println(rs.getInt("contract_number"));
                System.out.println(rs.getString("contract_date"));
                System.out.println(rs.getString("place"));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
