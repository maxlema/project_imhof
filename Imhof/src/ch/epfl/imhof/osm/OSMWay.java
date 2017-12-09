package ch.epfl.imhof.osm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.epfl.imhof.Attributes;

/** 
 * Un OSMWay représentant un chemin.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodes;
    
    /**
     * 
     * @param id
     * L'id unique du chemin
     * 
     * @param nodes
     * La liste des nodes formant le chemins
     * 
     * @param attributes
     * Les attributs du chemins
     * 
     * @throws IllegalArgumentException
     * Lance l'exception si la liste de nodes n'en possede pas suffisamment
     * 
     */

    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException {
        super(id, attributes);
        if(nodes.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.nodes = Collections.unmodifiableList(new ArrayList<OSMNode>(nodes));
    }
    
    /**
     * 
     * @return int
     * Retourne le nombre de nodes du chemin
     * 
     */

    public int nodesCount() {
        return nodes.size();
    }
    
    /**
     * 
     * @return List<OSMNode>
     * Retourne la liste des nodes du chemin
     * 
     */

    public List<OSMNode> nodes() {
        return nodes;
    }
    
    /**
     * 
     * @return List<OSMNode>
     * Retourne la liste des nodes du chemin sans la derniere si elle est la meme que la premiere
     * 
     */

    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            return Collections.unmodifiableList(nodes.subList(0,nodes.size()-1));
        }
        return nodes;
    }
    
    /**
     * 
     * @return OSMNode
     * Retourne la premiere node
     * 
     */

    public OSMNode firstNode() {
        return nodes.get(0);
    }
    
    /**
     * 
     * @return OSMNode
     * Retourne la derniere node
     * 
     */

    public OSMNode lastNode() {
        return nodes.get(nodes.size()-1);
    }
    
    /**
     * 
     * @return boolean
     * Indique si le chemin fait une boucle (ClosedPolyLine)
     * 
     */

    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    public static final class Builder extends OSMEntity.Builder {

        /**
         * @param nodes
         * Une liste d'OSMNode.
         * 
         * @param attributes
         * Une liste d'attributs.
         * 
         * Builder d'OSMWay.
         * 
         * @return addNode
         * Rajoute une OSMNode a la liste nodes.
         * 
         * @return isIncomplete
         * Retourne vrai ssi la méthode setIncomplete a été appelée au moins une fois sur le batisseur
         * ou si nodes a une taille < 2.
         * 
         * @return build
         * Retourne un OSMWay avec les paramètres du Builder.
         * 
         */

        private final List<OSMNode> nodes = new ArrayList<OSMNode>();
        
        /**
         * 
         * @param id
         * l'id unique du builder
         * 
         */

        public Builder(long id) {
            super(id);
        }
        
        /**
         * 
         * @param newNode
         * La node a ajouter
         * 
         */

        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }
        
        /**
         * 
         * @return boolean
         * Indique si le chemin est incomplet
         * 
         */
        
        @Override
        public boolean isIncomplete () {
            if (super.isIncomplete() || nodes.size() < 2) {
                return true;
            }
            return false;
        }
        
        /**
         * 
         * @return OSMWay
         * Construit une OSMWay
         * 
         * @throws IllegalStateException
         * Lance l'exception si le builder est incomplet
         * 
         */

        public OSMWay build() throws IllegalStateException {
            if(isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMWay(id, nodes, att.build());
        }
    }

}