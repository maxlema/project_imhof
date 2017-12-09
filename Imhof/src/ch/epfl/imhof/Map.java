package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/** 
 * Classe qui représente une carte projetée, composée d'entités géométriques attribuées.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class Map { 

    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;
    
    /**
     * 
     * @param polyLines
     * Une liste de PolyLine Attributed qui sert a représenter la carte.
     * 
     * @param polygons
     * Une liste de Polygon Attributed qui sert a représenter la carte.
     * 
     * Construit une Map avec les listes polyLines et polygons.
     * 
     */

    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList( new ArrayList<Attributed<PolyLine>>(polyLines));
        this.polygons = Collections.unmodifiableList( new ArrayList<Attributed<Polygon>>(polygons));
    }
    
    /** 
     * 
     * @return List<Attributed<PolyLine>>
     * Retourne la liste de PolyLine Attributed qui sert a représenter la carte.
     * 
     **/

    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }
    
    /**
     * 
     * @return List<Attributed<Polygon>
     * Retourne la liste de Polygon Attributed qui sert a représenter la carte.
     * 
     */

    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }

    public static final class Builder {
        
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<Attributed<PolyLine>>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<Attributed<Polygon>>();

        /**
         * 
         * @param newPolyLine
         * L'Attributed PolyLine qui sera ajouté à la liste polyLines.
         * 
         */

        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }
        
        /**
         * 
         * @param newPolygon
         * L'Attributed Polygon qui sera ajouté à la liste polygons.
         * 
         */

        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * 
         * @return Map
         * Retourne une nouvelle Map avec les listes polyLines et polygons.
         * 
         */
        
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
