package org.abbatia.action;

import org.abbatia.bbean.CultivoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EspacioInsuficienteEnCampo;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class NuevoCampoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        CultivoBBean oCultivoBBean;
        try {
            oCultivoBBean = new CultivoBBean();
            return oCultivoBBean.crearCultivo(abadia, usuario, resource);

        } catch (EspacioInsuficienteEnCampo e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.nomascampos"));
            notas.add(new Notificacion("mostrarEdificio.do?clave=" + e.getoEdificio().getIdDeEdificio(), resource.getMessage("mensajes.link.campo")));
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}