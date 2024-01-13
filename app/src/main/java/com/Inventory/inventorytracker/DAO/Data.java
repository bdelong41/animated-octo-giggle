package com.Inventory.inventorytracker.DAO;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
public class Data {
    private static String url;
    private static String uname = "AndroidStorageManagement";//AppointmentScheduler
    private static String passwd = "Passw0rd!";//Passw0rd!
    private static Connection con;

    //connection string vars
    private static String serverName = "172.16.0.6";

    private static String port = "3306";
    private static String databaseName = "StorageScanner";//client_schedule

    /**
     * Creates static database connection upon object creation.
     */
    static
    {
        url = "jdbc:mysql://" + serverName + ":" + port + "/" + databaseName +"?connectionTimeZone=UTC";

        try {
            con = DriverManager.getConnection(url, uname, passwd);
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            Log.d("Error Trace", sStackTrace);
        }
    }
    /**
     * Static function.
     * checks if connection is still active.
     * if the connection is closed then it creates a new connection.
     * @return Connection.
     */
    public static Connection getConnection() {
        //construct connection string
        url = "jdbc:mysql://" + serverName + ":" + port + "/" + databaseName +"?connectionTimeZone=Pacific";
        try {
            if (con.isClosed() || con == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(url, uname, passwd);
            }
            return con;
        }catch (SQLException | ClassNotFoundException | NullPointerException e){
            e.printStackTrace();
        }
        return con;
    }
    public static boolean closeConnection(){
        System.out.println("Close connection Called---------------------------------");
        if(con == null) {
            System.out.println("Connection Null");
            return true;
        }

        try{
            if(con.isClosed()){
                return true;
            }
            else{
                con.close();
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static String getDBName(){
        String name = null;
        //close current connection
        closeConnection();
        //create new connection
        getConnection();
        if(con == null) return null;
        try {
            Statement st = con.createStatement();
            String query = "Select database();";
            ResultSet rs = st.executeQuery(query);
            if(rs.next());
            {
                name = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        closeConnection();
        return name;
    }
    public static void setUname(String Username){
        uname = Username;
    }
    public static void setPasswd(String password){
        passwd = password;
    }
    public static String getUname() {
        return uname;
    }

    public static String getPasswd() {
        return passwd;
    }

    public static Connection getCon() {
        return con;
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getPort() {
        return port;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static Connection checkConnection(){
        try{
            if(con != null){
                if(!con.isClosed()){
                    con.close();
                }
            }
            url = "jdbc:mysql://" + serverName + ":" + port + "/" + databaseName +"?connectionTimeZone=Pacific";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uname, passwd);

            if(con == null){
                System.out.println("General Connection Failed");
            }
            else{
                System.out.println("General Connection Succeeded");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }

    public static void setServerName(String serverName) {
        Data.serverName = serverName;
    }

    public static void setPort(String port) {
        Data.port = port;
    }

    public static void setDatabaseName(String databaseName) {
        Data.databaseName = databaseName;
    }


}