package org.abbatia.action;

import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CongelarAbadiaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        AbadiaBBean oAbadiaBBean;

        if (usuario.getRegistrado() == 1) {
            oAbadiaBBean = new AbadiaBBean();
            if (usuario.getCongelado() == 0) {
                oAbadiaBBean.congelarAbadia(1, usuario.getIdDeUsuario(), abadia.getIdDeAbadia());
                usuario.setCongelado((short) 1);
                request.getSession().setAttribute(Constantes.USER_KEY, usuario);
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("supporters.bloqueado"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            } else {
                oAbadiaBBean.congelarAbadia(0, usuario.getIdDeUsuario(), abadia.getIdDeAbadia());
                usuario.setCongelado((short) 0);
                request.getSession().setAttribute(Constantes.USER_KEY, usuario);
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("supporters.desbloqueado"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }
        } else {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("supporters.noregistrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }

}