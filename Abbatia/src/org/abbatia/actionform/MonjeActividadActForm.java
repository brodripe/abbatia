package org.abbatia.actionform;

import org.abbatia.bean.datosMonjeActividad;
import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 18-sep-2004
 * Time: 19:50:57
 * To change this template use File | Settings | File Templates.
 */
public class MonjeActividadActForm extends ActionForm {
    private long idDeMonje;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    private String fechaDeEntradaEnAbadia;
    private int siguienteID;
    private int anteriorID;
    private int estado;   // 0 - vivo, 1 - muerto ( velatorio ), 2 - enfermo, 3 - viaje
    private int idDeJerarquia;
    private String jerarquia;
    private long idDeAbadia;
    private long idDeEspecializacion;
    private short edad;


    private ArrayList<datosMonjeActividad> listaActividades;

    public long getIdDeMonje() {
        return idDeMonje;
    }

    public void setIdDeMonje(long idDeMonje) {
        this.idDeMonje = idDeMonje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public String getFechaDeEntradaEnAbadia() {
        return fechaDeEntradaEnAbadia;
    }

    public void setFechaDeEntradaEnAbadia(String fechaDeEntradaEnAbadia) {
        this.fechaDeEntradaEnAbadia = fechaDeEntradaEnAbadia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdDeJerarquia() {
        return idDeJerarquia;
    }

    public void setIdDeJerarquia(int idDeJerarquia) {
        this.idDeJerarquia = idDeJerarquia;
    }

    public String getJerarquia() {
        return jerarquia;
    }

    public void setJerarquia(String jerarquia) {
        this.jerarquia = jerarquia;
    }

    public long getIdDeAbadia() {
        return idDeAbadia;
    }

    public void setIdDeAbadia(long idDeAbadia) {
        this.idDeAbadia = idDeAbadia;
    }

    public long getIdDeEspecializacion() {
        return idDeEspecializacion;
    }

    public void setIdDeEspecializacion(long idDeEspecializacion) {
        this.idDeEspecializacion = idDeEspecializacion;
    }

    public int getSiguienteID() {
        return siguienteID;
    }

    public void setSiguienteID(int siguienteID) {
        this.siguienteID = siguienteID;
    }

    public int getAnteriorID() {
        return anteriorID;
    }

    public void setAnteriorID(int anteriorID) {
        this.anteriorID = anteriorID;
    }

    public short getEdad() {
        return edad;
    }

    public void setEdad(short edad) {
        this.edad = edad;
    }


    public ArrayList<datosMonjeActividad> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(ArrayList<datosMonjeActividad> listaActividades) {
        this.listaActividades = listaActividades;
    }
}
