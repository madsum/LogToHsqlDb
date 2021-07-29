package com.test.dbmanager;

import com.test.domain.EventRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    static String connectionString = "jdbc:hsqldb:file:log-data/logs";
    static Connection con;
    static final String TABLE_NAME = "eventRecord";
    static final String EVENT_TABLE = "create table if not exists "+ TABLE_NAME +
            " (id varchar(32)," +
            "duration INTEGER," +
            "type varchar(32)," +
            "host varchar(32)," +
            "alert TINYINT);";

    static {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public DatabaseManager() {
    }

    public static void initializeDb() throws SQLException, URISyntaxException, IOException {
        try {
            con = DriverManager.getConnection(connectionString, "SA", "");
            // create table
            con.createStatement()
                    .executeUpdate(EVENT_TABLE);

        } catch (SQLException e) {
            throw e;
        }
    }

    public static void insertData(EventRecord eventRecord){
        Statement stmt = null;
        int result = 0;
        try {
            stmt = con.createStatement();
            result = stmt.executeUpdate("INSERT INTO "+ TABLE_NAME +
                  " VALUES ('"+eventRecord.getId()+"','"
                              + eventRecord.getDuration()+"','"
                              +eventRecord.getType()+"','"
                              +eventRecord.getHost()+"','"
                              +eventRecord.isAlert()+"');");

                    con.commit();
        }catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void closeDb() throws SQLException {
        if(con != null){
            con.close();
        }
    }
}
