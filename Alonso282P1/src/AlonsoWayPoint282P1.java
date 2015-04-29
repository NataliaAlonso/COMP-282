
import SimulationFramework.*;
import java.awt.Color;
/**
 *
 * @author Natalia Alonso
 */
public class AlonsoWayPoint282P1{
    private int xvalue;
    private int height;
    private int yvalue;
    private int gold=0;
    private int cost=0;
    private int mapX;
    private int mapY;
    private int neighbors;
    private boolean visited;
    private Marker mark;
    //int key;
    
    public AlonsoWayPoint282P1(int x, int y, int h, int c,int g, int mx, int my, int n){
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
}
