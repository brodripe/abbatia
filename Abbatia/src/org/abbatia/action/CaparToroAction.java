package org.abbatia.action;

import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaparToroAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        int iAnimalId = Integer.parseInt(request.getParameter("claveAnimal"));

        AnimalBBean oAnimalBBean;

        oAnimalBBean = new AnimalBBean();
        return oAnimalBBean.caparToro(iAnimalId, abadia, usuario);

    }
}