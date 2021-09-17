/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
class FilePrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePrinter.class);

    static void printError(String cashShopDumpertxt, Exception ex) {
    LOGGER.error(cashShopDumpertxt, ex);
    }
    
}
