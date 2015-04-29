/* M. Kofler, The Definitive Guide to MySQL 5, Third 
 Edition, 748pp, Apress, Berkeley CA., 2005. 
 Program source for SampleIntro is on page 512 
 Posted file shows use w/ a constructor. 
 Mike Barnes 5/24/2011 
 */

import java.sql.*;

public class NewMain {

    public static void main(String[] args) {
        try {
            Connection conn;
            Statement stmt;
            ResultSet res;
            // load Connector/J 
            Driver d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
            // establish connection 
            conn = DriverManager.getConnection(
                    "jdbc:mysql://vrlab.ecs.csun.edu/alonso64db",
                    "alonso64", "natalia866");
// execute SELECT query 
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT Name, Party FROM President WHERE Year = 2008"); // this is one line w/o break in file 
            // process results 
            while (res.next()) {
                String name = res.getString("Name");
                String party = res.getString("Party");
                System.out.println("Name = " + name + " Party = "
                        + party); // this is one line w/o break in file 
            }
            res.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}