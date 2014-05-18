package org.philco.cupExample.parser;

import java_cup.runtime.Symbol;
import org.philco.animals.parser;

/**
 * Created by phil on 5/8/14.
 */
public class main {

    public static void main(String[] argv) {
        boolean do_debug_parse = false;

        /* create a parsing object */
        parser parser_obj = new parser();

        /* open input files, etc. here */
        Symbol parse_tree = null;

        try {
            if (do_debug_parse)
                parse_tree = parser_obj.debug_parse();
            else
                parse_tree = parser_obj.parse();
        } catch (Exception e) {
            /* do cleanup here - - possibly rethrow e */
        } finally {
            /* do close out here */
        }
    }
}
