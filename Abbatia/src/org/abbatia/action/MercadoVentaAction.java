package org.abbatia.action;

import org.abbatia.actionform.MercadoVentaActForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.MercadoVenta;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Imports del Proyecto

public class MercadoVentaAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        String sAction = mapping.getParameter();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        MercadoVentaActForm mercadoVenta = (MercadoVentaActForm) actionForm;
        MercadoVenta oMercadoVenta;

        MercadoBBean oMercadoBBean;

        if (sAction == null) {
            sAction = "inicio";
        }


        int iMercancia = mercadoVenta.getMercancia();

        if (iMercancia == 0) iMercancia = Constantes.MERCANCIA_ALIMENTOS;

        //ArrayList al;

        oMercadoBBean = new MercadoBBean();
        // Pantalla de inicio
        if (sAction.equals("inicio")) {
            request.getSession().removeAttribute("notificacion");
            request.getSession().setAttribute("MercadosListas", oMercadoBBean.mercadoVentaInicio(abadia, usuario, resource)); // <--- nuevo???
            return mapping.findForward("venta");
        }

        // Pantalla para vender
        if (sAction.equals("venta")) {
            //al = (ArrayList)request.getSession().getAttribute("mercancia_tipo");
            oMercadoVenta = oMercadoBBean.recuperarProductos(abadia.getIdDeAbadia(), iMercancia, usuario);
            request.setAttribute("MercadosListas", oMercadoVenta);
            request.getSession().setAttribute("MercanciaTipo", oMercadoVenta.getMercanciaTipo());

            return mapping.findForward("venta1");
        }

        if (sAction.equals("venta1")) {
            //Contents.finalize();
            return mapping.findForward("venta2");
        }

        return mapping.findForward("failure");

    }
}
