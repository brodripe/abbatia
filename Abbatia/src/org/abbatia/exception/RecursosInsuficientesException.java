package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class RecursosInsuficientesException extends ApplicationException {
    public RecursosInsuficientesException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public RecursosInsuficientesException(String message, Logger logger) {
        super(message, null, logger);
    }

    protected void log(Logger logger) {
        logger.debug(getMessage());
    }
}