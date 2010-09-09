package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class CompraNoPermitidaException extends ApplicationException {

    public CompraNoPermitidaException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public CompraNoPermitidaException(String message, Logger logger) {
        this(message, null, logger);
    }

    protected void log(Logger logger) {
        logger.error(getMessage());
    }


}
