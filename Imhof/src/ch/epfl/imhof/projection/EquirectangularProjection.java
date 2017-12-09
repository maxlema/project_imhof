package ch.epfl.imhof.projection;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/** 
 * Transformation des coordonnées selon la formule équirectangulaire
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 */

public final class EquirectangularProjection implements Projection {

    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
