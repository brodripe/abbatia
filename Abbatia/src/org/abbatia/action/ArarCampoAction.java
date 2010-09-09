package org.abbatia.action;

import org.abbatia.bbean.CultivoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Confirmacion;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.CampoNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ArarCampoAction extends Action {

    private static Logger log = Logger.getLogger(ArarCampoAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        String idCampo = request.getParameter("clave");

        Confirmacion confirmacion;
        CultivoBBean oCultivoBBean;
        Edificio oEdificio;
        HashMap hmRequest;

        try {

            oCultivoBBean = new CultivoBBean();
            hmRequest = oCultivoBBean.ararCampo(abadia, usuario, idCampo, mapping, resource);

            if (!mapping.getParameter().equals("confirmar")) {
                //dado que ahora se podra arar en campoAD en cualquier punto del proceso de cultivo
                //verificaremos el estado en el que se encuentra el campoAD actualmente y mostraremos una alerta
                //indicando que se perdera la cosecha si se dedide arar en mitad del cultivo....
                confirmacion = (Confirmacion) hmRequest.get("DatosConfirmacion");
                if (confirmacion != null) {
                    request.setAttribute("DatosConfirmacion", confirmacion);
                    return mapping.findForward("confirmar");
                }
            }
            oEdificio = (Edificio) hmRequest.get("Edificio");
            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

        } catch (CampoNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.campo.noencontrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
