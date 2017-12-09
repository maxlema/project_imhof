package ch.epfl.imhof.painting;
import java.awt.BasicStroke;
/** 
 * Classe qui définit une couleur.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class LineStyle {
    
    private final float lineWidth;
    private final Color lineColor;
    private final float[] dashingPattern;
    private final LineCap lineCap;
    private final LineJoin lineJoin;
    
    /**
     * 
     * Les différents types de terminaison de ligne.
     *
     */
    
    public enum LineCap {
        
        BUTT(BasicStroke.CAP_BUTT), ROUND(BasicStroke.JOIN_ROUND), SQUARE(BasicStroke.CAP_SQUARE);
        
        private int lineCap;
        
        /**
         * 
         * @param lineCap
         * Le type de terminaison de ligne
         * 
         */
        
        private LineCap(int lineCap) {
            this.lineCap = lineCap;
        }
        
        /**
         * 
         * @return int
         * Retourne le type de la terminaison
         * 
         */

        public int getBLineCap() {
            return lineCap;
        }
    }
    
    /**
     * 
     *Les differents types de jointure des segments.
     *
     */
    
    public enum LineJoin { 
        BEVEL(BasicStroke.JOIN_BEVEL), MITER(BasicStroke.JOIN_MITER), ROUND(BasicStroke.JOIN_ROUND);

        private int lineJoin;
        
        /**
         * 
         * @param lineJoin
         * Le type de jointure des lignes
         * 
         */

        private LineJoin(int lineJoin) {
            this.lineJoin = lineJoin;
        }
        
        /**
         * 
         * @return int
         * Retourne le type de jointure
         * 
         */

        public int getBLineJoin() {
            return lineJoin;
        }
    }
    
    /**
     * 
     * @param lindeWidth
     * Epaisseur de la ligne.
     * 
     * @param lineColor
     * Couleur de la ligne.
     * 
     * @param dashingPattern
     * L'alternance des sections opaques et transparentes de la ligne.
     * 
     * @param lineCap
     * La terminaison de la ligne.
     * 
     * @param lineJoin
     * La jointure des segments.
     * 
     */

    public LineStyle(float lineWidth,
            Color lineColor,
            float[] dashingPattern,
            LineCap lineCap,
            LineJoin lineJoin) {
        if(lineWidth<0) throw new IllegalArgumentException("width invalid");

        if (dashingPattern != null) {
            for(float dp : dashingPattern) {
                if(dp<=0) throw new IllegalArgumentException("dashingPattern invalid");  
            }
            this.dashingPattern = dashingPattern.clone();
        } else {
            this.dashingPattern = null;
        }
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
    }
    
    /**
     * 
     * @param linewidth
     * Epaisseur de la ligne.
     * 
     * @param lineColor
     * Couleur de la ligne.
     * 
     */
    
    public LineStyle(float linewidth, Color lineColor) {
        this(linewidth, lineColor, null, LineCap.BUTT, LineJoin.MITER);
    }
    
    /**
     * 
     * @return float
     * Retourne la largeur de la ligne
     * 
     */
    
    public float getLineWidth() {
        return lineWidth;
    }
    
    /**
     * 
     * @return Color
     * Retourne la couleur de la ligne
     * 
     */
    
    public Color getLineColor() {
        return lineColor;
    }
    
    /**
     * 
     * @return float[]
     * Retourne l'alternance des sections opaques et transparentes de la ligne. 
     * 
     */
    
    public float[] getDashingPattern() {
        return dashingPattern;
    }
    
    /**
     * 
     * @return LineCap
     * Retourne le type de terminaison de la ligne
     * 
     */
    
    public LineCap getLineCap() {
        return lineCap;
    }
    
    /**
     * 
     * @return LineJoin
     * Retourne le type jointure de la ligne
     * 
     */
    
    public LineJoin getLineJoin() {
        return lineJoin;
    }
    
    /**
     * 
     * @param newWidth
     * La nouvelle largeur de la ligne
     * 
     * @return LineStyle
     * Retourne une LineStyle avec la nouvelle largeur
     * 
     */
    
    public LineStyle withWidth(float newWidth) {
        return new LineStyle(newWidth, getLineColor(), getDashingPattern(), getLineCap(), getLineJoin());
    }
    
    /**
     * 
     * @param newColor
     * La nouvelle couleur de la ligne
     * 
     * @return LineStyle
     * Retourne une LineStyle avec la nouvelle couleur
     * 
     */
    
    public LineStyle withColor(Color newColor) {
        return new LineStyle(getLineWidth(), newColor, getDashingPattern(), getLineCap(), getLineJoin());
    }
    
    /**
     * 
     * @param newDP
     * Le nouveau DashingPattern de la ligne
     * 
     * @return LineStyle
     * Retourne une LineStyle avec le nouveau DP
     * 
     */
    
    public LineStyle withDashingPattern(float[] newDP) {
        return new LineStyle(getLineWidth(), getLineColor(), newDP, getLineCap(), getLineJoin());
    }
    
    /**
     * 
     * @param newLC
     * Le nouveau type de terminaison de la ligne
     * 
     * @return LineStyle
     * Retourne une LineStyle avec le nouveau type de terminaison
     * 
     */
    
    public LineStyle withLineCap(LineCap newLC) {
        return new LineStyle(getLineWidth(), getLineColor(), getDashingPattern(), newLC, getLineJoin());
    }
    
    /**
     * 
     * @param newLJ
     * Le nouveau type de jointure de la ligne
     * 
     * @return LineStyle
     * Retourne une LineStyle avec le nouveau type de jointure
     * 
     */
    
    public LineStyle withLineJoin(LineJoin newLJ) {
        return new LineStyle(getLineWidth(), getLineColor(), getDashingPattern(), getLineCap(), newLJ);
    }

}
