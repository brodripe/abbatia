package org.abbatia.action;

import org.abbatia.actionform.DatosCopiaLibroActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.NoExisteSiguienteNivelException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class MostrarMonjesParaSubirNivelCopiaAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        DatosCopiaLibroActForm data = (DatosCopiaLibroActForm) actionForm;

        //InfoViajeCopia infoViajeCopia = null;
        //recuperamos el atributo recibido (clave del libro)
        String claveLibro = request.getParameter("clave");

        HashMap hmRequest;
        MonjeBBean oMonjeBBean;
        try {
            oMonjeBBean = new MonjeBBean();
            //si se trata del primer acceso
            if (data == null || data.getAccion().equals("inicio") || claveLibro != null) {
                oMonjeBBean.recuperarMonjesParaSubirNivel(claveLibro, data, usuario, abadia, resource);
                request.setAttribute("datosCopiaLibro", data);
                return actionMapping.findForward("mostrarmonjes");
                //si ya se ha seleccionado un monje
            } else if (data.getAccion().equals("seleccionado")) {
                data.setIdMonje(data.getSeleccion());
                hmRequest = oMonjeBBean.seleccionMonjesParaSubirNivel(data, usuario, abadia, resource, mensajes);

                if (hmRequest.get("ActionForward") != null) {
                    request.getSession().removeAttribute("datosCopiaLibro");
                    return (ActionForward) hmRequest.get("ActionForward");
                } else {
                    //se se han añadido mensajes
                    //recargamos la página de lista de monjes para copiar con los mensajes corresondientes.
                    saveMessages(request.getSession(), mensajes);
                    request.setAttribute("datosCopiaLibro", data);
                    return actionMapping.findForward("mostrarmonjes");
                }
            }
            return actionMapping.findForward("mostrarmonjes");
        } catch (NoExisteSiguienteNivelException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.nivel.noexiste"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
