/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbquery;

import java.sql.*; 
import java.util.Scanner;


/**
 *
 * @author Pates
 */
public class DBQuery {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int cost = getCost(80,40);
        System.out.println(cost);
    }
    
    public static int getCost(int x, int y){
	try { 
            Connection conn; 
            Statement stmt; 
            ResultSet res;
	    int g=0; 
            // load Connector/J 
            //Driver d = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            // establish connection 
            conn = DriverManager.getConnection("jdbc:mysql://vrlab.ecs.csun.edu/alonso64db", "alonso64", "natalia866"); 
           // execute SELECT query 
            stmt = conn.createStatement(); 

            res = stmt.executeQuery("SELECT Cost FROM City WHERE Place = '"+x+" "+y+"'");
            // process results 
            while (res.next()) { 
               g = res.getInt("Cost");
            } 
            res.close();
	    return g;
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }	
        return 0;
}
}
