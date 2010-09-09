package org.abbatia.action;

import org.abbatia.actionform.LibroDetalleActForm;
import org.abbatia.bbean.LibroBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsignarPrecioCopiaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        LibroDetalleActForm libro = (LibroDetalleActForm) form;
        String idLibro = request.getParameter("clave");
        ActionForward af = null;
        String precioCopiaS;
        LibroBBean oLibroBBean;

        Edificio oEdificio;

        oLibroBBean = new LibroBBean();
        if (libro.getAccion().equals("inicio")) {
            //recuperamos el objeto libro
            libro = oLibroBBean.recuperarDetalleLibro(Integer.parseInt(idLibro), usuario, resource);
            //verificamos que se trata de un libro de nuestra abbatia
            if (libro.getAbadiaid() == abadia.getIdDeAbadia()) {
                request.setAttribute("libroDetalle", libro);
                af = mapping.findForward("success");

            } else {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.asignarprecio.abadiaincorrecta"));
                saveErrors(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            }
        } else if (libro.getAccion().equals("actualizar")) {

            precioCopiaS = libro.getPrecioCopiaS();
            //recuperamos el objeto libro
            libro = oLibroBBean.recuperarDetalleLibro(libro.getIdLibro(), usuario, resource);

            try {
                libro.setPrecioCopia(Utilidades.formatStringToDouble(precioCopiaS));
            } catch (NullPointerException e) {
                request.setAttribute("libroDetalle", libro);
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.asignarprecio.precioincorrecto", libro.getPrecioMin(), libro.getPrecioMax()));
                saveErrors(request.getSession(), mensajes);
                return mapping.findForward("success");
            }
            //verificamos que se trata de un libro de nuestra abbatia
            if (libro.getAbadiaid() == abadia.getIdDeAbadia()) {
                //verificamos que el precio establecido se encuentra en los margenes permitidos
                if (libro.getPrecioCopia() > Utilidades.formatStringToDouble(libro.getPrecioMax()) || libro.getPrecioCopia() < Utilidades.formatStringToDouble(libro.getPrecioMin())) {
                    request.setAttribute("libroDetalle", libro);
                    mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.asignarprecio.precioincorrecto", libro.getPrecioMin(), libro.getPrecioMax()));
                    saveErrors(request.getSession(), mensajes);
                    af = mapping.findForward("success");
                } else {
                    //actualizamos el precio de copia del libro.
                    oEdificio = oLibroBBean.ActualizarPrecioCopia(abadia, usuario, libro);
                    //debería mostrar una página con el resumen.
                    Utilidades.eliminarRegistroContext(usuario.getNick());
                    request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                    af = mapping.findForward("cerrarPopUp");
                }

            } else {
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.asignarprecio.abadiaincorrecta"));
                saveErrors(request.getSession(), mensajes);
                af = mapping.findForward("mensajes");
            }
        }

        return af;

    }
}