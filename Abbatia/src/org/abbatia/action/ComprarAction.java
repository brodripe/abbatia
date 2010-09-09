package org.abbatia.action;

import org.abbatia.actionform.MercadoCompraForm;
import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Notificacion;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.*;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ComprarAction extends Action {
    private static Logger log = Logger.getLogger(ComprarAction.class.getName());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {
        //este metodo cargará un actionForm con los mensajes para mostrar.
        ActionMessages mensajes = new ActionMessages();
        MessageResources resource = getResources(request);

        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();

        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        MercadoBBean oMercadoBBean;
        EdificioBBean oEdificioBBean;
        HashMap<String, Serializable> hmRequest;

        try {
            MercadoCompraForm datosCompra = (MercadoCompraForm) actionForm;
            log.debug("ComprarAction. accion: " + datosCompra.getAccion());
            String sProductoId = request.getParameter("pid");

            if (datosCompra.getAccion() == null && sProductoId == null) {
                notas.add(new Notificacion("mercado_compra.do", resource.getMessage("mensajes.mercado.volver")));
                //request.getSession().setAttribute("notificacion", notas);
                request.setAttribute("notificacion", notas);
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.sinproducto"));
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }
            oMercadoBBean = new MercadoBBean();
            if (sProductoId != null) {
                hmRequest = oMercadoBBean.inicioCompra(datosCompra, sProductoId, abadia, usuario, mensajes);
                if (!mensajes.isEmpty()) {
                    saveMessages(request.getSession(), mensajes);
                    return mapping.findForward("mensajes");
                }
                request.getSession().setAttribute("monedas", hmRequest.get("monedas"));
                //request.setAttribute("monedas", hmRequest.get("monedas"));
                //request.getSession().setAttribute("MercadoCompraForm", datosCompra); // <--- nuevo???
                request.setAttribute("MercadoCompraForm", datosCompra); // <--- nuevo???
                return mapping.findForward("success");
            }
            // Accion de comprar
            if (datosCompra.getAccion().equals("comprar")) {

                mensajes.add(ActionMessages.GLOBAL_MESSAGE, oMercadoBBean.procesarCompra(resource, abadia, usuario, datosCompra));
                saveMessages(request.getSession(), mensajes);

                oEdificioBBean = new EdificioBBean();
                notas = oEdificioBBean.recuperarLinksEdificios(abadia, usuario);
                notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
                request.getSession().setAttribute("notificacion", notas);
                request.setAttribute("notificacion", notas);
                if (datosCompra.isVolverAlMercado()) {
                    request.getSession().setAttribute("location", "/mercado_compra_detalle.do");
                    //request.setAttribute("location", "/mercado_compra_detalle.do");
                } else {
                    request.getSession().setAttribute("location", "/MostrarMensaje.do");
                    //request.setAttribute("location", "/MostrarMensaje.do");
                }
                return mapping.findForward("cerrarPopUp");
            }

            return mapping.findForward("failure");

        } catch (CantidadErroneaException e) {
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.cantidaderronea"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (OroInsuficienteException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinoro"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteEnGranja e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinespacioengranja"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteEnGranero e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinespacioengranero"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteEnCocina e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinespacioencocina"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (EspacioInsuficienteEnAlmacen e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinespacioenalmacen"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (NoExisteAlmacenException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinalmacen"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (NoExisteEstabloException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.sinestablo"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (MoreExpensiveException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.productomuycaro"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (CalculoComisionTransitoException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.calculoruta.error"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (CompraNoPermitidaException e) {
            //notas.add(new Notificacion("mercado_compra_agrupado.do", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short)1));
            notas.add(new Notificacion("javascript:history.back(1);", resource.getMessage("mensajes.mercado.volver"), "mercado_historico.gif", (short) 1));
            //request.getSession().setAttribute("notificacion", notas);
            request.setAttribute("notificacion", notas);
            mensajes.add("msg", new ActionMessage("mensajes.aviso.compra.mismaip"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}
