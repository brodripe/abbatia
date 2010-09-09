package org.abbatia.action;

import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;


/**
 * Created by Benjamín Rodríguez.
 * User: benjamin.rodriguez
 * Date: 07-abr-2005
 * Time: 23:22:42
 */
public class EliminarPeriodoLibroCopiaAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        ActionMessages mensajes = new ActionMessages();
        ActionForward af;

        //recuperamos el atributo recibido (clave del libro)
        String clave = request.getParameter("clave");
        int idLibro;
        int idPeriodo;

        LibroBBean oLibroBBean;

        try {
            StringTokenizer st = new StringTokenizer(clave, ";");

            //extraigo el idlibro de la clave recibida
            idLibro = Integer.valueOf(st.nextToken());
            //extraigo el idperiodo de la clave recibida
            idPeriodo = Integer.valueOf(st.nextToken());

            oLibroBBean = new LibroBBean();
            af = oLibroBBean.eliminarPeriodoCopia(idLibro, idPeriodo, usuario, abadia, mensajes, actionMapping);
            if (!mensajes.isEmpty()) {
                saveMessages(request.getSession(), mensajes);
            }
            return af;

        } catch (NumberFormatException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.parametroincorrecto"));
            saveMessages(request.getSession(), mensajes);
            return actionMapping.findForward("mensajes");
        }
    }
}
