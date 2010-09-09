package org.abbatia.action;

import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EdificioNotFoundException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AislarAnimalAction extends Action {


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        String idAnimal = request.getParameter("claveAnimal");
        int iEdificioId;
        AnimalBBean oAnimalBBean;
        try {
            oAnimalBBean = new AnimalBBean();
            iEdificioId = oAnimalBBean.aislarAnimal(usuario, abadia, Integer.parseInt(idAnimal));
            return new ActionForward("/mostrarEdificio.do?clave=" + iEdificioId);
        } catch (EdificioNotFoundException e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.estasmanipulando"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
    }
}