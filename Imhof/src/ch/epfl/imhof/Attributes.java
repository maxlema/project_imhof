package ch.epfl.imhof;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** 
 * Classe qui crée une liste d'attributs
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class Attributes {
    
    private final Map<String, String> attributes;

    /**
     * 
     * @param attributes
     * Liste des attributs.
     * 
     * Construit une liste d'attributs.
     * 
     */
    
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }
    
    /**
     * 
     * @return boolean
     * Retourne si la liste d'attributs est vide.
     * 
     */
    
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * 
     * @param key
     * La clé dont on veut vérifier qu'elle est contenue dans attributes.
     * 
     * @return boolean
     * Retourne si la liste d'attributs contient la clé entrée en paramètre ou non.
     * 
     */
    
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * 
     * @param key
     * La clé dont on veut la valeur.
     * 
     * @return String
     * Retourne la valeur associée à la clé recherchée ou null si cette valeur n'existe pas.
     * 
     */
    
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * 
     * @param key
     * La clé dont on veut la valeur.
     * 
     * @param defaultValue
     * La valeur String par défaut qui sera retournée si la clé n'existe pas.
     * 
     * @return String
     * Retourne la valeur associée à la clé recherchée ou defaultValue si cette valeur n'existe pas.
     * 
     */
    
    public String get(String key, String defaultValue) {
        if (attributes.get(key) == null) {
            return defaultValue;
        }
        return attributes.get(key);  
    }

    /**
     * 
     * @param key
     * La clé dont on veut la valeur.
     * 
     * @param defaultValue
     * La valeur int par défaut qui sera retournée si la clé n'existe pas.
     * 
     * @return int
     * Retourne la valeur associée à la clé recherchée ou defaultValue si cette valeur n'existe pas.
     * 
     */
    
    public int get(String key, int defaultValue) {
        if (attributes.get(key) == null) {
            return defaultValue;
        }

        try {
            Integer.parseInt(attributes.get(key));
        } catch(NumberFormatException e) {
            return defaultValue;
        }
        return Integer.parseInt(attributes.get(key));
    }

    /**
     * 
     * @param keysToKeep
     * Le Set<String> des clés qu'on va garder.
     * 
     * @return Attributes
     * Retourne un Attributes avec en paramètre les clés gardées.
     * 
     */
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Map<String, String> attr = new HashMap<String, String>();
        for(String o: keysToKeep) {
            if(attributes.containsKey(o)) {
                attr.put(o, attributes.get(o));
            }
        }
        return new Attributes(attr);
    }

    public static final class Builder {

        private Map<String, String> map = new HashMap<String, String>();

        /**
         * 
         * @param key
         * La clé en String.
         * 
         * @param value
         * La valeur en String.
         * 
         */
        
        public void put(String key, String value) {
            map.put(key, value);
        }

        /**
         * 
         * @return Attributes
         * Retourne un nouvel Attributes avec la Map construite.
         * 
         */
        
        public Attributes build() {
            return new Attributes(map);
        }
    }
}
