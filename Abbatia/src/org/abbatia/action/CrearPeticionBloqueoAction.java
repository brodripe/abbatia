package org.abbatia.action;

import org.abbatia.actionform.PeticionBloqueoActForm;
import org.abbatia.bbean.AbadiaBBean;
import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrearPeticionBloqueoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Abadia oAbadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario oUsuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        PeticionBloqueoActForm formBloqueo = (PeticionBloqueoActForm) form;
        AdminBBean oAdminBBean;
        AbadiaBBean oAbadiaBBean;
        UsuarioBBean oUsuarioBBean;


        if (oUsuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR && oUsuario.getAdministrador() != Constantes.USUARIO_COLABORADOR_PLUS) {
            oAdminBBean = new AdminBBean();
            oAdminBBean.accesoIlegal(oAbadia, oUsuario);
        }

        oAbadiaBBean = new AbadiaBBean();
        if (mapping.getParameter().equals("BuscarAbadia")) {
            request.getSession().setAttribute("lista_abadias", oAbadiaBBean.buscarAbadiasPorNombre(formBloqueo));
            return mapping.findForward("success");
        } else if (mapping.getParameter().equals("FijarParametros")) {
            String szAbadiaId = request.getParameter("idAbadia");
            formBloqueo.setAbadiaId(Integer.valueOf(szAbadiaId));
            formBloqueo.setNombreAbadia(oAbadiaBBean.recuperarNombreAbadia(Integer.valueOf(szAbadiaId)));
            request.getSession().setAttribute("DatosPeticionBloqueo", formBloqueo);
            return mapping.findForward("success");
        } else if (mapping.getParameter().equals("CrearPeticion")) {
            oUsuarioBBean = new UsuarioBBean();
            formBloqueo.setUsuarioId(oUsuario.getIdDeUsuario());
            oUsuarioBBean.crearPeticionBloqueo(formBloqueo);
            request.getSession().setAttribute("DatosPeticionBloqueo", new PeticionBloqueoActForm());
            request.getSession().setAttribute("location", "/ListarPeticionesBloqueo.do");
        }

        return mapping.findForward("success");
    }
}
