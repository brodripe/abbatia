package org.abbatia.action;

import org.abbatia.actionform.BuscarAbadiasActForm;
import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/*
     Permite uscar seleccionar VARIAS abadias

*/


public class BuscarAbadiasAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        BuscarAbadiasActForm datos = (BuscarAbadiasActForm) form;

        AbadiaBBean oAbadiaBBean;
        HashMap hmRequest;
        hmRequest = new HashMap();
        String sAction = datos.getAccion();

        oAbadiaBBean = new AbadiaBBean();

        hmRequest = oAbadiaBBean.buscarAbadias(datos, abadia, mensajes);

        if (sAction.equals("inicio")) {
            request.getSession().setAttribute("regiones", hmRequest.get("regiones"));
            request.getSession().setAttribute("ordenes", hmRequest.get("ordenes"));
            return mapping.findForward("inicio");
        }

        if (sAction.equals("buscar")) {
            return mapping.findForward("mostrar");
        } else if (sAction.equals("aceptar")) {
            if (datos.getSeleccion() == null) {
                request.setAttribute("BuscarAbadiaForm", datos);
                return mapping.findForward("mostrar");
            }
            //si el número de destinatarios supera el permitido...
            if (datos.getSeleccion().length > Constantes.VARIOS_MAX_DESTINATARIOS) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.superado.limite.destinatarios", String.valueOf(Constantes.VARIOS_MAX_DESTINATARIOS)));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }
            request.setAttribute("MensajeForm", hmRequest.get("MensajeForm"));
            return mapping.findForward("aceptar");
        } else if (sAction.equals("cancelar")) {
            return mapping.findForward("cancelar");
        }
        return mapping.findForward("error");

    }
}
