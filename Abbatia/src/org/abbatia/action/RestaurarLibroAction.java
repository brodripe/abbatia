package org.abbatia.action;

import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.RestauracionLibroException;
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
public class RestaurarLibroAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        //recuperamos el atributo recibido (clave del libro)
        String claveLibro = request.getParameter("claveLibro");

        //recuperamos el objeto libro
        LibroBBean oLibroBBean;
        try {

            oLibroBBean = new LibroBBean();
            if (!GenericValidator.isBlankOrNull(claveLibro)) {
                return oLibroBBean.restaurarLibro(Integer.parseInt(claveLibro), abadia, usuario, mensajes);
            } else {
                return oLibroBBean.restaurarLibros(abadia, usuario, resource);
            }

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        } catch (RestauracionLibroException e) {
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward(Constantes.MENSAJES);
        }
    }
}
