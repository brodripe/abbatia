package org.abbatia.action;

import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MercadoHistoricoAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        MessageResources resource = getResources(request);
        int mercancia = (Integer) PropertyUtils.getSimpleProperty(actionForm, "mercancia");
        String sMercancia = "";

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        HashMap hmRequest;

        MercadoBBean oMercadoBBean;
        if (mercancia == Constantes.MERCANCIA_ALIMENTOS) {
            sMercancia = Constantes.MERCANCIA_ALIMENTOS_STR;
        } else if (mercancia == Constantes.MERCANCIA_ANIMALES) {
            sMercancia = Constantes.MERCANCIA_ANIMALES_STR;
        } else if (mercancia == Constantes.MERCANCIA_RECURSOS) {
            sMercancia = Constantes.MERCANCIA_RECURSOS_STR;
        }

        oMercadoBBean = new MercadoBBean();
        hmRequest = oMercadoBBean.cargarHistoricoMercado(abadia, usuario, sMercancia, resource);

        request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));
        request.setAttribute("compras", hmRequest.get("compras"));
        request.setAttribute("ventas", hmRequest.get("ventas"));

        return mapping.findForward("success");

    }
}
