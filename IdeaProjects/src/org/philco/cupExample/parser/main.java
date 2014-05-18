package org.philco.cupExample.parser;

import java_cup.runtime.Symbol;
import org.philco.animals.parser;

/**
 * Created by phil on 5/8/14.
 */
public class main {

    public static void main(String[] argv) {
        boolean do_debug_parse = false;

        for ( String arg : argv ) {
            if ( "-d".equals(arg))
                do_debug_parse = true;
        }

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
            System.err.println("Parsing exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            /* do close out here */
        }
    }
}
