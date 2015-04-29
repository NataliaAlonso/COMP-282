/*
 EmptySimFrame.java

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
public class EmptySimFrame extends SimFrame {
    // eliminate warning @ serialVersionUID

    private static final long serialVersionUID = 42L;
    // GUI components for application's menu
    /**
     * the simulation application
     */
    private EmptySimFrame app;
    // application variables;
    /**
     * the actors "bots" of the simulation
     */
    private ArrayList<Bot> bot;

    public static void main(String args[]) {
        EmptySimFrame app = new EmptySimFrame("EmptySimFrame", "terrain282.png");
        app.start();  // start is inherited from SimFrame
    }

    /**
     * Make the application: create the MenuBar, "help" dialogs,
     */
    public EmptySimFrame(String frameTitle, String imageFile) {
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
                JOptionPane.showMessageDialog(EmptySimFrame.this,
                        "An informational message string \n"
                        + "about how to use the program \n"
                        + "that can span several lines \n"
                        + "if needed.",
                        "Usage", // dialog window's title
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(EmptySimFrame.this,
                        "Sponge Bob \n"
                        + "sponge.bob.42@my.csun.edu \n"
                        + "Comp 282",
                        "author", // dialog window's title
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

    /**
     * Set up the actors (Bots), wayPoints (Markers), and possible traveral
     * paths (Connectors) for the simulation.
     */
    public void setSimModel() {
        // set any initial visual Markers or Connectors
        // get any required user mouse clicks for positional information.
        // initialize any algorithm halting conditions (ie, number of steps/moves).
        setStatus("Initial state of simulation");
    }

    /**
     * The "algorithm" used is very simple. Move a randomly selected
     * BayesianBot, 1/5 of time draw 2 temporary markers and their temporary
     * connector
     */
    public synchronized void simulateAlgorithm() {
        // Declare and set any local control variables.
        // Or set up the initial algorithm state:
        // declare and set any algorithm specific varibles
        while (runnable()) {
            // put your algorithm code here.
            // ...
            // The following statement must be at end of any
            // overridden abstact simulateAlgorithm() method
            checkStateToWait();
        }
    }
}
