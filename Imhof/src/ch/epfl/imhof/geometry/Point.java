package ch.epfl.imhof.geometry;

import java.util.function.Function;

/** 
 * Un point à la surface d'une carte, en coordonnées
 * cartésiennes.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class Point {

    private final double x;
    private final double y;
    
    /**
     * 
     * @param x
     * Coord x en metre
     * 
     * @param y
     * Coord y en metre
     * 
     */
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 
     * @return double
     * Retourne la coord x
     * 
     */
    
    public double x() {
        return x;
    }
    
    /**
     * 
     * @return double
     * Retourne la coord y
     * 
     */

    public double y() {
        return y;
    }
    
    /**
     * 
     * @param r1
     * Pt 1 dans le systeme 1
     * 
     * @param b1
     * Pt 1 dans le systeme 2
     * 
     * @param r2
     * Pt 2 dans le systeme 1
     * 
     * @param b2
     * Pt 2 dans le systeme 2
     * 
     * @return Function<Point, Point>
     * Retourne la fct du changement de coord
     * 
     */
    
    public static Function<Point, Point> alignedCoordinateChange(Point r1, Point b1, Point r2, Point b2) {
        if(r1.x() == b1.x() || r1.y() == b1.y() || r2.x() == b2.x() || r2.y() == b2.y()) {
            throw new IllegalArgumentException();
        }
        double cX = (b2.x() - b1.x())/(r2.x() - r1.x());
        double cY = (b2.y() - b1.y())/(r2.y() - r1.y());
        double iX = b1.x() -cX * r1.x();
        double iY = b1.y() -cY * r1.y();
        return nP -> {
            return new Point(cX*nP.x() + iX, cY*nP.y() + iY);
        };
    }
}
