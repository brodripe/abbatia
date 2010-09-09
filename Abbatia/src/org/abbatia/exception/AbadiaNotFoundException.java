package org.abbatia.exception;

import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class AbadiaNotFoundException extends ApplicationException {

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger Un objeto Logger
     */
    private Usuario oUsuario;

    public AbadiaNotFoundException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public AbadiaNotFoundException(String message, Logger logger) {
        super(message, null, logger);
    }

    public AbadiaNotFoundException(String message, Usuario p_oUsuario, Logger logger) {
        super(message, null, logger);
        setUsuario(p_oUsuario);
    }


    /**
     * Log de la excepcion
     *
     * @param logger
     */
    protected void log(Logger logger) {
        logger.error(getMessage());
    }

    public Usuario getUsuario() {
        return oUsuario;
    }

    public void setUsuario(Usuario oUsuario) {
        this.oUsuario = oUsuario;
    }
}

