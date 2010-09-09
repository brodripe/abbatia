package org.abbatia.exception;

import org.abbatia.bean.Monje;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class MuerteMonjeReciente extends ApplicationException {


    private Monje oMonje;

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public MuerteMonjeReciente(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public MuerteMonjeReciente(String message, Logger logger) {
        super(message, null, logger);
    }

    public MuerteMonjeReciente(String message, Monje p_oMonje, Logger logger) {
        super(message, null, logger);
        this.oMonje = p_oMonje;
    }

    public Monje getOMonje() {
        return oMonje;
    }

    public void setOMonje(Monje oMonje) {
        this.oMonje = oMonje;
    }

    /**
     * @param logger
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

}