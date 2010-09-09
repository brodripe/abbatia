package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 09-sep-2007
 * Time: 1:29:08
 * To change this template use File | Settings | File Templates.
 */
public class PeticionBloqueoActForm extends ActionForm {
    private int abadiaId;
    private String nombreAbadia;
    private String motivo;
    private int numDias;
    private long usuarioId;


    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public int getAbadiaId() {
        return abadiaId;
    }

    public void setAbadiaId(int abadiaId) {
        this.abadiaId = abadiaId;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }
}
