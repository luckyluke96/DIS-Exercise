package hamburg.dbis.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class PersistenceManager {

    static final private PersistenceManager _manager;

    static private Hashtable<Integer, Integer> buffer_taid;
    static private Hashtable<Integer, String> buffer_data;

    // TODO Add class variables if necessary
    int transaction_id = 0;

    static {
        try {
            _manager = new PersistenceManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private PersistenceManager() {
        // TODO Get the last used transaction id from the log (if present) at startup
        // TODO Initialize class variables if necessary

        buffer_taid = new Hashtable<Integer, Integer>();
        buffer_data = new Hashtable<Integer, String>();
    }

    static public PersistenceManager getInstance() {
        return _manager;
    }

    public synchronized int beginTransaction() {

        // TODO return a valid transaction id to the client
        transaction_id++;
        return transaction_id;
    }

    public void commit(int taid) {
        // TODO handle commits
    }

    public void write(int taid, int pageid, String data) {
        // TODO handle writes of Transaction taid on page pageid with data
        String userdata_filename = "Page_" + pageid + ".txt";
        try {
            String name= "log_data.txt";
            FileWriter fw = new FileWriter(name,true); //the true will append the new data
            fw.write("add a line\n");//appends the string to the file
            fw.close();

            FileWriter userPage = new FileWriter(userdata_filename);
            userPage.write(taid + ", " + data);
            userPage.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
