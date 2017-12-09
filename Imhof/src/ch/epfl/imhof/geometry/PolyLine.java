    package ch.epfl.imhof.geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * Classe abstraite qui définit ce qu'est une PolyLine.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 */

public abstract class PolyLine {

    private final List<Point> points;
    
    /**
     * 
     * @param points
     * La liste de points de la polyline
     * 
     * @throws IllegalArgumentException
     * Si la liste est vide
     * 
     */

    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if(points==null||points.size()==0) {
            throw new IllegalArgumentException();
        }
        
        this.points = Collections.unmodifiableList(new ArrayList<Point>(points));
    }
    
    /**
     * 
     * @return
     * Indique si la polyline est fermee
     * 
     */

    public abstract boolean isClosed();
    
    /**
     * 
     * @return
     * Retourne la liste de points de la polyline
     * 
     */

    public List<Point> points() {
        return points;
    }

    /**
     * 
     * @return
     * Retourne le premier point
     * 
     */
    
    public Point firstPoint() {
        return new Point(points.get(0).x(), points.get(0).y());
    }

    public static final class Builder {

        private final List<Point> points = new ArrayList<Point>();

        /**
         * 
         * @param newPoint
         * Un points a ajouter a la liste
         * 
         */
        
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * 
         * @return
         * cree une OpenPolyline
         * 
         */
        
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }
        
        /**
         * 
         * @return
         * cree une ClosedPolyline
         * 
         */

        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}