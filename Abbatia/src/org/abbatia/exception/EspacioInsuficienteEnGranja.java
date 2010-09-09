package org.abbatia.exception;

import org.apache.log4j.Logger;

public class EspacioInsuficienteEnGranja extends EspacioInsuficienteException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public EspacioInsuficienteEnGranja(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public EspacioInsuficienteEnGranja(String message, Logger logger) {
        super(message, null, logger);
    }

    /**
     * Log de la excepcion
     *
     * @param message
     * @param throwable
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

}
