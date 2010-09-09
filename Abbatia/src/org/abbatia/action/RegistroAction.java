package org.abbatia.action;

import org.abbatia.actionform.RegistroActForm;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Email;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Table;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.*;
import org.abbatia.utils.AbadiaConfiguracion;
import org.abbatia.core.CoreMail;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class RegistroAction extends Action {

    private static Logger log = Logger.getLogger(RegistroAction.class.getName());
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionMessages mensajes = new ActionMessages();
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();
        MessageResources resource = getResources(request);
        Usuario usuario;
        UsuarioBBean oUsuarioBBean;
        HashMap<String, ArrayList<Table>> hmRequest;

        try {

            oUsuarioBBean = new UsuarioBBean();
            if (request.getParameter("accion") == null) {
                hmRequest = oUsuarioBBean.registroUsuario();

                request.getSession().setAttribute("languages", hmRequest.get("languages"));
                request.getSession().setAttribute("paises", hmRequest.get("paises"));
                request.getSession().setAttribute("edades", hmRequest.get("edades"));
                request.getSession().setAttribute("sexo", hmRequest.get("sexo"));

                return mapping.findForward("pantallaregistro");
            }

            RegistroActForm usuariof = (RegistroActForm) form;

            if (!usuariof.isAceptaNormas())
            {
                throw new AceptacionNormasException("Es obligatorio aceptar las normas del juego", log);
            }
            usuario = oUsuarioBBean.registroUsuarioFin(usuariof, request.getRemoteAddr());
            //si el usuario tiene 14 años o menos, enviamos un correo al usuario indicando que debe enviarnos autorización.
            if (usuario.getEdad() <= 1)
            {
                //mandamos correo.
                // en este punto, gestionaremos el envio de un correo electrónico y redirigimos a una pantalla que lo notifique.
                Email email = new Email();
                email.setTo(usuario.getEmail());
                email.setAsunto(resource.getMessage("solicitud.autorizacion.alta.asunto"));
                email.setMsg(resource.getMessage("solicitud.autorizacion.alta.body"));
                email.setFilename(AbadiaConfiguracion.getBasePath() + AbadiaConfiguracion.getPropiedad("ruta.fichero.autorizacion"));
                //email.setFilename(AbadiaConfiguracion.getBasePath() + "/ayudas/docs/autorizacionmenores.pdf");
                CoreMail.enviarEmail(email);
            }

            notas.add(new Notificacion("/Continuar.do", "Inicio"));
            request.getSession().setAttribute("notificacion", notas);

            mensajes.add("msg", new ActionMessage("mensajes.info.registro.notificacion"));
            mensajes.add("msg", new ActionMessage("mensajes.info.registro.notificacion2"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (CorreoNoEnviadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.correonoenviado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (YaExisteMailException yece) {
            //request.getSession().setAttribute("languages", new adUtils().getTable(Constantes.TABLA_IDIOMA));
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.email.repetido"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (YaExisteAliasException yeae) {
            //request.getSession().setAttribute("languages", new adUtils().getTable(Constantes.TABLA_IDIOMA));
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.nick.repetido"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (AbadiaSQLException ex) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (AceptacionNormasException ex) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("usuario.condiciones.mandatory"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (AbadiaDBConnectionException adbce) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (java.lang.SecurityException adbce) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("mensajes.info.correonoenviado"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

    }
}
