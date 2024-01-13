package com.Inventory.inventorytracker.DAO;

import com.Inventory.inventorytracker.model.Box;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Performs all database CRUD operations.
 */
public class PackageDAOImpl {
    private Connection con;
    private String Delimiter = "@";

    private final String tableName = "StorageScanner";

    //data retrieval
//    public Box get(Integer id) {
//        Box box = null;
//        try {
//            con = Data.getConnection();//verifying connection is active
//            String query = "select Contact_ID, Contact_Name, Email from contacts where Contact_ID = ?;";
//            PreparedStatement st = con.prepareStatement(query);
//            st.setInt(1, id);
//            ResultSet rs = st.executeQuery();
//            if(rs.next())
//            {
//                box = new Box(rs.getInt(1));
//                box.setB(rs.getString(2));
//                box.setEmail(rs.getString(3));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return box;
//    }

    public List<Box> getAll() {
        Box box;
        List<Box> ls = new ArrayList<Box>();
        try {
            con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "select * from StorageScanner;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
//                box = new Box(rs.getInt("Contact_ID"));
                String[] contents = rs.getString("Contents").split(Delimiter);

                box = new Box(rs.getString("Owner"), Arrays.asList(contents), rs.getInt("id"),
                        rs.getInt("Box_id"), rs.getString("Description"));
                ls.add(box);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }

    //data alteration

    /**
     * Creates initial appointment record and returns the auto-generated id.
     * @param boxID - takes box id to initialize a new record in database.
     * @return id - returns the id of the newly created box.
     */
    public int create(Integer boxID){
        int id = -1;
        try {
            Connection con = Data.getConnection();
            Statement st = con.createStatement();
            String query = "insert into " + tableName + " (Box_id, Owner, Contents, Description)" +
                    "values(?, '', '', '');";
            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, boxID);

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Updates appointment fields in appointments whose record contains the same id as the parameter's.
     * @param t - Takes Appointment parameter to update the existing appointment fields with the specified
     * appointment id.
     * @return True if st returned a value greater than 0. Returns false if the
     * value is zero.
     */
    public boolean update(Box t) {
        Integer rs = 0;
        Connection con = null;
        try {
            con = Data.getConnection();
            String query = "update " + tableName +
                    "set Owner = ?, " +
                    "Contents = ?, " +
                    "Description = ? ;";

            PreparedStatement st = con.prepareStatement(query);

            //stringifying list contents
            String contents = "";
            for(String item: t.getContents()){
                contents = contents + "@" + item;
            }
            st.setString(1, t.getOwner());
            st.setString(2, contents);
            st.setString(3, t.getDescription());
            rs = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rs > 0){
            return true;
        }
        return false;
    }

    /**
     * Deletes an appointment record with the same id as the parameter's.
     * @return True if the PreparedStatement returned a value greater than 0. Returns false if the
     *      *          value is zero.
     */
    public boolean delete(int id) {

        int rs = 0;
        try {
            Connection con = Data.getConnection();//verifying connection is active
            Statement st = con.createStatement();
            String query = "delete from StorageScanner where id = '" + id + "';";
            rs = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rs > 0) return true;
        return false;
    }
}
