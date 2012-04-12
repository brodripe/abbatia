package org.abbatia.action;

import org.abbatia.actionform.LibroDetalleActForm;
import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EstadoLibroIncorrectoException;
import org.abbatia.exception.LibroNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesmontarLibroAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        LibroDetalleActForm libro = (LibroDetalleActForm) form;
        String idLibro = request.getParameter("clave");

        int iPergaminosSuciosObtenidos;

        ActionForward af = null;
        LibroBBean oLibroBBean;

        try {
            oLibroBBean = new LibroBBean();
            //recuperamos el objeto libro
            iPergaminosSuciosObtenidos = oLibroBBean.desmontarLibro(abadia, Integer.parseInt(idLibro));
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.desmontar.resultado", iPergaminosSuciosObtenidos));
            saveMessages(request.getSession(), mensajes);

        } catch (EstadoLibroIncorrectoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.estado.incorrecto"));
            saveErrors(request.getSession(), mensajes);

        } catch (LibroNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.desmontar.abadiaincorrecta"));
            saveErrors(request.getSession(), mensajes);
        }

        return mapping.findForward("mensajes");
    }
}