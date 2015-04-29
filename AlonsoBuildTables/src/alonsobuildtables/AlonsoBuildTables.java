/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alonsobuildtables;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author Pates
 */
public class AlonsoBuildTables {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        buildTables();
    }
    
    public static void buildTables(){                   
        try{            
            Scanner s = new Scanner(new File("waypointNeighbor.txt"));
            File cityFile = new File("city.txt");
            File mapFile = new File("map.txt");
            File treasureFile = new File("treasure.txt");
            PrintStream city = new PrintStream(cityFile);
            PrintStream map = new PrintStream(mapFile);
            PrintStream treasure = new PrintStream(treasureFile);
            while(s.hasNextInt()){
                int x = s.nextInt();
                int y = s.nextInt();
                int h = s.nextInt();
                int c = s.nextInt();
                int g = s.nextInt();
                int mx = s.nextInt();
                int my = s.nextInt();  
                int n = s.nextInt();
                for (int i = 0; i < n; i++){
                    int tempX = s.nextInt();
                    int tempY = s.nextInt();
                }
                if(c>0){
                    city.println(x+" "+y+"\t"+c);
                }
                if(g > 0){
                    treasure.println(x+" "+y+"\t"+g);
                }
                if(mx > 0 || my >0){
                    map.println(x+" "+y+"\t"+mx+" "+my);}
                }
        }
        catch(Exception e)
        {
            System.out.printf("Did not read file.");
        }
    }
}
