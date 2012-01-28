package org.abbatia.action;

import org.abbatia.actionform.DatosSacrificioActForm;
import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bean.*;
import org.abbatia.exception.AnimalesInsuficientesException;
import org.abbatia.exception.EdificioNotFoundException;
import org.abbatia.exception.EspacioInsuficienteEnCocina;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

public class SacrificarAnimalAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        DatosSacrificioActForm datos = (DatosSacrificioActForm) form;
        String Clave = request.getParameter("clave");
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        Edificio edificio = null;

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        String sAccion = mapping.getParameter();
        if (sAccion == null) {
            sAccion = "inicio";
        }

        AnimalBBean oAnimalBBean;
        EdificioBBean oEdificioBBean;
        try {
            oAnimalBBean = new AnimalBBean();
            oEdificioBBean = new EdificioBBean();
            if (sAccion.equals("inicio_grupo")) {
                if ((datos.getSeleccion() == null) || (datos.getSeleccion().length == 0))
                    return new ActionForward("/mostrarEdificio.do?clave=" +
                            oEdificioBBean.recuperarEdificioTipo(Constantes.EDIFICIO_GRANJA, abadia, usuario).getIdDeEdificio());
                Collection<DatosSacrificioGrupo> lstListaAgrupada = oAnimalBBean.sacrificarAnimalGrupo(datos, usuario, abadia);
                request.getSession().setAttribute("ListaAgrupada", lstListaAgrupada);
                return mapping.findForward("sacrificar");
            } else if (sAccion.equals("inicio")) {
                datos = new DatosSacrificioActForm();
                datos.setId(Clave);
                oAnimalBBean.sacrificarAnimalInicio(datos, usuario, abadia);
                request.getSession().setAttribute("SacrificioForm", datos);
                return mapping.findForward("sacrificar");
            } else if (sAccion.equals("confimacion")) {
                edificio = oAnimalBBean.sacrificarAnimalConfirmacion(datos, usuario, abadia, resource, mensajes, notas);
                //si el usuario no dispone de ningun animal de ese tipo....
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
                request.getSession().setAttribute("notificacion", notas);
                request.getSession().setAttribute("SacrificioForm", new DatosSacrificioActForm());
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            } else if (sAccion.equals("grupo_confirmar")) {
                edificio = oAnimalBBean.sacrificarAnimalGrupoConfirmacion(datos, usuario, abadia, resource, mensajes, notas);
                //si el usuario no dispone de ningun animal de ese tipo....
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
                request.getSession().setAttribute("notificacion", notas);
                request.getSession().setAttribute("SacrificioForm", new DatosSacrificioActForm());
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }

            return mapping.findForward("failure");
        } catch (EspacioInsuficienteEnCocina e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productomuycaro"));
            notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.volver.edificio", edificio.getNombre()), edificio.getGrafico_visualizacion() + "_" + edificio.getNivel() + ".gif", (short) 0));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver"), (short) 1));
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("mensaje", e.getMessage());
            return mapping.findForward("mensajes");
        } catch (EdificioNotFoundException e) {
            notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.volver.edificio", edificio.getNombre()), edificio.getGrafico_visualizacion() + "_" + edificio.getNivel() + ".gif", (short) 0));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver"), (short) 1));
            request.getSession().setAttribute("notificacion", notas);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.faltaedificio"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (AnimalesInsuficientesException e) {
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}