package ch.epfl.imhof.osm;
import ch.epfl.imhof.Attributes;

/**
 * Un OSMEntity est une classe abstraite qui sert à implémenter les autres OSM.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;
    
    /**
     * 
     * @param id
     * Un long qui représente un identifiant unique de l'entité.
     * 
     * @param attributes
     * La liste des attributs de l'entité.
     * 
     */

    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }
    
    /**
     * 
     * @return long
     * Retourne l'id.
     * 
     */

    public long id() {
        return id;
    }
    
    /**
     * 
     * @return Attributes
     * Retourne les attributs.
     * 
     */

    public Attributes attributes() {
        return attributes;
    }
    
    /**
     * 
     * @param key
     * La cle de l'attribut
     * @return boolean
     * Retourne si la liste d'attributs contient la clé donnée.
     * 
     */

    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }
    
    /**
     * 
     * @param key
     * La cle de l'attribut
     * @return String
     * Retourne la valeur attachée à l'attribut.
     * 
     */

    public String attributeValue(String key) {
        return attributes.get(key);
    }

    public static abstract class Builder {

        /**
         * @param id
         * L'identifiant unique de l'entité.
         * 
         * @param att
         * La liste d'attribut associée au Builder.
         * 
         * @param incomplete
         * Permet de savoir si l'entité est complète.
         * 
         * Le Builder d'OSMEntity
         * 
         * @return setAttributes
         * Ajoute une valeur associée à une clef à la liste d'attributs.
         * 
         * @return setIncomplete
         * Assigne la valeur true à incomplete.
         * 
         * @return isIncomplete
         * Retourne incomplete.
         * 
         */

        protected final long id;
        protected final Attributes.Builder att = new Attributes.Builder();
        private boolean incomplete = false;

        /**
         * 
         * @param id
         * L'identifiant unique de l'entité.
         * 
         */
        
        public Builder(long id) {
            this.id = id;
        }
        
        /**
         * 
         * @param key
         * La cle de l'attribut
         * 
         * @param value
         * L'attribut
         * 
         */

        public void setAttribute(String key, String value) {
            att.put(key, value);
        }

        public void setIncomplete() {
            incomplete = true;
        }
        
        /**
         * 
         * @return boolean
         * Set le builder selon incomplete
         * 
         */

        public boolean isIncomplete() {
            return incomplete;
        }
    }
}