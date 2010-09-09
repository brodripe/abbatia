package org.abbatia.action;

import org.abbatia.actionform.DatosDiplomaciaForm;
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
import java.util.HashMap;

public class MostrarDiplomaciaAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        DatosDiplomaciaForm diplomaciaForm = (DatosDiplomaciaForm) actionForm;

        int tab = 0;
        if (request.getParameter("tab") != null)
            tab = Integer.parseInt(request.getParameter("tab"));


        MessageResources resource = getResources(request);

        JerarquiaBBean oJerarquiaBBean;
        HashMap hmRequest;

        oJerarquiaBBean = new JerarquiaBBean();
        // Visualizar la diplomacia en tú abbatia
        if (tab == 0) {
            hmRequest = oJerarquiaBBean.recuperarJerarquiaTab0(diplomaciaForm, abadia, resource);
            request.setAttribute("Abad", hmRequest.get("Abad"));
            request.setAttribute("Elecciones", hmRequest.get("Elecciones"));
            request.setAttribute("DiplomaciaForm", diplomaciaForm);
            request.setAttribute("Cardenal", hmRequest.get("Cardenal"));
            request.setAttribute("Conclave", hmRequest.get("Conclave"));
            request.setAttribute("Funcion_Cardenal", hmRequest.get("Funcion_Cardenal"));
            request.setAttribute("Obispo", hmRequest.get("Obispo"));
            request.setAttribute("Papa", hmRequest.get("Papa"));

            return mapping.findForward("success");
        }

        // Visualizar la diplomacia en tú region!
        if (tab == 1) {
            hmRequest = oJerarquiaBBean.recuperarJerarquiaTab1(abadia, resource);
            request.setAttribute("Papa", hmRequest.get("Papa"));
            request.setAttribute("havotadoPapa", hmRequest.get("havotadoPapa"));
            request.setAttribute("Cardenales", hmRequest.get("Cardenales"));
            request.setAttribute("ObispoRegion", hmRequest.get("ObispoRegion"));
            request.setAttribute("havotadoObispo", hmRequest.get("havotadoObispo"));
            request.setAttribute("ComisionRegion", hmRequest.get("ComisionRegion"));
            return mapping.findForward("success1");
        }
        // Visualizar la diplomacia en tú region!
        if (tab == 2) {
            hmRequest = oJerarquiaBBean.recuperarJerarquiaTab2(resource);
            request.setAttribute("Papa", hmRequest.get("Papa"));
            request.setAttribute("Cardenales", hmRequest.get("Cardenales"));
            request.setAttribute("Obispos", hmRequest.get("Obispos"));
            return mapping.findForward("success2");
        }

        return mapping.findForward("success");
    }
}
