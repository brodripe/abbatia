package org.abbatia.action;

import org.abbatia.bbean.JerarquiaBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// Abbatia


public class NombrarAbadAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        String sClave = request.getParameter("clave");
        String sRevocar = request.getParameter("revocar");

        JerarquiaBBean oJerarquiaBBean;
        try {
            oJerarquiaBBean = new JerarquiaBBean();
            if (sRevocar == null) {
                oJerarquiaBBean.nombrarCargo(Integer.parseInt(sClave), abadia, Constantes.JERARQUIA_ABAD);
            } else {
                oJerarquiaBBean.revocarAbad(Integer.parseInt(sClave), abadia);
            }
            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarMonje.do?clave=" + sClave);

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.nombrarabad.identificador.invalido"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        }

    }
}
