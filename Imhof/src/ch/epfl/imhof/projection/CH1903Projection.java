package ch.epfl.imhof.projection;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

import java.lang.Math;

/** 
 * La projection selon le modèle CH1903 
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 */

public final class CH1903Projection implements Projection {
    
    @Override
    public Point project(PointGeo point) {
        double lon = Math.toDegrees(point.longitude());
        double lat = Math.toDegrees(point.latitude());

        double long1 = (0.0001)* ((lon *3600)-26782.5);
        double lat1 = (0.0001)*((lat * 3600) -169028.66);
        
        double long2 = Math.pow(long1, 2);
        double lat2 = Math.pow(lat1, 2);

        double x = 600072.37
                + 211455.93 * long1
                - 10938.51 * long1 * lat1
                - 0.36 * long1 * lat2
                - 44.54 * Math.pow(long1, 3);

        double y = 200147.07
                + 308807.95 * lat1
                + 3745.25 * long2
                + 76.63 * lat2
                - 194.56 * long2 * lat1
                + 119.79 * Math.pow(lat1,3);

        return new Point(x,y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 600000)/1000000;
        double y1 = (point.y() - 200000)/1000000;
        
        double x2 = Math.pow(x1, 2);
        double y2 = Math.pow(y1, 2);

        double long0 = 2.6779094 
                + 4.728982 * x1 
                + 0.791484 * x1 * y1 
                + 0.1306 * x1 * y2 
                - 0.0436 * Math.pow(x1,3);

        double lat0 = 16.9023892 
                + 3.238272 * y1 
                - 0.270978 * x2 
                - 0.002528 * y2 
                - 0.0447 * x2 * y1 
                - 0.0140 * Math.pow(y1,3);

        double longu = long0 * 100/36.0;
        double lat = lat0 * 100/36.0;

        // on passe des degrés aux radians
        return new PointGeo(Math.toRadians(longu) , Math.toRadians(lat));

    }
}
