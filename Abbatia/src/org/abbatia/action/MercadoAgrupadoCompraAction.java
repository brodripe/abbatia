package org.abbatia.action;

import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.MoreExpensiveException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MercadoAgrupadoCompraAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        MercadoBBean oMercadoBBean;
        HashMap hmRequest;
        try {
            oMercadoBBean = new MercadoBBean();
            hmRequest = oMercadoBBean.getMercadoCompraAgrupado(usuario, actionForm);
            // Información de las combos de la página
            request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));

            // Recuperar la lista del mercado
            request.setAttribute("MercadosListas", hmRequest.get("MercadosListas"));

            //recuperar la lista de familias de alimentos
            request.setAttribute("alimento_familia", hmRequest.get("alimento_familia"));
            return mapping.findForward("success");

        } catch (MoreExpensiveException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productomuycaro"));
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("mensaje", e.getMessage());
            return mapping.findForward("mensajes");

        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}
