package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class CantidadInsuficienteException extends ApplicationException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public CantidadInsuficienteException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public CantidadInsuficienteException(String message, Logger logger) {
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

