package ch.epfl.imhof;

/** 
 * Classe qui fait le lien entre les objets et leurs attributs.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class Attributed<T> {

    private final T value;
    private final Attributes attributes;

    /**
     * 
     * @param value
     * Nom de l'objet.
     * 
     * @param attributes
     * Les attributs de l'objet.
     * 
     * Construit un lien entre un objet et ses paramétres.
     * 
     */
    
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * 
     * @return T
     * Retourne l'objet.
     * 
     */
    
    public T value() {
        return value;
    }

    /**
     * 
     * @return Attributes
     * Retourne les attributs de l'objet.
     * 
     */
    
    public Attributes attributes() {
        return attributes;
    }

    /**
     * 
     * @param attributeName
     * Le nom de l'attribut qu'on cherche dans attributes.
     * 
     * @return boolean
     * Retourne si l'attribut est dans la liste des attributs.
     * 
     */
    
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * 
     * @param attributeName
     * Le nom de l'attribut dont on veut la valeur.
     * 
     * @return String
     * Retourne la valeur liée à l'attribut ou null s'il n'y en a pas.
     * 
     */
    
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }
    
    /**
     * 
     * @param attributeName
     * Le nom de l'attribut dont on veut la valeur.
     * 
     * @param defaultValue
     * La valeur par défaut qui sera retournée si l'attribut n'a pas de valeur.
     * 
     * @return String
     * Retourne la valeur liée à l'attribut ou defaultValue s'il n'y en a pas.
     * 
     */

    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * 
     * @param attributeName
     * Le nom de l'attribut dont on veut la valeur.
     * 
     * @param defaultValue
     * La valeur par défaut qui sera retournée si l'attribut n'a pas de valeur.
     * 
     * @return int
     * Retourne la valeur liée à l'attribut ou defaultValue s'il n'y en a pas.
     * 
     */
    
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}
