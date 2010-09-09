package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 01-jul-2006
 * Time: 0:10:44
 * To change this template use File | Settings | File Templates.
 */
public class EdificioMantenimiento {
    private int idAbadia;
    private int idEdificio;
    private int idTipoEdificio;
    private short nivel;
    private int estado;
    private double precioMantenimiento;
    private double oroTotal;
    private int idIdioma;
    private int mantenimiento;

    public int getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(int mantenimiento) {
        this.mantenimiento = mantenimiento;
    }


    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public double getPrecioMantenimiento() {
        return precioMantenimiento;
    }

    public void setPrecioMantenimiento(double precioMantenimiento) {
        this.precioMantenimiento = precioMantenimiento;
    }

    public double getOroTotal() {
        return oroTotal;
    }

    public void setOroTotal(double oroTotal) {
        this.oroTotal = oroTotal;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public int getIdTipoEdificio() {
        return idTipoEdificio;
    }

    public void setIdTipoEdificio(int idTipoEdificio) {
        this.idTipoEdificio = idTipoEdificio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
