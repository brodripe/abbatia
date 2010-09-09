package org.abbatia.exception;

import org.abbatia.exception.base.SystemException;
import org.apache.log4j.Logger;

/**
 * Excepcion principal que hereda de la clase AbadiaException
 */
public class AbadiaDBConnectionException extends SystemException {


    /**
     * Unico constructor for RootException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public AbadiaDBConnectionException(String message, Throwable throwable, Logger logger) {
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

