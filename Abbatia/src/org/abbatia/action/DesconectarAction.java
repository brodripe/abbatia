package org.abbatia.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesconectarAction extends Action {
    private static Logger log = Logger.getLogger(LoginAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO:  Override this org.apache.struts.action.Action method
        log.debug("Desactivar la sesión de usuario.");
        request.getSession().invalidate();
        return mapping.findForward("desconectar");
    }
}
