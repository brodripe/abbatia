package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class NoImplementadoException extends ApplicationException {

    /**
     * Constructor de la clase.
     *
     * @param message Mensaje explicativo del error producido
     * @param logger  Un objeto logger para escribir la traza
     */
    public NoImplementadoException(String message, Logger logger) {
        super(message, null, logger);
    }

    /**
     * Constructor de la clase.
     *
     * @param message   Mensaje explicativo del error producido
     * @param throwable Excepción original producida
     * @param logger    Un objeto logger para escribir la traza
     */
    public NoImplementadoException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    /**
     * Log de la excepcion
     *
     * @param logger Un objeto logger para escribir el mensaje
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

}
