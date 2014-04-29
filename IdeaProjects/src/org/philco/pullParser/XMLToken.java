package org.philco.pullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Original version by phil on 4/26/14.
 */
public class XMLToken {
    // used to indicate that an XML element is not yet complete, i.e., no end tag yet.
    private boolean complete = false;
    public boolean isComplete() { return complete; }
    public void setComplete(boolean b) { complete = b; }

    /** Tag name, i.e., <tagname></tagname> */
    private String tagName;
    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    /** Text between the tag and endtag, i.e., <tag>value</tag> */
    private String value;
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    /** attributes of the element, i.e., <tag a1=v1 a2=v2 ...>value</tag> */
    private HashMap<String,String> attributes = new HashMap<String, String>();
    public HashMap<String,String>getAttributes() { return attributes; }
    public void addAttribute(String key, String value)
    {
        if ( key == null || value == null )
            return;
        attributes.put(key, value);
    }

    /** elements defined before this element's endtag */
    private ArrayList<XMLToken> children = new ArrayList<XMLToken>();
    public ArrayList<XMLToken>getChildren() { return children; }
    public void addChild(XMLToken child) { children.add(child); }

    public XMLToken(String name)
    {
        this.tagName = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s", this.tagName));
        for (String key : attributes.keySet())
            sb.append(" ").append(key).append("=").append(attributes.get(key)).append(" ");
        if ( value != null )
            sb.append(" value=").append(value);
        if ( ! isComplete() )
            sb.append(" (incomplete)");
        return sb.toString();
    }
}
