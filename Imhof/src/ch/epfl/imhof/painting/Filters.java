package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/** 
 * Classe qui définit une couleur.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class Filters {
    
    private Filters() {}

    /**
     * 
     * @param attName
     * Nom de l'attribut dont on veut un prédicat.
     * 
     * @return Predicate<Attributed<?>>
     * Retourne un prédicat indiquant si un Attributed auquel on l'applique possède l'attribut attName.
     * 
     */
    
    public static Predicate<Attributed<?>> tagged(String attName) {
        return (a -> a.attributes().contains(attName));
    }

    /**
     * 
     * @param attName
     * Nom de l'attribut dont on veut un prédicat.
     * 
     * @param attValue
     * Nom des String dont on veut vérifier qu'au moins l'une d'entre elles est valeur de attName.
     * 
     * @return Predicate<Attributed<?>>
     * Retourne un prédicat qui n'est vrai que si la valeur attribuée à laquelle on l'applique
     * possède un attribut portant le nom donné et si de plus la valeur associée à cet attribut fait partie de celles données.
     * 
     */
    
    public static Predicate<Attributed<?>> tagged(String attName, String... attValue) {
        return (a -> {
            boolean bool = false;
            if (a.hasAttribute(attName)) {
                for (String string : attValue) {
                    if (string.equals(a.attributeValue(attName))) {
                        bool = true;
                    }
                }
            }
            return a.hasAttribute(attName) && bool;
        });
    }

    /**
     * 
     * @param entier
     * Int qui indique de quelle couche on parle.
     * 
     * @return Predicate<Attributed<?>>
     * Retourne un predicat qui n'est vrai que si l'objet sur lequel on l'applique se trouve a la couche donnee.
     */
    
    public static Predicate<Attributed<?>> onLayer(int entier) {
        return (a -> {
            int layer = a.attributeValue("layer", 0);
            if (layer < -5 && layer > 5) {
                layer = 0;
            }
            return entier == layer;
        });
    }

}
