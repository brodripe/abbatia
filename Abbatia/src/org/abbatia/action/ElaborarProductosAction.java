package org.abbatia.action;

import org.abbatia.actionform.DatosElaboracionActForm;
import org.abbatia.bbean.ElaboracionBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.bean.datosElaboracion;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ElaborarProductosAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        DatosElaboracionActForm formulario = (DatosElaboracionActForm) form;

        ElaboracionBBean oElaboracionBBean;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        ActionForward af = null;
        String sAccion = mapping.getParameter();
        String sProductoId;
        String sTipoElaboracion;
        if (sAccion == null) {
            sAccion = "requisitos";
        }

        //si se ha seleccionado un producto para elaborar...
        if (sAccion.equals("requisitos")) {
            sProductoId = request.getParameter("pid");
            if (!GenericValidator.isBlankOrNull(sProductoId)) {
                formulario.setIdProducto(Integer.valueOf(sProductoId));
            } else {
                return mapping.findForward("failure");
            }
            sTipoElaboracion = request.getParameter("tipo");
            if (GenericValidator.isBlankOrNull(sTipoElaboracion)) {
                return mapping.findForward("failure");
            }

            oElaboracionBBean = new ElaboracionBBean();
            //recuperamos los requisitos de elaboracion para ese producto
            oElaboracionBBean.recuperarRequisitosElaboracion(formulario, usuario, abadia, sTipoElaboracion);
            request.getSession().setAttribute("datosElaboracion", formulario);
            af = mapping.findForward("requisitos");

        } else if (sAccion.equals("elaborar")) {
            if (formulario.getCantidad() < 1) {
                notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.elaboracion.volver"), (short) 1));
                request.getSession().setAttribute("notificacion", notas);
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cantidad.incorrecta"));
                saveMessages(request.getSession(), mensajes);
                //recuperamos los requisitos de elaboracion para ese producto
                request.getSession().setAttribute("datosElaboracion", formulario);
                af = mapping.findForward("requisitos");
            } else {
                datosElaboracion datos = new datosElaboracion();
                oElaboracionBBean = new ElaboracionBBean();
                oElaboracionBBean.registroElaboracion(datos, formulario, abadia);
                Utilidades.eliminarRegistroContext(usuario.getNick());
                //reseteamos el actionform
                request.getSession().setAttribute("datosElaboracion", new DatosElaboracionActForm());
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + datos.getIdEdificio() + "&Tab=elaboracion");
                request.getSession().setAttribute("notificacion", notas);
                af = new ActionForward("/cerrarPopUp.do");
            }
        }

        return af;


    }
}