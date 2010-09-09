package org.abbatia.action;

import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.PlanificadorBBean;
import org.apache.struts.action.*;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Timer;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 13-sep-2004
 * Time: 1:19:23
 */
public class GestionPlanificador extends Action {
    public static Timer tempo = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Context initCtx;
        String sAccion = request.getParameter("accion");

        AdminBBean oAdminBBean;
        oAdminBBean = new AdminBBean();

        if (sAccion.equals("start")) {
            oAdminBBean.arrancarPlanificador();
            PlanificadorBBean.activarPlanificador();
            mensajes.add("msg", new ActionMessage("administracion.arrancarprocesos"));
/*
            initCtx = new InitialContext();
            initCtx.rebind(Constantes.ESTADO_PLANIFICADOR, "1");
*/
        } else {
            oAdminBBean.pararPlanificador();
            PlanificadorBBean.desactivarPlanificador();
            mensajes.add("msg", new ActionMessage("administracion.parandoprocesos"));
/*
            initCtx = new InitialContext();
            initCtx.rebind(Constantes.ESTADO_PLANIFICADOR, "0");
            initCtx.close();
*/
        }

        saveMessages(request.getSession(), mensajes);
        return mapping.findForward("mensajes");

    }
}
