package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class ExperienciaInsuficienteException extends ApplicationException {
    public ExperienciaInsuficienteException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public ExperienciaInsuficienteException(String message, Logger logger) {
        super(message, null, logger);
    }

    protected void log(Logger logger) {
        logger.debug(getMessage());
    }
}