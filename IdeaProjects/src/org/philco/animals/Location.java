package org.philco.animals;

import org.philco.pullParser.XMLToken;

/**
 * Original version by phil on 4/26/14.
 */
public class Location {
    private String value;
    public String getValue() { return value; }

    public Location(XMLToken xmlToken)
    {
        if ( xmlToken.getAttributes().containsKey("location"))
            value = xmlToken.getAttributes().get("location");
    }
}
