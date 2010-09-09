package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 15-mar-2006
 * Time: 0:15:28
 * To change this template use File | Settings | File Templates.
 */
public class MonjeInicio extends MonjeBase {
    //SELECT m.MONJEID, m.NOMBRE, l.LITERAL, a.NOMBRE AS NOMBRE_ABADIA, mv.FECHA_LLEGADA_DESTINO, mv.FECHA_LLEGADA_ORIGEN
    private String literal;
    private String nombreAbadia;
    private String fechaLlegadaDestino;
    private String fechaLlegadaOrigen;

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }

    public String getFechaLlegadaDestino() {
        return fechaLlegadaDestino;
    }

    public void setFechaLlegadaDestino(String fechaLlegadaDestino) {
        this.fechaLlegadaDestino = fechaLlegadaDestino;
    }

    public String getFechaLlegadaOrigen() {
        return fechaLlegadaOrigen;
    }

    public void setFechaLlegadaOrigen(String fechaLlegadaOrigen) {
        this.fechaLlegadaOrigen = fechaLlegadaOrigen;
    }
}
