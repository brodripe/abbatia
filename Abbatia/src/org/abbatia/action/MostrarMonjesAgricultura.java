package org.abbatia.action;

import org.abbatia.actionform.DatosCampoActForm;
import org.abbatia.bbean.MonjeBBean;
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

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class MostrarMonjesAgricultura extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        MonjeBBean oMonjeBBean;

        DatosCampoActForm data = (DatosCampoActForm) actionForm;
        ActionForward af = null;
        //recuperamos el atributo recibido (clave del libro)
        String claveCampo = request.getParameter("clave");

        oMonjeBBean = new MonjeBBean();
        //si se trata del primer acceso
        if (claveCampo != null) {
            data.setIdCampo(Integer.valueOf(claveCampo));
            oMonjeBBean.recuperarMonjesAgricultura(data, abadia, resource);
            request.setAttribute("DatosCampoForm", data);
            af = actionMapping.findForward("mostrarmonjes");
            //si ya se ha seleccionado un monje
        } else if (data.getAccion().equals("seleccionado")) {
            Edificio edificio = oMonjeBBean.fijarSeleccionMonjesAgricultura(data, abadia, usuario);

            request.getSession().removeAttribute("DatosCampoForm");
            Utilidades.eliminarRegistroContext(usuario.getNick());
            request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
            af = actionMapping.findForward("cerrarPopUp");
        }
        return af;
    }
}
