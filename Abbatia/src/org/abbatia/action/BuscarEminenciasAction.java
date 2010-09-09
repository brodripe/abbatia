package org.abbatia.action;


import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
// Abbatia


public class BuscarEminenciasAction extends Action {

    private static Logger log = Logger.getLogger(BuscarEminenciasAction.class.getName());

    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping    The ActionMapping used to select this instance.
     * @param actionForm The optional ActionForm bean for this request.
     * @param request    The HTTP Request we are processing.
     * @param response   The HTTP Response we are processing.
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        JerarquiaBBean oJerarquiaBBean;

        hmRequest.put("opcion", request.getParameter("opcion"));
        hmRequest.put("monjeid", request.getParameter("monjeid"));
        hmRequest.put("clave", request.getParameter("clave")); //sicarioid

        oJerarquiaBBean = new JerarquiaBBean();
        oJerarquiaBBean.buscarEminencias(hmRequest, resource, usuario);


        request.setAttribute("Titulo", hmRequest.get("Titulo"));
        request.setAttribute("Contents", hmRequest.get("Contents"));
        request.setAttribute("SicarioId", request.getParameter("clave"));

        return mapping.findForward("success");
    }
}

