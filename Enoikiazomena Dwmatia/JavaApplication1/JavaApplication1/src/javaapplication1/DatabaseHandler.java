/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class DatabaseHandler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        // TODO code application logic here
        DatabaseHandler db = new DatabaseHandler();

////
//      db.insertIntoDatabase(31,"SINGLE",false);
//
//    db.insertIntoDatabase(32,"SINGLE",false);
////
////
//  db.insertIntoDatabase(33,"DOUBLE",true);
////
//     db.insertIntoDatabase(34,"SINGLE",false);

        db.printRooms();
        db.printRoomTypes();
    }

    public void insertIntoDatabase(int id, String type, boolean booked) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getHSQLConnection();
            String sql = "INSERT INTO ROOMS VALUES (?,?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, type);
            stmt.setInt(3, booked ? 1 : 0);
            stmt.execute();


        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }



    }

    public void setPriceForRoomType(String roomTypeId, float price) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getHSQLConnection();
            String sql = "UPDATE ROOMTYPE SET PRICE=? WHERE TYPEID=?";

            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, price);
            stmt.setString(2, roomTypeId);
            stmt.execute();
            printRoomTypes();

        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }



    }

    public void printRooms() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getHSQLConnection();
            String sql = "SELECT * FROM ROOMS";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Room " + rs.getInt("RoomId") + " | " + rs.getString("RoomTypeId") + " | " + rs.getString("Booked"));
            }

        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }



    }

    public void printRoomTypes() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getHSQLConnection();
            String sql = "SELECT * FROM ROOMTYPE";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("TypeId") + " | " + rs.getString("Price"));
            }

        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }



    }

    private static Connection getHSQLConnection() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:hotel.db";
        Connection conn = DriverManager.getConnection(url, "sa", "");
        conn.setAutoCommit(true);

        return conn;

    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    public DefaultTableModel getPriceTableModel() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DefaultTableModel model = null;
        try {
            conn = getHSQLConnection();
            String sql = "SELECT * FROM ROOMTYPE";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            model = buildTableModel(rs);
        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return model;
    }

    public DefaultTableModel getBookingTableModel(Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DefaultTableModel model = null;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = getHSQLConnection();
            String sql = "select roomId, roomTypeId, customerLastName, customerFirstName, "
                    + "startDate, endDate, price,DATEDIFF ( 'day', startDate,endDate ) as duration, " +
                    " DATEDIFF ( 'day', startDate,endDate )*price  as cost" +
                    " from customer, rooms, roomtype, booking "
                    + "where rooms.roomtypeid=roomtype.typeid and booking.roomid=rooms.roomid and "
                    + " booking.customerid=customer.customerid " ;
                    
             if(startDate != null) {
                sql += " and startDate >= '"+sdf.format(startDate)+"'";
            }
            if (endDate != null) {
                sql+="and endDate <= '"+sdf.format(endDate)+"'";
            }
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            model = buildTableModel(rs);
        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return model;
    }
}
