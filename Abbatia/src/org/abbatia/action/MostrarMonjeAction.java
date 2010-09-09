package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Monje;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreExcepcion;
import org.abbatia.exception.MonjeNoEncontradoException;
import org.abbatia.exception.MonjeNoVisualizableException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
// Abbatia


public class MostrarMonjeAction extends Action {
    private static Logger log = Logger.getLogger(MostrarMonjeAction.class.getName());

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
                                 HttpServletResponse response) throws IOException, ServletException, AbadiaException {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        String sAction = request.getParameter("action");
        if (sAction == null) sAction = "inicio";
        String sClave = request.getParameter("clave");

        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();


        MonjeBBean oMonjeBBean;
        UsuarioBBean oUsuarioBBean;
        HashMap<String, Serializable> hmRequest;
        Monje oMonje;

        try {
            oMonjeBBean = new MonjeBBean();
            hmRequest = oMonjeBBean.recuperarDetalleMonje(Integer.parseInt(sClave), sAction, abadia, usuario, resource);

            request.getSession().setAttribute("hayabad", hmRequest.get("hayabad"));

            request.setAttribute("Visita_miabadia", hmRequest.get("Visita_miabadia"));
            oMonje = (Monje) hmRequest.get("Monje");

            request.setAttribute("Monje", oMonje);

            //CargasInicialesDietasBBean.reload();
            //ArrayList alTemp = (ArrayList) CargasInicialesDietasBBean.getValueList(oMonje.getComeFamiliaID1(), oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma());
            request.setAttribute("Alimentos1", CargasInicialesDietasBBean.getValueList(abadia.getNivelJerarquico(), oMonje.getComeFamiliaID1(),
                    oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma(), oMonje.getActMaitines()));
            request.setAttribute("Alimentos2", CargasInicialesDietasBBean.getValueList(abadia.getNivelJerarquico(), oMonje.getComeFamiliaID2(),
                    oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma(), oMonje.getActPrima()));
            request.setAttribute("Alimentos3", CargasInicialesDietasBBean.getValueList(abadia.getNivelJerarquico(), oMonje.getComeFamiliaID3(),
                    oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma(), oMonje.getActTercia()));
            request.setAttribute("Alimentos4", CargasInicialesDietasBBean.getValueList(abadia.getNivelJerarquico(), oMonje.getComeFamiliaID4(),
                    oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma(), oMonje.getActNona()));
            request.setAttribute("Alimentos5", CargasInicialesDietasBBean.getValueList(abadia.getNivelJerarquico(), oMonje.getComeFamiliaID5(),
                    oMonje.getIdDeJerarquia(), usuario.getIdDeIdioma(), oMonje.getActVispera()));

            request.getSession().setAttribute("alifam", hmRequest.get("alifam"));

            request.getSession().setAttribute("actividades", hmRequest.get("actividades"));

            if (sAction.equals("inicio")) {
                request.setAttribute("MonjeForm", hmRequest.get("MonjeForm"));
            }

            return mapping.findForward("success");

        } catch (MonjeNoEncontradoException e) {
            if (CoreExcepcion.controlExcepciones(e.getClass().getName(), request.getSession()) == 1) {
                mensajes.add("msg", new ActionMessage("mensajes.aviso.tramposo.accesomonje.bloqueo"));
                log.info("Bloqueamos el usuario con nick: " + usuario.getNick() + " por tratar de acceder a un monje que no es suyo.");
                oUsuarioBBean = new UsuarioBBean();
                oUsuarioBBean.bloquearUsuario(usuario.getIdDeUsuario(), 2, e.getClass().getName());
                request.getSession().invalidate();
            } else {
                mensajes.add("msg", new ActionMessage("mensajes.aviso.tramposo.accesomonje.aviso"));
            }
            notas.add(new Notificacion("index_main.do", "Inicio"));
            request.getSession().setAttribute("notificacion", notas);

            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (MonjeNoVisualizableException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.monje.de.viaje"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (AbadiaException e) {
            log.error("MostrarMonjeAction. AbadiaException", e);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        } catch (NumberFormatException e) {
            log.error("MostrarMonjeAction. NumberFormatException", e);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}
