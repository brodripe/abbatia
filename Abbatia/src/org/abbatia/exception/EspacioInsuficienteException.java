package org.abbatia.exception;

import org.abbatia.bean.Edificio;
import org.abbatia.exception.base.ApplicationException;
import org.apache.log4j.Logger;

public class EspacioInsuficienteException extends ApplicationException {

    private Edificio oEdificio;
    private Edificio oEdificioMercado;
    private double dCantidad;
    private double dVolumen;

    /**
     * Unico constructor for ConfigurationException
     *
     * @param message   Mensaje explicativo
     * @param throwable Excepcion original
     * @param logger    Un objeto Logger
     */
    public EspacioInsuficienteException(String message, Throwable throwable, Logger logger) {
        super(message, throwable, logger);
    }

    public EspacioInsuficienteException(String message, Logger logger) {
        super(message, null, logger);
    }


    /**
     * Log de la excepcion
     *
     * @param logger
     */
    protected void log(Logger logger) {
        logger.debug(getMessage());
    }

    public Edificio getoEdificio() {
        return oEdificio;
    }

    public void setoEdificio(Edificio oEdificio) {
        this.oEdificio = oEdificio;
    }

    public Edificio getoEdificioMercado() {
        return oEdificioMercado;
    }

    public void setoEdificioMercado(Edificio oEdificioMercado) {
        this.oEdificioMercado = oEdificioMercado;
    }

    public double getdCantidad() {
        return dCantidad;
    }

    public void setdCantidad(double dCantidad) {
        this.dCantidad = dCantidad;
    }

    public double getdVolumen() {
        return dVolumen;
    }

    public void setdVolumen(double dVolumen) {
        this.dVolumen = dVolumen;
    }
}

