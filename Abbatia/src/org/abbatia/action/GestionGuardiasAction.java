package org.abbatia.action;

import org.abbatia.bbean.GuardiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.DatosSalarioBean;
import org.abbatia.exception.NoDisponesDeGuardiasException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestionGuardiasAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        DatosSalarioBean formulario = new DatosSalarioBean();
        GuardiaBBean oGuardiaBBean;
        try {
            oGuardiaBBean = new GuardiaBBean();
            if (mapping.getParameter().equals("contratar")) {
                oGuardiaBBean.contratarGuardia(abadia, resource);
            } else if (mapping.getParameter().equals("despedir")) {
                oGuardiaBBean.despedirGuardia(abadia, resource);
            }
            formulario = oGuardiaBBean.recuperarDatosGuardias(abadia);
            //recuperamos el número de guardias
            formulario.setSalarioTotal(formulario.getSalarioGuardia() * formulario.getNumGuardias());
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("datos_guardias", formulario);
            return mapping.findForward("mostrar");
        } catch (OroInsuficienteException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.contratacion.dineroinsuficiente", String.valueOf(formulario.getSalarioGuardia())));
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("datos_guardias", formulario);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (NoDisponesDeGuardiasException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.despido.notienesguardias"));
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("datos_guardias", formulario);
            return mapping.findForward(Constantes.MENSAJES);
        }


    }
}
