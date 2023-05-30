package hamburg.dbis.recovery;

import hamburg.dbis.persistence.UserData;

import java.io.*;
import java.util.*;

public class RecoveryManager {

    static final private RecoveryManager _manager;

    // TODO Add class variables if necessary
    static private Hashtable<Integer, UserData> recoveryData;


    static {
        try {
            _manager = new RecoveryManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private RecoveryManager() {
        // TODO Initialize class variables if necessary
        recoveryData = new Hashtable<Integer, UserData>();
    }

    static public RecoveryManager getInstance() {
        return _manager;
    }

    public void startRecovery() {
        // TODO

        // go through log
        String filePath = "Logs/log_data.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String line;

            // Read all lines into a List
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            List<String> committedTaids = new ArrayList<>();

            // Iterate through lines in reverse order
            Collections.reverse(lines);
            for (String currentLine : lines) {

                String[] splitArray = currentLine.split(",\\s*");

                if (committedTaids.contains(splitArray[1])) {
                    // add to presistence
                    //System.out.println(currentLine);

                    // write in DB
                    if(!currentLine.contains("EOT")) {
                        String userdata_filename = "Page_" + splitArray[2] + ".txt";
                        FileWriter userPage = new FileWriter("Logs/UserData/" + userdata_filename);
                        String current_lsn = splitArray[0];
                        String data = "";
                        for (int i = 3; i < splitArray.length; i++) {
                            data = data + ", " + splitArray[i];
                        }
                        userPage.write(current_lsn + ", " + data);
                        userPage.close();
                    }
                }


                // update taid
                if (currentLine.contains("EOT")) {
                    //String[] splitArray = currentLine.split(",\\s*");
                    committedTaids.add(splitArray[1]);
                }

            }



        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    }
