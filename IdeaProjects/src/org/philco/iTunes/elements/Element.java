package org.philco.iTunes.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Element {
	ArrayList<Element> children = null;
	HashMap<String,String>attributes = null;
	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	String value, name;

	public Element(String name) {
		this.name = name;
	}
	
	public void addAttribute(String name, String value) {
		if ( attributes == null )
			attributes = new HashMap<String,String>();
		
		attributes.put(name, value);
	}
	
	public void addChild(Element element) {
		if ( children == null )
			 children = new ArrayList<Element>();
		
		children.add(element);
	}
	
	public List<Element>getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ( ( obj == null ) || ! (obj instanceof Element) )
			return false;
		
		Element that = (Element)obj;
		if ( ! name.isEmpty() && ! name.isEmpty() && that.name.equals(this.name))
			return true;

		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('<').append(name).append('>');
		if (value != null) {
			sb.append(value).append("</"+name+">");
		} else {
			sb.append("/>");
		}
		return sb.toString();
	}	
}
