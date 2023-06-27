package de.dis;

public class WarehouseManager {
    private final DataManager data_manager = new DataManager();
    public void populateWarehouse(){
        DataStaging data_staging = new DataStaging();
        data_staging.loadData();
    }

    // load data from the data warehouse db based on the parameters given
    public void loadDataMart(String[] request) {
        this.data_manager.getDataMart(request);
    }
}
