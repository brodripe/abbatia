package org.abbatia.exception;

import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

public class ErrorGeneralException extends AbadiaException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public ErrorGeneralException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public ErrorGeneralException(String message, Logger logger) {
        super(message, null, logger);
    }

    /**
     * Log de la excepcion
     *
     * @param logger
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

}
