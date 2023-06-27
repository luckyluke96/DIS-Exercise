package de.dis;

import de.dis.Date;
import de.dis.Factsales;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DataStaging {
    private final DataManager data_manager = new DataManager();

    // load data from data manager to data warehouse
    public void loadData() {
        ArrayList<Factsales> factsales;

        // load the file of the transformed data if exists
        // otherwise transform data and load the file saved
        if (!this.data_manager.transformedFileExists()) {
            System.out.println("transforming data...");
            transformData();
        }
        System.out.println("loading transformed data...");
        factsales = this.data_manager.loadTransformedData();

        // start from the last row inserted
        int row_counter = data_manager.get_counter();
        System.out.printf("The number of rows in the table factsales is %d.%n", row_counter);

        for (int counter = row_counter; counter < factsales.size(); counter++) {
            // insert sale item to the data warehouse db one by one
            factsales.get(counter).save();
        }
    }

    // save the transformed data to data manager
    public void transformData() {
        // printing for german text
        PrintStream ps = null;
        ps = new PrintStream(System.out, true, StandardCharsets.ISO_8859_1);

        List<String> raw_data = this.data_manager.extractFromFile();
        ResultSet shop_rs = this.data_manager.getTable("shop");
        ResultSet article_rs = this.data_manager.getTable("article");
        System.out.println("resultSets of tables shop and article retrieved!");

        // task 1: cleaning
        // remove duplicates
        LinkedHashSet<String> hash_set = new LinkedHashSet<>(raw_data);
        ArrayList<String> data_without_duplicates = new ArrayList<>(hash_set);
        int duplicates_n = raw_data.size() - data_without_duplicates.size();
        System.out.printf("%d duplicates removed!%n", duplicates_n);

        // loop through  List
        for (int counter = 0; counter < data_without_duplicates.size(); counter++) {
            // fields for factsales
            long date_id;
            long shop_id;
            long article_id;
            long sold_amount;
            double revenue;

            String line = data_without_duplicates.get(counter);
            // Date;Shop;Article;Sold;Revenue
            String[] line_split = line.split(";");

            // skip item with missing or extra data
            if (line_split.length != 5) {
                System.out.printf("row %d with missing or extra data skipped!%n", counter);
                continue;
            }

            // skip item with data that do not exist in the db
            String shop = line_split[1];
            boolean shop_is_in = itemIsInRs(shop, "name", shop_rs);

            String article = line_split[2];
            boolean article_is_in = itemIsInRs(article, "name", article_rs);

            if (!(shop_is_in && article_is_in)) {
                if (!shop_is_in) {
                    System.out.printf("row %d with data %s not in src db skipped!%n", counter, shop);
                } else {
                    System.out.printf("row %d with data %s not in src db skipped!%n", counter, article);
                }

                continue;
            }

            // task 2: convert date, shop and article strings to ids
            // date format: dd.mm.yyyy
            String date = line_split[0];
            ResultSet date_rs = this.data_manager.getTable("date");
            boolean date_is_in = itemIsInRs(date, "date", date_rs);

            if (date_is_in) {
                // get date_id
                date_id = getId("date", date, "date", date_rs);
            } else {
                // task 2.1: split and add a new date to db if date not found in date_rs
                // split date into {year, quarter, month, dayofyear}
                int[] splitted_date = splitDate(date);
                // create a new date in the date table in db
                date_id = createDate(date, splitted_date);
            }

            // save the transformed info to data manager
            shop_id = getId("shop", shop, "name", shop_rs);
            article_id = getId("article", article, "name", article_rs);
            sold_amount = Integer.parseInt(line_split[3]);
            revenue = Double.parseDouble(line_split[4].replace(",", "."));
            this.data_manager.addFactSale(date_id, shop_id, article_id, sold_amount, revenue);
        }

        // save all the transformed info to a file
        this.data_manager.saveTransformedData();
    }

    private long createDate(String date, int[] splitted_date) {
        long date_id;

        Date new_date = new Date();
        new_date.setDate(date);
        new_date.setYear(splitted_date[0]);
        new_date.setQuarter(splitted_date[1]);
        new_date.setMonth(splitted_date[2]);
        new_date.setDayofyear(splitted_date[3]);
        new_date.save();
        date_id = new_date.getDateid();

        return date_id;
    }
    private int getId(String table_name, String item_name, String field_name, ResultSet rs) {
        int id = 0;
        try {
            while (rs.next()) {
                String name = rs.getString(field_name);
                if (item_name.equals(name)) {
                    id = rs.getInt(String.format("%sid", table_name));
                    break;
                }
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    private int[] splitDate(String date) {
        String[] date_split = date.split("\\.");
        int day_of_month = Integer.parseInt(date_split[0]);
        int month = Integer.parseInt(date_split[1]);
        int year = Integer.parseInt(date_split[2]);

        LocalDate local_date = LocalDate.of(year, month, day_of_month);
        int day_of_year = local_date.getDayOfYear();
        int quarter = local_date.get(IsoFields.QUARTER_OF_YEAR);

        int[] result = {year, quarter, month, day_of_year};
        return result;
    }

    private boolean itemIsInRs(String item_name, String field_name, ResultSet rs) {
        boolean is_in = false;

        try {
            while (rs.next()) {
                String name = rs.getString(field_name);
                if (item_name.equals(name)) {
                    is_in = true;
                    break;
                }
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return is_in;
    }
}
