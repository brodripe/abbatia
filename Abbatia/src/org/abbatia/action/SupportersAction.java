package org.abbatia.action;

import org.abbatia.bbean.UsuarioBBean;
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
import java.util.ArrayList;

public class SupportersAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {

        MessageResources resource = getResources(request);

        int pagina = 0;
        Point total = new Point(0, 0);  // truco para utilizar variables por referencia ;-)


        String sPagina = request.getParameter("pagina");
        if (sPagina != null) pagina = Integer.parseInt(sPagina);

        // Lista de trabajos
        ArrayList alTable1;
        UsuarioBBean oUsuarioBBean;

        oUsuarioBBean = new UsuarioBBean();
        alTable1 = oUsuarioBBean.recuperarSupporters(pagina, total, resource);
        HTML cHTML = new HTML();
        String navega = cHTML.getNavigateBar("supporters.do", "", pagina, total.x / Constantes.REGISTROS_PAGINA);
        request.getSession().setAttribute("supporters", alTable1);
        request.setAttribute("Navega", navega);


        return mapping.findForward("success");
    }
}


