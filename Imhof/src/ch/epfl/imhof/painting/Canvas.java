package ch.epfl.imhof.painting;

/** 
 * Classe qui définit une couleur.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    
    public abstract void drawPolyLine(PolyLine polyL, LineStyle style);
    
    /**
     * 
     * @param polyG
     * Le Polygon qui sera dessiné.
     * 
     * @param color
     * La couleur du Polygon.
     * 
     */
    
    public abstract void drawPolygon(Polygon polyG, Color color);
}
