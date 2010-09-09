package org.abbatia.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class IndiceAction extends Action {
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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sID = "";
        if (request.getLocale() != null) {
            sID = request.getLocale().getLanguage();
        }
        if (sID.equals("es")) {
            setLocale(request, new Locale("es", "ES"));
        } else if (sID.equals("ca")) {
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
        } else if (sID.equals("en")) {
            setLocale(request, new Locale("en", "US"));
        }
        return mapping.findForward("success");
    }
}