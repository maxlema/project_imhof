package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * La carte.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class OSMMap {

    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;
    
    /**
     * 
     * @param ways
     * La liste des chemins, des ways.
     * 
     * @param relations
     * La liste des relations.
     * 
     */

    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<OSMWay>(ways));
        this.relations = Collections.unmodifiableList(new ArrayList<OSMRelation>(relations));
    }
    
    /**
     * 
     * @return List<OSMWay>
     * Retourne la liste des chemins.
     * 
     */

    public List<OSMWay> ways() {
        return ways;
    }
    
    /**
     * 
     * @return List<OSMRelation>
     * Retourne la liste des relations.
     * 
     */

    public List<OSMRelation> relations() {
        return relations;
    }
    
    public static final class Builder {

        private final Map<Long, OSMWay> ways = new HashMap<>();
        private final Map<Long, OSMNode> nodes = new HashMap<>();
        private final Map<Long, OSMRelation> relations = new HashMap<>();

        /**
         * 
         * @param newNode
         * La node a ajouter.
         * 
         */
        
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }
        
        /**
         * 
         * @param id
         * L'id de la node a trouver
         * 
         * @return OSMNode
         * Retourne la node qui est associee a l'id
         * 
         */

        public OSMNode nodeForId(long id) {
                return nodes.get(id);
        }
        
        /**
         * 
         * @param newWay
         * Chemin a ajouter.
         * 
         */

        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }
        
        /**
         * 
         * @param id
         * L'iddu chemin a trouver
         * 
         * @return OSMWay
         * Retourne le chemin correspondant a l'id
         * 
         */

        public OSMWay wayForId(long id) {
                return ways.get(id);
        }
        
        /**
         * 
         * @param newRelation
         * La relation a ajouter
         * 
         */

        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }
        
        /**
         * 
         * @param id
         * L'id de la relation a trouver
         * 
         * @return OSMRelation
         * Retourne la relation correspondante a l'id
         * 
         */

        public OSMRelation relationForId(long id) {
                return relations.get(id);
        }
        
        /**
         * 
         * @return OSMMap
         * Construit la map
         * 
         */

        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }
    }

}
