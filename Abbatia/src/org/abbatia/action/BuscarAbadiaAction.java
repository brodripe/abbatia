package org.abbatia.action;


import org.abbatia.actionform.BuscarAbadiaActForm;
import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
// Abbatia


public class BuscarAbadiaAction extends Action {
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

        /*
            if (p_mapRequest.get("orden") != null)
                p_afDatosFiltro.setOrden(Integer.parseInt((String) p_mapRequest.get("orden")));
            if (p_mapRequest.get("ordenid") != null)
                p_afDatosFiltro.setOrdenid(Integer.parseInt((String) p_mapRequest.get("ordenid")));
            if (p_mapRequest.get("pagina") != null)
                p_afDatosFiltro.setPagina(Integer.parseInt((String) p_mapRequest.get("pagina")));
            if (p_mapRequest.get("regionid") != null)
                p_afDatosFiltro.setRegion(Integer.parseInt((String) p_mapRequest.get("regionid")));

         */

        //Map<String,String> mapRequest = new HashMap<String,String>();
        HashMap hmRequest;
        AbadiaBBean oAbadiaBBean;

        /*
        mapRequest.put("accion", request.getParameter("accion"));
        mapRequest.put("ordenid", request.getParameter("ordenid"));
        mapRequest.put("pagina", request.getParameter("pagina"));
        mapRequest.put("region", request.getParameter("region"));
        */
        oAbadiaBBean = new AbadiaBBean();
        hmRequest = oAbadiaBBean.buscarAbadia(datosFiltro, abadia, usuario, resource);
        // opciones por parametro
        // Cargar regiones
        if (datosFiltro.getRegion() != -1) {
            request.setAttribute("regiones", hmRequest.get("regiones"));
        }

        if (datosFiltro.getBusqueda() == 1) {
            if (hmRequest.containsKey("forward"))
                return (ActionForward) hmRequest.get("forward");
        }
        // mostrar las abadias de la region
        request.setAttribute("Navega", hmRequest.get("Navega"));

        return mapping.findForward("success");

    }
}

