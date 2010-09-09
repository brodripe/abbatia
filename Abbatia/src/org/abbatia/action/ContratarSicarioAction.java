package org.abbatia.action;


import org.abbatia.bbean.SicarioBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bbean.UtilsBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
// Abbatia


public class ContratarSicarioAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        ActionMessages mensajes = new ActionMessages();

        UtilsBBean oUtilsBBean;
        UsuarioBBean oUsuarioBBean;
        SicarioBBean oSicarioBBean;

        int confirmar = 0, monjeid = 0, sicarioid = 0;
        ArrayList alTable;
        ArrayList<Notificacion> alNotas = new ArrayList<Notificacion>();
        HashMap hmRequest;

        oUtilsBBean = new UtilsBBean();

        String sPuntos = oUtilsBBean.recuperarPropiedadValor(Constantes.PROPIEDAD_TIPO_PARAMETROS, Constantes.PROPIEDAD_PUNTOS_SICARIO, "P");
        double dPuntos = Double.parseDouble(sPuntos);
        if (abadia.getPuntuacion() < dPuntos) {
            oUsuarioBBean = new UsuarioBBean();
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.sicario.puntosinsuficientes", Utilidades.redondear(dPuntos)));
            oUsuarioBBean.bloquearUsuario(usuario.getIdDeUsuario(), 4, "intenta contratar un sicario sin tener los puntos suficientes");
            request.getSession().invalidate();
            alNotas.add(new Notificacion("index_main.do", "Inicio"));
            request.getSession().setAttribute("notificacion", alNotas);
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward(Constantes.MENSAJES);
        }

        // opciones por parametro
        if (request.getParameter("confirmar") != null)
            confirmar = Integer.parseInt(request.getParameter("confirmar"));
        if (request.getParameter("monjeid") != null)
            monjeid = Integer.parseInt(request.getParameter("monjeid"));
        if (request.getParameter("sicarioid") != null)
            sicarioid = Integer.parseInt(request.getParameter("sicarioid"));

        // mostrar lista de sicarios
        oSicarioBBean = new SicarioBBean();
        if (confirmar == 0) {

            alTable = oSicarioBBean.recuperarSicarios(abadia, usuario);

            request.setAttribute("Contents", alTable);
            return mapping.findForward("success");
        }
        // preguntar al usuario si está seguro y mostrar el precio
        if (confirmar == 1) {
            hmRequest = oSicarioBBean.contratarSicario_1(sicarioid, monjeid, abadia, resource, mensajes);
            request.setAttribute("DatosContents", hmRequest.get("DatosContents"));
            //Intentas asesinar a una eminencia de tu propia abadía
            if (hmRequest.containsKey("notificacion")) {
                request.getSession().setAttribute("notificacion", hmRequest.get("notificacion"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }
            //validación de sicario incorrecta
            if (!mensajes.isEmpty()) {
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("failure");
            }
            //  Sets
            request.setAttribute("Nom_Sicario", hmRequest.get("Nom_Sicario"));
            request.setAttribute("Nom_Monje", hmRequest.get("Nom_Monje"));
            request.setAttribute("Nom_Abadia", hmRequest.get("Nom_Abadia"));
            request.setAttribute("Nom_Region", hmRequest.get("Nom_Region"));
            request.setAttribute("Dias", hmRequest.get("Dias"));
            request.setAttribute("Precio", hmRequest.get("Precio"));
            //
            request.setAttribute("monjeid", hmRequest.get("monjeid"));
            request.setAttribute("sicarioid", hmRequest.get("sicarioid"));
            //
            return mapping.findForward("confirmar");
        }
        // preguntar al usuario si está seguro y mostrar el precio
        if (confirmar == 2) {
            hmRequest = oSicarioBBean.contratarSicario_2(abadia, sicarioid, monjeid, mensajes, resource);
            //
            if (hmRequest.containsKey("notificacion")) {
                request.getSession().setAttribute("notificacion", hmRequest.get("notificacion"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }
            if (!mensajes.isEmpty()) {
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("failure");
            }

            // Tenemos dinero?
            // Mensaje
            alNotas.add(new Notificacion("poblacion.do", "Volver a la población"));
            request.getSession().setAttribute("notificacion", alNotas);
            mensajes.add("msg", new ActionMessage("sicario.confirmar.contratado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }

        return mapping.findForward("success");
    }
}

