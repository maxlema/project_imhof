package ch.epfl.imhof.painting;

import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/** 
 * Interface qui définit un Painter qui servira a dessiner la carte.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public interface Painter {
    
    public abstract Canvas drawMap(Map map, Canvas canvas);
    
    /**
     * 
     * @param color
     * La couleur du Painter des Polygon
     * 
     * @return Painter
     * Le Painter des Polygon
     * 
     */

    public static Painter polygon(Color color) {
        return (x,y) -> {
            List<Attributed<Polygon>> polyG = x.polygons();
            for(Attributed<Polygon> polyg : polyG) {
                y.drawPolygon(polyg.value(), color);
            }
            return y;
        };
    }
    
    /**
     * 
     * @param lineWidth
     * La largeur de la ligne
     * 
     * @param lineColor
     * La couleur de la ligne
     * 
     * @param lineCap
     * Le type de terminaison de la ligne
     * 
     * @param lineJoin
     * Le type de jointure de la ligne
     * 
     * @param dashingPattern
     * L'alternance des sections opaques et transparentes de la ligne.
     * 
     * @return Painter
     * Le Painter des lignes
     * 
     */
 
    public static Painter line(float lineWidth,Color lineColor, LineCap lineCap,LineJoin lineJoin, float... dashingPattern) {
        return (x,y) -> {
            LineStyle style = new LineStyle(lineWidth, lineColor, dashingPattern, lineCap, lineJoin);
            List<Attributed<PolyLine>> polyL = x.polyLines();
            for(Attributed<PolyLine> polyl : polyL) {
                y.drawPolyLine(polyl.value(), style);
            }
            return y;
        };
    }
    
    /**
     * 
     * @param lineWidth
     * La largeur de la ligne
     * 
     * @param lineColor
     * La couleur de la ligne
     * 
     * @return Painter
     * Le Painter de ligne avec des parametres par defaut
     * 
     */
    
    public static Painter line(float lineWidth, Color lineColor) {
        return (x,y) -> {
            LineStyle style = new LineStyle(lineWidth, lineColor);
            List<Attributed<PolyLine>> polyL = x.polyLines();
            for(Attributed<PolyLine> polyl : polyL) {
                y.drawPolyLine(polyl.value(), style);
            }
            return y;
        };
    }
    
    /**
     * 
     * @param lineWidth
     * La largeur de la ligne
     * 
     * @param lineColor
     * La couleur de la ligne
     * 
     * @param dashingPattern
     * L'alternance des sections opaques et transparentes de la ligne.
     * 
     * @param lineCap
     * Le type de terminaison de la ligne
     * 
     * @param lineJoin
     * Le type de jointure de la ligne
     * 
     * @return Painter
     * Le Painter des lignes exterieures, utile pour les bords de route
     * 
     */
    
    public static Painter outline(float lineWidth,Color lineColor, float[] dashingPattern, LineCap lineCap,LineJoin lineJoin) {
        return (x,y) -> {
            LineStyle style = new LineStyle(lineWidth, lineColor, dashingPattern, lineCap, lineJoin);
            List<Attributed<Polygon>> polyG = x.polygons();
            for(Attributed<Polygon> polyg : polyG) {
                y.drawPolyLine(polyg.value().shell(), style);
                for (ClosedPolyLine hole : polyg.value().holes()) {
                    y.drawPolyLine(hole, style);
                }
            }
            return y;
        };
    }
    
    /**
     * 
    * @param lineWidth
     * La largeur de la ligne
     * 
     * @param lineColor
     * La couleur de la ligne
     * 
     * @return Painter
     * Le Painter des lignes exterieures avec des parametres par defaut, utile pour les bords de route
     * 
     */
    
    public static Painter outline(float lineWidth, Color lineColor) {
        return (x,y) -> {
            LineStyle style = new LineStyle(lineWidth, lineColor);
            List<Attributed<Polygon>> polyG = x.polygons();
            for(Attributed<Polygon> polyg : polyG) {
                y.drawPolyLine(polyg.value().shell(), style);
                for (ClosedPolyLine hole : polyg.value().holes()) {
                    y.drawPolyLine(hole, style);
                }
            }
            return y;
        };
    }
    
    /**
     * 
     * @param predicate
     * Le predicat a respecter
     * 
     * @return Painter
     * Retourne un Painter qui suit le predicat
     * 
     */

    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (x,y) -> {
            Map.Builder map = new Map.Builder();

            for(Attributed<PolyLine> polyL : x.polyLines()) {
                if(predicate.test(polyL)) {
                    map.addPolyLine(polyL);
                }  
            }
            for(Attributed<Polygon> polyG : x.polygons()) {
                if(predicate.test(polyG)) {
                    map.addPolygon(polyG);
                }
            }
            return this.drawMap(map.build(), y);
        };
    }
    
    /**
     * 
     * @param painter
     * Le Painter de dessous
     * 
     * @return Painter
     * Un Painter qui est la concatenation de deux Painter l'un sur l'autre
     * 
     */

    public default Painter above(Painter painter) {
        return (x, y) -> this.drawMap(x, painter.drawMap(x, y));
    }
    
    /**
     * 
     * @return Painter
     * Retourne un Painter qui dessine les couches de -5 a 5
     * 
     */
    
    public default Painter layered() {
        return (x,y) -> {
            Painter paint = this.when(Filters.onLayer(-5));
            for(int i=-4; i<6; i++) {
                paint = this.when(Filters.onLayer(i)).above(paint);
            }
            return this.drawMap(x, y);
        };
    }
}
