/*
 Alonso282P1.java

 Natalia Alonso
 10/04/2013
 */

import SimulationFramework.*;
import alonso282p1.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
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

public class Alonso282P1 extends SimFrame {
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
    
    private Alonso282P1 app;
    private AlonsoPlayer282P1 player;
    HashMap<Point, AlonsoWayPoint282P1> wayPoints;
    Point destination;
    private int distance;
    // application variables;
    /**
     * the actors "bots" of the simulation
     */
    private ArrayList<AlonsoPlayer282P1> abot;
    
    /**
     *
     * @return
     */
    private HashMap<Point, AlonsoWayPoint282P1> readFile(){
        HashMap<Point, AlonsoWayPoint282P1> wayPoint;   
        wayPoint = new HashMap<>();
        try{
            Scanner s = new Scanner(new File("waypoint.txt"));
            while(s.hasNextInt()){
                int x = s.nextInt();
                int y = s.nextInt();
                int h = s.nextInt();
                int c = s.nextInt();
                int g = s.nextInt();
                int mx = s.nextInt();
                int my = s.nextInt();  
                int n = s.nextInt();

                AlonsoWayPoint282P1 aAlonsoWayPoint282P1 = new AlonsoWayPoint282P1(x,y,h,c,g,mx,my,n);
                Point pKey = new Point(x,y);
                wayPoint.put(pKey, aAlonsoWayPoint282P1);
               }
        }
        catch(Exception e)
        {
            System.out.printf("Did not read file.");
        }
        return wayPoint;
    }
      
    
    public static void main(String args[]) {
        
        Alonso282P1 app = new Alonso282P1("Alonso282P1", "terrain282.png");
        app.start();  // start is inherited from SimFrame
    }

    /**
     * Make the application: create the MenuBar, "help" dialogs,
     */
    public Alonso282P1(String frameTitle, String imageFile) {
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
                JOptionPane.showMessageDialog(Alonso282P1.this,
                            "Simulating the simple path taken \n"
                        +   "from a starting point to an ending  \n"
                        +   "point based on distance in 3D. \n",
                        "Usage", // dialog window's title
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(Alonso282P1.this,
                        "Natalia Alonso \n"
                        + "n.alonso213@gmail.com \n"
                        + "COMP 282",
                        "Author", // dialog window's title
                        JOptionPane.INFORMATION_MESSAGE,
                        //  author's picture 
                        new ImageIcon("author.png"));
            }
        });
        // add menu items to menu 
        aboutMenu.add(usageItem);   // add menu item to menu
        aboutMenu.add(authorItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        validate();  // resize layout managers
        // construct the application specific variables
    }
    
    private void addAlonsoWayPoint282P1Marker(AlonsoWayPoint282P1 w){
        int x = w.getX();
        int y = w.getY();
        int cost = w.getCost();
        int gold = w.getGold();
        int mapX = w.getMapX();
        int mapY = w.getMapY();
        if ( cost == 0 && gold == 0 && mapX == 0 && mapY == 0 ){
            Marker m = new Marker(x, y, Color.black);
            m.setSize(2);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        } 
        if( gold > 0 ){
            Marker m = new Marker(x, y, Color.yellow);
            m.setSize(4);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        if( cost > 0 ){
            Marker m = new Marker(x, y, Color.cyan);
            m.setSize(3);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        if( mapX > 0 || mapY > 0 ){
            Marker m = new Marker(x, y, Color.magenta);
            m.setSize(2);
            w.setMarker(m);
            animatePanel.addPermanentDrawable(m);
        }
        animatePanel.repaint();
    }
    
    private void makePlayer(String name, int x, int y, Color color) {
        player = new AlonsoPlayer282P1(name, x, y, color);
        animatePanel.addBot(player);
    }
    
    private void updatePlayer(int x, int y, int d){
        player.move(x,y);
        player.setStrength(wayPoints.get(new Point(x,y)).getGold());
        player.subStrength(d);
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
            addAlonsoWayPoint282P1Marker(wayPoints.get(i));}
        
        setStatus("Waypoints have been displayed.");
        
        int xinput;
        xinput = 0;
        int yinput;
        yinput = 0;
        //action listener for user click
        setStatus("Choose starting point.");
        boolean check = true;
        while(check)
        {
            Point temp;
            waitForMousePosition();     // check if user point is in list of waypoint
            if((int)mousePosition.getX()%20 == 0 && (int)mousePosition.getX()%20 == 0 )
            {
                xinput = (int)mousePosition.getX();
                yinput = (int) mousePosition.getY(); 
            }
            else
            {
                if((int)mousePosition.getX()%20 < 10){xinput = (int)mousePosition.getX() - (int)mousePosition.getX()%20;}
                if((int)mousePosition.getX()%20 >= 10){xinput = (int)mousePosition.getX() + 20 - (int)mousePosition.getX()%20;}
                if((int)mousePosition.getY()%20 < 10){yinput = (int)mousePosition.getY() - (int)mousePosition.getY()%20;}
                if((int)mousePosition.getY()%20 >= 10){yinput = (int)mousePosition.getY() + 20 - (int)mousePosition.getY()%20;}
            }    
            
            temp = new Point(xinput,yinput);
     
            if(wayPoints.containsKey(temp))
            {   
                wayPoints.get(temp).setVisited();
                Marker m = new Marker(xinput, yinput, Color.green);
                m.setSize(4);
                animatePanel.addTemporaryDrawable(m);

                makePlayer("Player",xinput,yinput,Color.red);

                check = false;
            }
        }
        setStatus("Choose destination.");
        check = true;
        while(check)
        {
            Point temp;
            waitForMousePosition();     // check if user point is in list of waypoint
            if((int)mousePosition.getX()%20 == 0 && (int)mousePosition.getX()%20 == 0 )
            {
                xinput = (int)mousePosition.getX();
                yinput = (int) mousePosition.getY();         
            }
            else{
                if((int)mousePosition.getX()%20 < 10){xinput = (int)mousePosition.getX() - (int)mousePosition.getX()%20;}
                if((int)mousePosition.getX()%20 >= 10){xinput = (int)mousePosition.getX() + 20 - (int)mousePosition.getX()%20;}
                if((int)mousePosition.getY()%20 < 10){yinput = (int)mousePosition.getY() - (int)mousePosition.getY()%20;}
                if((int)mousePosition.getY()%20 >= 10){yinput = (int)mousePosition.getY() + 20 - (int)mousePosition.getY()%20;}
            } 
            temp = new Point(xinput,yinput);
            if(wayPoints.containsKey(temp))
            {   
                Marker m = new Marker(xinput, yinput, Color.RED);
                m.setSize(4);
                animatePanel.addTemporaryDrawable(m);
                destination = new Point(xinput,yinput); 
                check = false;
            }
        }
        distance = 1000000;
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
        Point closest = new Point(xinput,yinput);
        Point current = new Point(xinput,yinput);
        Point start;
        int newx;
        int newy;
        int tempdistance;
        int count = 0;
        int pdistance = 0;
        while (runnable()) {
            // put your algorithm code here.
            // ...
            // The following statement must be at end of any
            // overridden abstact simulateAlgorithm() method
            while(closest != destination && player.getStrength() > 0)
            {
                distance = 1000000;
                start = new Point(xinput,yinput);
                closest = start;
                for(int i = -20; i <= 20; i+=20)
                {
                  for(int j = -20; j <= 20; j+=20) 
                  {
                    newx = xinput+i;
                    newy = yinput+j;
                    current = new Point(newx,newy);
                    if(wayPoints.containsKey(current) && wayPoints.get(current).getVisited()== false)
                    {
                        if( Math.sqrt(Math.pow(newx-xinput,2)+Math.pow(newy-yinput,2)+Math.pow(wayPoints.get(current).getHeight()-wayPoints.get(start).getHeight(),2)) <= 35)
                        {                                
                            tempdistance = (int) Math.sqrt(Math.pow(newx-wayPoints.get(destination).getX(),2)+Math.pow(newy-wayPoints.get(destination).getY(),2)+Math.pow(wayPoints.get(current).getHeight()-wayPoints.get(destination).getHeight(),2));
                            if( tempdistance < distance )
                            {
                                closest = current;
                                distance = tempdistance;
                                pdistance = (int) Math.sqrt(Math.pow(newx-xinput,2)+Math.pow(newy-yinput,2)+Math.pow(wayPoints.get(current).getHeight()-wayPoints.get(start).getHeight(),2));
                            }
                        }
                    }
                  }
                }
                xinput = (int)closest.getX();
                yinput = (int)closest.getY();
                if(closest == start)
                {
                    setStatus("Path ended; no valid moves. Total moves: "+count+". Total strength remaining: "+player.getStrength());
                    //setSimRunning(false); 
                   // setModelValid(false);
                    animatePanel.setComponentState(false, false, false, false, true);
                    return;
                }
                if(player.getStrength() < pdistance)
                {
                    setStatus("Path ended; not enough strength. Total moves: "+count+". Total strength remaining: "+player.getStrength());
                    //setSimRunning(false);
                   // setModelValid(false);
                    animatePanel.setComponentState(false, false, false, false, true);
                    return;
                }
                else{
                    updatePlayer(xinput,yinput,pdistance);
                    wayPoints.get(closest).setVisited();
                    count++;
                }
                if(wayPoints.get(destination).getVisited())
                {
                    setStatus("Destination reached. Total moves: "+count+". Total strength remaining: "+player.getStrength());
                    //setSimRunning(false);
                    //setModelValid(false);
                    animatePanel.setComponentState(false, false, false, false, true);
                    return;
                }                    
                checkStateToWait();
            }
        }
    }
}
