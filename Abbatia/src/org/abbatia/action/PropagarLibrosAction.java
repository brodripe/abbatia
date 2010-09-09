package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PropagarLibrosAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadiaOri = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String idLibroTipo = request.getParameter("clave");

        AdminBBean oAdminBBean;
        LibroBBean oLibroBBean;

        oAdminBBean = new AdminBBean();

        if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR && usuario.getAdministrador() != Constantes.USUARIO_COLABORADOR_PLUS) {
            oAdminBBean.accesoIlegal(abadiaOri, usuario);
            request.getSession().invalidate();
            mensajes.add("msg", new ActionMessage("mensajes.aviso.noeresadministrador"));
            mensajes.add("msg", new ActionMessage("mensajes.aviso.usuariobloqueado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
        try {
            oLibroBBean = new LibroBBean();
            if (GenericValidator.isBlankOrNull(idLibroTipo)) {
                request.setAttribute("LibrosTipo", oLibroBBean.recuperarLibrosPropagar(usuario));
            } else {
                request.setAttribute("LibrosTipo", oLibroBBean.propagarLibro(Integer.parseInt(idLibroTipo), usuario));
            }
            return mapping.findForward("success");
        } catch (AbadiaSQLException e) {
            mensajes.add("msg", new ActionMessage("registro.error.conexion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

    }
}
