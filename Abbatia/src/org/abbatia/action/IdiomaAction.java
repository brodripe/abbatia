package org.abbatia.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class IdiomaAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sID = request.getParameter("id");
        if (sID.equals("es")) {
            setLocale(request, new Locale("es", "ES"));
            //request.getSession().setAttribute(Globals.LOCALE_KEY, new Locale("es", "ES"));
        } else if (sID.equals("ct")) {
            setLocale(request, new Locale("ca", "ES"));
        } else if (sID.equals("gl")) {
            setLocale(request, new Locale("gl", "ES"));
        } else if (sID.equals("ek")) {
            setLocale(request, new Locale("ek", "ES"));
        } else if (sID.equals("lt")) {
            setLocale(request, new Locale("lt", "IT"));
        } else if (sID.equals("de")) {
            setLocale(request, new Locale("de", "DE"));
        } else if (sID.equals("fr")) {
            setLocale(request, new Locale("fr", "fr"));
        } else if (sID.equals("it")) {
            setLocale(request, new Locale("it", "IT"));
        } else if (sID.equals("pt")) {
            setLocale(request, new Locale("pt", "PT"));
        } else if (sID.equals("us")) {
            setLocale(request, new Locale("en", "US"));
            //request.getSession().setAttribute(Globals.LOCALE_KEY, new Locale("en", "US"));
        }
        return mapping.findForward("desconectar");
    }
}
