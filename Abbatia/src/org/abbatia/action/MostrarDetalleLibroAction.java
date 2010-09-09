package org.abbatia.action;

import org.abbatia.actionform.LibroDetalleActForm;
import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 05-may-2005
 * Time: 23:33:49
 */
public class MostrarDetalleLibroAction extends Action {
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        LibroDetalleActForm libroDetalle;

        LibroBBean oLibroBBean;
        String idLibro = request.getParameter("clave");

        if (GenericValidator.isBlankOrNull(idLibro)) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.sinlibro"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }

        oLibroBBean = new LibroBBean();
        //recuperamos los detalles del libro base
        libroDetalle = oLibroBBean.recuperarDetalleLibroConCopias(idLibro, usuario, resource);
        //recuperamos los detalles de las copias del libro base en curso
        if (libroDetalle == null) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libronoencontrado"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }

        request.setAttribute("libroDetalle", libroDetalle);

        return actionMapping.findForward("success");
    }
}
