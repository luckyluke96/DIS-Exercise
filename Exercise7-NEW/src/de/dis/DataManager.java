package de.dis;


import de.dis.DbConnectionManager;
import de.dis.Factsales;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {

    private final String[] geo_values = {"shop", "city", "region", "country"};
    private final String[] product_values = {"article", "productgroup", "productfamily"};
    private ArrayList<de.dis.Factsales> factsales = new ArrayList<>();
    private final String transformed_data_fn = "transformed_data.tmp";
    private int row_counter = 0;

    public int get_counter() {
        // check the # of rows inserted already
        // connect to db
        Connection conn = de.dis.DbConnectionManager.getInstance().getConnection();
        //String sql = "SELECT salesid FROM factsales";
        String sql = "SELECT salesid FROM sales_fact";

        try {
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery(sql);
            rs.last();
            this.row_counter = rs.getRow();

            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return this.row_counter;
    }

    public void saveTransformedData() {
        try {
            FileOutputStream fos = new FileOutputStream(this.transformed_data_fn);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.factsales);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("All transformed sale data saved to file!");
    }

    public boolean transformedFileExists(){
        Path path = Paths.get(this.transformed_data_fn);
        return Files.exists(path);
    }
    public ArrayList<Factsales> loadTransformedData() {
        try {
            FileInputStream fis = new FileInputStream(this.transformed_data_fn);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.factsales = (ArrayList<Factsales>)ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this.factsales;
    }
    public void addFactSale(Long date_id, Long shop_id, Long article_id, Long sold_amount, Double revenue) {
        Factsales fact_sale = new Factsales();
        fact_sale.setDateid(date_id);
        fact_sale.setShopid(shop_id);
        fact_sale.setArticleid(article_id);
        fact_sale.setSoldamount(sold_amount);
        fact_sale.setRevenue(revenue);

        this.factsales.add(fact_sale);
    }

    // extract data from postgresql database
    // tables: shop, article, date
    public ResultSet getTable(String table_name) {
        ResultSet rs = null;

        // connect to db
        Connection conn = DbConnectionManager.getInstance().getConnection();
        
        System.out.println(conn);
        
        String sql = "";
        // get the resultSet of the specified table
        switch(table_name) {
            case "shop":
                sql = "SELECT shopid, name FROM shop";
                break;

            case "article":
                sql = "SELECT articleid, name FROM article";
                break;

            case "date":
                sql = "SELECT dateid, date FROM date";
                break;
        }

        try {
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = s.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rs;
    }

    // extract data from source
    public List<String> extractFromFile() {
        String csv_file = "sales.csv";
        Path path = Paths.get("ressources", csv_file);
        List<String> lines = new ArrayList<>();

        Charset inputCharset = StandardCharsets.ISO_8859_1;
        try {
            BufferedReader br = Files.newBufferedReader(path, inputCharset);
            String line;
            while ((line = br.readLine()) != null) {
                    lines.add(line);
            }

            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lines.remove(0);
        System.out.println("sales.csv extracted!");
        return lines;
    }

    // from data warehouse
    public void getDataMart(String[] request) {
        Connection conn = DbConnectionManager.getInstance().getConnection();
        String geo_info = request[0];
        String time_info = request[1];
        String product_info = request[2];

        int geo_idx = Arrays.asList(geo_values).indexOf(geo_info);
        int product_idx = Arrays.asList(product_values).indexOf(product_info);

        StringBuilder str = new StringBuilder();
        //str.append(String.format("SELECT G.name, T.%s, P.name, SUM(F.soldamount), SUM(F.revenue) FROM factsales F ", time_info));
        str.append(String.format("SELECT G.name, T.%s, P.name, SUM(F.soldamount), SUM(F.revenue) FROM sales_fact F ", time_info));

        str.append("JOIN date T ON F.dateid = T.dateid ");
        for (int j = 0; j<= geo_idx; j++) {
            if (j==0) {
                if (j==geo_idx) {
                    str.append(String.format("JOIN %s G ON F.%sid = G.%sid ",
                            geo_values[j], geo_values[j], geo_values[j]));
                } else {
                    str.append(String.format("JOIN %s ON F.%sid = %s.%sid ",
                            geo_values[j], geo_values[j], geo_values[j], geo_values[j]));
                }
            } else if (j>0) {
                if (j==geo_idx) {
                    str.append(String.format("JOIN %s G ON %s.%sid = G.%sid ",
                            geo_values[j], geo_values[j-1], geo_values[j], geo_values[j]));
                } else {
                    str.append(String.format("JOIN %s ON %s.%sid = %s.%sid ",
                            geo_values[j], geo_values[j - 1], geo_values[j], geo_values[j], geo_values[j]));
                }
            }
        }
        for (int j = 0; j<= product_idx; j++) {
            if (j==0) {
                if (j==product_idx) {
                    str.append(String.format("JOIN %s P ON F.%sid = P.%sid ",
                            product_values[j], product_values[j], product_values[j]));
                } else {
                    str.append(String.format("JOIN %s ON F.%sid = %s.%sid ",
                            product_values[j], product_values[j], product_values[j], product_values[j]));
                }
            } else if (j>0) {
                if (j==product_idx) {
                    str.append(String.format("JOIN %s P ON %s.%sid = P.%sid ",
                            product_values[j], product_values[j-1], product_values[j], product_values[j]));
                } else {
                    str.append(String.format("JOIN %s ON %s.%sid = %s.%sid ",
                            product_values[j], product_values[j - 1], product_values[j], product_values[j], product_values[j]));
                }
            }
        }
        str.append(String.format("GROUP BY CUBE(G.name, T.%s, P.name)", time_info));

        try {
            PreparedStatement ps = conn.prepareStatement(str.toString());

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            System.out.printf("%s, %s, %s, sold_amount, revenue_sum%n", geo_info, time_info, product_info);
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                }
                System.out.printf("%n");
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
