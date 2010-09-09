package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EspacioInsuficienteException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class MonjesAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        ActionErrors errors = new ActionErrors();
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        String sAction = request.getParameter("action");
        if (sAction == null) sAction = "inicio";

        // Inicio?
        if (sAction.equals("inicio")) {
            return mapping.findForward("success");
        }

        MonjeBBean oMonjeBBean;

        try {
            oMonjeBBean = new MonjeBBean();
            // Accion de reclutar novicio
            if (sAction.equals("reclutar_novicio")) {

                oMonjeBBean.reclutarNovicio(abadia, usuario, mensajes, notas, resource);
                if (!notas.isEmpty()) {
                    request.getSession().setAttribute("notificacion", notas);
                } else {
                    request.getSession().removeAttribute("notificacion");
                }

                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }

            return mapping.findForward("failure");

        } catch (EspacioInsuficienteException e) {
            notas.add(new Notificacion("main.do", "Volver"));
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
