package org.philco.animals;

import org.philco.pullParser.XMLToken;

/**
 * Original version by phil on 4/26/14.
 */
public class Type {
    private String value;
    public String getValue() { return value; }

    public Type(XMLToken xmlToken)
    {
        if ( xmlToken.getAttributes().containsKey("type"))
            value = xmlToken.getAttributes().get("type");
    }
}
