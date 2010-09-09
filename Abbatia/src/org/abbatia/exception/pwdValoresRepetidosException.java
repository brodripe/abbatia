package org.abbatia.exception;

import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class pwdValoresRepetidosException extends ApplicationException {

    public pwdValoresRepetidosException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public pwdValoresRepetidosException(String message, Logger logger) {
        super(message, null, logger);
    }

    public pwdValoresRepetidosException(String message, Logger logger, Usuario usuario) {
        super(message + "- Nick: " + usuario.getNick(), null, logger);
    }

    protected void log(Logger logger) {
        logger.error(getMessage());
    }

}
