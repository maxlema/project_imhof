package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/** 
 * Classe qui permet de dessiner le relief d'une carte
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class ReliefShader {
    
    private final Projection project;
    private final DigitalElevationModel dem;
    private final Vector3 light;
    
    /**
     * 
     * @param proj
     * La projection utilisee pour passer en coordonnees du plan
     * 
     * @param dem
     * Le relief à dessiner
     * 
     * @param light
     * Le vecteur de lumiere
     * 
     */
    
    public ReliefShader(Projection proj, DigitalElevationModel dem , Vector3 light) {
        this.project = proj;
        this.dem = dem;
        this.light = light.normalized();
    }
    /**
     * 
     * @param bl
     * Le Point en bas a gauche de la carte
     * 
     * @param tr
     * Le Point en haut a droite de la carte
     * 
     * @param largeur
     * La largeur de la carte
     * 
     * @param hauteur
     * La hauteur de la carte
     * 
     * @param rayon
     * Le rayon de floutage
     * 
     * @return BufferedImage
     * L'image du relief floute
     * 
     * @throws IllegalArgumentException
     * Si la hauteur ou la largeur ou le rayon a une valeur negative
     * 
     */
    
    public BufferedImage shadedRelief(Point bl, Point tr, int largeur, int hauteur, double rayon) throws IllegalArgumentException {
        if(largeur<=0 || hauteur<=0 || rayon<0) {
            throw new IllegalArgumentException("Negative input");
        }
        if (rayon == 0) {
            Function<Point, Point> fct = Point.alignedCoordinateChange(new Point(0,hauteur), bl, new Point(largeur, 0), tr);
            return drawRelief(largeur, hauteur, fct);
        } else {
        
        int zoneTampon = (int) Math.ceil(rayon);
        
        Function<Point, Point> fct = Point.alignedCoordinateChange(new Point(zoneTampon,hauteur+zoneTampon), bl, new Point(largeur+zoneTampon, zoneTampon), tr);
        BufferedImage image = drawRelief(largeur + 2*zoneTampon, hauteur + 2*zoneTampon, fct);
        
        float[] kern = kernelG(rayon);
        Kernel kern1 = new Kernel(kern.length, 1, kern);
        Kernel kern2 = new Kernel(1, kern.length, kern);
        
        image = blur(image, kern1);
        image = blur(image, kern2);
        
        return image.getSubimage(zoneTampon, zoneTampon, largeur, hauteur);
        }
    }
    
    /**
     * 
     * @param largeur
     * La largeur de l'image
     * 
     * @param hauteur
     * La hauteur de l'image
     * 
     * @param fct
     * La fonction du changement de coordonnees
     * 
     * @return BufferedImage
     * Retourne l'image du relief non floute
     * 
     */
    
    private BufferedImage drawRelief(int largeur, int hauteur, Function<Point, Point> fct) {
        
        double cosinus;
        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        
        for(int i=0; i<largeur; i++) {
            for(int j=0; j<hauteur; j++) {
                PointGeo point = project.inverse(fct.apply(new Point(i,j)));
                Vector3 normal = dem.normalAt(point).normalized();
                cosinus = light.scalarProduct(normal);
                double rg = (0.5)*(cosinus+1);
                double b = (0.5)*(0.7*cosinus + 1);
                
                Color color = Color.rgb(rg, rg, b);
                
                image.setRGB(i, j, color.convert().getRGB());
            }
        }
        return image;
    }
    
    /**
     * 
     * @param rayon
     * Le rayon de floutage qui sert à calculer le kernel
     * 
     * @return float[]
     * Le tableau de float qui represente la colonne ou ligne centrale du kernel
     * 
     */
    
    private float[] kernelG(double rayon) {
        double theta = rayon/3d;
        int r = (int) Math.ceil(rayon);
        int n = 2*r+ 1;
        float somme = 0f;
        float temp = 0f;
        
        float[] matrice = new float[n];
        
        for (int i = 0; i < r+1; i++) {
            temp = (float) Math.exp(-Math.pow((Math.abs(r-i)), 2)/(2*Math.pow(theta, 2)));
            somme += 2*temp;
            matrice[i] = temp;
        }
        somme -= temp;
        for (int i = 0; i < r+1; i++) {
            matrice[i] = matrice[i]/somme;
            matrice[n-i-1] = matrice[i];
        }
        
        return matrice;
    }
    
    /**
     * 
     * @param img
     * La BufferedImage qui va etre floutee
     * 
     * @param kernel
     * Le kernel qui sert au floutage
     * 
     * @return BufferedImage
     * Retourne l'image floutee
     * 
     */
    
    private BufferedImage blur(BufferedImage img, Kernel kernel) {
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolve.filter(img, null);
    } 

}