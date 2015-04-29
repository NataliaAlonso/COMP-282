
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import SimulationFramework.*;
import java.awt.Color;

/**
 *
 * @author Natalia Alonso
 */
public class AlonsoPlayer282P1 extends Bot {
     private int xvalue, yvalue;
     int strength;
     
     
     public AlonsoPlayer282P1(String name, int x, int y, Color colorValue){
        super(name, x,y,colorValue);
        xvalue = x;
        yvalue = y;
        strength = 600;
    }
     
     @Override
    public void reset() {
        super.reset();
        xvalue = (int) point.getX();
        yvalue = (int) point.getY();
        strength = 600;
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
