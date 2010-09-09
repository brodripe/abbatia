package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.CardenalSinFuncionException;
import org.abbatia.exception.NoEsCardenalException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class listarMercadoAdminAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        int mercancia = (Integer) PropertyUtils.getSimpleProperty(form, "mercancia");

        AdminBBean oAdminBBean;
        HashMap hmRequest;

        try {
            oAdminBBean = new AdminBBean();
            //en primer lugar, validaremos el usuario que está accediendo al listado.
            //podemos encontrar 2 variantes
            if (usuario.getAdministrador() == 1)
            //1 - Entra un usuario administrador, por defecto se cargaran los alimentos a la venta
            //    pero se mostrara una combo con los distintos tipos de venta (recursos, animales, alimentos, reliquias y libros)
            {
                hmRequest = oAdminBBean.listarMercadoAdminInicio(mercancia, usuario);
                request.setAttribute("lista_mercancias", hmRequest.get("lista_mercancias"));
                request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));
                return mapping.findForward("success");
            } else
            //2 - Entra un cardenal. Miraremos la responsabilidad del cardenal para ver qué mercado puede adminstrar
            //  para mostrar solo los elementos de mercado que le tocan sin opción de cambiar (recursos, animales, alimentos, reliquias y libros)
            {
                //aquí, comprobaremos si el usuario tiene un cardenal, y recuperaremos si tiene responsabilidades de mercado
                //en caso de tenerlas, obtendremos el ámbito de mercado que puede controlar e informaremos la variable "mercancia"
                hmRequest = oAdminBBean.listarMercadoAdmin(mercancia, abadia, usuario);
                request.setAttribute("lista_mercancias", hmRequest.get("lista_mercancias"));
                request.setAttribute("mercancia_tipo", hmRequest.get("mercancia_tipo"));

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
        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}