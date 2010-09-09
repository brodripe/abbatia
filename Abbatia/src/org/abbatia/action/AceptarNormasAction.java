package org.abbatia.action;

import org.abbatia.bbean.AnimalBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AceptarNormasAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        boolean estadoCheck;

        try
        {
            estadoCheck = (Boolean) PropertyUtils.getSimpleProperty(form, "estadoCheck");

            UsuarioBBean oUsuarioBBean = new UsuarioBBean();
            oUsuarioBBean.actualizarEstadoAceptacionNormas(usuario.getIdDeUsuario(), estadoCheck);
        }catch(NullPointerException e){}

        return mapping.findForward("success");
    }
}