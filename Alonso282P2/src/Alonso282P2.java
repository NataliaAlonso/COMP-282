/*
 Alonso282P2.java

 Natalia Alonso
 11/11/2013
  
  * Note: My player is extra greedy, and will find the path if there is a treasure map at the starting point and/or final destination. 
  * Note: The player's paths through the map remain marked even as treasure maps are found 
  * so the user can see all the places the player visits and what routes were taken (shown in red).
 */

import SimulationFramework.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
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

public class Alonso282P2 extends SimFrame {
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
    
    private Alonso282P2 app;
    private AlonsoPlayer282P2 player;
    HashMap<Point, AlonsoWayPoint282P2> wayPoints;
    Point start;
    Point destination;
    Point current;
    Point treasureDestination;
    HashMap<Point,Point> closedList;
    PriorityQueue<Node> openList;
    HashSet<Point> HSOpenList;
    boolean noPath;
    // application variables;
    /**
     * the actors "bots" of the simulation
     */
    
    /**
     *
     * @return
     */
    private HashMap<Point, AlonsoWayPoint282P2> readFile(){                     // Read set file
        HashMap<Point, AlonsoWayPoint282P2> wayPoint;   
        wayPoint = new HashMap<Point, AlonsoWayPoint282P2>();
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
                AlonsoWayPoint282P2 aAlonsoWayPoint282P2 = new AlonsoWayPoint282P2(x,y,h,c,g,mx,my,n,tempList);
                Point pKey = new Point(x,y);
                wayPoint.put(pKey, aAlonsoWayPoint282P2);
               }
        }
        catch(Exception e)
        {
            System.out.printf("Did not read file.");
        }
        return wayPoint;
    }
      
    
    public static void main(String args[]) {
        Alonso282P2 app = new Alonso282P2("Alonso282P2 - Treasure Map!", "terrain282.png");
        app.start();                                                            // start is inherited from SimFrame
    }

    /**
     * Make the application: create the MenuBar, "help" dialogs,
     */
    public Alonso282P2(String frameTitle, String imageFile) {
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
                JOptionPane.showMessageDialog(Alonso282P2.this,
                            "Simulating the A* greedy path taken \n"
                        +   "from a starting point to an ending  \n"
                        +   "point based on distance in 3D. If   \n"
                        +   "player finds a treasure map, it goes\n"
                        +   "to the treasure then resumes its    \n"
                        +   "journey to the destination from that\n"
                        +   "point.                              \n",
                        "Usage", // dialog window's title
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(Alonso282P2.this,
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
    
    private void addAlonsoWayPoint282P2Marker(AlonsoWayPoint282P2 w){           // Add wayPoint
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
    
    private void makePlayer(String name, int x, int y, Color color) {           // Make new player
        player = new AlonsoPlayer282P2(name, x, y, color);
        animatePanel.addBot(player);
    }
    
    private void updatePlayer(int x, int y, int d){                             // Update player attributes
        player.move(x,y);
        player.setWealth(wayPoints.get(new Point(x,y)).getGold());
        if(player.getWealth() >= wayPoints.get(new Point(x,y)).getCost()){
            player.subWealth(wayPoints.get(new Point(x,y)).getCost());
            player.setStrength(wayPoints.get(new Point(x,y)).getCost());
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
            checkStateToWait();
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
	Stack<Point> pathStack;
        pathStack = new Stack<Point>();
	if(noPath == false){
            pathStack.add(d);
            while(current != start){
                pathStack.add(current);
                current = closedList.get(current);
            }   
        }
        return pathStack;
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
        setStatus("Initial state of simulation");   
        wayPoints = readFile();
        Set<Point> keyset = wayPoints.keySet();
       
        for(Point i : keyset){
            addAlonsoWayPoint282P2Marker(wayPoints.get(i));}
        
        setStatus("Waypoints have been displayed.");
        
        int xinput;
        xinput = 0;
        int yinput;
        yinput = 0;
        //action listener for user click
        setStatus("Choose starting point.");                                    // Choose starting point
        boolean check = true;
        while(check)
        {
            Point temp;
            waitForMousePosition();                                             // Check if user point is in list of waypoints
            temp = getPoint((int)mousePosition.getX(),(int)mousePosition.getY());
            xinput = (int)temp.getX();
            yinput = (int)temp.getY();
            if(wayPoints.containsKey(temp))
            {   
                wayPoints.get(temp).setVisited();
                Marker m = new Marker(xinput, yinput, Color.BLUE);
                m.setSize(4);
                animatePanel.addTemporaryDrawable(m);

                makePlayer("Player",xinput,yinput,Color.red);

                check = false;
            }
        }
        setStatus("Choose destination.");                                       // Choose destination
        check = true;
        while(check)
        {
            Point temp;
            waitForMousePosition();                                             // Check if user point is in list of waypoints
            temp = getPoint((int)mousePosition.getX(),(int)mousePosition.getY());
            xinput = (int)temp.getX();
            yinput = (int)temp.getY();
            if(wayPoints.containsKey(temp))
            {   
                Marker m = new Marker(xinput, yinput, Color.BLUE);
                m.setSize(4);
                animatePanel.addTemporaryDrawable(m);
                destination = new Point(xinput,yinput); 
                check = false;
            }
        }
        setSimRunning(true);
    }

    @Override
    public synchronized void simulateAlgorithm() {
        // Declare and set any local control variables.
        // Or set up the initial algorithm state:
        // declare and set any algorithm specific varibles
        int xinput;
        int yinput;
        xinput = (int)player.getPoint().getX();
        yinput = (int)player.getPoint().getY();
        start = new Point(xinput,yinput);
        current = start;
        HSOpenList = new HashSet<Point>();
        closedList = new HashMap<Point,Point>();
        openList = new PriorityQueue<Node>(500,new Comparator<Node>() {
            public int compare(Node node1, Node node2) {
                return (node1.getDistance() < node2.getDistance()) ? -1 : (node1.getDistance() > node2.getDistance() ? 1 : 0);
            }
        });

        while (runnable()) {
            // put your algorithm code here.
            // ...
            // The following statement must be at end of any
            // overridden abstact simulateAlgorithm() method
            
            start = current;
            Stack<Point> pathStack = AstarPath(start, destination);
            
            if(noPath == true){
                setStatus("Failure, no path. Player "+ player.getStrength()+"  $"+player.getWealth());
                System.out.println("Failure, no path. Player "+ player.getStrength()+"  $"+player.getWealth());
            }
            else{
                while(pathStack.empty() != true)
                {
                    Point nextPoint = pathStack.pop();
                   
                    updatePlayer((int)nextPoint.getX(),(int)nextPoint.getY(),eucDist(nextPoint,current));
                    wayPoints.get(nextPoint).setVisited();
                    
                    if(wayPoints.get(nextPoint).getGold()>0){
                        setStatus("Gold ("+nextPoint.getX()+","+nextPoint.getY()+") $"+wayPoints.get(nextPoint).getGold()+" Player "+ player.getStrength()+"  $"+player.getWealth());
                        System.out.println("Gold ("+nextPoint.getX()+","+nextPoint.getY()+") $"+wayPoints.get(nextPoint).getGold()+" Player "+ player.getStrength()+"  $"+player.getWealth());
                        current = nextPoint;

                    }
                    if(wayPoints.get(nextPoint).getCost()>0){
                        setStatus("Cost ("+nextPoint.getX()+","+nextPoint.getY()+") $"+wayPoints.get(nextPoint).getCost()+" Player "+ player.getStrength()+"  $"+player.getWealth());
                        System.out.println("Cost ("+nextPoint.getX()+","+nextPoint.getY()+") $"+wayPoints.get(nextPoint).getCost()+" Player "+ player.getStrength()+"  $"+player.getWealth());
                        current = nextPoint;
                    }
                    if((wayPoints.get(nextPoint).getMapX()>0 || wayPoints.get(nextPoint).getMapY()>0) && player.getTreasure()==false){
                        setStatus("Map ("+nextPoint.getX()+","+nextPoint.getY()+") Treasure("+wayPoints.get(nextPoint).getMapX()+","+wayPoints.get(nextPoint).getMapY()+") Player "+player.getStrength()+"  $"+player.getWealth());
                        System.out.println("Map ("+nextPoint.getX()+","+nextPoint.getY()+") Treasure("+wayPoints.get(nextPoint).getMapX()+","+wayPoints.get(nextPoint).getMapY()+") Player "+player.getStrength()+"  $"+player.getWealth());
                        
                        closedList.clear();
                        openList.clear();
                        HSOpenList.clear();                       
                        current = nextPoint;
                        start = nextPoint;
                        treasureDestination = new Point(wayPoints.get(current).getMapX(),wayPoints.get(current).getMapY());
                        player.setTreasure();
                        wayPoints.get(current).setMapVisit();
                        
                        pathStack = AstarPath(start, treasureDestination);
                    }
                    checkStateToWait();
                }
                if(player.getTreasure() == true){
                    player.setTreasure();
                    setStatus("Success, reached treasure("+treasureDestination.getX()+","+treasureDestination.getY()+") Player "+ player.getStrength()+"  $"+player.getWealth());
                    System.out.println("Success, reached treasure("+treasureDestination.getX()+","+treasureDestination.getY()+") Player "+ player.getStrength()+"  $"+player.getWealth());
                    
                }
                else{
                    setStatus("Success, goal("+destination.getX()+","+destination.getY()+") Player "+ player.getStrength()+"  $"+player.getWealth());
                    System.out.println("Success, goal("+destination.getX()+","+destination.getY()+") Player "+ player.getStrength()+"  $"+player.getWealth());
                }
            }
            closedList.clear();
            openList.clear();
            HSOpenList.clear();
            if(wayPoints.get(destination).getVisited()){
                setSimRunning(false);
                return;
            }
        }
    }
}
