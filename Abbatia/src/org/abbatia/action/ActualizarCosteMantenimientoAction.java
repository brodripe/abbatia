package org.abbatia.action;

import net.sf.json.JSONObject;
import org.abbatia.bbean.EdificioBBean;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActualizarCosteMantenimientoAction extends Action {
    private static Logger log = Logger.getLogger(RegistroAction.class.getName());

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it),
     * with provision for handling exceptions thrown by the business logic.
     * Return an {@link org.apache.struts.action.ActionForward} instance describing where and how
     * control should be forwarded, or <code>null</code> if the response
     * has already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     * @since Struts 1.1
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String szTipoEdificioId = request.getParameter("tipoEdificioId");
        String szNivel = request.getParameter("nivelId");
        EdificioBBean oEdificioBBean;
        int iCosteMantenimiento;

        try {
            oEdificioBBean = new EdificioBBean();
            iCosteMantenimiento = oEdificioBBean.recuperarCosteMantenimiento(szTipoEdificioId, Short.valueOf(szNivel));

            //Do your database work here to grab the name and populate our HashMap hm
            //In this example I'll just hard-code the name though.

            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            hm.put("coste", iCosteMantenimiento);

            //each key from our hash map becomes a key in our JSON object
            JSONObject json = JSONObject.fromObject(hm);

            //Plop it in the header so prototype can grab it.
            response.setHeader("X-JSON", json.toString());

            return mapping.findForward("success");

        } catch (Exception e) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("executeError", "Couldn't find the Full Name because an error occured.");

            JSONObject json = JSONObject.fromObject(hm);

            response.setHeader("X-JSON", json.toString());
            return mapping.findForward("success");

        }
    }
}