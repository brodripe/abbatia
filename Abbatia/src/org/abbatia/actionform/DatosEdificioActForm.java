package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class DatosEdificioActForm extends ActionForm {
    private int idDeEdificio;
    private int tipo_edificio;
    private String accion;
    private String nombre_edificio;
    private String descripcion_edificio;
    private String coste_oro;
    private String coste_madera;
    private String coste_piedra;
    private String coste_hierro;
    private String coste_total;
    private int mantenimiento_0;
    private int mantenimiento_1;
    private int mantenimiento_2;
    private int mantenimiento_3;


    public int getIdDeEdificio() {
        return idDeEdificio;
    }

    public void setIdDeEdificio(int idDeEdificio) {
        this.idDeEdificio = idDeEdificio;
    }

    public int getMantenimiento_0() {
        return mantenimiento_0;
    }

    public void setMantenimiento_0(int mantenimiento_0) {
        this.mantenimiento_0 = mantenimiento_0;
    }

    public int getMantenimiento_1() {
        return mantenimiento_1;
    }

    public void setMantenimiento_1(int mantenimiento_1) {
        this.mantenimiento_1 = mantenimiento_1;
    }

    public int getMantenimiento_2() {
        return mantenimiento_2;
    }

    public void setMantenimiento_2(int mantenimiento_2) {
        this.mantenimiento_2 = mantenimiento_2;
    }

    public int getMantenimiento_3() {
        return mantenimiento_3;
    }

    public void setMantenimiento_3(int mantenimiento_3) {
        this.mantenimiento_3 = mantenimiento_3;
    }

    private int dias_costruccion;


    public void setTipo_edificio(int tipo_edificio) {
        this.tipo_edificio = tipo_edificio;
    }


    public int getTipo_edificio() {
        return tipo_edificio;
    }


    public void setNombre_edificio(String nombre_edificio) {
        this.nombre_edificio = nombre_edificio;
    }


    public String getNombre_edificio() {
        return nombre_edificio;
    }


    public void setDescripcion_edificio(String descripcion_edificio) {
        this.descripcion_edificio = descripcion_edificio;
    }


    public String getDescripcion_edificio() {
        return descripcion_edificio;
    }


    public void setCoste_oro(String coste_oro) {
        this.coste_oro = coste_oro;
    }


    public String getCoste_oro() {
        return coste_oro;
    }


    public void setCoste_madera(String coste_madera) {
        this.coste_madera = coste_madera;
    }


    public String getCoste_madera() {
        return coste_madera;
    }


    public void setCoste_piedra(String coste_piedra) {
        this.coste_piedra = coste_piedra;
    }


    public String getCoste_piedra() {
        return coste_piedra;
    }


    public void setCoste_hierro(String coste_hierro) {
        this.coste_hierro = coste_hierro;
    }


    public String getCoste_hierro() {
        return coste_hierro;
    }


    public void setDias_costruccion(int dias_costruccion) {
        this.dias_costruccion = dias_costruccion;
    }


    public int getDias_costruccion() {
        return dias_costruccion;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setCoste_total(String coste_total) {
        this.coste_total = coste_total;
    }

    public String getAccion() {
        return accion;
    }

    public String getCoste_total() {
        return coste_total;
    }

}
