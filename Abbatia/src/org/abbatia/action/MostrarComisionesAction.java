package org.abbatia.action;

import org.abbatia.actionform.BuscarAbadiaActForm;
import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// Abbatia


public class MostrarComisionesAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping             The ActionMapping used to select this instance.
     * @param actionForm          The optional ActionForm bean for this request.
     * @param request             The HTTP Request we are processing.
     * @param httpServletResponse The HTTP Response we are processing.
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        BuscarAbadiaActForm datosFiltro = (BuscarAbadiaActForm) actionForm;

        JerarquiaBBean oJerarquiaBBean;

        // opciones por parametro
        if (request.getParameter("opcion") != null)
            datosFiltro.setOpcion(Integer.parseInt(request.getParameter("opcion")));
        if (request.getParameter("orden") != null)
            datosFiltro.setOrden(Integer.parseInt(request.getParameter("orden")));
        if (request.getParameter("ordenid") != null)
            datosFiltro.setOrdenid(Integer.parseInt(request.getParameter("ordenid")));
        if (request.getParameter("pagina") != null)
            datosFiltro.setPagina(Integer.parseInt(request.getParameter("pagina")));

        oJerarquiaBBean = new JerarquiaBBean();
        datosFiltro.setContents(oJerarquiaBBean.recuperarComisiones(abadia, usuario, datosFiltro, resource));

        HTML cHTML = new HTML();
        String navega = cHTML.getNavigateBar("mostrarComisiones.do", "opcion=" + datosFiltro.getOpcion() + "&orden=" + datosFiltro.getOrden() + "&ordenid=" + datosFiltro.getOrdenid(),
                datosFiltro.getPagina(), datosFiltro.getTotal() / Constantes.REGISTROS_PAGINA);
        request.setAttribute("Navega", navega);

        return mapping.findForward("success");
    }
}

