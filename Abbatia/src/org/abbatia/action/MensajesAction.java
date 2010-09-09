package org.abbatia.action;

import org.abbatia.actionform.BuscarAbadiasActForm;
import org.abbatia.actionform.MensajeActForm;
import org.abbatia.bbean.CorreoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Correo;
import org.abbatia.exception.ExcedidoNumDestinatariosException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;

public class MensajesAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        MensajeActForm mensaje = (MensajeActForm) form;
        String correoid = request.getParameter("correoid");

        int iNumDestinatarios = 0;
        CorreoBBean oCorreoBBean;
        try {
            oCorreoBBean = new CorreoBBean();
            //si el id de correo está informado, debemos mostrar el correo seleccionado.
            if (correoid != null) {
                Correo correo = oCorreoBBean.recuperarCorreo(correoid, abadia);
                mensaje.setDestinatario(correo.getAbadiaDestino());
                mensaje.setDesde(correo.getAbadiaOrigen());
                mensaje.setIdAbadiaOrigen(correo.getIdAbadiaOrigen());
                mensaje.setMsg(correo.getTexto());
                mensaje.setTipo("consulta");
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("success");
            }
            //si el actionform no está informado, se trata de la primera invocación.
            if (mensaje.getAccion() != null && mensaje.getAccion().equals("inicio")) {
                //Tenemos que cargar la combo de regiones.
                mensaje = new MensajeActForm();
                BuscarAbadiasActForm datos = new BuscarAbadiasActForm();
                request.setAttribute("BuscarAbadiaForm", datos);
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("nuevo");
            } else if (mensaje.getAccion() != null && mensaje.getAccion().equals("validar")) {
                StringTokenizer st = new StringTokenizer(mensaje.getDestinatario(), ";");
                mensaje.setCoste(Utilidades.redondear(Constantes.VARIOS_COSTE_ENVIO_MENSAJE * st.countTokens()));
                mensaje.setCosteUnitario(String.valueOf(Constantes.VARIOS_COSTE_ENVIO_MENSAJE));
                mensaje.setAccion("inicio");
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("validar");
            } else if (mensaje.getAccion() != null && mensaje.getAccion().equals("contestar")) {
                //Tenemos que cargar la combo de regiones.
                mensaje.setDestinatario(mensaje.getDesde());
                mensaje.setDestinatarioid(String.valueOf(mensaje.getIdAbadiaOrigen()));
                mensaje.setIdAbadiaOrigen(abadia.getIdDeAbadia());
                mensaje.setDesde(abadia.getNombre());
                mensaje.setDireccion("salida");
                mensaje.setTipo("nuevo");
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("nuevo");

            } else if (mensaje.getAccion() != null && mensaje.getAccion().equals("aceptar")) {
                iNumDestinatarios = oCorreoBBean.aceptarEnvio(mensaje, abadia);
                //verificamos si la abbatia dispone de oro suficiente para enviar el mensaje.
                request.setAttribute("MensajeForm", mensaje);
                return mapping.findForward("outbox");
            }
            return super.execute(mapping, form, request, response);
        } catch (OroInsuficienteException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.oroinsuficiente", String.valueOf(iNumDestinatarios * Constantes.VARIOS_COSTE_ENVIO_MENSAJE)));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (ExcedidoNumDestinatariosException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.superado.limite.destinatarios", String.valueOf(Constantes.VARIOS_MAX_DESTINATARIOS)));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}