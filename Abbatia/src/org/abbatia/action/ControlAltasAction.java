package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControlAltasAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadiaOri = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String idUsuario = request.getParameter("clave");
        MessageResources resource = getResources(request);

        AdminBBean oAdminBBean;
        oAdminBBean = new AdminBBean();
        if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR && usuario.getAdministrador() != Constantes.USUARIO_COLABORADOR_PLUS) {
            oAdminBBean.accesoIlegal(abadiaOri, usuario);
            request.getSession().invalidate();
            mensajes.add("msg", new ActionMessage("mensajes.aviso.noeresadministrador"));
            mensajes.add("msg", new ActionMessage("mensajes.aviso.usuariobloqueado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

        request.setAttribute("AltasPendientes", oAdminBBean.controlAltas(idUsuario, mapping, resource));
        return mapping.findForward("success");

    }
}
