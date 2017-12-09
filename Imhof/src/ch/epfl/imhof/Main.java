package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * 
 * Classe principale du projet, construit la carte a partir des fichiers donnes
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class Main {
    
    /**
     * 
     * @param args
     * Les arguments contenant le nom des fichiers,
     * la position des points haut-droit et bas-gauche,
     * la resolution et le nom du fichier a creer
     * 
     * @throws Exception
     * 
     */

    public static void main(String[] args) throws Exception {
        
         if (args.length != 8) {
            throw new IllegalArgumentException("not enough Arguments");
        }
        
        String OSMFile = args[0];
        String HGTFile = args[1];
        double longitudeBL =  Math.toRadians(Double.parseDouble(args[2]));
        double latitudeBL = Math.toRadians(Double.parseDouble(args[3]));
        double longitudeTR = Math.toRadians(Double.parseDouble(args[4]));
        double latitudeTR = Math.toRadians(Double.parseDouble(args[5]));
        int resolution = Integer.parseInt(args[6]);
        String fileNameOut = args[7];
        
        int nbPixL = (int) (resolution/0.0254);
        
        Vector3 light = new Vector3(-1,1,1);
        Projection proj = new CH1903Projection();
        
        PointGeo pBL = new PointGeo(longitudeBL, latitudeBL);
        PointGeo pTR = new PointGeo(longitudeTR, latitudeTR);
        Point bl = proj.project(pBL);
        Point tr = proj.project(pTR);
        
        int hauteur = (int) Math.round(nbPixL*0.00004*(latitudeTR - latitudeBL)*Earth.RADIUS);
        int largeur = (int) Math.round(((tr.x()-bl.x())/(tr.y()-bl.y()))*hauteur);
        
        double rFlou = 0.0017*nbPixL;

        try { HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File("data/"+HGTFile));
        ReliefShader sombre = new ReliefShader(proj, dem, light);
        BufferedImage img = sombre.shadedRelief(bl, tr, largeur, hauteur, rFlou);

        Painter peintre_suisse = SwissPainter.painter();

        OSMMap osmmap = OSMMapReader.readOSMFile(OSMFile, true);
        OSMToGeoTransformer trans = new OSMToGeoTransformer(proj);
        Map map = trans.transform(osmmap);
        
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, largeur, hauteur, resolution, Color.WHITE);

        peintre_suisse.drawMap(map, canvas);
        
        BufferedImage img2 = canvas.image();
        BufferedImage imgFinal = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        
        for(int i = 0; i < hauteur; i++) {
            for(int j = 0; j < largeur; j++) {
                Color couleur1 = Color.rgb(img.getRGB(j, i));
                Color couleur2 = Color.rgb(img2.getRGB(j, i));
                Color couleurFinal = Color.multiply(couleur1,couleur2);
                imgFinal.setRGB(j, i, couleurFinal.convert().getRGB());
            }
        }
        
        dem.close();
        ImageIO.write(imgFinal, "png", new File(fileNameOut));
        } catch (IOException e) { e.printStackTrace();
        } catch (IllegalArgumentException e) { e.printStackTrace();
        }  
    }
}