package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class AnimalesInsuficientesException extends ApplicationException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public AnimalesInsuficientesException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public AnimalesInsuficientesException(String message, Logger logger) {
        super(message, null, logger);
    }

    /**
     * Log de la excepcion
     */
    protected void log(Logger logger) {
        logger.error(getMessage());
    }

}