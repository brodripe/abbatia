package org.abbatia.action;

import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreExcepcion;
import org.abbatia.exception.EdificioNotFoundException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class MostrarEdificioAction extends Action {
    private static Logger log = Logger.getLogger(MostrarEdificioAction.class.getName());

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
                                 HttpServletResponse response) throws IOException,
            ServletException, SQLException, AbadiaException {

        //recupero del objeto request la clave de monje
        int iClave = Integer.parseInt(request.getParameter("clave"));
        int detalle = 0;
        String Tab = "init";
        boolean contenidos = true;
        if (request.getParameter("detalle") != null) detalle = Integer.parseInt(request.getParameter("detalle"));
        if (request.getParameter("Tab") != null) Tab = request.getParameter("Tab");
        if (!Tab.equals("init") && (!Tab.equals("salar")) && (!Tab.equals("alacena")) && (!Tab.equals("guisos")) &&
                (!Tab.equals("velatorio")) && (!Tab.equals("cementerio")) && (!Tab.equals("osario")))
            contenidos = false;

        //ActionErrors errors = new ActionErrors();
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);


        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        HashMap<String, String> hmParameters = new HashMap<String, String>();
        hmParameters.put("Tab", request.getParameter("Tab"));
        hmParameters.put("detalle", request.getParameter("detalle"));
        hmParameters.put("familia", request.getParameter("familia"));

        HashMap hmRequest;
        EdificioBBean oEdificioBBean;
        UsuarioBBean oUsuarioBBean;
        Edificio oEdificio;
        try {
            oEdificioBBean = new EdificioBBean();
            hmRequest = oEdificioBBean.recuperarDetalleEdificio(iClave,
                    contenidos, abadia, usuario, resource, form, detalle, hmParameters);

            //hmRequest.put("Edificio", oEdificio);
            oEdificio = (Edificio) hmRequest.get("Edificio");
            if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_ABADIA) {
                return mapping.findForward("inicio");
            }

            if (hmRequest.containsKey("MonjesEdificio")) {
                request.setAttribute("MonjesEdificio", hmRequest.get("MonjesEdificio"));
            }

            if (hmRequest.containsKey("consumos")) {
                request.setAttribute("consumos", hmRequest.get("consumos"));
            }

            if (hmRequest.containsKey("elaborables")) {
                request.setAttribute("elaborables", hmRequest.get("elaborables"));
            }

            if (hmRequest.containsKey("datosElaboracion")) {
                request.setAttribute("datosElaboracion", hmRequest.get("datosElaboracion"));
            }

            if (hmRequest.containsKey(Constantes.TABLA_ABADIAS)) {
                request.setAttribute(Constantes.TABLA_ABADIAS, hmRequest.get(Constantes.TABLA_ABADIAS));
            }

            if (hmRequest.containsKey(Constantes.TABLA_LIBROS)) {
                request.setAttribute(Constantes.TABLA_LIBROS, hmRequest.get(Constantes.TABLA_LIBROS));
            }

            if (hmRequest.containsKey(Constantes.TABLA_IDIOMAS)) {
                request.setAttribute(Constantes.TABLA_IDIOMAS, hmRequest.get(Constantes.TABLA_IDIOMAS));
            }

            if (hmRequest.containsKey(Constantes.TABLA_REGIONES)) {
                request.setAttribute(Constantes.TABLA_REGIONES, hmRequest.get(Constantes.TABLA_REGIONES));
            }
            if (hmRequest.containsKey(Constantes.TABLA_CATEGORIAS)) {
                request.setAttribute(Constantes.TABLA_CATEGORIAS, hmRequest.get(Constantes.TABLA_CATEGORIAS));
            }

            if (hmRequest.containsKey("FiltroLibros")) {
                request.setAttribute("FiltroLibros", hmRequest.get("FiltroLibros"));
            }


            request.setAttribute("Edificio", hmRequest.get("Edificio"));
            request.setAttribute("DatosNivel", hmRequest.get("DatosNivel"));
            request.setAttribute("Detalle", hmRequest.get("Detalle"));
            request.setAttribute("Tab", hmRequest.get("Tab"));

            if (request.getParameter("pagesize") != null) {
                request.getSession().setAttribute("pagesize", Integer.valueOf(request.getParameter("pagesize")));
            }

            request.getSession().removeAttribute("notificacion");
            return mapping.findForward("success");

        } catch (EdificioNotFoundException e) {
            if (CoreExcepcion.controlExcepciones(e.getClass().getName(), request.getSession()) == 1) {
                mensajes.add("msg", new ActionMessage("mensajes.aviso.tramposo.accesoedificio"));
                log.info("Bloqueamos el usuario con nick: " + usuario.getNick() + " por tratar de colarse en un edificio de otra abbatia.");
                oUsuarioBBean = new UsuarioBBean();
                oUsuarioBBean.bloquearUsuario(usuario.getIdDeUsuario(), 2, e.getClass().getName());
                request.getSession().invalidate();
            } else {
                mensajes.add("msg", new ActionMessage("mensajes.error.edificionoexiste"));
            }
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");

        } catch (AbadiaException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.aplicacion"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("error");
        }
    }
}

