/*
 Connector.java

 Mike Barnes
 8/12/2013
 */
package SimulationFramework;
// CLASSPATH = ... /282projects/SimulationFrameworkV3
// PATH = ... /282projects/SimulationFrameworkV3/SimulationFramework

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Connector is used to draw lines between points on the simulation's canvas.
 * Often Connectors are used to join 2 Markers. In algorithm simulation
 * Connectors can be either temporary or permanent. Permanent connectors are
 * only cleared when the simulation's "Reset" option is selected. Temporary
 * connectors are cleared when the simulation's "Clear" option is selected.
 * <p>
 * Connector UML class diagram
 * <p>
 * <Img align left src = "../../UML/Connector.png">
 *
 *
 * @since 8/12/2013
 * @version 3.0
 * @author G. M. Barnes
 */
public class Connector implements Drawable {

    /**
     * connector's beginning position
     */
    private Point pointA;
    /**
     * connector's beginning position
     */
    private Point pointB;
    /**
     * connector's color
     */
    private Color color;

    /**
     * Make a Connector using screen positions
     *
     * @param x connector's beginning horizontal screen position
     * @param y connector's beginning vertical screen position
     * @param x2 connector's end horizontal screen position
     * @param y2 connector's end vertical screen position
     * @param colorValue connector's color
     */
    public Connector(int x, int y, int x2, int y2, Color colorValue) {
        pointA = new Point(x, y);
        pointB = new Point(x2, y2);
        color = colorValue;
    }

    /**
     * Make a Connector using 2 markers
     *
     * @param ptA connector begins at this Point
     * @param ptB connector ends at this Point
     * @param colorValue connector's color
     */
    public Connector(Point ptA, Point ptB, Color colorValue) {
        pointA = ptA;
        pointB = ptB;
        color = colorValue;
    }

    /**
     * Make a Connector using 2 markers
     *
     * @param m1 connector begins at this Marker
     * @param m2 connector ends at this Marker
     * @param colorValue connector's color
     */
    public Connector(Marker m1, Marker m2, Color colorValue) {
        pointA = m1.getPoint();
        pointB = m2.getPoint();
        color = colorValue;
    }

    /**
     * Draw the connector on the simulation's canvas
     */
    public synchronized void draw(Graphics g) {
        Color tColor;
        tColor = g.getColor();  // save exisiting color
        g.setColor(color);
        g.drawLine((int) pointA.getX(), (int) pointA.getY(),
                (int) pointB.getX(), (int) pointB.getY());
        g.setColor(tColor);   // restore previous color
    }
}