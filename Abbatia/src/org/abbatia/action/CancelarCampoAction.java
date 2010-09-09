package org.abbatia.action;

import org.abbatia.bbean.CultivoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelarCampoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        String idCampo = request.getParameter("clave");

        CultivoBBean oCultivoBBean;

        oCultivoBBean = new CultivoBBean();

        Edificio edificio = oCultivoBBean.cancelarCampo(idCampo, abadia, usuario, resource);

        Utilidades.eliminarRegistroContext(usuario.getNick());
        return new ActionForward("/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());

    }
}