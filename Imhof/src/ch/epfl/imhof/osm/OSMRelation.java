package ch.epfl.imhof.osm;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;

/** 
 * Un ensemble d'entités OSM.
 * 
 * @author Maxime Lemarignier (247926)
 * @author Julien Linder (245786)
 * 
 */

public final class OSMRelation extends OSMEntity {

    private final List<Member> members;
    
    /**
     * 
     * @param id
     * L'id unique de la relation
     * 
     * @param members
     * La liste des membres de la relations
     * 
     * @param attributes
     * Les attributs de la relations
     * 
     */

    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }
    
    /**
     * 
     * @return List<Member>
     * Retourne la liste des membres de la relations
     * 
     */

    public List<Member> members() {
        return Collections.unmodifiableList(new ArrayList<Member>(members));
    }

    public static final class Member {

        private final Type type;
        private final String role;
        private final OSMEntity member;
        
        /**
         * 
         * @param type
         * Le type du membre
         * 
         * @param role
         * Le role du membre
         * 
         * @param member
         * Le membre lui-meme
         * 
         */

        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }
        
        /**
         * 
         * @return Type
         * Retourne le type du membre
         * 
         */

        public Type type() {
            return type;
        }
        
        /**
         * 
         * @return String
         * Retourne le nom du membre
         * 
         */

        public String role() {
            return role;
        }
        
        /**
         * 
         * @return OSMEntity
         * Retourne le membre
         * 
         */

        public OSMEntity member() {
            return member;
        }

        public enum Type {
            NODE,WAY,RELATION;
        }   
    }

    public static final class Builder extends OSMEntity.Builder {

        private final List<Member> members = new ArrayList<>();
        
        /**
         * 
         * @param id
         * L'id unique du membre
         * 
         */

        public Builder(long id) {
            super(id);
        }
        
        /**
         * 
         * @param type
         * Le type du membre a ajouter
         * 
         * @param role
         * Le role du membre a ajouter
         * 
         * @param newMember
         * Le membre a ajouter
         * 
         */

        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }
        
        /**
         * 
         * @return OSMRelation
         * Construit une OSMRelation
         * 
         * @throws IllegalStateException
         * Lance l'exception si la relation est incomplete
         * 
         */

        public OSMRelation build() throws IllegalStateException {
            if(isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMRelation(id, members, att.build());
        }
    }
}
