package org.abbatia.exception;

import org.abbatia.exception.base.SystemException;
import org.apache.log4j.Logger;

/**
 * @author user
 *         <p/>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class AbadiaFormatXMLException extends SystemException

{

    public AbadiaFormatXMLException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    /**
     * Log de la excepcion
     *
     * @param logger
     */
    protected void log(Logger logger) {
        logger.error(getMessage());
    }
}
	