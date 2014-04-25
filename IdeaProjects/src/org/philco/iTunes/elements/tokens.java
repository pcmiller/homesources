package org.philco.iTunes.elements;

/**

 * Created by phil on 4/11/14.
 */
public class tokens {
    String name;
    boolean inTag;
    boolean skipNext;
    boolean keepNext;
    boolean valueOnly;

    public tokens(String name, boolean inTag, boolean skipNext, boolean keepNext, boolean valueOnly) {
        this.name = name;
        this.inTag = inTag;
        this.skipNext = skipNext;
        this.keepNext = keepNext;
        this.valueOnly = valueOnly;
    }
}
