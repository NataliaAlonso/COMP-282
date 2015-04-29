/*
 Random Binary Tree 

 Inserts and displays randomly ordered, sorted, or "balance-sorted" keys 
 into a binary tree using the SimulationFramework.

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

// Node for Binary tree.
class BinaryNode {

    private int key;
    private BinaryNode right, left;
    private Marker mark;  // Simulation FrameworkV2 display

    public BinaryNode(int k) {
        key = k;
        right = left = null;
        mark = null;
    }

    public int compareTo(int k) {
        return key > k ? -1 : 1;
    }

    public int getKey() {
        return key;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public Marker getMarker() {
        return mark;
    }

    public void setLeft(BinaryNode n) {
        left = n;
    }

    public void setRight(BinaryNode n) {
        right = n;
    }

    public void setMarker(Marker m) {
        mark = m;
    }
}

public class RandomBinaryTreeDemo extends SimFrame {
    // eliminate warning @ serialVersionUID

    private static final long serialVersionUID = 42L;
    // constants -- change DIM and SPACE if background image size is not 512 by 512
    private final int DIM = 500;  // pixel dimension of the display  DIM by DIM
    private final int SPACE = 7;  // grid spacing for nodes in display
    private final int KEYS = (int) Math.sqrt(Math.pow(DIM / SPACE, 2) * 2);
    private final float SCALE = 0.71f; //  vertical offset multiplier, aspect ratio   
    private final boolean RANDOMIZE = true;  // set false for linked list
    // variables
    private RandomBinaryTreeDemo app;
    private BinaryNode root;
    private float scale;
    private int[] key;
    private ArrayList<Float> treeLevel;  // used in inorder()
    private ArrayList<Integer> treeVisit;     // used in inorder()

    public static void main(String args[]) {
        RandomBinaryTreeDemo app = new RandomBinaryTreeDemo("Random Binary Tree Demo", "cloud.png");
        app.start();  // start is inherited from SimFrame
    }

    public RandomBinaryTreeDemo(String frameTitle, String imageFile) {
        super(frameTitle, imageFile);
        // create menus
        JMenuBar menuBar = new JMenuBar();
        // set About and Usage menu items and listeners.
        aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        aboutMenu.setToolTipText(
                "Display information about this program");
        // create a menu item and the dialog it invoke 
        usageItem = new JMenuItem("usage");
        authorItem = new JMenuItem("author");
        usageItem.addActionListener( // anonymous inner class event handler
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(RandomBinaryTreeDemo.this,
                        "Creates and displays a random \n"
                        + "binary tree.  Displays tree statistics. \n"
                        + "In class demonstration for Comp 282.",
                        "Usage", // dialog window's title
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        // create a menu item and the dialog it invokes
        authorItem.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(RandomBinaryTreeDemo.this,
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
        treeLevel = new ArrayList<Float>();
        treeVisit = new ArrayList<Integer>();
        // create keys to insert into random binary tree   
        key = new int[KEYS];
        for (int i = 0; i < KEYS; i++) {
            key[i] = i;
        }
    }

    // Recursive inorder tree traversal to 
    // 1.  verify proper key order insertion
    // 2.  collect keys visited in ArrayList treeVisit
    // 3.  collect tree levels in ArrayList treeLevel      
    private void inorder(BinaryNode n, int level) {
        if (n == null) {
            return; // halt
        }      // traverse
        inorder(n.getLeft(), level + 1);
        treeVisit.add(n.getKey());
        treeLevel.add(new Float(level));
        inorder(n.getRight(), level + 1);
    }

    // Show current tree by adding node markers and connectors to display
    private void addNodeMarker(BinaryNode n, int level, Marker parent) {
        int x = SPACE + (int) ((float) n.getKey() / (float) KEYS * DIM);
        int y = (int) (level * (SPACE * scale));
        Marker m = new Marker(x, y, Color.black);
        n.setMarker(m);
        setStatus("Inserted " + n.getKey() + " at (" + x + ", " + y + ")");
        animatePanel.addPermanentDrawable(m);
        if (parent != null) // root has no parent connector
        {
            animatePanel.addPermanentDrawable(new Connector(m, parent, Color.red));
        }
    }

    // Set up next valid simulation model. Randomize keys.
    public void setSimModel() {
        int rKey, tKey;
        setStatus("Initial state of simulation");
        // randomize keys -- set RANDOMIZE = false  for "linked list tree"
        if (RANDOMIZE) {
            scale = 5 * SCALE;
            for (int i = 0; i < KEYS; i++) {
                rKey = (int) (Math.random() * KEYS);
                tKey = key[i];
                key[i] = key[rKey];
                key[rKey] = tKey;
            }
        } else {
            scale = SCALE;  // linked list
        }
        // clear treeVisit and treeLevel collections
        treeVisit.clear();
        treeLevel.clear();
    }

    // Insert randomly ordered keys into binary tree and 
    // update simulation display.
    public synchronized void simulateAlgorithm() {
        // Declare and set any local control variables.
        // Or set up the initial algorithm state:
        // declare and set any algorithm specific variables
        BinaryNode current, newNode;
        int level = 1;  // level of node in tree
        int count = 0;
        root = new BinaryNode(key[0]);
        addNodeMarker(root, level, null);
        while (runnable()) {
            checkStateToWait();
            count++;
            //insert remaining keys
            if (count < KEYS) {
                level = 1;
                current = root;
                newNode = new BinaryNode(key[count]);  // node to insert
                while (current != null) {
                    startTime = getAlgoTime();
                    level++;
                    if (current.compareTo(key[count]) < 0) // current > key[count],  go left
                    {
                        if (current.getLeft() != null) {
                            current = current.getLeft();
                        } else {  // insert as left child
                            current.setLeft(newNode);
                            addNodeMarker(newNode, level, current.getMarker());
                            current = null;
                        }
                    } else // current <= key[count], go right       
                    if (current.getRight() != null) {
                        current = current.getRight();
                    } else {  // insert as right child
                        current.setRight(newNode);
                        addNodeMarker(newNode, level, current.getMarker());
                        current = null;
                    }
                    totalTime += (getAlgoTime() - startTime);
                }
            } else { // count == KEYS
                // all keys are inserted, only reset should be available
                setSimRunning(false);
                setModelValid(false);
                animatePanel.setComponentState(false, false, false, false, true);
                // collect tree statistics
                inorder(root, 1);
                // verify tree and compute statistics
                boolean validTree = true;
                Iterator<Integer> visitIterator = treeVisit.iterator();
                Integer last = -1;
                while (visitIterator.hasNext()) {
                    if (last > visitIterator.next()) {
                        validTree = false;
                    }
                }
                float average = 0.0f;
                int max = 0;
                int currentLevel;
                Iterator<Float> levelIterator = treeLevel.iterator();
                while (levelIterator.hasNext()) {
                    currentLevel = (int) levelIterator.next().floatValue();
                    average += currentLevel;
                    if (max < currentLevel) {
                        max = currentLevel;
                    }
                }
                average = average / treeLevel.size();
                setStatus("Valid tree =  " + validTree
                        + ",  keys = " + KEYS
                        + ",  " + String.format("max level = %3d, average level = %5.2f", max, average)
                        + ",  " + String.format("time = %5.2f msecs", timeInMilliseconds()));
                checkStateToWait();
            }
        }
    }
}
