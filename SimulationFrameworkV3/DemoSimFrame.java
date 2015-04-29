/*
 DemoSimFrame.java

 Mike Barnes
 8/12/2013
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import SimulationFramework.*;
// CLASSPATH = ... /282projects/SimulationFrameworkV3
// PATH = ... /282projects/SimulationFrameworkV3/SimulationFramework

/**
 * DemoSimFrame is the simulation's main class (simulation app) that is a
 * subclass of SimFrame.
 * <p> * Compile BayesBot.java before compiling DemoSimFrame.java
 *
 * <p>
 *
 * 282 Simulation Framework applications must have a subclass of SimFrame that
 * also has a main method. The simulation app can make the appropriate author
 * and usage "help" dialogs, override setSimModel() and simulateAlgorithm()
 * abstract methods inherited from SimFrame. They should also add any specific
 * model semantics and actions.
 *
 * <p>
 *
 * The simulated algorithm is defined in simulateAlgorithm().
 *
 * <p>
 * DemoSimFrame UML class diagram
 * <p>
 * <Img align left src="../UML/DemoSimFrame.png">
 *
 * @since 8/15/2013
 * @version 3.0
 * @author G. M. Barnes
 */
public class DemoSimFrame extends SimFrame {
    // eliminate warning @ serialVersionUID

    private static final long serialVersionUID = 42L;
    // constants for this demonstration
    private final int min = 20;
    private final int max = 450;
    // GUI components for application's menu
    /**
     * the simulation application
     */
    private DemoSimFrame app;
    // application variables;
    /**
     * the actors "bots" of the simulation
     */
    private ArrayList<Bot> bot;
    /**
     * a source for pseudo-random values
     */
    private Random aRandom;
    /**
     * count of moves to make before stopping
     */
    private int moves;

    public static void main(String args[]) {
        DemoSimFrame app = new DemoSimFrame("DemoSimFrame", "cloud.png");
        app.start();  // start is inherited from SimFrame
    }

    /**
     * Make the application: create the MenuBar, "help" dialogs,
     */
    public DemoSimFrame(String frameTitle, String imageFile) {
        super(frameTitle, imageFile);
        // create menus
        JMenuBar menuBar = new JMenuBar();
        // set About and Usage menu items and listeners.
        aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        aboutMenu.setToolTipText(
                "Display informatiion about this program");
        // create a menu item and the dialog it invoke 
        usageItem = new JMenuItem("usage");
        authorItem = new JMenuItem("author");
        usageItem.addActionListener( // anonymous inner class event handler
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(DemoSimFrame.this,
                        "Simulation creates 3 bots that move in a random walk. \n"
                        + "Each \"turn\" a bot is randomly selected to move. \n"
                        + "Speed of the simulation \"turns\" is set by the slider. \n"
                        + "User prompts are displayed in the status line (bottom).\n"
                        + "1.  Use mouse click to set initial positions for 3 bots\n"
                        + "2.  \"Start\" button will begin simulation. \n"
                        + "3.  \"Stop\" button will halt (pause) simulation. \n"
                        + "4.  \"Clear\" button will erase current simulation turns \n"
                        + "    and restore initial simulation bot positions.\n"
                        + "5.  \"Reset\" button will erase all simulation values \n"
                        + "    and enable use to start a new simulation.\n",
                        "Usage",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(DemoSimFrame.this,
                        "G. Michael Barnes\n"
                        + "Computer Science Dept\n"
                        + "California State University Northridge\n\n"
                        + "www.csun.edu/~renzo\n"
                        + "818.677.2299",
                        "Author",
                        JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon("renzo.png"));
            }
        });
        // add menu items to menu 
        aboutMenu.add(usageItem);   // add menu item to menu
        aboutMenu.add(authorItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        validate();  // resize layout managers
        // construct the application specific variables
        aRandom = new Random();
    }

    /**
     * Create BayesBots, add them to application's ArrayList and add them to
     * AnimatePanel's ArrayList.
     *
     * @param name of the bot
     * @param x initial horizontal position of the bot
     * @param y initial vertical position of the bot
     * @param color of the bot
     */
    private void makeBayesBot(String name, int x, int y, Color color) {
        Bot b = new BayesBot(name, x, y, color);
        bot.add(b);
        animatePanel.addBot(b);
    }

    /**
     * Set up the actors (Bots), wayPoints (Markers), and possible traveral
     * paths (Connectors) for the simulation.
     */
    public void setSimModel() {
        setStatus("Initial state of simulation");
        // add 81 permanent markers
        for (int x = min; x <= max; x += min) {
            for (int y = min; y <= max; y += min) {
                animatePanel.addPermanentDrawable(new Marker(x, y, Color.black));
            }
        }
        // add 4 permanent connectors
        animatePanel.addPermanentDrawable(new Connector(min, min, min, max, Color.black));
        animatePanel.addPermanentDrawable(new Connector(min, max, max, max, Color.black));
        animatePanel.addPermanentDrawable(new Connector(max, max, max, min, Color.black));
        animatePanel.addPermanentDrawable(new Connector(max, min, min, min, Color.black));
        animatePanel.repaint();
        //  create Bot collection
        bot = new ArrayList<Bot>();
        // create 3 BayesBots 
        setStatus("Enter first bot position");
        waitForMousePosition();
        makeBayesBot("Red", (int) mousePosition.getX(),
                (int) mousePosition.getY(), Color.red);
        setStatus("Enter second bot position");
        waitForMousePosition();
        makeBayesBot("Green", (int) mousePosition.getX(),
                (int) mousePosition.getY(), Color.green);
        setStatus("Enter third bot position");
        waitForMousePosition();
        makeBayesBot("Blue", (int) mousePosition.getX(),
                (int) mousePosition.getY(), Color.blue);
        moves = 0;  // initialize count of moves to make
    }

    /**
     * The "algorithm" used is very simple. Move a randomly selected
     * BayesianBot, 1/5 of time draw 2 temporary markers and their temporary
     * connector
     */
    public synchronized void simulateAlgorithm() {
        while (runnable()) {
            Bot aBot = bot.get(aRandom.nextInt(bot.size()));
            aBot.move();
            // approximately every 5 turns add 2 temporary markers 
            // and connect them with a temporary connector.
            Marker m1, m2;

            if (aRandom.nextInt(5) == 0) {
                m1 = new Marker(aRandom.nextInt(max - min) + min,
                        aRandom.nextInt(max - min) + min, Color.orange);
                m2 = new Marker(aRandom.nextInt(max - min) + min,
                        aRandom.nextInt(max - min) + min, Color.orange);
                animatePanel.addTemporaryDrawable(m1);
                animatePanel.addTemporaryDrawable(m2);
                animatePanel.addTemporaryDrawable(new Connector(m1, m2, Color.orange));
            }
            moves++;
            // test if algorithm is finished
            if (moves == max) {
                // only "Reset" button should be active now
                animatePanel.setComponentState(false, false, false, false, true);
                setStatus("moves == max,  algorithm is done");
                return;
            }
            // the following statement must be at end of any
            // overridden abstact simulateAlgorithm() method
            checkStateToWait();
        }
    }
}
