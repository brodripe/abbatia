package org.abbatia.action;

import org.abbatia.actionform.SiembraActForm;
import org.abbatia.bbean.CultivoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Confirmacion;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.CampoNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SembrarCampoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String idCampo = request.getParameter("clave");
        SiembraActForm siembra = (SiembraActForm) form;

        Confirmacion confirmacion = null;
        Edificio edificio;
        Object oObject;

        CultivoBBean oCultivoBBean;

        try {
            oCultivoBBean = new CultivoBBean();
            if (idCampo != null) {
                oObject = oCultivoBBean.sembrarCampoInicio(abadia, usuario, Integer.valueOf(idCampo), mapping.getParameter(), resource);
                if (oObject instanceof Confirmacion) {
                    request.setAttribute("DatosConfirmacion", oObject);
                    return mapping.findForward("confirmar");
                } else {
                    request.setAttribute("SiembraActForm", oObject);
                    return mapping.findForward("seleccion");
                }
            } else if (siembra.getAccion().equals("confirmar")) {
                edificio = oCultivoBBean.sembrarCampoConfirmacion(abadia, usuario, siembra, resource);
                request.removeAttribute("SiembraActForm");

                Utilidades.eliminarRegistroContext(usuario.getNick());
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());

                return mapping.findForward("cerrarPopUp");
            }
            return mapping.findForward("error");
        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid", idCampo));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);

        } catch (CampoNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.campo.noencontrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        }
    }
}