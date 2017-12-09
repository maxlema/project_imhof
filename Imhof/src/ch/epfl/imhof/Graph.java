package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 
 * Classe qui cree un graphe permettant de savoir quelles OSMEntity sont voisines entre elles.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class Graph<N> {

    private final Map<N, Set<N>> neighbors;

    /**
     * 
     * @param neighbors
     * Map qui attribue a une OSMEntity ses voisins.
     * 
     * Construit un graphe avec une map d'OSMEntity et de leurs voisins.
     * 
     */
    
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> map = new HashMap<>();
        for(Map.Entry<N, Set<N>> o: neighbors.entrySet()) {
            Set<N> set = Collections.unmodifiableSet(new HashSet<>(o.getValue()));
            map.put(o.getKey(), set);
        }
        this.neighbors = Collections.unmodifiableMap(map);
    }

    /**
     * 
     * @return Set<N>
     * Retourne un Set compose des clefs de la map.
     * 
     */
    
    public Set<N> nodes() {
        return neighbors.keySet();
    }

    /**
     * 
     * @param node
     * Une OSMENtity qui est entrée et dont on cherche les voisins.
     * 
     * @return Set<N>
     * Retourne le Set des voisins associés a une OSMEntity.
     * 
     * @throws IllegalArgumentException
     * Si la OSMENtity entrée n'existe pas.
     * 
     */
    
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!neighbors.containsKey(node)) {
            throw new IllegalArgumentException();
        }
        return neighbors.get(node);
    }

    public static final class Builder<N> {
        
        private Map<N, Set<N>> neighbors = new HashMap<>();

        /**
         * 
         * @param n
         * OSMEntity qui est ajoutée à la Map des voisins.
         * 
         */
        
        public void addNode(N n) {
            if(!neighbors.containsKey(n)) {
                neighbors.put(n, new HashSet<N>());
            }
        }
        
        /**
         * 
         * @param n1
         * Premiere OSMEntity à laquelle on veut ajouter un lien.
         * 
         * @param n2
         * Deuxieme OSMEntity à laquelle on veut ajouter un lien.
         * Chaque OSMENtity est ajoutée dans le Set des voisins de l'autre.
         * 
         * @throws IllegalArgumentException
         * Si une des deux OSMEntity n'existe pas.
         * 
         */

        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if(neighbors.containsKey(n1) && neighbors.containsKey(n2)) {
                neighbors.get(n1).add(n2);
                neighbors.get(n2).add(n1);
            } else {
                throw new IllegalArgumentException();
            }
        }

        /**
         * 
         * @return Graph<N>
         * Construit un nouveau graph a partir du Builder.
         * 
         */
        
        public Graph<N> build() {
            return new Graph<N>(neighbors);
        }
    }
}
