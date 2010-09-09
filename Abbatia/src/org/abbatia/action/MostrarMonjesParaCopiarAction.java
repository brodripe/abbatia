package org.abbatia.action;

import org.abbatia.actionform.DatosCopiaLibroActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.LibroIncompletoException;
import org.abbatia.exception.LibroSaturadoException;
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
public class MostrarMonjesParaCopiarAction extends Action {
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
        HashMap hmRequest;
        MonjeBBean oMonjeBBean;

        try {
            oMonjeBBean = new MonjeBBean();
            //si se trata del primer acceso
            if (data == null || data.getAccion().equals("inicio") || claveLibro != null) {
                oMonjeBBean.recuperarMonjesParaCopia(claveLibro, data, usuario, abadia, resource);

                request.setAttribute("datosCopiaLibro", data);
                return actionMapping.findForward("mostrarmonjes");

                //si ya se ha seleccionado un monje
            } else if (data.getAccion().equals("seleccionado")) {
                data.setIdMonje(data.getSeleccion());
                hmRequest = oMonjeBBean.seleccionMonjesParaCopia(data, usuario, abadia, resource, mensajes);

                if (hmRequest.get("ActionForward") != null) {
                    request.getSession().removeAttribute("datosCopiaLibro");
                    return (ActionForward) hmRequest.get("ActionForward");
                }


                if (mensajes.isEmpty()) {
                    request.getSession().setAttribute(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA, hmRequest.get(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA));
                    request.setAttribute("msgCaballo", hmRequest.get("msgCaballo"));
                    request.setAttribute("Caballo", hmRequest.get("Caballo"));
                    request.setAttribute("ViajarForm", hmRequest.get("ViajarForm"));

                    return actionMapping.findForward("datosviaje");
                } else {
                    //se se han añadido mensajes
                    //recargamos la página de lista de monjes para copiar con los mensajes corresondientes.
                    saveMessages(request.getSession(), mensajes);
                    request.setAttribute("datosCopiaLibro", data);
                    return actionMapping.findForward("mostrarmonjes");
                }
            } else {
                return actionMapping.findForward("mostrarmonjes");
            }
        } catch (LibroIncompletoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.librosincompleto"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        } catch (LibroSaturadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.librosaturado"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
