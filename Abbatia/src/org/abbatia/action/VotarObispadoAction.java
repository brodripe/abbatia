package org.abbatia.action;

import org.abbatia.actionform.VotacionActForm;
import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
     Página de Inicio del juego, llama adInicioContents y Inicio contents
*/

public class VotarObispadoAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        MessageResources resource = getResources(request);

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        VotacionActForm datosVotacion = (VotacionActForm) actionForm;
        String seleccion = request.getParameter("seleccion");

        JerarquiaBBean oJerarquiaBBean;
        oJerarquiaBBean = new JerarquiaBBean();
        oJerarquiaBBean.votarObispo(seleccion, datosVotacion, abadia, resource);
        request.setAttribute("DatosVotacion", datosVotacion);
        return mapping.findForward("success");
    }
}
