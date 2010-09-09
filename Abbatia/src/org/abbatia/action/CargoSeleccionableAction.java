package org.abbatia.action;

import org.abbatia.actionform.DatosDiplomaciaForm;
import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CargoSeleccionableAction extends Action {


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DatosDiplomaciaForm diplomaciaForm = (DatosDiplomaciaForm) form;

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);

        JerarquiaBBean oJerarquiaBBean;

        oJerarquiaBBean = new JerarquiaBBean();
        oJerarquiaBBean.cambiarEstadoSeleccionable(diplomaciaForm, abadia);

        return mapping.findForward("diplomacia");

    }

}

