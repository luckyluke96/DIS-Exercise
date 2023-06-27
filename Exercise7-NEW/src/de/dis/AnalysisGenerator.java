package de.dis;

import java.util.Arrays;
import java.util.Scanner;

public class AnalysisGenerator {
    /*
    Produces output that the manager can use.
    The desired granularity level
    of each dimension is given by the parameters; e.g. geo = "country" is the most general
    and geo = "shop" is the most fine-grained granularity level for the geographical dimension.
    */

    private final String[] geo_values = {"shop", "city", "region", "country"};
    private final String[] time_values = {"date", "dayofyear", "month", "quarter", "year"};
    private final String[] product_values = {"article", "productgroup", "productfamily"};

    private final WarehouseManager data_warehouse = new WarehouseManager();

    private static String[] getRequest() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter one of the geo info: [shop, city, region, country]");
        String geo_info = sc.nextLine();
        System.out.println("Please enter one of the time categories: [date, dayofyear, month, quarter, year]");
        String time_info = sc.nextLine();
        System.out.println("Please enter one of the product info: [article , productgroup , productfamily]");
        String product_info = sc.nextLine();

        String[] request = {geo_info, time_info, product_info};
        System.out.println("-------------------------------------------");
        System.out.println("You have entered the following information:");
        System.out.printf("Geo: %s%nTime: %s%nProduct: %s%n", geo_info, time_info, product_info);
        System.out.println("-------------------------------------------");

        return request;
    }

    public void showAnalysis() {
        this.data_warehouse.populateWarehouse();
        boolean check = true;
        while (check) {
            String[] request = getRequest();
            // check if the request matches the format
            boolean geo_in = Arrays.asList(geo_values).contains(request[0]);
            boolean date_in = Arrays.asList(time_values).contains(request[1]);
            boolean product_in = Arrays.asList(product_values).contains(request[2]);

            if (!(geo_in && date_in && product_in)) {
                System.out.println("You have entered the wrong information, please enter again!");
                System.out.println("-------------------------------------------");
                request = getRequest();
            }
            // if not, display error messages and show the menu again
            // otherwise, show the analysis and display the menu again
            // terminates when exit is chosen
            analyse(request);
            System.out.println("-------------------------------------------");
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter yes if you would like to analyse more!");
            if(!sc.nextLine().equals("yes")) {
                check = false;
            }


        }
    }

    private void analyse(String[] request) {
        this.data_warehouse.loadDataMart(request);
    }
}
