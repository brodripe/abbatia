package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 18-sep-2004
 * Time: 19:28:35
 * To change this template use File | Settings | File Templates.
 */
public class datosMonjeActividad {
    private short periodo;
    private String fecha;
    private String mensaje;
    private short tareaid;
    private String tarea;

    public short getTareaid() {
        return tareaid;
    }

    public void setTareaid(short tareaid) {
        this.tareaid = tareaid;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }
}
