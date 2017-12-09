package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.projection.Projection;

/** 
 * Classe qui transforme des données OSM en carte.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class OSMToGeoTransformer {

    private final Projection projection;

    private static final Set<String> ATTRIBUTS = new HashSet<String>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic",
            "landuse", "leisure", "man_made", "military", "natural",
            "office", "place", "power", "public_transport", "shop",
            "sport", "tourism", "water", "waterway", "wetland"));

    private static final Set<String> LISTPOLYLINE = new HashSet<String>(Arrays.asList("bridge", "highway", "layer", "man_made", "railway",
            "tunnel", "waterway"));

    private static final Set<String> LISTPOLYGON = new HashSet<String>(Arrays.asList("building", "landuse", "layer", "leisure", "natural",
            "waterway"));
    
    /**
     * 
     * @param projection
     * Le type de projection utilisee pour passer de PointGeo au plan
     * 
     */

    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }
    
    /**
     * 
     * @param map
     * L'OSMMap a transformer en Map
     * 
     * @return Map
     * Retourne map transformee
     * 
     */

    public Map transform(OSMMap map) {
        Map.Builder carte = new Map.Builder();
        List<Attributed<Polygon>> listPolyg = new ArrayList<>();


        for (OSMWay way : map.ways()) {
                if (isSurface(way)) {
                    Attributes attrPolyg = way.attributes().keepOnlyKeys(
                            LISTPOLYGON);
                    if (!attrPolyg.isEmpty()) {
                        carte.addPolygon(new Attributed<Polygon>(new Polygon(
                                endCircle(way)), attrPolyg));
                    }

                }
                else {
                    Attributes attrPolyL = way.attributes().keepOnlyKeys(LISTPOLYLINE);
                    if(!attrPolyL.isEmpty()) {
                        PolyLine.Builder polyLine = new PolyLine.Builder();

                        for (OSMNode o : way.nodes()) {
                            polyLine.addPoint(projection.project(o.position()));
                        }
                        if (way.isClosed()) {
                            carte.addPolyLine(new Attributed<PolyLine>(polyLine.buildClosed(), attrPolyL));
                        } else {
                            carte.addPolyLine(new Attributed<PolyLine>(polyLine.buildOpen(), attrPolyL));
                        }
                    }
                }
        }

        for (OSMRelation rel : map.relations()) {
            if(rel.attributeValue("type") != null && rel.attributeValue("type").equals("multipolygon")) {
                Attributes attrPolyg2 = rel.attributes().keepOnlyKeys(LISTPOLYGON);

                if(!attrPolyg2.isEmpty()) {
                    listPolyg = assemblePolygon(rel, attrPolyg2);

                    for (Attributed<Polygon> poly : listPolyg) {
                        carte.addPolygon(poly);
                    }
                }
            }
        }
        return carte.build();
    }
    
    /**
     * 
     * @param relation
     * La relation donc on doit calculer les rings
     * 
     * @param role
     * Le role de la relation
     * 
     * @return List<ClosedPolyLine>
     * Calcule l'ensemble des anneaux
     * 
     */

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {

        Set<OSMNode> visites = new HashSet<>();
        List<OSMWay> listWay = new ArrayList<>();
        List<ClosedPolyLine> listPolyC = new ArrayList<>();
        Graph.Builder<OSMNode> graph = new Graph.Builder<>();

        for (Member mem : relation.members()) {
            if(mem.type()==Type.WAY && role.equals(mem.role())) {
                listWay.add((OSMWay) mem.member());
            }
        }

        for(OSMWay way : listWay) {
            for(OSMNode node : way.nonRepeatingNodes()) {
                graph.addNode(node);
            }
        }

        for(OSMWay way : listWay) {
            for (int i = 0; i < way.nodesCount()-1; i++) {
                graph.addEdge(way.nodes().get(i), way.nodes().get(i+1));
            }
        }

        Graph<OSMNode> finalGraph = graph.build();
        
        for(OSMNode i : finalGraph.nodes()) {
            if(finalGraph.neighborsOf(i).size() != 2) {
                return listPolyC;
            }
        }

        Set<OSMNode> nodes = finalGraph.nodes();
        OSMNode actuel;

        while ((actuel = randomNode(nodes, visites)) != null) {
            PolyLine.Builder poly = new PolyLine.Builder();

            do {
                poly.addPoint(projection.project(actuel.position()));
                actuel = randomNode(finalGraph.neighborsOf(actuel), visites);
            } while (actuel != null);
            
            listPolyC.add(poly.buildClosed());
        }
        visites.clear();
        
        return listPolyC;
    }
    
    /**
     * 
     * @param nodes
     * Le set des nodes a visiter
     * 
     * @param visites
     * Le set des nodes visites
     * 
     * @return OSMNode
     * Retourne les node non visites
     * 
     */

    private OSMNode randomNode(Set<OSMNode> nodes, Set<OSMNode> visites) {
        for(OSMNode node : nodes) {
            
            if(!visites.contains(node)) {
                visites.add(node);
                return node;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param relation
     * La relation donc on doit calculer les polygons
     * 
     * @param attributes
     * Les attributs a attacher aux polygons
     * 
     * @return List<Attributed<Polygon>>
     * Retourne la liste des polygons attaches a la relation
     * 
     */

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {

        Attributes polyAttributs = attributes;
        List<Attributed<Polygon>> polygons = new ArrayList<>();
        List<ClosedPolyLine> holes = new ArrayList<>();
        List<ClosedPolyLine> inner = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outer = ringsForRole(relation, "outer");
        
        sortPoly(inner);
        sortPoly(outer);

        for(ClosedPolyLine out : outer) {
            for(ClosedPolyLine inn : inner) {
                
                if (containsAllPoint(out, inn)) {
                    holes.add(inn);
                }
            }
            
            Polygon polygon = new Polygon(out, holes);
            Attributed<Polygon> attPolygon = new Attributed<>(polygon, polyAttributs);
            
            polygons.add(attPolygon);
            inner.removeAll(holes);
            holes.clear();
        }
        
        return polygons;
    }
    
    /**
     * 
     * @param l
     * La liste a trier
     * 
     */

    public static void sortPoly(List<ClosedPolyLine> l) {
        Collections.sort(l, (s1, s2) -> ((Double)s1.area()).compareTo((Double)s2.area()));
    }
    
    /**
     * 
     * @param way
     * L'OSMWay a verifier
     * 
     * @return boolean
     * Indique si la OSMWay est une surface
     * 
     */

    private boolean isSurface(OSMWay way) {
        if(way.isClosed()) {
            for(String attributs : ATTRIBUTS) {
                if(way.hasAttribute(attributs)) {
                    return true;
                }
            }
        }
        return(way.hasAttribute("area") &&
                (way.attributeValue("area").equals("1")
                        || way.attributeValue("area").equals("yes")
                        || way.attributeValue("area").equals("true")));
    }
    
    /**
     * 
     * @param way
     * L'OSMWay a utiliser
     * 
     * @return ClosedPolyLine
     * Construit une ClosedPolyLine avec l'OSMWay donnee
     * 
     */

    private ClosedPolyLine endCircle(OSMWay way) {
        
        PolyLine.Builder poly = new PolyLine.Builder();
        
        for (OSMNode o : way.nonRepeatingNodes()) {
            poly.addPoint(projection.project(o.position()));
        }
        
        return poly.buildClosed();
    }
    
    /**
     * 
     * @param outer
     * Le cercle exterieur contenant inner
     * 
     * @param inner
     * Le cercle interieur a outer
     * 
     * @return boolean
     * Verifie si outer contient bien tout inner
     * 
     */

    private boolean containsAllPoint(ClosedPolyLine outer, ClosedPolyLine inner) {
        for (Point in : inner.points()) {
            if(!outer.containsPoint(in)) {
                return false;
            }
        }
        return true;
    }
}