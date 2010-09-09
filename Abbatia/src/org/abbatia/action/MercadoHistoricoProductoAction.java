package org.abbatia.action;

import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MercadoHistoricoProductoAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        String sProducto = request.getParameter("idproducto");
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        MercadoBBean oMercadoBBean;
        HashMap hmRequest;
        try {
            oMercadoBBean = new MercadoBBean();
            hmRequest = oMercadoBBean.cargarHistoricoMercadoProducto(abadia, usuario, sProducto, resource);

            request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));
            request.setAttribute("compras", hmRequest.get("compras"));
            request.setAttribute("ventas", hmRequest.get("ventas"));

            return mapping.findForward("success");

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
