package org.abbatia.action;

import org.abbatia.actionform.DatosVentaAgrupadaActForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreExcepcion;
import org.abbatia.exception.CantidadInsuficienteException;
import org.abbatia.exception.EspacioInsuficienteEnCocina;
import org.abbatia.exception.ProductoNoEncontradoException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
// Imports del Proyecto

public class VenderAgrupadoAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.

        ActionMessages mensajes = new ActionMessages();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MessageResources resource = getResources(request);
        String lid;
        String merc;
        DatosVentaAgrupadaActForm datosVenta = (DatosVentaAgrupadaActForm) actionForm;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        Edificio edificio;
        String sAccion = mapping.getParameter();
        if (sAccion == null) {
            sAccion = "inicio";
        }

        MercadoBBean oMercadoBBEan;
        UsuarioBBean oUsuarioBBean;
        try {

            oMercadoBBEan = new MercadoBBean();
            if (sAccion.equals("inicio")) {
                lid = request.getParameter("lid");
                merc = request.getParameter("merc");
                //si el identificado de producto está informado
                //asumimos que debemos cargar el formulario de venta.
                datosVenta.setIdProducto(Integer.parseInt(lid));
                datosVenta.setMercancia(merc);

                Alimento alimento = oMercadoBBEan.ventaAgrupadaInicio(datosVenta, abadia, usuario);
                //VErificamos si el usuario lleva menos de un mes abbatia jugando
                datosVenta.setDescripcionFamilia(alimento.getDescFam());
                datosVenta.setDescripcionProducto(alimento.getDescAli());
                datosVenta.setPrecio(alimento.getPrecioMercado());
                //calculamos el precio al que la ciudad compraria el producto...
                datosVenta.setPrecioVentaCiudad(Utilidades.redondear(Utilidades.formatStringToDouble(alimento.getPrecioMercado()) / 4));
                datosVenta.setPrecioMercado(alimento.getPrecioMercado());
                request.getSession().setAttribute("VentaAgrupadaForm", datosVenta); // <--- nuevo???
                return mapping.findForward("success");

            } else if (sAccion.equals("vender")) {
                // Lo vamos a vender!!!!
                //verificamos el que el valor precio sea positivo.
                if (!GenericValidator.isBlankOrNull(datosVenta.getPrecio())) {
                    double dPrecio = Utilidades.formatStringToDouble(datosVenta.getPrecio());
                    if (dPrecio < 0.1) {
                        mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.importe.negativo"));
                        saveMessages(request.getSession(), mensajes);
                        notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver")));
                        request.getSession().setAttribute("notificacion", notas);
                        return mapping.findForward(Constantes.MENSAJES);
                    }
                } else {
                    mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cantidad.incorrecta"));
                    saveMessages(request.getSession(), mensajes);
                    notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver")));
                    notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.cerrar"), (short) 1));
                    request.getSession().setAttribute("notificacion", notas);
                    return mapping.findForward(Constantes.MENSAJES);
                }

                edificio = oMercadoBBEan.ventaAgrupadaConfirmacion(datosVenta, abadia, usuario, resource, mensajes, notas);

                request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
                //notas.add(new Notificacion("/cerrarPopUp.do", resource.getMessage("mensajes.link.volver.edificio", edificio.getNombre()), edificio.getGrafico_visualizacion() + "_" + edificio.getNivel() + ".gif", (short) 1));

                saveMessages(request.getSession(), mensajes);
                //reseteamos el actionform
                request.getSession().setAttribute("VentaAgrupadaForm", new DatosVentaAgrupadaActForm()); // <--- nuevo???
                request.getSession().setAttribute("notificacion", notas);
                return mapping.findForward("mensajes");
            }

            return mapping.findForward("failure");
        } catch (CantidadInsuficienteException e) {
            mensajes.add("msg", new ActionMessage("mensajes.info.cantidadinsuficiente"));
            saveMessages(request.getSession(), mensajes);
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.link.volver")));
            notas.add(new Notificacion("javascript:window.parent.cClick();", resource.getMessage("mensajes.link.cerrar"), (short) 1));
            request.getSession().setAttribute("notificacion", notas);
            return mapping.findForward("mensajes");
        } catch (ProductoNoEncontradoException e) {
            //si se produce esta excepción, deberiamos registrar en la sesión un contador para bloquear
            //la abbatia si se produce este error más de x veces.
            if (CoreExcepcion.controlExcepciones(e.getClass().getName(), request.getSession()) == 1) {
                mensajes.add("msg", new ActionMessage("mensajes.aviso.tramposo.ventasdobles"));
                oUsuarioBBean = new UsuarioBBean();
                oUsuarioBBean.bloquearUsuario(usuario.getIdDeUsuario(), 2, Constantes.BLOQUEO_VENTAS_INEXISTENTES);
                request.getSession().invalidate();
            } else {
                mensajes.add("msg", new ActionMessage("mensajes.aviso.lotesnodisponibles"));
            }

            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteEnCocina e) {
            saveMessages(request.getSession(), mensajes);
            request.getSession().setAttribute("notificacion", notas);
            return mapping.findForward("mensajes");
        }

    }
}
