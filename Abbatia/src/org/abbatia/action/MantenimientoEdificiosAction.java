package org.abbatia.action;

import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaSinEdificiosException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MantenimientoEdificiosAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        //HashMap hmParams = (HashMap)request.getParameterMap();
        HashMap<String, Serializable> hmRequest;
        List alEdificios;

        EdificioBBean oEdificioBBean;

        try {
            oEdificioBBean = new EdificioBBean();
            hmRequest = oEdificioBBean.gestionMantenimiento(abadia, usuario, resource, request.getParameter("aceptar"), request.getParameterMap());
            //ponemos el coste total de mantenimiento en la request
            request.setAttribute("costeMantenimiento", hmRequest.get("costeMantenimiento"));

            alEdificios = (ArrayList) hmRequest.get("edificios");
            //cargamos el arraylist de edificios en la request.
            request.setAttribute("edificios", alEdificios);
            //vamos a la pantalla de gestión de mantenimiento.
            return mapping.findForward("success");

        } catch (AbadiaSinEdificiosException e) {
            mensajes.add("msg", new ActionMessage("mensajes.error.abbatia.sinedificios"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
    }
}
