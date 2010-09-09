package org.abbatia.action;

import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AnimalNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DevolverAnimalAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String idAnimal = request.getParameter("claveAnimal");

        AnimalBBean oAnimalBBean;

        oAnimalBBean = new AnimalBBean();

        try {
            return oAnimalBBean.devolverAnimal(Integer.parseInt(idAnimal), usuario);
        } catch (AnimalNoEncontradoException e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.estasmanipulando"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

    }
}