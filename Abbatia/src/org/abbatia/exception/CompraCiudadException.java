package org.abbatia.exception;

import org.abbatia.bean.Edificio;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class CompraCiudadException extends ApplicationException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    private Edificio oEdificio;

    public CompraCiudadException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public CompraCiudadException(String message, Logger logger) {
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

    public Edificio getOEdificio() {
        return oEdificio;
    }

    public void setOEdificio(Edificio oEdificio) {
        this.oEdificio = oEdificio;
    }
}

