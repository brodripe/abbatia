package org.abbatia.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PublicidadAction extends Action {
    private static Logger log = Logger.getLogger(PublicidadAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
        String sid = request.getParameter("id");
        if (sid != null)
        {
          adPublicidad pubs = new adPublicidad();
          Publicidad banner = pubs.recuperar(Integer.parseInt(sid));
          pubs.finalize();

          request.setAttribute("url", new String(banner.getUrl()));

          return mapping.findForward("redirect");
        } else
        */
        return mapping.findForward("info");
    }
}
