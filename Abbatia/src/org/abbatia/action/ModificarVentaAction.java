package org.abbatia.action;

import org.abbatia.actionform.DatosVentaActForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ModificarVentaAction extends Action {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it),
     * with provision for handling exceptions thrown by the business logic.
     * Return an {@link ActionForward} instance describing where and how
     * control should be forwarded, or <code>null</code> if the response
     * has already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     * @since Struts 1.1
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Edificio edificio;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        DatosVentaActForm datosVenta = (DatosVentaActForm) form;

        MercadoBBean oMercadoBBean;
        String sAccion = mapping.getParameter();
        if (sAccion == null) {
            sAccion = "inicio";
        }
        String sPID = request.getParameter("pid");

        oMercadoBBean = new MercadoBBean();
        //si la acción no esta informada, quiere decir que venimos de la pantalla de venta...
        if (sAccion.equals("inicio")) {
            if (!GenericValidator.isBlankOrNull(sPID)) {
                datosVenta = oMercadoBBean.modificarVentaInicio(Integer.parseInt(sPID), abadia);
                request.getSession().setAttribute("DatosVentaForm", datosVenta);
                return mapping.findForward("mostrar");
            } else {
                return mapping.findForward("failure");
            }
        } else if (sAccion.equals("modificar")) {
            edificio = oMercadoBBean.modificarVenta(datosVenta, abadia, usuario, resource);
            //reseteamos el actionform
            request.getSession().setAttribute("DatosVentaForm", new DatosVentaActForm());

            request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
            notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.mercado.volver"), (short) 1));
            request.getSession().setAttribute("notificacion", notas);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.modificarventa", datosVenta.getDescripcionProducto(), datosVenta.getPrecio()));
            if (datosVenta.isVenderACiudad()) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.comprapreciominimo"));
            }
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

        return mapping.findForward("failure");

    }
}
