package ch.epfl.imhof.projection;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * L'interface des projections qui définit les méthodes de projection
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 */

public interface Projection {
    
    /**
     * 
     * @param point
     * Le PointGeo a transformer
     * 
     * @return Point
     * Retourne le Point correspondant
     * 
     */

    Point project(PointGeo point);
    
    /**
     * 
     * @param point
     * Le Point a transformer
     * 
     * @return PointGeo
     * Retourne le PointGeo correspondant
     * 
     */

    PointGeo inverse(Point point); 

}
