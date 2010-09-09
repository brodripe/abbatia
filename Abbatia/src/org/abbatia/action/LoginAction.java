package org.abbatia.action;

import org.abbatia.actionform.LoginForm;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LoginAction extends Action {
    private static Logger log = Logger.getLogger(LoginAction.class.getName());

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     * @deprecated Use the <code>execute()</code> method instead
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = ((LoginForm) form).getUsuario();
        String password = ((LoginForm) form).getPwd();

        ActionMessages errors = new ActionMessages();
        ActionMessages mensajes = new ActionMessages();
        HttpSession session = request.getSession();
        Usuario usuario = null;
        Abadia abadia;

        ArrayList<Notificacion> alNotas = new ArrayList<Notificacion>();

        MessageResources resource = getResources(request);

        UsuarioBBean oUsuarioBBean = new UsuarioBBean();
        HashMap hmReturn;

        try {

            hmReturn = oUsuarioBBean.login(username, password, request.getRemoteAddr(), resource);
            usuario = (Usuario) hmReturn.get(Constantes.USER_KEY);
            abadia = (Abadia) hmReturn.get(Constantes.ABADIA);
            //si el usuario aun no tiene abbatia, continuamos con el registro.
            if (usuario.getIdDeIdioma() == Constantes.IDIOMA_CASTELLANO) {
                setLocale(request, new Locale("es", "ES"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_CATALAN) {
                setLocale(request, new Locale("ca", "ES"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_GALLEGO) {
                setLocale(request, new Locale("gl", "ES"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_EUSKERA) {
                setLocale(request, new Locale("ek", "ES"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_INGLES) {
                setLocale(request, new Locale("en", "US"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_FRANCES) {
                setLocale(request, new Locale("fr", "FR"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_ALEMAN) {
                setLocale(request, new Locale("de", "DE"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_ITALIANO) {
                setLocale(request, new Locale("it", "IT"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_PORTUGUES) {
                setLocale(request, new Locale("pt", "PT"));
            } else if (usuario.getIdDeIdioma() == Constantes.IDIOMA_LATIN) {
                setLocale(request, new Locale("lt", "IT"));
            }

            session.setAttribute(Constantes.USER_KEY, usuario);
            session.setAttribute(Constantes.ABADIA, abadia);

            //si el usuario tiene asociada una abadía verificamos que no esté marcada para borrado....
            if (abadia.getFechaEliminacion() != null) {
                int iDias = CoreTiempo.getDiferenciaDiasInt(abadia.getFechaEliminacion(), CoreTiempo.getTiempoRealString());
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.eliminacion.abadia.aviso", String.valueOf(Constantes.VARIOS_DIAS_ELIMINACION - iDias)));
                alNotas.add(new Notificacion("/CancelarEliminacion.do", resource.getMessage("mensajes.eliminacion.abadia.cancelar")));
                request.setAttribute("notificacion", alNotas);
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward(Constantes.MENSAJES);
            }

            //verificar si la contraseña es "potente"
            try {
                Utilidades.check(usuario.getContrasena());
                if (!usuario.isAceptaNormas())
                {
                    return mapping.findForward("aceptacionNormas");
                }
            } catch (pwdSinNumerosException e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura"));
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura.sinnumeros"));
                alNotas.add(new Notificacion("/getUserInfo.do", resource.getMessage("mensajes.pwd.modificar")));
                alNotas.add(new Notificacion("/Continuar.do", resource.getMessage("mensajes.pwd.continuar")));
                request.setAttribute("notificacion", alNotas);
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward(Constantes.MENSAJES);
            } catch (pwdSinMayusculasException e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura"));
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura.sinmayusculas"));
                alNotas.add(new Notificacion("/getUserInfo.do", resource.getMessage("mensajes.pwd.modificar")));
                alNotas.add(new Notificacion("/Continuar.do", resource.getMessage("mensajes.pwd.continuar")));
                request.setAttribute("notificacion", alNotas);
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward(Constantes.MENSAJES);
            } catch (pwdSinMinusculasException e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura"));
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura.sinminusculas"));
                alNotas.add(new Notificacion("/getUserInfo.do", resource.getMessage("mensajes.pwd.modificar")));
                alNotas.add(new Notificacion("/Continuar.do", resource.getMessage("mensajes.pwd.continuar")));
                request.setAttribute("notificacion", alNotas);
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward(Constantes.MENSAJES);
            } catch (pwdCortaException e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura"));
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.pwd.pocosegura.corta"));
                alNotas.add(new Notificacion("/getUserInfo.do", resource.getMessage("mensajes.pwd.modificar")));
                alNotas.add(new Notificacion("/Continuar.do", resource.getMessage("mensajes.pwd.continuar")));
                request.setAttribute("notificacion", alNotas);
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward(Constantes.MENSAJES);
            }

            // Return success
            return mapping.findForward(Constantes.WELCOME);

        } catch (UsuarioBloqueadoException e) {
            errors.add("usuario", new ActionMessage("error.login.bloqueado"));
            saveMessages(request, errors);
            // return to input page
            return mapping.findForward(Constantes.MENSAJES);

        } catch (AbadiaNotFoundException e) {
            //En este caso debemos redirigir a un action que se encargue de la carga de las tablas en lugar de hacerlo aquí.
            request.getSession().setAttribute(Constantes.USER_KEY, e.getUsuario());
            HashMap hmRequest = oUsuarioBBean.cargarTablasRegistro();
            request.setAttribute("regiones", hmRequest.get("regiones"));
            request.setAttribute("ordenes", hmRequest.get("ordenes"));
            request.setAttribute("actividades", hmRequest.get("actividades"));
            return mapping.findForward("registroabadia");
        } catch (ValidacionIncorrectaException vie) {
            errors.add("usuario", new ActionMessage("error.login.invalid"));
            if (usuario != null && usuario.getBloqueado() == 1) {
                errors.add("usuario", new ActionMessage("error.login.bloqueado"));
                saveErrors(request, errors);
                return mapping.findForward("failure");
            }
            int reintentos = oUsuarioBBean.gestionValidacionIncorrecta(username, request.getRemoteAddr());
            //si superan
            if (reintentos == Constantes.VARIOS_REINTENTOS_LOGIN) {
                errors.add("usuario", new ActionMessage("mensajes.login.bloqueo.reintentos", username, String.valueOf(reintentos)));
            } else {
                errors.add("usuario", new ActionMessage("mensajes.login.bloqueo.aviso", String.valueOf(reintentos + 1)));
            }

            saveErrors(request, errors);
            return mapping.findForward("failure");
        } catch (AbadiaDBConnectionException ce) {
            log.error("LoginAction. Error al conectar con la base de datos", ce);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (UsuarioSinAbadiaException usae) {
            log.error("LoginAction. No se ha encontrado la abbatia del usuario", usae);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.noabadia"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } catch (AbadiaException e) {
            log.error("LOGBRP - Al validar usuario SQLException: ", e);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }

        // Save our logged-in user in the session, because we use it again later.

    } // end perform

}
