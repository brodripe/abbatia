package org.abbatia.action;

import org.abbatia.actionform.AbadiaActForm;
import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.AbadiaYaExisteException;
import org.abbatia.exception.RegionNoValidaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class RegistroAbadiaAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        String sAction = request.getParameter("accion");
        ActionMessages errors = new ActionErrors();
        ActionMessages mensajes = new ActionMessages();
        AbadiaActForm abadiaf = (AbadiaActForm) form;


        MessageResources resource = getResources(request);
        AbadiaBBean oAbadiaBBean;
        Object oReturn;

        try {
            oAbadiaBBean = new AbadiaBBean();
            if (sAction.equals("registrar")) {
                return mapping.findForward("success");
            } else if (sAction.equals("registro2")) {
                oReturn = oAbadiaBBean.registroAbadia2(abadiaf, usuario);

                if (oReturn instanceof Locale) {
                    setLocale(request, (Locale) oReturn);
                    request.getSession().setAttribute(Constantes.USER_KEY, usuario);
                    request.getSession().setAttribute(Constantes.ABADIA, abadia);
                    return mapping.findForward("principal");
                }
                //validamos si el usuario ya dispone de una abadía.
                if (oReturn instanceof HashMap) {
                    request.getSession().setAttribute("nombre_abadia", abadiaf.getNombreAbadia());
                    request.getSession().setAttribute("regiones", ((HashMap) oReturn).get("regiones"));
                    request.getSession().setAttribute("ordenes", ((HashMap) oReturn).get("ordenes"));
                    request.getSession().setAttribute("actividades", ((HashMap) oReturn).get("actividades"));
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("registro.error.abadiayaexiste"));
                    saveErrors(request, errors);
                    return mapping.findForward("errorregistro");
                }

                request.getSession().setAttribute("AbadiaForm", abadiaf);
                return mapping.findForward("registro2");

            } else if (sAction.equals("registro3")) {
                abadiaf = (AbadiaActForm) request.getSession().getAttribute("AbadiaForm");
                oReturn = oAbadiaBBean.registroAbadia3(abadiaf, usuario, resource);

                if (oReturn instanceof Locale) {
                    setLocale(request, (Locale) oReturn);
                    request.getSession().setAttribute(Constantes.USER_KEY, usuario);
                    request.getSession().setAttribute(Constantes.ABADIA, abadia);
                    return mapping.findForward("principal");
                }

                if (oReturn instanceof Abadia) {
                    request.getSession().setAttribute(Constantes.ABADIA, oReturn);
                }
                return mapping.findForward("success");
            }
            return mapping.findForward("success");
        } catch (AbadiaSQLException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (AbadiaDBConnectionException adbce) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (AbadiaYaExisteException ayee) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("registro.error.abadiayaexiste"));
            saveErrors(request, errors);
            return mapping.findForward("registro2");
        } catch (RegionNoValidaException e) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("registro.error.regionnovalida"));
            saveErrors(request, errors);
            return mapping.findForward("errorregistro");
        }
    }
}
