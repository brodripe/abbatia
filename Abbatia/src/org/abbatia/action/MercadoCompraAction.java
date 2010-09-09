package org.abbatia.action;

import org.abbatia.actionform.BuscarMercadoCompraForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.MoreExpensiveException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MercadoCompraAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        BuscarMercadoCompraForm datosFiltro = (BuscarMercadoCompraForm) actionForm;

        MercadoBBean oMercadoBBean;
        HashMap hmRequest;
        try {
            String sAction = request.getParameter("action");
            if (sAction == null) sAction = "inicio";

            if (sAction.equals("inicio")) {
                oMercadoBBean = new MercadoBBean();
                hmRequest = oMercadoBBean.inicioMercadoCompra(abadia, usuario, datosFiltro, resource);

                // Información de las combos de la página
                request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));
                request.setAttribute("mercado", hmRequest.get("mercado"));
                request.setAttribute("compra_tipo", hmRequest.get("compra_tipo"));
                request.setAttribute("filtro", hmRequest.get("filtro"));

                request.setAttribute("mercancia_tipo_f", String.valueOf(datosFiltro.getMercancia()));
                request.setAttribute("MercadosListas", hmRequest.get("MercadosListas")); // <--- nuevo???

                return mapping.findForward("success");
            }

            return mapping.findForward("failure");

        } catch (MoreExpensiveException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productomuycaro"));
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("mensaje", e.getMessage());
            return mapping.findForward("mensajes");
        }
    }
}
