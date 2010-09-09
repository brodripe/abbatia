package org.abbatia.action;

import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/*
     Página de Inicio del juego, llama adInicioContents y Inicio contents
*/

public class MainAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        AbadiaBBean oAbadiaBBean;
        HashMap hmRequest;

        String sMens = request.getParameter("listar_mensajes");
        if (sMens == null) sMens = "0";
        String sBorr = request.getParameter("borrar_mensajes");
        if (sBorr == null) sBorr = "0";

        oAbadiaBBean = new AbadiaBBean();
        hmRequest = oAbadiaBBean.cargaInicialMain(sBorr, sMens, abadia, usuario, resource);

        request.setAttribute("Congelada", String.valueOf(usuario.getCongelado()));
        request.setAttribute("DatosContents", hmRequest.get("DatosContents"));
        request.setAttribute("Tiempo", CoreTiempo.getTiempoAbadiaStringConHorasObj(resource));

        request.setAttribute("Elecciones", hmRequest.get("Elecciones"));

        // Cónclave

        if (hmRequest.get("Fumata") != null) {
            request.setAttribute("Fumata", hmRequest.get("Fumata"));
        }
        request.setAttribute("Conclave", hmRequest.get("Conclave"));

        return mapping.findForward("success");
    }
}
