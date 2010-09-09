package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class UsuarioNoEncontradoException extends ApplicationException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public UsuarioNoEncontradoException(String message, Throwable throwable, Logger logger) {
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

