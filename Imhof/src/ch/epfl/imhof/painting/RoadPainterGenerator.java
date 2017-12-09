package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public class RoadPainterGenerator {
    
    private static final Predicate<Attributed<?>> pont = Filters.tagged("bridge");
    private static final Predicate<Attributed<?>> tunnel = Filters.tagged("tunnel");
    private static final Predicate<Attributed<?>> plains = (pont.or(tunnel)).negate();
    
    private RoadPainterGenerator() { }
    
    /**
     * 
     * @param roadSpec
     * La liste des parametres de dessins selon le type de route
     * 
     * @return Painter
     * Le Painter de route avec les parametres de roadSpec
     * 
     */
    
    public static Painter painterForRoads(RoadSpec... roadSpec) {
        
        RoadSpec p = roadSpec[0];
        Painter interieurP = p.interPont;
        Painter bordP = p.bordPont;
        Painter interieurR = p.interRoute;
        Painter bordR = p.bordRoute;
        Painter tunnel = p.subRoute;
        
        for (int i=1; i<roadSpec.length; i++) {
            interieurP = interieurP.above(roadSpec[i].interPont);
            bordP = bordP.above(roadSpec[i].bordPont);
            interieurR = interieurR.above(roadSpec[i].interRoute);
            bordR = bordR.above(roadSpec[i].bordRoute);
            tunnel = tunnel.above(roadSpec[i].subRoute);
        }
        return interieurP.above(bordP.above(interieurR.above(bordR.above(tunnel))));
    }
    
    /**
     * 
     * La classe qui definit les parametres de dessins de chaque route
     *
     */
    
    public static class RoadSpec {
        
        private final Painter interPont, bordPont, interRoute, bordRoute, subRoute;
        
        /**
         * 
         * @param predicate
         * Un predicat a suivre
         * 
         * @param wi
         * La largeur de l'interieur des routes
         * 
         * @param ci
         * La couleur de l'interieur des routes
         * 
         * @param wc
         * La largeur qui permet de calculer celle des bordures de route
         * 
         * @param cc
         * La couleur des bordures de route
         * 
         * @throws IllegalArgumentException
         * Lance l'exception quand la largeur est negative
         * 
         */
        
        public RoadSpec(Predicate<Attributed<?>> predicate, float wi, Color ci, float wc, Color cc) throws IllegalArgumentException {
            if(wi<=0 || wc <=0) {
                throw new IllegalArgumentException("negative width");
            }
            interPont = Painter.line(wi, ci, LineCap.ROUND, LineJoin.ROUND, null).when(pont).when(predicate);
            bordPont = Painter.line(wi + 2*wc, cc, LineCap.BUTT, LineJoin.ROUND, null).when(pont).when(predicate);
            interRoute = Painter.line(wi, ci, LineCap.ROUND, LineJoin.ROUND, null).when(plains).when(predicate);  
            bordRoute = Painter.line(wi + 2*wc, cc, LineCap.ROUND, LineJoin.ROUND, null).when(plains).when(predicate);
            subRoute = Painter.line(wi/2, cc, LineCap.BUTT, LineJoin.ROUND, 2*wi, 2*wi).when(tunnel).when(predicate);
        }
    }
    
}
