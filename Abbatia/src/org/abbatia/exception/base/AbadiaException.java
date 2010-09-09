package org.abbatia.exception.base;

import org.apache.log4j.Logger;

/**
 * Excepcion principal que hereda de la clase Exception
 */
public abstract class AbadiaException extends Exception {

    private final Throwable t;

    /**
     * Unico constructor for RootException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public AbadiaException(String message, Throwable throwable, Logger logger) {
        super(message, throwable);
        t = throwable;
        if (t != null) t.printStackTrace();
        log(logger);
    }

    protected abstract void log(Logger logger);

    /**
     * Devuelve la excepcion original si la hay
     *
     * @return la causa original de la excepcion
     */
    public final Throwable getCause() {
        return t;
    }

    /**
     * Devuelve la excepcion original si la hay
     *
     * @return la causa original de la excepcion
     */
    public String getMessage() {
        StringBuffer strBuffer = new StringBuffer(super.getMessage());

        if (t != null) {
            strBuffer.append("[");
            strBuffer.append(t.getMessage());
            strBuffer.append("]");
        }

        return strBuffer.toString();
    }
}
