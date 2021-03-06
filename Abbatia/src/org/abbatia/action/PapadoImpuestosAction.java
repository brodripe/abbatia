package org.abbatia.action;


import org.abbatia.actionform.ObispadoImpuestosActForm;
import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Comisiones;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
// Abbatia


public class PapadoImpuestosAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping             The ActionMapping used to select this instance.
     * @param actionForm          The optional ActionForm bean for this request.
     * @param request             The HTTP Request we are processing.
     * @param httpServletResponse The HTTP Response we are processing.
     * @return ActionForward
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        MessageResources resource = getResources(request);

        int pagina = 0, orden = 2, ordenid = 2, tab = 0;
        Point total = new Point(0, 0);  // truco para utilizar variables por referencia ;-)

        String sPagina = request.getParameter("pagina");
        if (sPagina != null) pagina = Integer.parseInt(sPagina);
        if (request.getParameter("orden") != null) orden = Integer.parseInt(request.getParameter("orden"));
        if (request.getParameter("ordenid") != null) ordenid = Integer.parseInt(request.getParameter("ordenid"));
        if (request.getParameter("tab") != null) tab = Integer.parseInt(request.getParameter("tab"));

        ObispadoImpuestosActForm imps = (ObispadoImpuestosActForm) actionForm;

        JerarquiaBBean oJerarquiaBBean;
        oJerarquiaBBean = new JerarquiaBBean();
        Comisiones coms = oJerarquiaBBean.gestionImpuestosPapado(imps, tab, abadia, pagina, orden, ordenid, total, resource);

        HTML cHTML = new HTML();
        String navega = cHTML.getNavigateBar("papadoImpuestos.do", "orden=" + orden + "&ordenid=" + ordenid, pagina, total.x / Constantes.REGISTROS_PAGINA);
        request.setAttribute("Navega", navega);
        request.setAttribute("Tab", String.valueOf(tab));
        //
        imps.setTotal(Utilidades.redondear(coms.getTotal()));
        imps.setPapado(coms.getPapado());
        imps.setCardenales(coms.getCardenales());

        return mapping.findForward("success");
    }
}
