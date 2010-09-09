package org.abbatia.action;


import org.abbatia.bbean.ViajeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.ApplicationException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Abbatia


public class VisitaAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping             The ActionMapping used to select this instance.
     * @param actionForm          The optional ActionForm bean for this request.
     * @param request             The HTTP Request we are processing.
     * @param httpServletResponse The HTTP Response we are processing.
     * @return ActionForward
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        String accion = "marchar";

        if (request.getParameter("action") != null)
            accion = request.getParameter("action");

        Object oReturn;
        ViajeBBean oViajeBBean;
        try {
            oViajeBBean = new ViajeBBean();
            oReturn = oViajeBBean.gestionVisita(Integer.parseInt(request.getParameter("clave")), mapping.getParameter(), accion, abadia, usuario, resource, mensajes);
            if (mapping.getParameter().equals("inicio")) {
                request.setAttribute("DatosConfirmacion", oReturn);
                return mapping.findForward("confirmar");
            } else if (mapping.getParameter().equals("confirmar")) {
                saveMessages(request.getSession(), mensajes);
            }
            return mapping.findForward("mensajes");
        } catch (ApplicationException e) {
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
    }
}

