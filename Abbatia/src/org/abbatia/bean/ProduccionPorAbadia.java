package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 07-dic-2004
 * Time: 15:25:25
 * To change this template use File | Settings | File Templates.
 */
public class ProduccionPorAbadia {
    private int idAbadia;
    private int idEdificio;
    private int idEdificioOri;
    private int idAlimento;
    private int idRecurso;
    private String descripcion;
    private String unidad_medida;
    private double cantidad;
    private int recoge_monje;


    public int getIdEdificioOri() {
        return idEdificioOri;
    }

    public void setIdEdificioOri(int idEdificioOri) {
        this.idEdificioOri = idEdificioOri;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getRecoge_monje() {
        return recoge_monje;
    }

    public void setRecoge_monje(int recoge_monje) {
        this.recoge_monje = recoge_monje;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }
}
