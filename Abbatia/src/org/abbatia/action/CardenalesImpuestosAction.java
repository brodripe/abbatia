package org.abbatia.action;


import org.abbatia.actionform.ObispadoImpuestosActForm;
import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
// Abbatia


public class CardenalesImpuestosAction extends Action {

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

        oJerarquiaBBean.recuperarImpuestosCardenal(imps, tab, pagina, orden, ordenid, total, resource, abadia);

        HTML cHTML = new HTML();
        String navega = cHTML.getNavigateBar("cardenalesImpuestos.do", "orden=" + orden + "&ordenid=" + ordenid, pagina, total.x / Constantes.REGISTROS_PAGINA);
        request.setAttribute("Navega", navega);
        request.setAttribute("Tab", String.valueOf(tab));
        //
        return mapping.findForward("success");
    }
}
