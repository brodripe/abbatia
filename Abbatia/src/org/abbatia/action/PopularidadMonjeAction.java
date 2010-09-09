package org.abbatia.action;


import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.NoEsEminenciaException;
import org.abbatia.exception.VotoFraudulentoException;
import org.abbatia.exception.YaHaVotadoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
// Abbatia


public class PopularidadMonjeAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     *
     * @param mapping    The ActionMapping used to select this instance.
     * @param actionForm The optional ActionForm bean for this request.
     * @param request    The HTTP Request we are processing.
     * @param response   The HTTP Response we are processing.
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();
        int monjeid = 0;
        String sMonje = request.getParameter("clave");
        if (sMonje != null) monjeid = Integer.parseInt(sMonje);
        String sVoto = request.getParameter("voto");

        //este metodo cargará un actionForm con los mensajes para mostrar.
        ActionMessages mensajes = new ActionMessages();

        MonjeBBean oMonjeBBean;
        try {
            oMonjeBBean = new MonjeBBean();
            oMonjeBBean.votarPopularidad(monjeid, abadia, usuario, sVoto);

            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarDiplomacia.do?tab=1");
        } catch (YaHaVotadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.popularidad.yarealizado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (NoEsEminenciaException e) {
            //No se puede votar la popularidad de un monje si no es obispo o más...
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.popularidad.noeseminencia"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (VotoFraudulentoException e) {
            request.getSession().invalidate();
            notas.add(new Notificacion("index_main.do", "Inicio"));
            request.getSession().setAttribute("notificacion", notas);
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.popularidad.abispo.de.otra.region"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
