package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 12-sep-2004
 * Time: 14:06:29
 * To change this template use File | Settings | File Templates.
 */
public class datosProceso {
    private int idProceso;
    private int intervalo;
    private String name;
    private String hora;
    private String proceso;
    private String clase;
    private String parametro;

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public String getName() {
        return name;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public void setName(String name) {
        this.name = name;
    }
}
