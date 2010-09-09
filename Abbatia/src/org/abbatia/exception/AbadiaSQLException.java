package org.abbatia.exception;

import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.SystemException;
import org.apache.log4j.Logger;

public class AbadiaSQLException extends SystemException {

    public AbadiaSQLException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public AbadiaSQLException(String message, Logger logger) {
        super(message, null, logger);
    }

    public AbadiaSQLException(String message, Logger logger, Usuario usuario) {
        super(message + "- Nick: " + usuario.getNick(), null, logger);
    }

    protected void log(Logger logger) {
        logger.error(getMessage());
    }

}