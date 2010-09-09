package org.abbatia.action;

import org.abbatia.bbean.UtilsBBean;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class LoginFrmAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        HashMap hmRequest;
        UtilsBBean oUtilsBBean;
        oUtilsBBean = new UtilsBBean();

        hmRequest = oUtilsBBean.recuperarParametrosLoginFrm();

        request.getSession().setAttribute("isbn", hmRequest.get("isbn"));
        request.getSession().setAttribute("titulo", hmRequest.get("titulo"));
        request.getSession().setAttribute("autor", hmRequest.get("autor"));

        return mapping.findForward("success");

    }

}
