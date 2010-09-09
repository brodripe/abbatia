package org.abbatia.exception.base;

import org.apache.log4j.Logger;

/**
 * Excepcion principal que hereda de la clase AbadiaException
 */
public class ApplicationException extends AbadiaException {


    /**
     * Unico constructor for RootException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public ApplicationException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    /**
     * Log de la excepcion
     *
     * @param logger
     */
    protected void log(Logger logger) {
        logger.error(getMessage(), getCause());
    }

}
