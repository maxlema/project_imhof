package ch.epfl.imhof.geometry;
import java.util.List;

/** 
 * Une OpenPolyline soit une PolyLine ouverte.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class OpenPolyLine extends PolyLine {

    /**
     * 
     * @param points
     * La liste des points de la polyline
     * 
     * @throws IllegalArgumentException
     * Si la polyline est vide
     * 
     */
    
    public OpenPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
    }
    
    @Override
    public boolean isClosed() {
        return false;
    }
}
