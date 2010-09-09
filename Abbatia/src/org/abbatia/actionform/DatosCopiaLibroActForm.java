package org.abbatia.actionform;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class DatosCopiaLibroActForm extends ActionForm {
    private String accion = "inicio";
    private String nombreLibro;
    private int idLibro;
    private int idMonje;
    private ArrayList monjes;
    private int seleccion = 0;
    private int estadoPrima = 0;
    private int estadoTercia = 0;
    private int estadoNona = 0;
    private int estadoVisperas = 0;
    private int[] periodo;

    public int[] getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int[] periodo) {
        this.periodo = periodo;
    }

    public int getEstadoPrima() {
        return estadoPrima;
    }

    public void setEstadoPrima(int estadoPrima) {
        this.estadoPrima = estadoPrima;
    }

    public int getEstadoTercia() {
        return estadoTercia;
    }

    public void setEstadoTercia(int estadoTercia) {
        this.estadoTercia = estadoTercia;
    }

    public int getEstadoNona() {
        return estadoNona;
    }

    public void setEstadoNona(int estadoNona) {
        this.estadoNona = estadoNona;
    }

    public int getEstadoVisperas() {
        return estadoVisperas;
    }

    public void setEstadoVisperas(int estadoVisperas) {
        this.estadoVisperas = estadoVisperas;
    }

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public ArrayList getMonjes() {
        return monjes;
    }

    public void setMonjes(ArrayList monjes) {
        this.monjes = monjes;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        if (accion.equals("seleccionado") && seleccion == 0) {
            errors.add("seleccion", new ActionError("error.seleccion.sinseleccion"));
            return errors;
        }
        if (accion.equals("seleccionado") && periodo == null) {
            errors.add("seleccion", new ActionError("error.seleccion.sinseleccion.periodo"));
            return errors;
        }

        return super.validate(actionMapping, httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }
}