package ch.epfl.imhof.painting;

/** 
 * Classe qui définit une couleur.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class Color {
    
    private final double r, g, b;
    
    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color WHITE = new Color(1, 1, 1);
    public final static Color BLACK = new Color(0, 0, 0);
    
    /**
     * 
     * @param r
     * Valeur attribuée à la couleur rouge.
     * 
     * @param g
     * Valeur attribuée à la couleur verte.
     * 
     * @param b
     * Valeur attribuée à la couleur bleue.
     * 
     * Construit une couleur à l'aide des valeurs pour les troics couleurs primaires.
     * 
     */
    
    private Color(double r, double g, double b) {
        testValue(r,g,b);
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    /**
     * 
     * @param val1
     * Teste si le double de la couleur rouge est valide.
     * 
     * @param val2
     * Teste si le double de la couleur verte est valide.
     * 
     * @param val3
     * Teste si le double de la couleur bleue est valide.
     * 
     * @return boolean
     * Retourne si les doubles des couleurs sont valides.
     * 
     */
    
    private static boolean testValue(double val1, double val2, double val3) {
        if (val1<0 || val1>1) throw new IllegalArgumentException("red invalid");
        if (val2<0 || val2>1) throw new IllegalArgumentException("green invalid");
        if (val3<0 || val3>1) throw new IllegalArgumentException("blue invalid");
        return true;
    }
    
    /**
     * 
     * @param val
     * Le double définissant l'intensité de gris.
     * 
     * @return Color
     * Construit la couleur grise.
     * 
     */
    
    public static Color gray(double val) {
        if (val<0 || val>1) throw new IllegalArgumentException("value invalid");
        return new Color(val, val, val);
    }
    
   /** 
    * 
    * @param vR
    * Le double de la couleur rouge.
    * 
    * @param vG
    * Le double de la couleur verte.
    *  
    * @param vB
    * Le double de la couleur bleue. 
    * 
    * @return Color
    * Construit la couleur avec les valeur r, g et b entrées.
    * 
    */
    
    public static Color rgb(double vR, double vG, double vB) {
        testValue(vR, vG, vB);
        return new Color(vR, vG, vB);
    }
    
    /**
     * 
     * @param val
     * La valeur de la couleur rgb avec un int.
     * 
     * @return Color
     * Construit la couleur avec les composantes individuelles « empaquetées » dans un entier de type int.
     * 
     */
    
    public static Color rgb(int val) {
        return new Color(((val >> 16) & 0xFF) / 255d,
                         ((val >>  8) & 0xFF) / 255d,
                         ((val >>  0) & 0xFF) / 255d);
    }
    
    /**
     * 
     * @return double
     * Retourne le double associé à la couleur rouge.
     * 
     */
    
    public double getRed() { return r;}
    
    /**
     * 
     * @return double
     * Retourne le double associé à la couleur verte.
     * 
     */
    
    public double getGreen() { return g;}
    
    /**
     * 
     * @return double
     * Retourne le double associé à la couleur bleue.
     * 
     */
    
    public double getBlue() { return b;}
    
    /**
     * 
     * @param c1
     * La premiere Color à multiplier.
     * 
     * @param c2
     * La seconde Color à multiplier.
     * 
     * @return Color
     * Retourne la couleur résultant de la multiplication de c1 et c2.
     * 
     */
    
    public static Color multiply(Color c1, Color c2) {
        return new Color(c1.getRed()*c2.getRed(),
                         c1.getGreen()*c2.getGreen(),
                         c1.getBlue()*c2.getBlue());
    }
    
    /**
     * 
     * @return java.awt.Color
     * Convertit la couleur de la classe en java.awt.Color.
     * 
     */
    
    public java.awt.Color convert() {
        return new java.awt.Color((float)r, (float)g, (float)b);
    }
}
