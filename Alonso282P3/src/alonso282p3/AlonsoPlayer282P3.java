package alonso282p3;

/*
 AlonsoPlayer282P3.java

 Natalia Alonso
 12/8/2013
  
  * Note: The player's paths through the map remain marked even as treasure maps are found 
  * so the user can see all the places the player visits and what routes were taken (shown in red).
 */

import SimulationFramework.*;
import java.awt.Color;
import java.awt.Point;
import java.util.Stack;

public class AlonsoPlayer282P3 extends Bot {
     int xvalue, yvalue, dxvalue, dyvalue;
     int strength;
     int wealth;
     boolean treasure;
     boolean isPlaying;
     Point goal;
     Point start;
     Stack<Point> path;
     Point treasureGoal;
     
     public AlonsoPlayer282P3(String name, int x, int y, int dx, int dy, Color colorValue){
        super(name, x,y,colorValue);
        xvalue = x;
        yvalue = y;
        dxvalue = dx;
        dyvalue = dy;
        isPlaying = true;
        strength = 2000;
        treasure = false;
        start = new Point(x,y);
        goal = new Point(dx,dy);
    }
     
     @Override
    public void reset() {
        super.reset();
        xvalue = (int) point.getX();
        yvalue = (int) point.getY();
        strength = 2000;
    }
     
     public void setPath(Stack p){
         path = p;
     }
     
     public void setPlaying(){
         isPlaying =  !isPlaying;
     }
     
     boolean getPlaying(){
         return isPlaying;
     }
     
     public void setTreasure(){
         treasure =  !treasure;
     }
     
     boolean getTreasure(){
         return treasure;
     }
     
     public int getStrength(){
         return strength;
     }
     
     public void setStrength(int x)
     {
         strength += x;
     }
     
     public void subStrength(int x)
     {
         strength -= x;
     }
     
     public int getWealth(){
         return wealth;
     }
     
     public void setWealth(int x)
     {
         wealth += x;
     }
     
     public void subWealth(int x)
     {
         wealth -= x;
     }
    
    /**
     *
     * @param x
     * @param y
     */
    public void move(int x, int y) {
        moveTo(x, y);
        xvalue = x;
        yvalue = y;
    }

    @Override
    public void move() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
