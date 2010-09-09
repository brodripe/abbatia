package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DesbloquearUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadiaOri = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String idUsuario = request.getParameter("clave");
        AdminBBean oAdminBBean;
        UsuarioBBean oUsuarioBBean;


        if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR && usuario.getAdministrador() != Constantes.USUARIO_COLABORADOR_PLUS) {
            oAdminBBean = new AdminBBean();
            oAdminBBean.accesoIlegal(abadiaOri, usuario);
            request.getSession().invalidate();
            mensajes.add("msg", new ActionMessage("mensajes.aviso.noeresadministrador"));
            mensajes.add("msg", new ActionMessage("mensajes.aviso.usuariobloqueado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

        oUsuarioBBean = new UsuarioBBean();
        request.setAttribute("bloqueados", oUsuarioBBean.desbloquearUsuario(idUsuario));

        return mapping.findForward("success");


    }
}
