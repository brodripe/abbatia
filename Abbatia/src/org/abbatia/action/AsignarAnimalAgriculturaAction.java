package org.abbatia.action;

import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Animal;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AnimalNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsignarAnimalAgriculturaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        String sIdAnimal = request.getParameter("claveAnimal");

        Animal animal;
        AnimalBBean oAnimalBBean;

        try {
            oAnimalBBean = new AnimalBBean();
            animal = oAnimalBBean.asignarAnimalAgricultura(sIdAnimal, abadia);
            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + animal.getEdificioid());

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.animal.noencontrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (AnimalNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.animal.noencontrado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
