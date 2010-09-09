package org.abbatia.action;

import org.abbatia.ListenerAbadia;
import org.abbatia.bbean.InicioBBean;
import org.abbatia.bean.InicioMain;
import org.abbatia.core.CoreTiempo;
import org.abbatia.utils.HTML;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


public class InicioMainAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        MessageResources resource = getResources(request);

        HashMap hmRequest;
        InicioBBean oInicioBBean;
        Context initCtx;

        int pagina = 0, region = -1;

        String sParam = request.getParameter("param");
        if (sParam == null) sParam = "inicio";

        InicioMain oInicioMain;

        oInicioBBean = new InicioBBean();
        // Listar top users
        if (sParam.equals("topusers")) {
            String sPagina = request.getParameter("pagina");
            if (sPagina != null) pagina = Integer.parseInt(sPagina);
            String sRegion = request.getParameter("region");
            if (sRegion != null) region = Integer.parseInt(sRegion);

            oInicioMain = oInicioBBean.getTopUsuarios(pagina, region);

            HTML cHTML = new HTML();
            String navega = cHTML.getNavigateBar("index_main.do", "param=topusers&region=" + region, pagina, oInicioMain.getCountMejoresAbadias() / 100);
            //
            request.setAttribute("Navega", navega);
            request.setAttribute("DatosInicio", oInicioMain);
            return mapping.findForward("topusers");
        } else {
            //oInicioMain = new InicioMain();
            hmRequest = oInicioBBean.getDatosIniciales(sParam, resource);

            request.setAttribute("DatosInicio", hmRequest.get("DatosInicio"));
            request.setAttribute("Tiempo", CoreTiempo.getTiempoAbadiaStringConHorasObj(resource));
            request.setAttribute("UsuariosConectados", ListenerAbadia.getContador());
            request.setAttribute("UsuariosRegistrados", hmRequest.get("UsuariosRegistrados"));
            request.setAttribute("UsuariosActivos", hmRequest.get("UsuariosActivos"));
            request.setAttribute("AltasPendientes", hmRequest.get("AltasPendientes"));

            request.setAttribute("Banner125", hmRequest.get("Banner125"));
            request.setAttribute("Banner468", hmRequest.get("Banner468"));

/*
            //cargar el estado del planificador en la request
            initCtx = new InitialContext();
            request.setAttribute("planificador", initCtx.lookup(Constantes.ESTADO_PLANIFICADOR));
            initCtx.close();
*/

            return mapping.findForward("success");
        }


    }
}

