package org.abbatia.action;

import org.abbatia.actionform.VotacionPapaActForm;
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


public class VotarPapaAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        MessageResources resource = getResources(request);

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        VotacionPapaActForm datosVotacionPapa = (VotacionPapaActForm) actionForm;
        String seleccion = request.getParameter("seleccion");

        JerarquiaBBean oJerarquiaBBean;
        oJerarquiaBBean = new JerarquiaBBean();
        oJerarquiaBBean.votarPapa(seleccion, datosVotacionPapa, abadia, resource);
        request.setAttribute("DatosVotacionPapa", datosVotacionPapa);
        return mapping.findForward("success");

    }
}
