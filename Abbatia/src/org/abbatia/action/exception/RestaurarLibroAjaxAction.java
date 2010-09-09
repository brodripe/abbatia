package org.abbatia.action.exception;

import net.sf.json.JSONObject;
import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.RestauracionLibroException;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class RestaurarLibroAjaxAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        ActionMessage mensaje;
        MessageResources resource = getResources(request);
        //recuperamos el atributo recibido (clave del libro)
        String claveLibro = request.getParameter("claveLibro");
        String szRetorno = "OK";

        //recuperamos el objeto libro
        LibroBBean oLibroBBean;
        try {
            oLibroBBean = new LibroBBean();
            if (!GenericValidator.isBlankOrNull(claveLibro)) {
                oLibroBBean.restaurarLibro(Integer.parseInt(claveLibro), abadia, usuario, mensajes);
            }
        } catch (NumberFormatException e) {
            mensaje = (ActionMessage)mensajes.get(ActionMessages.GLOBAL_MESSAGE).next();
            szRetorno = resource.getMessage(mensaje.getKey());
        } catch (RestauracionLibroException e) {
            mensaje = (ActionMessage)mensajes.get(ActionMessages.GLOBAL_MESSAGE).next();
            szRetorno = resource.getMessage(mensaje.getKey());
        }
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("retorno", szRetorno);
        //each key from our hash map becomes a key in our JSON object
        JSONObject json = JSONObject.fromObject(hm);
        //Plop it in the header so prototype can grab it.
        response.setHeader("X-JSON", json.toString());
        return actionMapping.findForward("success");
    }
}