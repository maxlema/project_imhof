package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/** 
 * Une classe qui represente le relief
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class HGTDigitalElevationModel implements DigitalElevationModel  {

    private final File file;
    private ShortBuffer buffer;
    private final double delta;
    private final double s;
    private final PointGeo base;
    private final double longLigne;
    private final FileInputStream stream;
    private final long length;
    
    /**
     * 
     * @param file
     * Le fichier a lire
     * 
     * @throws IllegalArgumentException
     * Si le nom du fichier ne repond aux conventions
     * 
     * @throws FileNotFoundException
     * Si le fichier n'est pas trouve
     * 
     * @throws IOException
     *
     */
    
    public HGTDigitalElevationModel(File file) throws IllegalArgumentException, FileNotFoundException, IOException {
        String fileName = file.getName();
        
        if(fileName.length() != 11 || Math.pow(Math.sqrt(file.length()/2d), 2)*2 != file.length()
                || (fileName.charAt(0) != 'N' && fileName.charAt(0) != 'S')
                || (!Character.isDigit(fileName.charAt(1)) || !Character.isDigit(fileName.charAt(2)))
                || (fileName.charAt(3) == 'E' && fileName.charAt(3) == 'W')
                || (!Character.isDigit(fileName.charAt(4)) || !Character.isDigit(fileName.charAt(5)) || !Character.isDigit(fileName.charAt(6)))
                || !fileName.substring(7).equals(".hgt")) {
            
            throw new IllegalArgumentException();
        }
      
        this.file = file;
        length = file.length();
        base = getInitial();
        stream = new FileInputStream(file);
        
        buffer = stream.getChannel()
                .map(MapMode.READ_ONLY, 0, length)
                .asShortBuffer();

        longLigne = Math.sqrt(length/2d)-1;
        delta = Math.toRadians(1.0/longLigne);
        s = Earth.RADIUS*delta;

    }

    @Override
    public void close() throws Exception {
        buffer = null;
        stream.close();
    }

    @Override
    public Vector3 normalAt(PointGeo pointgeo) {
        
        isIn(pointgeo);
        
        double i = Math.floor((pointgeo.latitude() - base.latitude())/delta);
        double j = Math.floor((pointgeo.longitude() - base.longitude())/delta);
        
        int z1 = getPos(i, j);
        int z2 = getPos(i+1, j);
        int z3 = getPos(i+1, j+1);
        int z4 = getPos(i, j+1);
        
        double x = (0.5)*s*(z1-z4+z2-z3);
        double y = (0.5)*s*(z1+z4-z2-z3);
        double z = s*s;

        return new Vector3(x,y,z);
    }
    
    /**
     * 
     * @param i
     * Position x dans le fichier
     * 
     * @param j
     * Position y dans le fichier
     * 
     * @return int
     * L'altitude du point a cette position
     * 
     */
    
    private int getPos(double i, double j) {
        return buffer.get((int) (length/2d - (i+1)*(longLigne + 1) + j));
    }
    
    /**
     * 
     * @return PointGeo
     * Retourne le premier point (en haut a gauche)
     * 
     */
    
    private PointGeo getInitial() {
        String start = file.getName();
        double latBase = 1;
        double longBase = 1;
        
        
        switch (start.charAt(0)) {
        case 'N':
            break;

        case 'S' :
            latBase = latBase*-1;
            break;
        }
        
       latBase = Math.toRadians(latBase*Double.parseDouble(start.substring(1, 3)));
        
       switch (start.charAt(3)) {
       case 'E':
           break;

       case 'W' :
           latBase = latBase*-1;
           break;
       }
        
       longBase = Math.toRadians(longBase*Double.parseDouble(start.substring(4, 7)));
       
       return new PointGeo(longBase, latBase);
        
    }
    
    /**
     * 
     * @param pt
     * Le PointGeo dont on veut verifier s'il est dans le fichier
     * 
     */
    
    private void isIn(PointGeo pt) {
        if (pt.latitude() < base.latitude() || pt.longitude() < base.longitude() || pt.latitude() > longLigne || pt.longitude() > longLigne) {
            throw new IllegalArgumentException("Point out of HGT bounds " + pt.latitude() + " ," + pt.longitude() + " ," + base.latitude() + " ," + base.longitude());
        }
    }

}
