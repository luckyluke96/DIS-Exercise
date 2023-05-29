package hamburg.dbis.recovery;

import hamburg.dbis.persistence.UserData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

            // Iterate through lines in reverse order
            Collections.reverse(lines);
            for (String currentLine : lines) {
                List<String> substrings = new ArrayList<>();

                String[] tokens = currentLine.split(",");

                for (int i = 1; i < tokens.length; i++) {
                    substrings.add(tokens[i]);
                }

                for (String substring : substrings) {
                    System.out.println(substring);
                    // Perform any desired operations with the substring
                }
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    }
