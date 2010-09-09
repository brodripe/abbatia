package org.abbatia.action;

import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MultiClickAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionMessages mensajes = new ActionMessages();
        mensajes.add("msg", new ActionMessage("principal.proceso.inacabado"));
        saveMessages(request.getSession(), mensajes);
        return mapping.findForward(Constantes.MENSAJES);
    }
}
