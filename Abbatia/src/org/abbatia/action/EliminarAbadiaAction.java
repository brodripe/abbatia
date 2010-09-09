package org.abbatia.action;

import org.abbatia.actionform.AdminActForm;
import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaConVisitantesException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EliminarAbadiaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        AdminActForm formulario = (AdminActForm) form;
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        AbadiaBBean oAbadiaBBean;

        try {
            if (formulario.getAccion() == null) {
                return mapping.findForward("success");
            } else if (formulario.getAccion().equals("confirma")) {
                //si la abadía pertenece a un administrador..
                if (usuario.getAdministrador() == Constantes.USUARIO_ADMINISTRADOR) {
                    //no dejamos borrarla
                    mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.eliminar.abadia.administrador"));
                    saveMessages(request.getSession(), mensajes);
                    return mapping.findForward(Constantes.MENSAJES);
                } else {
                    oAbadiaBBean = new AbadiaBBean();
                    oAbadiaBBean.marcarAbadiaParaEliminacion(abadia);

                    mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aliminacion.abadia.aceptar", String.valueOf(Constantes.VARIOS_DIAS_ELIMINACION)));
                    saveMessages(request.getSession(), mensajes);
                    return mapping.findForward(Constantes.MENSAJES);
                }
            }

            return mapping.findForward("success");
        } catch (AbadiaConVisitantesException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.eliminar.abadia.visitantes"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        }
    }
}