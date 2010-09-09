package org.abbatia.action;

import org.abbatia.actionform.MensajeActForm;
import org.abbatia.bbean.CorreoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Correo;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.AccesoDenegadoException;
import org.abbatia.exception.CorreoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MensajesEnviadosAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        MensajeActForm mensaje = (MensajeActForm) form;
        String correoid = request.getParameter("correoid");

        CorreoBBean oCorreoBBean;

        try {
            oCorreoBBean = new CorreoBBean();

            if (mapping.getParameter() != null && mapping.getParameter().equals("borrar")) {
                oCorreoBBean.eliminarEnviados(mensaje, abadia);
                request.setAttribute("MensajeForm", mensaje);

                return mapping.findForward("outbox");
            }
            mensaje.setAccion("inicio");
            //si el id de correo está informado, debemos mostrar el correo seleccionado.
            if (correoid != null) {
                oCorreoBBean.mostrarEnviado(correoid, mensaje, abadia);
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("mostrar");
            }

            Correo correo = new Correo();
            correo.setIdAbadiaOrigen(abadia.getIdDeAbadia());

            correo.setFecha_abadia(CoreTiempo.getTiempoAbadiaString());
            oCorreoBBean.recuperarEnviados(mensaje, abadia);
            request.setAttribute("MensajeForm", mensaje);

            return mapping.findForward("outbox");

        } catch (AccesoDenegadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.tramposo.accesocorreo"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (CorreoNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.noencontrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");

        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}