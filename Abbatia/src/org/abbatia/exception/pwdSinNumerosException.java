package org.abbatia.exception;

import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class pwdSinNumerosException extends ApplicationException {

    public pwdSinNumerosException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public pwdSinNumerosException(String message, Logger logger) {
        super(message, null, logger);
    }

    public pwdSinNumerosException(String message, Logger logger, Usuario usuario) {
        super(message + "- Nick: " + usuario.getNick(), null, logger);
    }

    protected void log(Logger logger) {
        logger.warn(getMessage());
    }

}
