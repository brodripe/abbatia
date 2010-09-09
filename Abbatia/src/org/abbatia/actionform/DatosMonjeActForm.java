package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class DatosMonjeActForm extends ActionForm {
    private String codigo = "";       //codigo de monje
    private String nombre = "";       //nombre del monje
    private String apellido1 = "";
    private String apellido2 = "";
    private String cabadia = "";      //codigo de abbatia
    private String fnacimiento = "";  //fecha de nacimiento
    private String fentrada = "";     //fecha de entrada
    private String ccargo = "";       //código de cargo
    private String cespecialidad = "";//codigo de especializacion
    private ArrayList Habilidades;    //tabla de habilidades


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getCodigo() {
        return codigo;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setCabadia(String cabadia) {
        this.cabadia = cabadia;
    }


    public String getCabadia() {
        return cabadia;
    }


    public void setFnacimiento(String fnacimiento) {
        this.fnacimiento = fnacimiento;
    }


    public String getFnacimiento() {
        return fnacimiento;
    }


    public void setFentrada(String fentrada) {
        this.fentrada = fentrada;
    }


    public String getFentrada() {
        return fentrada;
    }


    public void setCcargo(String ccargo) {
        this.ccargo = ccargo;
    }


    public String getCcargo() {
        return ccargo;
    }


    public void setCespecialidad(String cespecialidad) {
        this.cespecialidad = cespecialidad;
    }


    public String getCespecialidad() {
        return cespecialidad;
    }


    public void setHabilidades(ArrayList Habilidades) {
        this.Habilidades = Habilidades;
    }


    public ArrayList getHabilidades() {
        return Habilidades;
    }


    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }


    public String getApellido1() {
        return apellido1;
    }


    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }


    public String getApellido2() {
        return apellido2;
    }
}