package org.abbatia.action;

import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.InicioContents;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PoblacionAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);

        InicioContents Contents;

        AbadiaBBean oAbadiaBBean;
        oAbadiaBBean = new AbadiaBBean();
        Contents = oAbadiaBBean.gestionAldeanos(abadia);

        request.setAttribute("minimo_puntos", Utilidades.redondear(Contents.getPuntos()));
        request.setAttribute("puede_contratar", Contents.getPuedeContratar());
        request.setAttribute("DatosContents", Contents);

        return mapping.findForward("success");
    }
}

