package ch.epfl.imhof;

/**
 * 
 * Un Vector3 est un vecteur constitué de 3 composantes, x, y et z.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class Vector3 {
    
    private final double x, y, z;
    
    /**
     * 
     * Construit un Vector3 avec x, y et z.
     * 
     * @param x
     * La coordonnée x du vecteur.
     * 
     * @param y
     * La coordonnée y du vecteur.
     * 
     * @param z
     * La coordonnée z du vecteur.
     * 
     */
    
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * 
     * @return double
     * Retourne la norme du vecteur.
     * 
     */
    
    public double norm() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    /**
     * 
     * @return normalized
     * Retourne le Vector3 normalisé.
     * 
     */
    
    public Vector3 normalized() {
        return new Vector3(this.x/norm(), this.y/norm(), this.z/norm());
    }
    
    /**
     * 
     * @return double
     * Retourne le produit scalaire de this et du Vector3 passé en argument.
     * 
     */
    
    public double scalarProduct(Vector3 vect) {
        return this.x*vect.x() + this.y*vect.y() + this.z*vect.z();
    }
    
    /**
     * 
     * @return double
     * Retourne la coordonnée x du Vector3.
     * 
     */
    
    private double x() {
        return x;
    }
    
    /**
     * 
     * @return double
     * Retourne la coordonnée y du Vector3.
     * 
     */
    
    private double y() {
        return y;
    }
    
    /**
     * 
     * @return double
     * Retourne la coordonnée z du Vector3.
     * 
     */
    
    private double z() {
        return z;
    }

}
