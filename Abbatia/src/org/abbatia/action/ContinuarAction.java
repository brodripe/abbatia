package org.abbatia.action;

import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContinuarAction extends Action {

    private static Logger log = Logger.getLogger(ContinuarAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);

        try {
            //adAbadia.actualizarFechaEliminacion(abbatia.getIdDeAbadia(), null);
            return mapping.findForward("principal");

        } catch (Exception e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
    }
}
