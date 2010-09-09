package org.abbatia.action;


import org.abbatia.actionform.ViajarActForm;
import org.abbatia.bbean.ViajeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.InfoViajeCopia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.ExperienciaInsuficienteException;
import org.abbatia.exception.MonjesInsuficientesException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Abbatia


public class ViajarParaCopiarAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping             The ActionMapping used to select this instance.
     * @param actionForm          The optional ActionForm bean for this request.
     * @param request             The HTTP Request we are processing.
     * @param httpServletResponse The HTTP Response we are processing.
     * @return ActionForward
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        ViajarActForm viajarFrm = (ViajarActForm) actionForm;

        InfoViajeCopia infoViajeCopia = (InfoViajeCopia) request.getSession().getAttribute(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA);

        if (infoViajeCopia == null) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.sindatos"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

        ViajeBBean oViajeBBean;

        try {
            viajarFrm.setAbadiaid_destino(infoViajeCopia.getIdAbadiaDestino());
            viajarFrm.setMonjeid(infoViajeCopia.getIdMonje());
            viajarFrm.setIdLibro(infoViajeCopia.getIdLibro());
            viajarFrm.setAbadiaid_origen(abadia.getIdDeAbadia());

            oViajeBBean = new ViajeBBean();
            oViajeBBean.viajarParaCopiar(viajarFrm, infoViajeCopia, abadia, usuario, resource);
            // Controlar que el tio no haga la pirula!
            mensajes.add("msg", new ActionMessage("viajar.confirmar.solicitud.copia", infoViajeCopia.getNombreLibro()));

            //borramos el objeto con los datos de la copia de la sesion
            request.getSession().removeAttribute(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA);
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (ExperienciaInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.sinexperiencia"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (MonjesInsuficientesException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.pocosmonjes"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (OroInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.sindinero"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        }
    }
}

