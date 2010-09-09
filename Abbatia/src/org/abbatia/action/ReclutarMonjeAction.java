package org.abbatia.action;

import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.EspacioInsuficienteEnDormitorio;
import org.abbatia.exception.MuerteMonjeReciente;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ReclutarMonjeAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();


        MonjeBBean oMonjeBBean;
        try {
            oMonjeBBean = new MonjeBBean();
            oMonjeBBean.reclutarNovicio(abadia, usuario);

            return mapping.findForward("success");
        } catch (EspacioInsuficienteEnDormitorio e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.nohayespacioendormitorio"));
            notas.add(new Notificacion("main.do", "Volver"));
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        } catch (MuerteMonjeReciente e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.reclutar.reciente", e.getOMonje().getNombre() + " " + resource.getMessage("monjes.abadia.de") + " " + e.getOMonje().getApellido1()));
            notas.add(new Notificacion("main.do", "Volver"));
            request.getSession().setAttribute("notificacion", notas);
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        }

    }
}