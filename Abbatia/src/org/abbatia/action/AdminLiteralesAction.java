package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class AdminLiteralesAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        //String sPost = request.getParameter("post");
/*        HashMap<String, Serializable> hmRequest = (HashMap<String, Serializable>) request.getParameterMap();*/
        AdminBBean oAdminBBean;
        HashMap hmReturn;

        oAdminBBean = new AdminBBean();
        hmReturn = oAdminBBean.administrarLiterales(usuario, request);
        request.getSession().setAttribute("literales", hmReturn.get("literales"));
        request.setAttribute("Navega", hmReturn.get("Navega"));
        request.setAttribute("sIdioma1", hmReturn.get("sIdioma1"));
        request.setAttribute("sIdioma2", hmReturn.get("sIdioma2"));
        request.setAttribute("Idioma1", hmReturn.get("Idioma1"));
        request.setAttribute("Idioma2", hmReturn.get("Idioma2"));
        request.setAttribute("Pagina", hmReturn.get("Pagina"));
        request.setAttribute("Nuevos", hmReturn.get("Nuevos"));

        return mapping.findForward("success");
    }
}
