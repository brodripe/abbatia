package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 02-dic-2007
 * Time: 0:34:59
 * To change this template use File | Settings | File Templates.
 */
public class LibroTipo {
    private int idLibroTipo;
    private String nombre;
    private String descripcion;


    public int getIdLibroTipo() {
        return idLibroTipo;
    }

    public void setIdLibroTipo(int idLibroTipo) {
        this.idLibroTipo = idLibroTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
