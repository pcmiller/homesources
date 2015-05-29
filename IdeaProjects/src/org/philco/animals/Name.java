package org.philco.animals;

import org.philco.pullParser.XMLToken;

/**
 * Original version by phil on 4/26/14.
 */
public class Name {
    private String value;
    public String getValue() { return value; }

    public Name(XMLToken xmlToken)
    {
        if ( xmlToken.getAttributes().containsKey("name"))
            value = xmlToken.getAttributes().get("name");
    }
}
