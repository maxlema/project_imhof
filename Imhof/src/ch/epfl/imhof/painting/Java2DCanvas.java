package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/** 
 * Classe qui définit les méthodes de dessin.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public class Java2DCanvas implements Canvas {
    
    private final Point bL;
    private final Point tR;
    private final int width;
    private final int height;
    private final int resolution;
    private final Color fondColor;
    private final Function<Point, Point> transform;
    private final Graphics2D ctx;
    private final BufferedImage image;

    /**
     * 
     * @param bL
     * Le point en bas à gauche de la carte.
     * 
     * @param tR
     * Le point en haut à droite de la carte.
     * 
     * @param width
     * La largeur de l'image
     * 
     * @param height
     * La hauteur de l'image
     * 
     * @param resolution
     * La resolution de l'image
     * 
     * @param fondColor
     * La couleur de fond de l'image
     * 
     */
    
    public Java2DCanvas(Point bL, Point tR, int width, int height, int resolution, Color fondColor) {
        if(width < 0 || height < 0 || resolution < 0) throw new IllegalArgumentException();
        this.bL = bL;
        this.tR = tR;
        this.width = width;
        this.height = height;
        this.resolution = resolution;
        this.fondColor = fondColor;   
        
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setColor(fondColor.convert());
        ctx.fillRect(0, 0, width, height);
        ctx.scale(resolution/72d, resolution/72d);
        this.transform = Point.alignedCoordinateChange(bL, new Point(0,height/(resolution/72d)), tR, new Point(width/(resolution/72d), 0));
    }
    
    @Override
    public void drawPolyLine(PolyLine polyL, LineStyle style) {
        Path2D.Double chemin = chemin(polyL);
        BasicStroke b = new BasicStroke(style.getLineWidth(), style.getLineCap().getBLineCap(), style.getLineJoin().getBLineJoin(), 10.0f, style.getDashingPattern(), 0f);
        ctx.setColor(style.getLineColor().convert());
        ctx.setStroke(b);
        ctx.draw(chemin);
    }

    @Override
    public void drawPolygon(Polygon polyG, Color color) {
        PolyLine shell = polyG.shell();
        Path2D.Double coque = chemin(shell);
        Area aire = new Area(coque);
        for (ClosedPolyLine  Locher: polyG.holes()) {
            aire.subtract(new Area(chemin(Locher)));
        }
        ctx.setColor(color.convert());
        ctx.fill(aire);
    }
    
    /**
     * 
     * @return BufferedImage
     * Retourne l'image
     * 
     */

    public BufferedImage image() {
        return image;
    }
    
    /**
     * 
     * @param polyL
     * La polyline qui sert a creer le chemin
     * 
     * @return Path2D.Double
     * Le chemin a dessiner
     * 
     */


    private Path2D.Double chemin(PolyLine polyL) {
        //sert à créer un chemin avec une polyLine
        Path2D.Double chemin = new Path2D.Double();
        Point projection = transform.apply(new Point(polyL.firstPoint().x(), polyL.firstPoint().y()));

        chemin.moveTo(projection.x(), projection.y());
        for (Point p: polyL.points()) {
            if (p.x() != polyL.firstPoint().x() && p.y() != polyL.firstPoint().y()) {
                projection = transform.apply(new Point(p.x(), p.y()));
                chemin.lineTo(projection.x(), projection.y());
            }
        }

        if (polyL.isClosed()) {
            chemin.closePath();
        }
        return chemin;
    }
}
