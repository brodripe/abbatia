package org.abbatia.action;

import org.abbatia.actionform.DatosMolinoActForm;
import org.abbatia.bbean.TareasBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.LoteNoDisponibleException;
import org.abbatia.exception.base.ApplicationException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 04-nov-2004
 * Time: 22:10:15
 */
public class MolerCerealesAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        DatosMolinoActForm formulario = (DatosMolinoActForm) actionForm;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();
        MessageResources resource = getResources(request);

        TareasBBean oTareasBBean;

        String sAccion = actionMapping.getParameter();
        if (sAccion == null) {
            sAccion = "inicio";
        }

        try {
            oTareasBBean = new TareasBBean();
            //en la primera carga del action, mostraremos la lista de cereales que tiene la abbatia
            //Asolicado a la selección del grano, el usuario asignara la cantidad que desea moler. (con la validaciones pertinentes)
            if (sAccion.equals("inicio")) {
                oTareasBBean.molerCerealesInicio(formulario, abadia, usuario);
                saveMessages(request.getSession(), mensajes);
                return actionMapping.findForward("seleccion");
            } else if (sAccion.equals("moler")) {
                //la segunda vez que se carga, será para crear un registro donde se informe en la base de datos el inicio de
                // la acción moler sobre los cereales seleccionados.
                Edificio edificio = oTareasBBean.molerCereales(formulario, mensajes, abadia, usuario);
                //generar mensaje avisando de la necesidad de asignar un monje al molino.
                Utilidades.eliminarRegistroContext(usuario.getNick());
                //formulario = new DatosMolinoActForm();
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
                return new ActionForward("/cerrarPopUp.do");
                //return actionMapping.findForward("/cerrarPopUp.do");
            }

            return actionMapping.findForward("success");
        } catch (LoteNoDisponibleException e) {
            saveMessages(request.getSession(), mensajes);
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver")));
            request.getSession().setAttribute("notificacion", notas);
            return actionMapping.findForward(Constantes.MENSAJES);
        } catch (ApplicationException e) {
            saveMessages(request.getSession(), mensajes);
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver")));
            request.getSession().setAttribute("notificacion", notas);
            return actionMapping.findForward(Constantes.MENSAJES);
        }
    }
}
