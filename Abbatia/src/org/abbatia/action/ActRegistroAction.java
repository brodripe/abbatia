package org.abbatia.action;

import org.abbatia.actionform.RegistroActForm;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.YaExisteAliasException;
import org.abbatia.exception.YaExisteMailException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class ActRegistroAction extends Action {
    private static Logger log = Logger.getLogger(RegistroAction.class.getName());

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it),
     * with provision for handling exceptions thrown by the business logic.
     * Return an {@link ActionForward} instance describing where and how
     * control should be forwarded, or <code>null</code> if the response
     * has already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     * @since Struts 1.1
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionMessages errors = new ActionErrors();
        RegistroActForm usuariof = (RegistroActForm) form;

        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        UsuarioBBean oUsuarioBBean;

        try {
            oUsuarioBBean = new UsuarioBBean();
            oUsuarioBBean.actualizarRegistro(usuario, usuariof, request.getRemoteAddr());
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
            }

            request.getSession().setAttribute(Constantes.USER_KEY, usuario);

            return mapping.findForward("success");

        } catch (YaExisteMailException yece) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.email.repetido"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (YaExisteAliasException yeae) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.nick.repetido"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (AbadiaException ex) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.database.failure"));
            saveErrors(request, errors);
            log.debug("Error de SQL: " + ex);
            return (new ActionForward(mapping.getInput()));
        }

    }
}
