package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/** 
 * Une interface qui represente le relief
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public interface DigitalElevationModel extends AutoCloseable {
    
    /**
     * 
     * @param pointgeo
     * Le PointGeo dont on veut le vecteur normal
     * 
     * @return Vector3
     * Retourne le vecteur normal au PointGeo
     * 
     */

    public abstract Vector3 normalAt(PointGeo pointgeo);
}
