package org.philco.iTunes.xmlParser;

import java.util.HashMap;

/**
 * Original version by phil on 4/25/14.
 */
public class track {
    HashMap<String, String>keys;
    static final String[] keysToKeep = new String[] {
            "Name",
            "Artist",
            "Composer",
            "Album",
            "Genre",
            "Track Number",
            "Location",
    };
    static final String[] keysToSkip = new String[] {
            "Track ID",
            "Kind",
            "Size",
            "Total Time",
            "Disc Number",
            "Disc Count",
            "Year",
            "Date Modified",
            "Date Added",
            "Bit Rate",
            "Sample Rate",
            "Sort Album Artist",
            "Persistent ID",
            "Track Type",
            "File Folder Count",
            "Library Folder Count",
            "Album Artist",
    };

    private boolean inArray(String stringToFind, String[] arrayToSearch) {
        for (String s : arrayToSearch)
            if ( stringToFind.equals(s))
                return true;
        return false;
    }

    public track() {
        keys = new HashMap<String,String>();
    }

    public void add(String key, String value) throws UnrecognizedElementException {
        if ( key == null || key.isEmpty() )
            return;

        if ( inArray(key, keysToSkip))
            return;

        if ( value == null || value.isEmpty() )
            return;

        if ( inArray(key, keysToKeep))
            keys.put(key, value);

        throw new UnrecognizedElementException(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : keys.keySet()) {
            sb.append(key);
            sb.append('=');
            sb.append(keys.get(key));
            sb.append(',');
        }

        return sb.toString();
    }
}
