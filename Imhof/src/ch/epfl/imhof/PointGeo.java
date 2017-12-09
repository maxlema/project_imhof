package ch.epfl.imhof;

/** 
 * 
 * Un point à la surface de la Terre, en coordonnées
 * sphériques.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class PointGeo {
    
    private final double longitude;
    private final double latitude;
    
    /**
     * 
     * @param longitude
     * longitude du point en radians 
     * 
     * @param latitude
     * latitude du point en radians 
     * 
     * @throws IllegalArgumentException
     * Lance l'exception si la longitude sort de l'intervalle [-Pi, Pi] ou
     * si la latitude sort de l'intervalle [-Pi/2, Pi/2]
     * 
     * Construit un point avec la latitude et la longitude
     * données.
     * 
     */
    
    public PointGeo(double longitude, double latitude) throws IllegalArgumentException {
        if(longitude<-Math.PI || longitude>Math.PI || latitude<-(Math.PI/2) || latitude>(Math.PI/2)) {
            throw new IllegalArgumentException();
        }
        
        this.longitude = longitude;
        this.latitude = latitude;  
    }

    /**
     * 
     * @return double
     * Retourne la longitude en radians
     * 
     */
    
    public double longitude() {
        return longitude;
    }
    
    /**
     * 
     * @return double
     * Retourne la latitude en radians
     * 
     */
    
    public double latitude() {
        return latitude;
    }
}
