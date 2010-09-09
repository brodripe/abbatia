package org.abbatia.action;

import org.abbatia.actionform.MostrarMonjeActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.MonjeNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ActualizarDatosMonjeAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();

        MessageResources oResource = getResources(request);
        MostrarMonjeActForm oMonjeActFrm = (MostrarMonjeActForm) form;
        MonjeBBean oMonjeBBean;

        try {
            oMonjeBBean = new MonjeBBean();
            oMonjeBBean.actualizarDatosMonje(oMonjeActFrm, abadia, usuario, oResource);

            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarMonje.do?clave=" + oMonjeActFrm.getIdMonje());

        } catch (MonjeNoEncontradoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(oResource.getMessage("mensajes.aviso.monje.noencontrado")));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
