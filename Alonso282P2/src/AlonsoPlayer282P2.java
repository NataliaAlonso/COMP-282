/*
 AlonsoPlayer282P2.java

 Natalia Alonso
 11/11/2013
 */

import SimulationFramework.*;
import java.awt.Color;

public class AlonsoPlayer282P2 extends Bot {
     private int xvalue, yvalue;
     int strength;
     int wealth;
     boolean treasure;
     
     public AlonsoPlayer282P2(String name, int x, int y, Color colorValue){
        super(name, x,y,colorValue);
        xvalue = x;
        yvalue = y;
        strength = 2000;
        wealth = 1000;
        treasure = false;
    }
     
     @Override
    public void reset() {
        super.reset();
        xvalue = (int) point.getX();
        yvalue = (int) point.getY();
        strength = 2000;
        wealth = 1000;
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
