package org.abbatia.action;

import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelarEliminacionAbadiaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);

        AbadiaBBean oAbadiaBBean;

        oAbadiaBBean = new AbadiaBBean();

        oAbadiaBBean.cancelarEliminacionAbadia(abadia.getIdDeAbadia());

        return mapping.findForward("principal");

    }
}
