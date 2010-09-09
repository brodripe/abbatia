package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class MensajeActForm extends ActionForm {
    private String accion = "inicio";
    private int correoid;
    private long idAbadiaOrigen;
    private String desde;
    private String destinatario;
    private String destinatarioid;
    private String asunto;
    private String msg;
    private String coste;
    private String costeUnitario;
    private ArrayList correos;
    private String tipo;
    private String direccion;
    private int[] seleccion;


    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCosteUnitario() {
        return costeUnitario;
    }

    public void setCosteUnitario(String costeUnitario) {
        this.costeUnitario = costeUnitario;
    }

    public int[] getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int[] seleccion) {
        this.seleccion = seleccion;
    }


    public void setIdAbadiaOrigen(long idAbadiaOrigen) {
        this.idAbadiaOrigen = idAbadiaOrigen;
    }


    public long getIdAbadiaOrigen() {
        return idAbadiaOrigen;
    }


    public void setDesde(String desde) {
        this.desde = desde;
    }


    public String getDesde() {
        return desde;
    }


    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    public String getDestinatario() {
        return destinatario;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getAccion() {
        return accion;
    }


    public void setCorreoid(int correoid) {
        this.correoid = correoid;
    }


    public int getCorreoid() {
        return correoid;
    }


    public void setDestinatarioid(String destinatarioid) {
        this.destinatarioid = destinatarioid;
    }


    public String getDestinatarioid() {
        return destinatarioid;
    }


    public void setCoste(String coste) {
        this.coste = coste;
    }


    public String getCoste() {
        return coste;
    }


    public void setCorreos(ArrayList correos) {
        this.correos = correos;
    }


    public ArrayList getCorreos() {
        return correos;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getTipo() {
        return tipo;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getDireccion() {
        return direccion;
    }
}