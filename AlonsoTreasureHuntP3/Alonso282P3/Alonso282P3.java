package alonso282p3;

/*
 Alonso282P3.java

 Natalia Alonso
 12/8/2013
  
  * Note: The player's paths through the map remain marked even as treasure maps are found 
  * so the user can see all the places the player visits and what routes were taken (shown in red).
 */

import SimulationFramework.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import javax.swing.*;
// CLASSPATH = ... /282projects/SimulationFrameworkV3
// PATH = ... /282projects/SimulationFrameworkV3/SimulationFramework

/**
 * EmptySimFrame is the simulation's main class (simulation app) that is a
 * subclass of SimFrame.
 * <p> * 282 Simulation Framework applications must have a subclass of SimFrame
 * that also has a main method. The simulation app can make the appropriate
 * author and usage "help" dialogs, override setSimModel() and
 * simulateAlgorithm() abstract methods inherited from SimFrame. They should
 * also add any specific model semantics and actions.
 *
 * <p>
 *
 * The simulated algorithm is defined in simulateAlgorithm().
 *
 * <p>
 * EmptySimFrame UML class diagram
 * <p>
 * <Img align left src="../UML/EmptySimFrame.png">
 *
 * @since 8/12/2013
 * @version 3.0
 * @author G. M. Barnes
 */

public class Alonso282P3 extends SimFrame {
    // eliminate warning @ serialVersionUID

    private static final long serialVersionUID = 42L;
    // GUI components for application's menu
    /**
     * the simulation application
     */
    private final int DIM = 500;                    // pixel dimension of the display  DIM by DIM
    private final int SPACE = 7;                    // grid spacing for nodes in display
    private final int KEYS = (int) Math.sqrt(Math.pow(DIM / SPACE, 2) * 2);
    private final float SCALE = 0.71f;              //  vertical offset multiplier, aspect ratio   
    private final boolean RANDOMIZE = true;         // set false for linked list
    // variables
    
    private Alonso282P3 app;
    private AlonsoPlayer282P3 player0;
    private AlonsoPlayer282P3 player1;
    private AlonsoPlayer282P3 player2;
    private AlonsoPlayer282P3 player3;
    ArrayList<AlonsoPlayer282P3> playerList;
    HashMap<Point, AlonsoWayPointP3> wayPoints;
    Point start;
    Point destination;
    Point current;
    Point treasureDestination;
    HashMap<Point,Point> closedList;
    PriorityQueue<Node> openList;
    HashSet<Point> HSOpenList;
    HashMap<Integer,Stack> playerPaths;
    boolean noPath;
    Connection conn;
    // application variables;
    /**
     * the actors "bots" of the simulation
     */
    
    /**
     *
     * @return
     */
    private HashMap<Point, AlonsoWayPointP3> readFile(){                     // Read set file
        HashMap<Point, AlonsoWayPointP3> wayPoint;   
        wayPoint = new HashMap<Point, AlonsoWayPointP3>();
        try{            
            Scanner s = new Scanner(new File("waypointNeighbor.txt"));
            while(s.hasNextInt()){
                int x = s.nextInt();
                int y = s.nextInt();
                int h = s.nextInt();
                int c = s.nextInt();
                int g = s.nextInt();
                int mx = s.nextInt();
                int my = s.nextInt();  
                int n = s.nextInt();
                ArrayList<Point> tempList = new ArrayList<Point>();
                for (int i = 0; i < n; i++){
                    int tempX = s.nextInt();
                    int tempY = s.nextInt();
                    tempList.add(new Point(tempX,tempY));
                }   
                AlonsoWayPointP3 aAlonsoWayPointP3 = new AlonsoWayPointP3(x,y,h,c,g,mx,my,n,tempList);
                Point pKey = new Point(x,y);
                wayPoint.put(pKey, aAlonsoWayPointP3);
               }
        }
        catch(Exception e)
        {
            System.out.printf("Did not read file.");
        }
        return wayPoint;
    }
      
    
    public static void main(String args[]) {
        Alonso282P3 app = new Alonso282P3("Alonso282P3 - Treasure Hunt!", "terrain282.png");
        app.start();                                                            // start is inherited from SimFrame
    }

    /**
     * Make the application: create the MenuBar, "help" dialogs,
     */
    public Alonso282P3(String frameTitle, String imageFile) {
        super(frameTitle, imageFile);
        // create menus
        JMenuBar menuBar = new JMenuBar();
        // set About and Usage menu items and listeners.
        aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        aboutMenu.setToolTipText(
                "Display information about this program");
        // create a menu item and the dialog it invoke 
        usageItem = new JMenuItem("Usage");
        authorItem = new JMenuItem("Author");
        usageItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(Alonso282P3.this,
                            "Simulating the A* greedy path taken \n"
                        +   "from a starting point to an ending  \n"
                        +   "point based on distance in 3D by 4  \n"
                        +   "bots to preset destinations. If a   \n"
                        +   "player finds a treasure map, it goes\n"
                        +   "to the treasure then resumes its    \n"
                        +   "journey to the destination from that\n"
                        +   "point. If the player encounters     \n"
                        +   "another player, they have a contest.\n",
                        "Usage", // dialog window's title
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(Alonso282P3.this,
                        "Natalia Alonso \n"
                        + "n.alonso213@gmail.com \n"
                        + "COMP 282",
                        "Author", // dialog window's title
                        JOptionPane.INFORMATION_MESSAGE,
                        //  author's picture 
                        new ImageIcon("author.png"));
            }
        });
        aboutMenu.add(usageItem);    
        aboutMenu.add(authorItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        validate();
    }
    
    private void addAlonsoWayPointP3Marker(AlonsoWayPointP3 w){           // Add wayPoint
        int x = w.getX();
        int y = w.getY();
        int cost = w.getCost();
        int gold = w.getGold();
        int mapX = w.getMapX();
        int mapY = w.getMapY();
        if ( cost == 0 && gold == 0 && mapX == 0 && mapY == 0 ){                // Normal marker
            Marker m = new Marker(x, y, Color.black);
            m.setSize(2);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        } 
        if( gold > 0 ){                                                         // Gold marker
            Marker m = new Marker(x, y, Color.yellow);
            m.setSize(5);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        if( cost > 0 ){                                                         // Cost marker
            Marker m = new Marker(x, y, Color.cyan);
            m.setSize(5);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        if( mapX > 0 || mapY > 0 ){                                             // Treasure map marker
            Marker m = new Marker(x, y, Color.magenta);
            m.setSize(5);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        ArrayList<Point> tempList = w.getNeighborList();                        // Add connectors between neighbors
        for(int i = 0; i <w.getNeighbors(); i++)
        {
            Connector c = new Connector(w.getX(),w.getY(),(int)tempList.get(i).getX(),(int)tempList.get(i).getY(),Color.black);
            animatePanel.addPermanentDrawable(c);
        }
        
        animatePanel.repaint();
    }
    
    private AlonsoPlayer282P3 makePlayer(String name, int x, int y, int dx, int dy, Color color) {           // Make new player
        AlonsoPlayer282P3 player = new AlonsoPlayer282P3(name, x, y, dx, dy, color);
        animatePanel.addBot(player);
        return player;
    }
    
    private void updatePlayer(int x, int y, int d, AlonsoPlayer282P3 player, int i){                             // Update player attributes
        player.move(x,y);
        updatePlayerTable(x,y,i);
        int treasure = getTreasure(x,y);
        player.setWealth(treasure);
        updateWealth(player.getWealth(),i);
        int cost = getCost(x,y);
        if(getWealth(i) >= cost){
            player.subWealth(cost);
            updateWealth(player.getWealth(),i);
            player.setStrength(cost);
        }
        player.subStrength(d);
    }
    
    public Point getPoint(int x, int y){                                        // Get point chosen
	if(x%20 == 0 && y%20 == 0 )
	{
            return new Point(x,y); 
	}
	else
	{
            if(x%20 < 10){x = x - x%20;}
            if(x%20 >= 10){x = x + 20 - x%20;}
            if(y%20 < 10){y = y - y%20;}
            if(y%20 >= 10){y = y + 20 - y%20;}
	}
	return new Point(x,y);
    }
    
    public int eucDist(Point a, Point b){                                       // Calculate Euclidean 3D distance
	return (int) Math.sqrt(Math.pow(wayPoints.get(a).getX()-wayPoints.get(b).getX(),2)+Math.pow(wayPoints.get(a).getY()-wayPoints.get(b).getY(),2)+Math.pow(wayPoints.get(a).getHeight()-wayPoints.get(b).getHeight(),2));
    }
    
    public Stack AstarPath(Point start, Point d){                               // A* path algorithm
	current = start;
        Node currentNode;
	animatePanel.clearTemporaryDrawables();
            
	Marker m = new Marker((int)start.getX(),(int)start.getY(), Color.BLUE);
	m.setSize(4);
	animatePanel.addTemporaryDrawable(m);
	
	m = new Marker((int)d.getX(),(int)d.getY(), Color.BLUE);
	m.setSize(4);
	animatePanel.addTemporaryDrawable(m);
   
        openList.add(new Node((int)start.getX(),(int)start.getY(),eucDist(start,d),current));
	HSOpenList.add(current);
	boolean cont = true;
	noPath = false;		
	while(cont)       // Player continues to their destination even when they have zero or negative strength or wealth
	{            
            if(openList.size()>0){
                currentNode = openList.poll();
                current = new Point(currentNode.getX(),currentNode.getY());

                HSOpenList.remove(current);
                closedList.put(current,currentNode.getBack());

                Marker cm = new Marker((int)current.getX(),(int)current.getY(), Color.gray );
                cm.setSize(2);
                animatePanel.addTemporaryDrawable(cm);
                
                
                
                int numNeighbors = wayPoints.get(current).getNeighbors();
                for(int i = 0; i < numNeighbors; i++){
                    Point tempPoint = wayPoints.get(current).getNeighborList().get(i);
                    if ((tempPoint.getX() == d.getX()) && (tempPoint.getY() == d.getY())){
                            i = numNeighbors+1;
                            cont = false;
                    }
                    else{
                        Node tempNode = new Node((int)tempPoint.getX(),(int)tempPoint.getY(),currentNode.getDistance()+eucDist(tempPoint,current)+eucDist(tempPoint,d),current);
                        if( HSOpenList.contains(tempPoint)!=true && closedList.containsKey(tempPoint)!=true ){
                                openList.add(tempNode);
                                Marker om = new Marker((int)tempPoint.getX(),(int)tempPoint.getY(), Color.WHITE);
                                om.setSize(3);
                                animatePanel.addTemporaryDrawable(om);  
                        }
                    }
                } 
            }
            else{
                cont = false;
                noPath = true;
            }
            
	}
        checkStateToWait();
	Stack<Point> pathStack;
        pathStack = new Stack<Point>();
	if(noPath == false){
            pathStack.add(d);
            while(current != start){
                pathStack.add(current);
                current = closedList.get(current);
            }   
        }
        animatePanel.clearTemporaryDrawables();
        return pathStack;
    }
    
public Point getDBPoint(int p){
	try { 
            Statement stmt; 
            ResultSet res;
	    int x =0;
            int y =0; 
            stmt = conn.createStatement(); 
            res = stmt.executeQuery("SELECT Place FROM Player WHERE ID = "+ p);
            while (res.next()) { 
                String place = res.getString("Place"); 
                Scanner s = new Scanner(place);
                x = s.nextInt();
                y = s.nextInt();
            } 
            res.close();
            stmt.close();
            Point tempPoint = new Point(x,y);
	    return tempPoint; 
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
            return null; 
        }

}

public int getWealth(int p){
	try { 
            Statement stmt; 
            ResultSet res;
	    int g=0; 
            stmt = conn.createStatement(); 
            res = stmt.executeQuery("SELECT Wealth FROM Player WHERE ID = "+ p);
            while (res.next()) { 
               g = res.getInt("Wealth");
            } 
            res.close();
            stmt.close();
	    return g;
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
	return 0;	
}

public int getCost(int x, int y){
	try { 
            Statement stmt; 
            ResultSet res;
	    int g=0; 
            stmt = conn.createStatement(); 
            res = stmt.executeQuery("SELECT Cost FROM City WHERE Place = '"+x+" "+y+"'");
            while (res.next()) { 
               g = res.getInt("Cost");
            } 
            res.close();
            stmt.close();
	    return g;
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }	
        return 0;
}

public int getTreasure(int x, int y){
	try { 
            Statement stmt; 
            ResultSet res;
	    int g=0; 
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT Gold FROM Treasure WHERE Place = '"+x+" "+y+"'");
            while (res.next()) { 
               g = res.getInt("Gold");
            } 
            res.close();
            stmt.close();
            return g;
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
        return 0;
}

public Point getTPoint(int x, int y){
	try { 
            Statement stmt; 
            ResultSet res;
	    int mx=0;
            int my=0;  
            stmt = conn.createStatement();
            res = stmt.executeQuery("SELECT treasurePlace FROM Map WHERE Place = '"+x+" "+y+"'");
            while (res.next()) { 
                String place = res.getString("treasurePlace"); 
                Scanner s = new Scanner(place);
                mx = s.nextInt();
                my = s.nextInt();
            } 
            res.close();
            stmt.close();
	    return new Point(mx,my); 
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
        return null;
}

void updatePlayerTable(int x, int y, int i){
        try { 
            Statement stmt; 
            stmt = conn.createStatement();
            String u = "update Player set Place = '"+x+" "+y+"' where ID = '"+i+"'";
            stmt.executeUpdate(u);
            stmt.close();
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
}

void updateWealth(int w, int i){
        try { 
            Statement stmt; 
            stmt = conn.createStatement();
            String u = "update Player set Wealth = '"+w+"' where ID = '"+i+"'";
            stmt.executeUpdate(u);
            stmt.close();
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
}

void updateTreasureTable(int x, int y){
        try { 
            Statement stmt; 
            stmt = conn.createStatement();
            String u = "update Treasure set Gold = 0 where Place = '"+x+" "+y+"'";
            stmt.executeUpdate(u);
            stmt.close();            
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
}

void updateMapTable(int x, int y){
        try { 
            Statement stmt; 
            stmt = conn.createStatement();
            String u = "update Map set treasurePlace = '0 0' where Place = '"+x+" "+y+"'";
            stmt.executeUpdate(u);
            stmt.close();
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
}    

void CreateTable(){
        try { 
            Statement stmt; 
            stmt = conn.createStatement();
            String u = "DELETE FROM Player";
            stmt.executeUpdate(u);
            u = "DELETE FROM Treasure";
            stmt.executeUpdate(u);
            u = "DELETE FROM Map";
            stmt.executeUpdate(u);
            u = "DELETE FROM City";
            stmt.executeUpdate(u);
	    u = "INSERT INTO Player SELECT * FROM iPlayer";
            stmt.executeUpdate(u);
	    u = "INSERT INTO Treasure SELECT * FROM iTreasure";
            stmt.executeUpdate(u);
	    u = "INSERT INTO Map SELECT * FROM iMap";
            stmt.executeUpdate(u);
	    u = "INSERT INTO City SELECT * FROM iCity";
            stmt.executeUpdate(u);		
            stmt.close();
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
}
    /**
     * Set up the actors (Bots), wayPoints (Markers), and possible traversal
     * paths (Connectors) for the simulation.
     */
    @Override
    public void setSimModel() {
        // set any initial visual Markers or Connectors
        // get any required user mouse clicks for positional information.
        // initialize any algorithm halting conditions (ie, number of steps/moves).
        try{
        conn = DriverManager.getConnection("jdbc:mysql://vrlab.ecs.csun.edu/alonso64db", "alonso64", "natalia866");
        setStatus("Initial state of simulation");   
        wayPoints = readFile();
        Set<Point> keyset = wayPoints.keySet();
       
        for(Point i : keyset){
            addAlonsoWayPointP3Marker(wayPoints.get(i));}
        
        setStatus("Waypoints have been displayed.");
        CreateTable();
        
        player0 = makePlayer("Player1",(int)getDBPoint(1).getX(),(int)getDBPoint(1).getY(),500,440,Color.red);
        player0.setWealth(getWealth(1));
        player1 = makePlayer("Player2",(int)getDBPoint(2).getX(),(int)getDBPoint(2).getY(),20,500,Color.pink);
        player1.setWealth(getWealth(2));
        player2 = makePlayer("Player3",(int)getDBPoint(3).getX(),(int)getDBPoint(3).getY(),500,20,Color.orange);
        player2.setWealth(getWealth(3));
        player3 = makePlayer("Player4",(int)getDBPoint(4).getX(),(int)getDBPoint(4).getY(),20,20,Color.blue);
        player3.setWealth(getWealth(4));
        
        playerList = new ArrayList<AlonsoPlayer282P3>();
        playerList.add(player0);
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
                
        // Set player initial positions in simulation
        setSimRunning(true);
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
    }

    @Override
    public synchronized void simulateAlgorithm() {
        // Declare and set any local control variables.
        // Or set up the initial algorithm state:
        // declare and set any algorithm specific varibles
        try{
            Date date = new Date();
            long time = date.getTime();
            Random r = new Random(time);
            Stack<Point> currentStack;
            Point nextPoint;
            int index;
            AlonsoPlayer282P3 currentPlayer;
            int iwealth;
            int cwealth;
            int treasure;
            int ccost;
            Point tpoint;
            boolean finish;
            Point ipoint;
            int npX;
            int npY;

            for(int p=0; p<4;p++){
                    HSOpenList = new HashSet<Point>();
                    closedList = new HashMap<Point,Point>();
                    openList = new PriorityQueue<Node>(500,new Comparator<Node>() {
                        public int compare(Node node1, Node node2) {
                            return (node1.getDistance() < node2.getDistance()) ? -1 : (node1.getDistance() > node2.getDistance() ? 1 : 0);
                        }
                    });
                    playerList.get(p).setPath(AstarPath(playerList.get(p).start, playerList.get(p).goal));
                    setStatus("Player < "+p+" > path = < "+playerList.get(p).start.getX()+","+playerList.get(p).start.getY()+" > to < "+playerList.get(p).goal.getX()+","+playerList.get(p).goal.getY()+" > wealth = < "+playerList.get(p).getWealth()+" > strength = < "+playerList.get(p).getStrength()+" >");
                    System.out.println("Player < "+p+" > path = < "+playerList.get(p).start.getX()+","+playerList.get(p).start.getY()+" > to < "+playerList.get(p).goal.getX()+","+playerList.get(p).goal.getY()+" > wealth = < "+playerList.get(p).getWealth()+" > strength = < "+playerList.get(p).getStrength()+" >");
                    playerList.get(p).path.pop();
            }

            while (runnable()) {
                // put your algorithm code here.
                // ...
                // The following statement must be at end of any
                // overridden abstact simulateAlgorithm() method
                index = r.nextInt(4);
                if(playerList.get(index).isPlaying){
                    currentStack = playerList.get(index).path;
                    nextPoint = currentStack.pop();
                    currentPlayer = playerList.get(index);
                    current = getDBPoint(index+1);
                    npX=(int)nextPoint.getX();
                    npY=(int)nextPoint.getY();
                    updatePlayer(npX,npY,eucDist(nextPoint,current),playerList.get(index),index+1);
                    checkStateToWait();
                    iwealth = getWealth(index+1);
                    ipoint = getDBPoint(index+1);

                    for(int c = 0; c<4; c++){
                       if(c != index && (getDBPoint(c+1) == ipoint)){
                           cwealth = getWealth(c+1);
                           if(iwealth > cwealth){
                               setStatus("Contest! Player < "+index+" > with wealth < "+iwealth+" > wins agains player < "+c+" > with wealth < "+cwealth+" >");
                               System.out.println("Contest! Player < "+index+" > with wealth < "+iwealth+" > wins agains player < "+c+" > with wealth < "+cwealth+" >");
                               updateWealth(iwealth+(cwealth/3),index+1);
                               updateWealth(cwealth-(cwealth/3),c+1);
                               playerList.get(index).setWealth(playerList.get(c).getWealth()/3);
                               playerList.get(c).subWealth(playerList.get(c).getWealth()/3);
                               iwealth = getWealth(index+1);
                           }
                           else if(getWealth(index+1) < getWealth(c+1)){
                               setStatus("Contest! Player < "+c+" > with wealth < "+cwealth+" > wins agains player < "+index+" > with wealth < "+iwealth+" >");
                               System.out.println("Contest! Player < "+c+" > with wealth < "+cwealth+" > wins agains player < "+index+" > with wealth < "+iwealth+" >");
                               updateWealth(cwealth+(iwealth/3),c+1);
                               updateWealth(iwealth-(iwealth/3),index+1);
                               playerList.get(c).setWealth(playerList.get(index).getWealth()/3);
                               playerList.get(index).subWealth(playerList.get(index).getWealth()/3);
                               iwealth = getWealth(index+1);
                           }
                       }
                    }
                    treasure = getTreasure(npX,npY);
                    if(treasure > 0){
                        setStatus("Player < "+index+" > at treasure = < "+npX+","+npY+" >  wealth = < "+iwealth+" > gold = < "+treasure+" >");
                        System.out.println("Player < "+index+" > at treasure = < "+npX+","+npY+" >  wealth = < "+iwealth+" > gold = < "+treasure+" >");
                        updateTreasureTable(npX,npY);
                        current = nextPoint;
                    }
                    ccost = getCost(npX,npY);
                    if(ccost>0){
                        setStatus("Player < "+index+" > at city = < "+npX+","+npY+" > cost = < "+ccost+" > wealth = < "+iwealth+" > strength = < "+currentPlayer.getStrength()+" >");
                        System.out.println("Player < "+index+" > at city = < "+npX+","+npY+" > cost = < "+ccost+" > wealth = < "+iwealth+" > strength = < "+currentPlayer.getStrength()+" >");
                        current = nextPoint;
                    }
                    tpoint = getTPoint(npX,npY);
                    if((tpoint.getX() != 0)&& (tpoint.getY()!=0) && currentPlayer.getTreasure()==false){
                        setStatus("Player < "+ index +" > at map = < "+npX+","+npY+" > treasure at = < "+tpoint.getX()+","+tpoint.getY()+" >");
                        System.out.println("Player < "+ index +" > at map = < "+ npX+","+npY +" > treasure at = < "+ tpoint.getX() +","+ tpoint.getY() +" >");

                        closedList.clear();
                        openList.clear();
                        HSOpenList.clear();                       
                        current = nextPoint;
                        start = nextPoint;
                        playerList.get(index).treasureGoal = tpoint;
                        playerList.get(index).setTreasure();
                        wayPoints.get(current).setMapVisit();

                        playerList.get(index).setPath(AstarPath(start, currentPlayer.treasureGoal));
                        updateMapTable(npX,npY);
                     }

                     if((currentPlayer.getTreasure() == true) && (nextPoint == currentPlayer.treasureGoal)){
                        currentPlayer.setTreasure();
                        setStatus("Player "+ index+" reached treasure("+currentPlayer.treasureGoal.getX()+","+currentPlayer.treasureGoal.getY()+") Player "+ currentPlayer.getStrength()+"  $"+iwealth);
                        System.out.println("Player "+ index+" reached treasure("+currentPlayer.treasureGoal.getX()+","+currentPlayer.treasureGoal.getY()+") Player "+ currentPlayer.getStrength()+"  $"+iwealth);
                        playerList.get(index).setPath(AstarPath(nextPoint, currentPlayer.goal));
                     }
                     if(currentPlayer.goal == nextPoint){
                         playerList.get(index).setPlaying();
                         playerList.get(index).setWealth(playerList.get(index).getStrength());
                         updateWealth(playerList.get(index).getStrength(),index+1);
                         iwealth = getWealth(index+1);
                         playerList.get(index).subStrength(playerList.get(index).getStrength());
                         setStatus("Player < "+ index +" > is done  goal = < "+ currentPlayer.goal.getX()+","+currentPlayer.goal.getY() +" >  wealth = < "+ iwealth +" >");
                         System.out.println("Player < "+ index +" > is done  goal = < "+ currentPlayer.goal.getX() +","+ currentPlayer.goal.getY() +" >  wealth = < "+ iwealth +" >");
                     }
                }
                closedList.clear();
                openList.clear();
                HSOpenList.clear();
                finish = true;
                for(int done = 0; done<4;done++){
                    if(playerList.get(done).isPlaying){
                        finish = false;
                    }
                }
                if(finish){
                    conn.close();
                    setSimRunning(false);
                    System.out.println("Simulation complete.");
                }
            }
        
        } 
        catch (Exception e) { 
            System.out.println("Error: " + e); 
        }
    }
}
