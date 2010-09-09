package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 14-abr-2006
 * Time: 0:16:25
 * To change this template use File | Settings | File Templates.
 */
public class Confirmacion {
    private String textoConfirmacion;
    private String titulo;
    private String accionSi;
    private String accionNo;

    public String getTextoConfirmacion() {
        return textoConfirmacion;
    }

    public void setTextoConfirmacion(String textoConfirmacion) {
        this.textoConfirmacion = textoConfirmacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAccionSi() {
        return accionSi;
    }

    public void setAccionSi(String accionSi) {
        this.accionSi = accionSi;
    }

    public String getAccionNo() {
        return accionNo;
    }

    public void setAccionNo(String accionNo) {
        this.accionNo = accionNo;
    }


}
