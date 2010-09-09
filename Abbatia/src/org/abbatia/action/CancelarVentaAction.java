package org.abbatia.action;

import org.abbatia.actionform.DatosVentaActForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.ProductoNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class CancelarVentaAction extends Action {

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

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        DatosVentaActForm datosVenta = (DatosVentaActForm) form;
        DatosVentaActForm datos;

        HashMap hmRequest;

        MercadoBBean oMercadoBBean;

        String sPID = request.getParameter("pid");
        String sAccion = mapping.getParameter();
        if (sAccion == null) {
            sAccion = "inicio";
        }

        try {
            oMercadoBBean = new MercadoBBean();
            //si la acción no esta informada, quiere decir que venimos de la pantalla de venta...
            //
            if (sAccion.equals("inicio")) {
                datos = oMercadoBBean.mostrarDatosCancelarVenta(sPID, abadia);
                request.getSession().setAttribute("DatosVentaForm", datos);

                return mapping.findForward("mostrar");
            } else if (sAccion.equals("cancelar")) {

                hmRequest = oMercadoBBean.cancelarVenta(datosVenta, abadia, usuario, resource, mensajes);
                if (!mensajes.isEmpty()) {
                    request.getSession().setAttribute("notificacion", hmRequest.get("notificacion"));
                    saveMessages(request.getSession(), mensajes);
                }

                if (hmRequest.get("location") != null) {
                    request.getSession().setAttribute("location", hmRequest.get("location"));
                }

                request.getSession().setAttribute("DatosVentaForm", new DatosVentaActForm());
                return mapping.findForward("mensajes");
            }
            return mapping.findForward("failure");
        } catch (ProductoNoEncontradoException e) {
            mensajes.add("msg", new ActionMessage("mensajes.aviso.lotesnodisponibles"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
