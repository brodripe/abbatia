package org.abbatia.action;

import org.abbatia.bbean.AlimentoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AlimentoNoSalableException;
import org.abbatia.exception.CantidadInsuficienteException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SalarAlimentoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        Edificio edificio;
        String sTab = "";
        String idAlimento = request.getParameter("clave");
        AlimentoBBean oAlimentoBBean;
        try {
            oAlimentoBBean = new AlimentoBBean();
            edificio = oAlimentoBBean.salarAlimento(usuario, abadia, Integer.parseInt(idAlimento), mapping.getParameter());

            //si la opción es "salar" todo...
            if (mapping.getParameter().equals("todo")) {
                sTab = "Tab=init";

            } else if (mapping.getParameter().equals("lote")) {
                sTab = "Tab=salar";
            }
            Utilidades.eliminarRegistroContext(usuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio() + "&" + sTab);
        } catch (CantidadInsuficienteException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cocina.salar.recursoinsuficiente"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid", idAlimento));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (AlimentoNoSalableException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cocina.salar.alimentoincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("failure");
        }
    }
}
