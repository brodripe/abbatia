package org.abbatia.exception;

import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class LoteNoDisponibleException extends ApplicationException {

    public LoteNoDisponibleException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public LoteNoDisponibleException(String message, Logger logger) {
        this(message, null, logger);
    }

    protected void log(Logger logger) {
        logger.error(getMessage());
    }


}
