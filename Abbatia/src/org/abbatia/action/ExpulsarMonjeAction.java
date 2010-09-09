package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Confirmacion;
import org.abbatia.bean.Monje;
import org.abbatia.bean.Notificacion;
import org.abbatia.exception.GenericException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ExpulsarMonjeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        int idMonje;

        Confirmacion confirmacion;

        Monje monje;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        MonjeBBean oMonjeBBean;

        try {
            idMonje = Integer.valueOf(request.getParameter("clave"));

            oMonjeBBean = new MonjeBBean();
            monje = oMonjeBBean.inicioExpulsarMonje(idMonje, abadia, resource, mensajes, notas);

            //solicitamos confirmacion.
            if (mapping.getParameter().equals("inicio")) {
                confirmacion = new Confirmacion();
                //cuidado, si decides arar el campo, perderás la cosecha actual, estás seguro....
                //mostrar dialogo estándar con opciones si o no.
                //accion si
                confirmacion.setAccionSi("/expulsarMonjeConfirmacion.do?clave=" + monje.getIdMonje());
                //accion no
                confirmacion.setAccionNo("/mostrarMonje.do?clave=" + monje.getIdMonje());
                //texto del mensaje
                confirmacion.setTextoConfirmacion(resource.getMessage("mensajes.aviso.expulsar.monje.texto", monje.getNombreCompuesto(), Utilidades.redondear(monje.getPuntosMonje())));
                //titulo del diálogo
                confirmacion.setTitulo(resource.getMessage("mensajes.aviso.expulsar.monje.titulo"));

                request.setAttribute("DatosConfirmacion", confirmacion);

                return mapping.findForward("confirmar");
            } else if (mapping.getParameter().equals("confirmar")) {
                oMonjeBBean.confirmarExpulsarMonje(monje, abadia, resource, mensajes);
            }

            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (GenericException e) {
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }

}
