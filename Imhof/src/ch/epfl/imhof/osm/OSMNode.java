package ch.epfl.imhof.osm;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/** 
 *
 * Un point dans le systeme OSM.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class OSMNode extends OSMEntity {

    private final PointGeo position;

    /**
     * 
     * @param id
     * L'id unique de la node
     * 
     * @param position
     * La position en coord PointGeo
     * 
     * @param attributes
     * Les attributs de la node
     * 
     */
    
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }
    
    /**
     * 
     * @return PointGeo
     * Retourne la position
     * 
     */

    public PointGeo position() {
        return position;
    }

    public static final class Builder extends OSMEntity.Builder {

        private final PointGeo position;

        /**
         * 
         * @param id
         * L'id unique de la node
         * 
         * @param position
         * La position en coord PointGeo
         * 
         */
        
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }
        
        /**
         * 
         * @return OSMNode
         * Construit l'OSMNode
         * 
         * @throws IllegalStateException
         * Lance l'exception si l'entite est incomplete
         * 
         */

        public OSMNode build() throws IllegalStateException {
            if(isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMNode(id, position,att.build());
        }
    }
}