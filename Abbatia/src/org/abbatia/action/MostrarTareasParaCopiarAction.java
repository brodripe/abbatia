package org.abbatia.action;

import org.abbatia.actionform.DatosCopiaLibroActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.LibroSaturadoException;
import org.abbatia.utils.Constantes;
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
public class MostrarTareasParaCopiarAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        DatosCopiaLibroActForm data = (DatosCopiaLibroActForm) actionForm;

        //recuperamos el atributo recibido (clave del libro)
        String claveLibro = request.getParameter("clave");
        MonjeBBean oMonjeBBean;
        try {
            oMonjeBBean = new MonjeBBean();
            oMonjeBBean.recuperarTareasCopia(claveLibro, data, abadia, usuario, resource);
            request.setAttribute("datosCopiaLibro", data);
            return actionMapping.findForward("mostrarmonjes");
        } catch (LibroSaturadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.librosaturado"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
