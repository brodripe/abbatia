package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ListarMonjesAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        MessageResources resource = getResources(request);
        MonjeBBean oMonjeBBean;
        HashMap hmRequest;

        oMonjeBBean = new MonjeBBean();
        hmRequest = oMonjeBBean.listarMonjes(abadia, resource);

        request.setAttribute("Monjes", hmRequest.get("Monjes"));
        request.setAttribute("Monjes_enfermos", hmRequest.get("Monjes_enfermos"));
        request.setAttribute("Monjes_viaje", hmRequest.get("Monjes_viaje"));
        request.setAttribute("Monjes_visita", hmRequest.get("Monjes_visita"));
        request.setAttribute("Monjes_visita_miabadia", hmRequest.get("Monjes_visita_miabadia"));
        request.setAttribute("Monjes_visita_enfermos", hmRequest.get("Monjes_visita_enfermos"));

        //devuelvo un success indicando que todo ha ido ok.
        return mapping.findForward("success");

    }
}
