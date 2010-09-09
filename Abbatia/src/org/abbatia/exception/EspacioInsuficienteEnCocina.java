package org.abbatia.exception;

import org.abbatia.bean.Edificio;
import org.apache.log4j.Logger;

public class EspacioInsuficienteEnCocina extends EspacioInsuficienteException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public EspacioInsuficienteEnCocina(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public EspacioInsuficienteEnCocina(String message, Logger logger) {
        super(message, null, logger);
    }

    public EspacioInsuficienteEnCocina(String message, Edificio p_oEdificio, Logger logger) {
        super(message, null, logger);
        this.setoEdificio(p_oEdificio);
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
