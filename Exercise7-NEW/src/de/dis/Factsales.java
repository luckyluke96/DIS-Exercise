package de.dis;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Factsales implements Serializable {

    private long salesid;
    private long dateid;
    private long shopid;
    private long articleid;
    private long soldamount;
    private double revenue;


    public long getSalesid() {
      return salesid;
    }

    public void setSalesid(long salesid) {
      this.salesid = salesid;
    }


    public long getDateid() {
      return dateid;
    }

    public void setDateid(long dateid) {
      this.dateid = dateid;
    }


    public long getShopid() {
      return shopid;
    }

    public void setShopid(long shopid) {
      this.shopid = shopid;
    }


    public long getArticleid() {
      return articleid;
    }

    public void setArticleid(long articleid) {
      this.articleid = articleid;
    }


    public long getSoldamount() {
      return soldamount;
    }

    public void setSoldamount(long soldamount) {
      this.soldamount = soldamount;
    }


    public double getRevenue() {
      return revenue;
    }

    public void setRevenue(double revenue) {
      this.revenue = revenue;
    }

    public void save() {
        String SQL = "INSERT INTO sales_fact(dateid,shopid,articleid,soldamount,revenue) " + "VALUES(?,?,?,?,?)";

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(SQL, new String[]{"salesid"});

            ps.setLong(1, getDateid());
            ps.setLong(2, getShopid());
            ps.setLong(3, getArticleid());
            ps.setLong(4, getSoldamount());
            ps.setDouble(5, getRevenue());
            ps.addBatch();

            try {
                // Batch is ready, execute it to insert the data
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    setDateid(rs.getInt(1));
                }
            } catch (SQLException e) {
                System.out.println("Error message: " + e.getMessage());
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
