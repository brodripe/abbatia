package org.abbatia.exception;

import org.abbatia.bean.Edificio;
import org.apache.log4j.Logger;

public class EspacioInsuficienteEnCampo extends EspacioInsuficienteException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public EspacioInsuficienteEnCampo(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public EspacioInsuficienteEnCampo(String message, Logger logger) {
        super(message, null, logger);
    }

    public EspacioInsuficienteEnCampo(String message, Edificio p_oEdificio, Logger logger) {
        super(message, null, logger);
        this.setoEdificio(p_oEdificio);
    }

    /**
     * Log de la excepcion
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

}