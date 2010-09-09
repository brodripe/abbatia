package org.abbatia.bean;

import java.io.Serializable;

public class Notificacion implements Serializable {
    private String action;
    private String texto;
    private String grafico;
    private short salto = 0;


    public Notificacion(String action, String texto) {
        this.action = action;
        this.texto = texto;
    }

    public Notificacion(String action, String texto, short salto) {
        this.action = action;
        this.texto = texto;
        this.salto = salto;
    }

    public Notificacion(String action, String texto, String grafico) {
        this.action = action;
        this.texto = texto;
        this.grafico = grafico;
    }

    public Notificacion(String action, String texto, String grafico, short salto) {
        this.action = action;
        this.texto = texto;
        this.grafico = grafico;
        this.salto = salto;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String getAction() {
        return action;
    }


    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setGrafico(String grafico) {
        this.grafico = grafico;
    }

    public String getTexto() {
        return texto;
    }

    public String getGrafico() {
        return grafico;
    }

    public short getSalto() {
        return salto;
    }

    public void setSalto(short salto) {
        this.salto = salto;
    }

}
