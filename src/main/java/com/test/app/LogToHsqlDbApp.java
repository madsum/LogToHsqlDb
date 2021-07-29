package com.test.app;

import com.test.dbmanager.DatabaseManager;
import com.test.json.JsonParser;

import java.io.File;

public class LogToHsqlDbApp {


    public static void main(String[] args) throws Exception {
        File file;
        if (args.length > 0) {
            file = new File(args[0]);

            if(!file.exists() || file.isDirectory()) {
                System.err.println( "Invalid file. Please provide a proper log file!" );
            }

        } else {
            System.err.println( "Please provide log file path as an argument." );
            return;
        }
        var data = JsonParser.parseFile(file);
        DatabaseManager.initializeDb();
        data.entrySet().stream()
                .forEach(e -> {
                   DatabaseManager.insertData(e.getValue());
                });

        DatabaseManager.closeDb();
        System.out.println("Log processing is done successfully. Please check the directory log-data.");
    }
}
