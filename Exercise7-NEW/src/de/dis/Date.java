package de.dis;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Date {

    private long dateid;
    private long month;
    private long quarter;
    private long year;
    private long dayofyear;
    private String date;


    public long getDateid() {
      return dateid;
    }

    public void setDateid(long dateid) {
      this.dateid = dateid;
    }


    public long getMonth() {
      return month;
    }

    public void setMonth(long month) {
      this.month = month;
    }


    public long getQuarter() {
      return quarter;
    }

    public void setQuarter(long quarter) {
      this.quarter = quarter;
    }


    public long getYear() {
      return year;
    }

    public void setYear(long year) {
      this.year = year;
    }


    public long getDayofyear() {
      return dayofyear;
    }

    public void setDayofyear(long dayofyear) {
      this.dayofyear = dayofyear;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public void save() {
        String SQL = "INSERT INTO date(date,year,quarter,month,dayofyear,dateid) " + "VALUES(?,?,?,?,?,?)";

        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(SQL, new String[]{"dateid"});

            ps.setString(1, getDate());
            ps.setLong(2, getYear());
            ps.setLong(3, getQuarter());
            ps.setLong(4, getMonth());
            ps.setLong(5, getDayofyear());
            ps.setLong(6, getDateid());
            ps.addBatch();

            try {
                // Batch is ready, execute it to insert the data
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    //setDateid(rs.getInt(1));
                    System.out.printf("new date %s with id %d inserted!%n", this.date, this.dateid);

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
