package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

/** 
 * Classe qui permet de lire les fichier xml et qui retourne une OSMMap creee avec les donnees lues.
 * 
 * @author Maxime Lemarignier (247926)
 * @autor Julien Linder (245786)
 * 
 */

public final class OSMMapReader {

    /**
     * Constructeur prive non instanciable, lis les fichier xml.
     * 
     * @return readOSMFile
     * Lis les fichiers OSM et retourne une OSMMap.
     * Lance SAXException et IOException.
     * 
     */

    private OSMMapReader() {}
    
    /**
     * 
     * @param fileName
     * Le nom du fichier
     * 
     * @param unGZip
     * Indique si c'est un .zip
     * 
     * @return OSMMap
     * Lis les fichiers OSM et retourne une OSMMap.
     * 
     * @throws SAXException
     * @throws IOException
     * 
     */

    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException {

        OSMMap.Builder osmMap = new OSMMap.Builder();

        try(InputStream j = newInput(fileName, unGZip)) {
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(new DefaultHandler() {

                OSMNode.Builder osmNode = null;
                OSMWay.Builder osmWay = null;
                OSMRelation.Builder osmRelation = null;
                
                private static final String TAG_NAME = "tag";
                private static final String NODE_NAME = "node";
                private static final String WAY_NAME = "way";
                private static final String RELATION_NAME = "relation";
                private static final String ND_NAME = "nd";
                private static final String MEMBER_NAME = "member";
                private static final String REF_NAME = "ref";
                private static final String ROLE_NAME = "role";
                private static final String ID_NAME = "id";
                private static final String LAT_NAME = "lat";
                private static final String LON_NAME = "lon";
                private static final String KEY_NAME = "k";
                private static final String VALUE_NAME = "v";
                private static final String TYPE_NAME = "type";

                @Override
                public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case NODE_NAME:
                        double lat = Double.parseDouble(atts.getValue(LAT_NAME));
                        double lon = Double.parseDouble(atts.getValue(LON_NAME));
                        osmNode = new OSMNode.Builder(Long.parseLong(atts.getValue(ID_NAME)), new PointGeo(Math.toRadians(lon), Math.toRadians(lat)));
                        break;

                    case WAY_NAME:
                        osmWay = new OSMWay.Builder(Long.parseLong(atts.getValue(ID_NAME)));
                        break;

                    case ND_NAME:
                        if (osmMap.nodeForId(Long.parseLong(atts.getValue(REF_NAME))) != null) {
                            osmWay.addNode(osmMap.nodeForId(Long.parseLong(atts.getValue(REF_NAME))));
                        } else {
                            osmWay.setIncomplete();
                        }
                        break;

                    case RELATION_NAME:
                        osmRelation = new OSMRelation.Builder(Long.parseLong(atts.getValue(ID_NAME)));
                        break;

                    case TAG_NAME:
                        String key = atts.getValue(KEY_NAME);
                        String value = atts.getValue(VALUE_NAME);

                        if(osmNode != null) {
                            osmNode.setAttribute(key, value);

                        } else if(osmRelation!=null) {
                            osmRelation.setAttribute(key, value);

                        } else if(osmWay != null) {
                            osmWay.setAttribute(key, value);
                        }
                        break;

                    case MEMBER_NAME:
                        switch (atts.getValue(TYPE_NAME)) {
                        case NODE_NAME:
                            if(osmMap.nodeForId(Long.parseLong(atts.getValue(REF_NAME))) == null) {
                                osmRelation.setIncomplete();
                            } else {
                                osmRelation.addMember(Type.NODE, atts.getValue(ROLE_NAME), osmMap.nodeForId(Long.parseLong(atts.getValue(REF_NAME))));
                            }
                            break;

                        case WAY_NAME:
                            if(osmMap.wayForId(Long.parseLong(atts.getValue(REF_NAME))) == null) {
                                osmRelation.setIncomplete();
                            } else {
                                osmRelation.addMember(Type.WAY, atts.getValue(ROLE_NAME), osmMap.wayForId(Long.parseLong(atts.getValue(REF_NAME))));
                            }
                            break;

                        case RELATION_NAME:
                            if(osmMap.relationForId(Long.parseLong(atts.getValue(REF_NAME))) == null) {
                                osmRelation.setIncomplete();
                            } else {
                                osmRelation.addMember(Type.RELATION, atts.getValue(ROLE_NAME), osmMap.relationForId(Long.parseLong(atts.getValue(REF_NAME))));
                            }
                            break;
                        }
                        break;
                    }
                }

                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName) {
                    case NODE_NAME:
                        osmMap.addNode(osmNode.build());
                        osmNode = null;
                        break;

                    case WAY_NAME:
                        if (!osmWay.isIncomplete()) {
                            osmMap.addWay(osmWay.build());
                        }
                        osmWay = null;
                        break;

                    case RELATION_NAME:
                        if(!osmRelation.isIncomplete()) {
                            osmMap.addRelation(osmRelation.build());
                        }
                        osmRelation = null;
                        break;
                    }
                }

            });
            r.parse(new InputSource(j));
        }
        return osmMap.build();

    }
    
    /**
     * 
     * @param fileName
     * Le nom du fichier
     * 
     * @param unGZip
     * Indique si c'est un .zip
     * 
     * @return InputStream
     * Retourne le flot necessaire
     * 
     * @throws IOException
     * 
     */
    
    private static InputStream newInput(String fileName, boolean unGZip) throws IOException {
        InputStream i = new BufferedInputStream(new FileInputStream(fileName));
        if(unGZip) {
            i = new GZIPInputStream(i);
        }
        return i;
    }
}