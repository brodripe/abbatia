package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ListarTrabajosAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws IOException, ServletException {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        // Lista de trabajos
        ArrayList alTable;
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        String frw = "success";

        MonjeBBean oMonjeBBean;
        try {
            oMonjeBBean = new MonjeBBean();
            // Lista de actividades
            alTable = oMonjeBBean.listarTrabajos(request.getParameter("ahora"), abadia, usuario, resource);
            if (request.getParameter("ahora") != null) {
                frw = "successAhora";
            }
            request.getSession().setAttribute("actividades", alTable);

        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
        return mapping.findForward(frw);
    }
}
