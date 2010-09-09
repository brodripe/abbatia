package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bean.Table;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminProcessAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        AdminBBean oAdminBBean;
        HashMap<String, ArrayList<Table>> hmRequest;
        // Lista de procesos
        if ((usuario.getAdministrador() == 1) || (usuario.getAdministrador() == 2) || (usuario.getAdministrador() == 3)) {
            oAdminBBean = new AdminBBean();
            hmRequest = oAdminBBean.administrarProcesos(usuario);
            request.getSession().setAttribute("runs", hmRequest.get("runs"));
            request.getSession().setAttribute("logs", hmRequest.get("logs"));
            request.getSession().setAttribute("hora", CoreTiempo.getTiempoRealStringConHoras());
            request.getSession().setAttribute("horaabbatia", CoreTiempo.getTiempoAbadiaStringConHoras());
        }

        return mapping.findForward("success");
    }
}
