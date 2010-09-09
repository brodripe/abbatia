package org.abbatia.action;


import org.abbatia.actionform.ViajarActForm;
import org.abbatia.bbean.ViajeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.ExperienciaInsuficienteException;
import org.abbatia.exception.MonjeIncorrectoException;
import org.abbatia.exception.MonjesInsuficientesException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
// Abbatia


public class ViajarAction extends Action {

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
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        ViajarActForm viajarFrm = (ViajarActForm) actionForm;

        HashMap hmRequest;
        ViajeBBean oViajeBBean;

        try {

            if (request.getParameter("abadiaid") != null)
                viajarFrm.setAbadiaid_destino(Integer.parseInt(request.getParameter("abadiaid")));
            if (request.getParameter("monjeid") != null)
                viajarFrm.setMonjeid(Integer.parseInt(request.getParameter("monjeid")));
            if (request.getParameter("accion") == null)
                viajarFrm.setAccion("datos");

            oViajeBBean = new ViajeBBean();

            if ((viajarFrm.getAccion().equals("aceptar")) && ((viajarFrm.getMonjeid() != 0)) && ((viajarFrm.getAbadiaid_destino() != abadia.getIdDeAbadia()))) {
                oViajeBBean.viajarConfirmacion(viajarFrm, abadia, usuario, resource);
                mensajes.add("msg", new ActionMessage("viajar.confirmar.solicitud"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");

            } else {
                hmRequest = oViajeBBean.viajarInicio(viajarFrm, abadia, usuario, resource);

                request.setAttribute("monjes", hmRequest.get("monjes"));
                request.setAttribute("DatosContents", hmRequest.get("DatosContents"));

                request.setAttribute("msgCaballo", hmRequest.get("msgCaballo"));
                request.setAttribute("Caballo", hmRequest.get("Caballo"));
                return mapping.findForward("success");
            }
        } catch (ExperienciaInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.sinexperiencia"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (MonjesInsuficientesException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.pocosmonjes"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (MonjeIncorrectoException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.error.monjeincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (OroInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("viajar.confirmar.sindinero"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}

