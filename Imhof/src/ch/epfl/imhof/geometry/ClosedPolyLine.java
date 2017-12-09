package ch.epfl.imhof.geometry;
import java.util.List;

/** 
 * Une ClosedPolyline soit une PolyLine fermée.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class ClosedPolyLine extends PolyLine {
    
    /**
     * 
     * @param points
     * la liste de points de la polyline
     * 
     * @throws IllegalArgumentException
     * si la liste est vide
     * 
     */

    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
    }
    
    /**
     * 
     * @param polyline
     * une polyline quelconque
     * 
     */

    public ClosedPolyLine(ClosedPolyLine polyline) {
        super(polyline.points());
    }

    @Override
    public boolean isClosed() {
        return true;
    }
    
    /**
     * 
     * @return double
     * Retourne l'aire de la ClosedPolyLine.
     * 
     */

    public double area() {
        double area = 0;
        if(points().size()==1) {
            return area;
        }

        for (int i = 0; i < this.points().size(); i++) {
            double xI = this.points().get(i).x();
            double yJ = this.points().get(Math.floorMod(i+1, this.points().size())).y();
            double yH = this.points().get(Math.floorMod(i-1, this.points().size())).y();
            area += xI * (yJ - yH);
        }
        return Math.abs(area)/2;
    }
    
    /**
     * 
     * @param p
     * Le point a determiner
     * @param p1
     * Le premier pt du segment
     * @param p2
     * Le dernier pt du segment
     * @return boolean
     * Retourne si le Point est à gauche de la droite.
     * 
     */

    private boolean isLeft(Point p, Point p1, Point p2) {
        return (p1.x() - p.x()) * (p2.y() - p.y()) > (p2.x() - p.x()) * (p1.y() - p.y());
    }
    
    /**
     * 
     * @param p
     * Le point a verifier
     * @return boolean
     * Retourne si le point est contenu dans la PolyLine ou non.
     * 
     */

    public boolean containsPoint(Point p) {
        // calcul de l'indice de sommet d'un point
        int indice = 0;
        for(int i=0; i<this.points().size(); i++) {
            if(this.points().get(i).y() <= p.y()) {
                if(this.points().get(Math.floorMod(i+1, this.points().size())).y() > p.y()
                        && isLeft(p, this.points().get(i), this.points().get(Math.floorMod(i+1, this.points().size())))) {
                    
                    indice +=1;
                }
            } else {
                if(this.points().get(Math.floorMod(i+1, this.points().size())).y() <= p.y()
                        && isLeft(p, this.points().get(Math.floorMod(i+1, this.points().size())), this.points().get(i)) ) {
                    
                    indice -=1;
                }
            }
        }
        return indice!=0;
    }
}
