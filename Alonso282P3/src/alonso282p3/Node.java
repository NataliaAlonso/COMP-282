package alonso282p3;

/*
 Node.java

 Natalia Alonso
 12/8/2013
  
  * Note: The player's paths through the map remain marked even as treasure maps are found 
  * so the user can see all the places the player visits and what routes were taken (shown in red).
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
