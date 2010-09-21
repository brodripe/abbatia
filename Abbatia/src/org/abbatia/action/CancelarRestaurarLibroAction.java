package org.abbatia.action;

import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class CancelarRestaurarLibroAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        ActionForward af;
        MessageResources resource = getResources(request);

        LibroBBean oLibroBBean;
        int iLibroId;
        String claveLibro = request.getParameter("claveLibro");

        try {
            //recuperamos el atributo recibido (clave del libro)
            //iLibroId = Integer.parseInt(request.getParameter("claveLibro"));
            oLibroBBean = new LibroBBean();
            if (!GenericValidator.isBlankOrNull(claveLibro)) {
                af = oLibroBBean.cancelarRestauracion(Integer.parseInt(claveLibro), usuario, abadia, mensajes, actionMapping);
                if (!mensajes.isEmpty()) {
                    saveMessages(request.getSession(), mensajes);
                }

            } else {
                af = oLibroBBean.cancelarRestauraciones(usuario, abadia, mensajes, actionMapping, resource);
                if (!mensajes.isEmpty()) {
                    saveMessages(request.getSession(), mensajes);
                }
            }
            return af;

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
