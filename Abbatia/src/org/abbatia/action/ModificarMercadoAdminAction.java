package org.abbatia.action;

import org.abbatia.actionform.MercadoAdminForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.CardenalSinFuncionException;
import org.abbatia.exception.NoEsCardenalException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificarMercadoAdminAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MercadoAdminForm mercadoForm = (MercadoAdminForm) form;
        String idProducto = request.getParameter("idProducto");
        MercadoAdminForm mercado = null;
        String szActionForward;

        MercadoBBean oMercadoBBean;
        try {
            oMercadoBBean = new MercadoBBean();
            //en primer lugar, validaremos el usuario que está accediendo a la pantalla de modiicacion
            //podemos encontrar 2 variantes
            if (usuario.getAdministrador() == 1 && idProducto != null && mercadoForm.getAccion().equals("inicio"))
            //1 - Entra un usuario administrador, se mira si está informado el idProducto
            {
                //recuperamos los datos de venta del producto
                request.setAttribute("datosMercadoAdmin", oMercadoBBean.getDatosProductoVenta(usuario, Integer.valueOf(idProducto)));
                return mapping.findForward("success");
            } else if (idProducto != null && mercadoForm.getAccion().equals("inicio"))
            //2 - Entra un cardenal. Miraremos si está informado el idProducto y si corresponde con el ámbito que puede administrar el cardenal
            {
                request.setAttribute("datosMercadoAdmin", oMercadoBBean.inicioMercadoAdmin(abadia, usuario, Integer.valueOf(idProducto)));
                return mapping.findForward("success");
            } else if (mercadoForm.getAccion().equals("actualizar")) {
                szActionForward = oMercadoBBean.actualizarMercadoAdmin(mercadoForm, mercado, usuario, abadia, mensajes);
                if (szActionForward.equals("success")) {
                    request.setAttribute("datosMercadoAdmin", mercado);
                    saveMessages(request.getSession(), mensajes);
                    return mapping.findForward(szActionForward);
                } else {
                    return mapping.findForward(szActionForward);
                }
            } else {
                return mapping.findForward("success");
            }
        } catch (CardenalSinFuncionException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.cardenalsinfunciones"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (NoEsCardenalException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.debessercardenal"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}