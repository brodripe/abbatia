package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 02-oct-2004
 * Time: 10:36:44
 * To change this template use File | Settings | File Templates.
 */
public class MonjeProceso extends MonjeBase {
    private int idActividad;
    private int idLibroCopia;


    public int getIdLibroCopia() {
        return idLibroCopia;
    }

    public void setIdLibroCopia(int idLibroCopia) {
        this.idLibroCopia = idLibroCopia;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }
}
