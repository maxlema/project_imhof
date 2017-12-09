package ch.epfl.imhof.geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * Un Polygon representé par une ClosedPolyLine et rempli de trous.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class Polygon {
    
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;
    
    /**
     * 
     * @param shell
     * La forme exterieure du polygon
     * 
     * @param holes
     * La liste des trous du polygon
     * 
     */
    
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }
    
    /**
     * 
     * @param shell
     * La forme exterieure du polygon, la liste des trous est vide
     * 
     */
    
    public Polygon(ClosedPolyLine shell) {
        this(shell, Collections.emptyList());
    }
    
    /**
     * 
     * @return ClosedPolyLine
     * Retourne la forme du Polygon
     * 
     */
    
    public ClosedPolyLine shell() {
        return shell;
    }
    
    /**
     * 
     * @return List<ClosedPolyLine>
     * Retourne la liste des trous
     * 
     */
    
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
