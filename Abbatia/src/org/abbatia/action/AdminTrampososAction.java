package org.abbatia.action;

import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;

public class AdminTrampososAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        int pagina = 0;
        Point total = new Point(0, 0);  // truco para utilizar variables por referencia ;-)


        String sPagina = request.getParameter("pagina");
        if (sPagina != null) pagina = Integer.parseInt(sPagina);

        // Lista de trabajos
        ArrayList alTable1;
        ActionMessages mensajes = new ActionMessages();
        UsuarioBBean oUsuarioBBean;

        try {
            oUsuarioBBean = new UsuarioBBean();
            // Para administracion y para colaboradores
            if ((usuario.getAdministrador() == 1) || (usuario.getAdministrador() == 2)) {
                alTable1 = oUsuarioBBean.recuperarPosiblesTramposos(pagina, total);
                HTML cHTML = new HTML();
                String navega = cHTML.getNavigateBar("AdminTramposos.do", "", pagina, total.x / Constantes.REGISTROS_PAGINA);
                request.getSession().setAttribute("tramposos", alTable1);
                request.setAttribute("Navega", navega);
            }

        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}

