package org.philco.iTunes.items;

import java.util.HashMap;
import java.util.Map;

public class itemCollection {
	private Map<String,String> values;

	public itemCollection() {
		super();
		this.values = new HashMap<String,String>();
	}
	public void add(String key, String value) {
		values.put(key, value);
	}

}
