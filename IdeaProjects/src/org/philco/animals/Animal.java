package org.philco.animals;

import org.philco.pullParser.XMLToken;

import java.util.HashMap;

/**
 * Represents the XML animal data type in object form
 */
public class Animal {
    private int id;
    private String type;
    private String name;
    private String location;

    public Animal() {}

    /*
    <zoo>
    <animal id="5">
    <name>Zeb</name>
    <type>Zebra</type>
    <location>A1B</location>
    </animal>
    </zoo>
    */

    public Animal(XMLToken xmlToken)
    {
        HashMap<String,String>attributes = xmlToken.getAttributes();
        if ( attributes.containsKey("id"))
            setId(Integer.parseInt(attributes.get("id")));
        if ( attributes.containsKey("type"))
            setType(attributes.get("type"));
        if ( attributes.containsKey("name"))
            setName(attributes.get("name"));
        if ( attributes.containsKey("location"))
            setLocation(attributes.get("location"));
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) { this.type = type; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Animal object created: id=")
                .append(getId())
                .append("  name=")
                .append(getName())
                .append("  type=")
                .append(getType())
                .append("  location=")
                .append(getLocation())
                .toString();
    }

    public enum Element {ANIMAL, TYPE, NAME, LOCATION}
}