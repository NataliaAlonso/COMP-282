package alonso282p3;

/*
 AlonsoWayPointP3.java

 Natalia Alonso
 12/8/2013
  
  * Note: The player's paths through the map remain marked even as treasure maps are found 
  * so the user can see all the places the player visits and what routes were taken (shown in red).
 */

import SimulationFramework.*;
import java.awt.Color;
import java.util.*;
import java.awt.*;

public class AlonsoWayPointP3{
    private int xvalue;
    private int height;
    private int yvalue;
    private int gold=0;
    private int cost=0;
    private int mapX;
    private int mapY;
    private int neighbors;
    private ArrayList<Point> neighborList;
    private boolean visited;
    private Marker mark;
    
    public AlonsoWayPointP3(int x, int y, int h, int c,int g, int mx, int my, int n, ArrayList<Point> a){
        xvalue = x;
        yvalue = y;
        height = h;
        cost = c;
        gold = g;
        mapX = mx;
        mapY = my;
        neighbors = n;
        visited = false;
        mark = null;
        neighborList = a;
    }
    
    public void setMapVisit(){
        mapX=0;
        mapY=0;
    }
    
    public int getX(){
        return xvalue;
    }
    public int getY(){
        return yvalue;
    }
    
    public int getGold(){
        return gold;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getCost(){
        return cost;
    }
    
    public int getMapX(){
        return mapX;
    }
    
    public int getMapY(){
        return mapY;
    }
    
    public int getNeighbors(){
        return neighbors;
    }
    
    public void setMarker(Marker m) {
        mark = m;
    }
    
    public void setVisited(){
        visited = true;
    }
    
    public boolean getVisited(){
        return visited;
    }
    
    public ArrayList<Point> getNeighborList(){
        return neighborList;
    }
}

