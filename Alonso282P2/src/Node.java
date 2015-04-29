/*
 Node.java

 Natalia Alonso
 11/11/2013
 */

import java.awt.Point;

public class Node {
    private int xvalue, yvalue;
    int distance;
    Point back;
    
    public Node(int x, int y, int d, Point b){
        distance = d;
        xvalue = x;
        yvalue = y;
        back = b;
    }
    
    public int getX(){
        return xvalue;
    }
    
    public int getY(){
        return yvalue;
    }
    
    public int getDistance(){
        return distance;
    }
    
    public Point getBack(){
        return back;
     }
}
