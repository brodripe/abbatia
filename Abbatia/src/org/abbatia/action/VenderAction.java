package org.abbatia.action;

import org.abbatia.actionform.MercadoVentaActForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.CantidadInsuficienteException;
import org.abbatia.exception.CompraCiudadException;
import org.abbatia.exception.EspacioInsuficienteException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
// Imports del Proyecto

public class VenderAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.

        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);

        MercadoVentaActForm datosVenta = (MercadoVentaActForm) actionForm;

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();
        Edificio edificio = null;
        String sAccion = mapping.getParameter();
        MercadoBBean oMercado;

        try {

            if (sAccion == null) {
                sAccion = "inicio";
            }
            //si el identificado de producto está informado
            //asumimos que debemos cargar el formulario de venta.
            if (sAccion.equals("inicio")) {
                datosVenta.setId(request.getParameter("lid"));
                datosVenta.setMercado(request.getParameter("merc"));

                oMercado = new MercadoBBean();
                oMercado.inicioVenta(datosVenta, abadia, usuario);
                request.getSession().setAttribute("VentaForm", datosVenta); // <--- nuevo???

                return mapping.findForward("success");
            }
            // Lo vamos a vender!!!!
            if (sAccion.equals("vender")) {
                oMercado = new MercadoBBean();
                edificio = oMercado.ejecutarVenta(datosVenta, abadia, usuario, resource);
                mensajes.add("msg", new ActionMessage("mensajes.info.productoalaventa"));
                saveMessages(request.getSession(), mensajes);

                if (edificio == null || edificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_MUROS) {
                    request.getSession().setAttribute("location", "/mercado_venta.do");
                    notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver")));
                } else {
                    request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
                    notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver.edificio", edificio.getNombre()), edificio.getGrafico_visualizacion() + "_" + edificio.getNivel() + ".gif", (short) 1));
                }
                request.getSession().setAttribute("notificacion", notas);

                request.getSession().setAttribute("VentaForm", new MercadoVentaActForm()); // <--- nuevo???
                return mapping.findForward("mensajes");
            }

            return mapping.findForward("failure");
        } catch (CompraCiudadException e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.comprapreciominimo"));
            saveMessages(request.getSession(), mensajes);
            if (e.getOEdificio() == null || e.getOEdificio().getIdDeTipoDeEdificio() == Constantes.EDIFICIO_MUROS) {
                request.getSession().setAttribute("location", "/mercado_venta.do");
                notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver")));
            } else {
                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + e.getOEdificio().getIdDeEdificio());
                notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver.edificio", e.getOEdificio().getNombre()), e.getOEdificio().getGrafico_visualizacion() + "_" + e.getOEdificio().getNivel() + ".gif", (short) 1));
            }
            request.getSession().setAttribute("notificacion", notas);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteException e) {
            //mostramos mensaje indicando que no se puede poner a la venta el producto por falta de espacio
            mensajes.add("msg", new ActionMessage("mensajes.info.espacioinsuficiente", Utilidades.redondear(e.getdVolumen() * e.getdCantidad()), Utilidades.redondear(e.getoEdificioMercado().getAlmacenamiento() - e.getoEdificioMercado().getAlmacenamientoActual())));
            saveMessages(request.getSession(), mensajes);
            //request.getSession().setAttribute("location", "javascript:windows.parent.cClick()");

            notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.volver.edificio", e.getoEdificio().getNombre()), e.getoEdificio().getGrafico_visualizacion() + "_" + e.getoEdificio().getNivel() + ".gif", (short) 0));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver"), (short) 1));
            request.getSession().setAttribute("notificacion", notas);
            return mapping.findForward("mensajes");

        } catch (CantidadInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.cantidadinsuficiente"));
            saveMessages(request.getSession(), mensajes);
            if (edificio == null) {
                notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.volver")));
                notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver")));
            } else {
                notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.volver.edificio", edificio.getNombre()), edificio.getGrafico_visualizacion() + "_" + edificio.getNivel() + ".gif", (short) 0));
                notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver"), (short) 1));
            }
            request.getSession().setAttribute("notificacion", notas);
            return mapping.findForward("mensajes");

        } catch (AbadiaException e) {
            return mapping.findForward("failure");
        }

    }
}
