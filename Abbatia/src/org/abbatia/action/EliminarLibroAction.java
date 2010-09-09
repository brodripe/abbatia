package org.abbatia.action;

import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class EliminarLibroAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        ActionForward af;

        LibroBBean oLibroBBean;

        //recuperamos el atributo recibido (clave del libro)
        String claveLibro = request.getParameter("clave");

        try {
            oLibroBBean = new LibroBBean();
            af = oLibroBBean.eliminarLibro(Integer.parseInt(claveLibro), abadia, usuario, mensajes, actionMapping);
            if (!mensajes.isEmpty()) {
                saveMessages(request, mensajes);
            }
            return af;

        } catch (java.lang.NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
