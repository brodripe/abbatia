package org.abbatia.action;

import org.abbatia.actionform.MostrarMonjeActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.MonjeNoVisualizableException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
// Abbatia


public class EvaluarDietaAction extends Action {
    private static Logger log = Logger.getLogger(EvaluarDietaAction.class.getName());

    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return ActionForward
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        MostrarMonjeActForm monjeActForm = (MostrarMonjeActForm) form;

        MonjeBBean oMonjeBBean;
        //creamos los objetos ad
        HashMap hmRequest;

        try {
            oMonjeBBean = new MonjeBBean();
            hmRequest = oMonjeBBean.evaluarDieta(monjeActForm, abadia, usuario, resource);

            request.getSession().setAttribute("hayabad", hmRequest.get("hayabad"));

            request.setAttribute("Visita_miabadia", hmRequest.get("Visita_miabadia"));
            request.setAttribute("Monje", hmRequest.get("Monje"));

            request.getSession().setAttribute("alifam", hmRequest.get("alifam"));

            // Lista de actividades
            request.getSession().setAttribute("actividades", hmRequest.get("actividades"));

            request.setAttribute("MonjeForm", monjeActForm);
            request.setAttribute("evaluar", "1");

            return mapping.findForward("success");


        } catch (NumberFormatException e) {
            log.error("EvaluarDietaAction. NumberFormatException", e);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (MonjeNoVisualizableException e) {
            return mapping.findForward("failure");
        }


    }
}
