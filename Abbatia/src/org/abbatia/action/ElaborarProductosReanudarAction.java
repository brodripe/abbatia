package org.abbatia.action;

import org.abbatia.bbean.ElaboracionBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EdificioNotFoundException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ElaborarProductosReanudarAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        ElaboracionBBean oElaboracionBBean;
        String sClave = request.getParameter("clave");

        try {
            //si se ha seleccionado la elaboracion a pausar
            if (sClave != null) {
                oElaboracionBBean = new ElaboracionBBean();
                return oElaboracionBBean.reanudarElaboracion(Integer.parseInt(sClave), abadia, usuario);
            } else {
                //no se ha informado la clave del producto
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametronoinformado"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }

        } catch (java.lang.NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EdificioNotFoundException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.elaboracion.eliminar.noestuya"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}