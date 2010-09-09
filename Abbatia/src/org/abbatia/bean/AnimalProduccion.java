package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 07-dic-2004
 * Time: 12:31:47
 * To change this template use File | Settings | File Templates.
 */
public class AnimalProduccion {
    private int idEdificio;
    private int idAlimento;
    private int idRecurso;
    private double cantidad;
    private int recoge_monje;


    public int getRecoge_monje() {
        return recoge_monje;
    }

    public void setRecoge_monje(int recoge_monje) {
        this.recoge_monje = recoge_monje;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }
}
