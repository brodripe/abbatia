package org.abbatia.action;

import org.abbatia.actionform.MonjeActividadActForm;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
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
import java.util.ArrayList;
// Abbatia


public class MostrarActividadMonjeAction extends Action {

    private static Logger log = Logger.getLogger(MostrarActividadMonjeAction.class.getName());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException, AbadiaException {

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MonjeActividadActForm formulario = (MonjeActividadActForm) form;

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        String sClave = request.getParameter("clave");

        MessageResources resource = getResources(request);

        ActionMessages mensajes = new ActionMessages();

        MonjeBBean oMonjeBBean;
        UsuarioBBean oUsuarioBBean;

        try {

            oMonjeBBean = new MonjeBBean();
            oMonjeBBean.mostrarActividadesMonje(Integer.valueOf(sClave), formulario, abadia, usuario, resource);
            request.setAttribute("ActividadesMonjeForm", formulario);
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
            return mapping.findForward("failure");
        }

    }
}
