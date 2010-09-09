package org.abbatia.action;

import org.abbatia.core.CoreTiempo;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CabeceraAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MessageResources resource = getResources(request);
        request.setAttribute("Tiempo", CoreTiempo.getTiempoAbadiaStringConHorasObj(resource));
        return mapping.findForward("success");
    }
}
