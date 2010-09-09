package org.abbatia.action;

import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.AbadiaSinObispoException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class ListaEdificiosAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {

        //  Dado que en este punto, el usuario debe estar registrado...
        //  nos limitaremos a recoger de la sesión el objeto abbatia y leeremos
        //  de él la lista de monjes.
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        ArrayList edificios;
        HashMap<String, String> hmRequest = new HashMap<String, String>();

        EdificioBBean oEdificioBBean;

        String sAbadiaIdVisualizar = request.getParameter("abadiaid_obispado");
        try {
            oEdificioBBean = new EdificioBBean();
            // Mostrar los edificios de otro usuario si eres obispo
            if (sAbadiaIdVisualizar != null) {
                edificios = oEdificioBBean.recuperarListadoEdificoObispo(Integer.parseInt(sAbadiaIdVisualizar), abadia, usuario, resource, hmRequest);
                // Comprobar que realmente tenemos un obispo
                request.setAttribute("Nombre", hmRequest.get("Nombre"));
                request.setAttribute("Links", "0");
            } else {
                edificios = oEdificioBBean.recuperarListadoEdificios(abadia, usuario, resource, hmRequest);
                request.setAttribute("Links", "1");
            }

            request.setAttribute("Nieve", hmRequest.get("Nieve"));
            // De noche?
            if (CoreTiempo.esNoche()) {
                request.setAttribute("Noche", "1");
            } else {
                request.setAttribute("Noche", "0");
            }

            //meto el arraylist de datos ListaMonjesActForm en el objeto request
            request.setAttribute("Edificios", edificios);

            //devuelvo un success indicando que todo ha ido ok.
            return mapping.findForward("success");

        } catch (AbadiaSinObispoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.sinacceso"));
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
