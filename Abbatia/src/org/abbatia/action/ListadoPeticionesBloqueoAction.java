package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ListadoPeticionesBloqueoAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadiaOri = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String szPeticionId;
        HashMap hmRequest;

        AdminBBean oAdminBBean;

        oAdminBBean = new AdminBBean();
        if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR && usuario.getAdministrador() != Constantes.USUARIO_COLABORADOR_PLUS) {
            oAdminBBean.accesoIlegal(abadiaOri, usuario);
            return mapping.findForward("mensajes");
        }

        szPeticionId = request.getParameter("idPeticion");
        hmRequest = oAdminBBean.gestionBloqueos(szPeticionId, mapping, usuario);

        //recupera la lista de usuarios pendientes de confirmación USUARIO_TIPO = 99
        request.setAttribute("BloqueosAbiertos", hmRequest.get("BloqueosAbiertos"));
        request.setAttribute("BloqueosCerrados", hmRequest.get("BloqueosCerrados"));

        return mapping.findForward("success");

    }
}
