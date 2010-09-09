package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.GestionDblClickBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EjecutarProcesoAction extends Action {
    private static Logger log = Logger.getLogger(EjecutarProcesoAction.class.getName());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        MessageResources resource = getResources(request);
        ActionMessages mensajes = new ActionMessages();

        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        String sProceso = request.getParameter("clave");
        InitialContext initCtx;
        String sValidado;

        AdminBBean oAdminBBean;

        sValidado = (String) request.getSession().getAttribute("validado");
        if (sValidado == null) {
            sValidado = "0";
        }

        GestionDblClickBBean.resetUserTime(usuario.getNick());
/*
        initCtx = new InitialContext();
        initCtx.rebind(usuario.getNick(), null);
        initCtx.close();
*/

        if (sValidado.equals("0")) {
            mensajes.add("msg", new ActionMessage("mensajes.aviso.administrador.no.logado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
        oAdminBBean = new AdminBBean();
        if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR) {
            oAdminBBean.accesoIlegal(abadia, usuario);
            return mapping.findForward("mensajes");
        } else if (!GenericValidator.isBlankOrNull(sProceso)) {
            // Direcciones IP autorizadas para ejecutar el proceso... solo para debugar en local
            String IPaddr = request.getRemoteAddr();
            log.info("Ejecución manual del proceso main.do - IP:" + IPaddr);
            request.setAttribute("Tiempo", CoreTiempo.getTiempoAbadiaStringConHorasObj(resource));
            oAdminBBean.ejecutarProceso(Integer.parseInt(sProceso));
            return mapping.findForward("success");
        } else {
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinproceso"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}

