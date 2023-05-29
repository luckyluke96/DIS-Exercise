package hamburg.dbis.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class PersistenceManager {

    static final private PersistenceManager _manager;

    static private Hashtable<Integer, UserData> buffer;

    // TODO Add class variables if necessary
    int transaction_id = 0;
    int logSequenceNumber = readLSN();

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


        buffer = new Hashtable<Integer, UserData>();

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

        // write EOT
        try {
            logSequenceNumber = readLSN() + 1;
            String lsn = String.format("%04d", logSequenceNumber);
            String name= "Logs/log_data.txt";
            FileWriter fw = new FileWriter(name,true); //the true will append the new data
            fw.write(lsn + ", " + taid + ", EOT\n");//appends the string to the file
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // set commit to true in buffer for taid
        Collection<UserData> entries = buffer.values();
        for (UserData entry : entries) {
            if(entry.getTaid() == taid ) {
                entry.setCommitted(true);
            }
        }

    }

    public void write(int taid, int pageid, String data) {
        // TODO handle writes of Transaction taid on page pageid with data

        try {
            logSequenceNumber = readLSN() + 1;
            String lsn = String.format("%04d", logSequenceNumber);
            String name= "Logs/log_data.txt";
            FileWriter fw = new FileWriter(name,true); //the true will append the new data
            fw.write(lsn + ", " + taid +", " + pageid + ", " + data +"\n");//appends the string to the file
            fw.close();

            /*
            // new user data page
            FileWriter userPage = new FileWriter("Logs/UserData/"+userdata_filename);
            userPage.write(lsn + ", " + data);
            userPage.close();


             */

            // write to buffer taid as key in hashtable
            UserData userData = new UserData(logSequenceNumber, taid, pageid, data);
            buffer.put(logSequenceNumber, userData);
            // wait for random * 100 milliseconds
            int time = (int) (Math.random() * (500 - 100) + 100);
            Thread.sleep(time);

            // check buffer size
            if(buffer.size() > 5) {
                Iterator<Map.Entry<Integer, UserData>> iterator = buffer.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, UserData> entry = iterator.next();

                    if(entry.getValue().getCommitted() == true) {
                        int key = entry.getKey();
                        String userdata_filename = "Page_" + entry.getValue().getPageid() + ".txt";
                        FileWriter userPage = new FileWriter("Logs/UserData/" + userdata_filename);
                        String current_lsn = String.format("%04d", entry.getValue().getLsn());
                        userPage.write( current_lsn + ", " + entry.getValue().getData());
                        userPage.close();

                        iterator.remove();
                    }
                }

                /*
                Collection<UserData> entries = buffer.values();
                for (UserData entry : entries) {
                    if(entry.getCommitted() == true) {
                        // new user data page
                        FileWriter userPage = new FileWriter("Logs/UserData/"+userdata_filename);
                        userPage.write(lsn + ", " + data);
                        userPage.close();

                        // delete from buffer
                        System.out.println("lsn: "+entry.getLsn());

                        //System.out.println("key: "+entry.getKey());
                        buffer.remove(entry.getLsn());

                    }
                }*/


            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public int readLSN() {
        String filePath = "Logs/log_data.txt";
        int lsn = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String lastLine = null;

            while ((currentLine = br.readLine()) != null) {
                lastLine = currentLine;
            }

            if (lastLine != null) {
                lsn = Integer.parseInt(lastLine.substring(0, lastLine.indexOf(',')));
            } else {
                System.out.println("The file is empty.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lsn;
    }
}
