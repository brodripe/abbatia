package org.abbatia.bbean;

import org.abbatia.actionform.DatosMolinoActForm;
import org.abbatia.adbean.adAlimentoLotes;
import org.abbatia.adbean.adAlimentos;
import org.abbatia.adbean.adEdificio;
import org.abbatia.adbean.adMolino;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.LoteNoDisponibleException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.ApplicationException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class TareasBBean {
    private static Logger log = Logger.getLogger(TareasBBean.class.getName());

    public void molerCerealesInicio(DatosMolinoActForm p_afFormulario, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".molerCerealesInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAlimentos oAlimentosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAlimentosAD = new adAlimentos(con);
            //cambiar por "recuperarAlimentosParaMolino"
            ArrayList listaCereales = oAlimentosAD.recuperarAlimentosParaMolino(p_oAbadia, p_oUsuario);
            p_afFormulario.setProductos(listaCereales);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Edificio molerCereales(DatosMolinoActForm p_afFormulario, ActionMessages p_oMensajes, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".molerCereales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAlimentos oAlimentosAD;
        adAlimentoLotes oAlimentoLotesAD;
        adMolino oMolinoAD;
        adEdificio oEdificioAD;

        AlimentoLote oAlimentoLote = null;
        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oAlimentosAD = new adAlimentos(con);
            oAlimentoLotesAD = new adAlimentoLotes(con);
            //la segunda vez que se carga, será para crear un registro donde se informe en la base de datos el inicio de
            // la acción moler sobre los cereales seleccionados.

            //validar disponibilidad de recursos..cantidad
            if (p_afFormulario.getSeleccion() == 0) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.nadaseleccionado"));
            } else if (p_afFormulario.getCantidad() < 1) {
                p_oMensajes.add("msg", new ActionMessage("error.cantidad.incorrecta"));
            } else {
                oAlimentoLote = oAlimentoLotesAD.recuperarAlimentoLote(p_afFormulario.getSeleccion());

                if (oAlimentoLote == null) {
                    p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.lotesnodisponibles"));
                    throw new LoteNoDisponibleException(sTrace + " Lote no disponible", log);
                }

                if (oAlimentoLote.getCantidad() < p_afFormulario.getCantidad()) {
                    p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.cantidadinsuficiente"));
                }
            }
            if (!p_oMensajes.isEmpty()) {
                ArrayList listaCereales = oAlimentosAD.recuperarAlimentosParaMolino(p_oAbadia, p_oUsuario);
                p_afFormulario.setProductos(listaCereales);
                throw new ApplicationException(sTrace + " Valor incorrecto", null, log);
            }
            //insertar registro en tabla molino
            ProductoMolino producto = new ProductoMolino();
            producto.setProductoid_entrada(oAlimentoLote.getIdAlimento());
            producto.setAbadiaid(p_oAbadia.getIdDeAbadia());
            producto.setFecha_inicio(CoreTiempo.getTiempoAbadiaString());
            producto.setCantidad_ini(p_afFormulario.getCantidad());
            producto.setCantidad_act(p_afFormulario.getCantidad());

            oMolinoAD = new adMolino(con);
            producto.setRecursoid_salida(oMolinoAD.recuperarRecursoPorProducto(p_oAbadia, oAlimentoLote.getIdAlimento()));
            if (oMolinoAD.existeProductoMolino(p_oAbadia.getIdDeAbadia(), oAlimentoLote.getIdAlimento()))
            {
                oMolinoAD.actualizarProductoMolino(producto);
            }else
            {
                oMolinoAD.insertarProductoMolino(producto);
            }

            //restar recurso del lote seleccionado
            oAlimentoLotesAD.ModificarCantidad(p_afFormulario.getSeleccion(), oAlimentoLote.getCantidad() - p_afFormulario.getCantidad());


            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_MOLINO, p_oAbadia, p_oUsuario);

            ConnectionFactory.commitTransaction(con);

            return oEdificio;
        } catch (Exception e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new SystemException(sTrace + "SistemException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
