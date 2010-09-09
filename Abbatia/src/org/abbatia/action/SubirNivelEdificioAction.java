package org.abbatia.action;

import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.InicioContents;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.LibroConstruccionInsuficiente;
import org.abbatia.exception.NivelEdificioObispoIncorrecto;
import org.abbatia.exception.RecursosInsuficientesException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


public class SubirNivelEdificioAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        int lClave = Integer.parseInt(request.getParameter("clave"));
        String Confirma = request.getParameter("confirma");

        Abadia abadia, Obispado = null;
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        ActionForward af;
        ActionMessages mensajes = new ActionMessages();

        Edificio oEdificio;
        HashMap hmDatosSubida;

        EdificioBBean oEdificioBBean;
        AbadiaBBean oAbadiaBBean;
        String sAbadiaId = request.getParameter("abadiaid_obispado");

        /* Recuperar la abbatia que tenemos que visualizar!!*/
        if (!GenericValidator.isBlankOrNull(sAbadiaId)) {
            Obispado = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
            oEdificioBBean = new EdificioBBean();
            abadia = oEdificioBBean.subirNivelEdificioObispo(Obispado, Integer.parseInt(sAbadiaId));
            request.setAttribute("Nombre", abadia.getNombre());
            request.setAttribute("Abadiaid_Obispado", request.getParameter("abadiaid_obispado"));
        } else {
            abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
            oAbadiaBBean = new AbadiaBBean();
            // Recursos de la abbatia
            InicioContents datos = oAbadiaBBean.getRecursos(abadia);
            request.setAttribute("DatosContents", datos);
        }


        if (Confirma == null) {
            oEdificioBBean = new EdificioBBean();
            hmDatosSubida = oEdificioBBean.subirNivelEdificio(lClave, abadia, Obispado, usuario, resource);
            if (Obispado != null) {
                request.setAttribute("TotalOro", hmDatosSubida.get("TotalOro"));
            }

            request.setAttribute("Edificio", hmDatosSubida.get("Edificio"));
            request.setAttribute("DatosNivel", hmDatosSubida.get("DatosNivel"));

            return mapping.findForward("confirmar");
        } else {
            try {

                oEdificioBBean = new EdificioBBean();
                oEdificio = oEdificioBBean.subirNivelConfirmado(lClave, abadia, Obispado, usuario, resource);
                if (request.getParameter("abadiaid_obispado") != null) {
                    mensajes.add("msg", new ActionMessage("mensajes.aviso.ampliacion.iniciada", oEdificio.getNombre(), abadia.getNombre()));
                    saveMessages(request.getSession(), mensajes);
                    af = mapping.findForward("mensajes");
                } else {
                    Utilidades.eliminarRegistroContext(usuario.getNick());
                    af = new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                }

            } catch (RecursosInsuficientesException ri) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.edificio.sinrecursos"));
                saveMessages(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            } catch (NivelEdificioObispoIncorrecto e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.ampliacion.obispo.nivelexcesivo"));
                saveMessages(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            } catch (LibroConstruccionInsuficiente e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.ampliacion.faltalibro.construccion"));
                saveMessages(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            } catch (AbadiaSQLException e) {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.database.general"));
                saveMessages(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            }
        }

        return af;

    }
}
