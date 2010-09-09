package org.abbatia.bbean;

import org.abbatia.actionform.DatosElaboracionActForm;
import org.abbatia.adbean.adAlimentos;
import org.abbatia.adbean.adElaboracionAlimentos;
import org.abbatia.adbean.adRecurso;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.bean.datosElaboracion;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.EdificioNotFoundException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class ElaboracionBBean {
    private static Logger log = Logger.getLogger(ElaboracionBBean.class.getName());

    public void recuperarRequisitosElaboracion(DatosElaboracionActForm p_afDatosElaboracion,
                                               Usuario p_oUsuario, Abadia p_oAbadia,
                                               String p_sTipoElaboracion) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarRequisitosElaboracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElaboracionAlimentos oElaboracionAD;
        adAlimentos oAlimentosAD;
        adRecurso oRecursoAD;

        ArrayList alRequisitos;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos los requisitos de elaboracion para ese producto
            oElaboracionAD = new adElaboracionAlimentos(con);
            alRequisitos = oElaboracionAD.recuperarRequisitosPorProducto(p_oUsuario, p_afDatosElaboracion.getIdProducto());

            if (p_sTipoElaboracion.equals("A")) {
                //evaluar si se trata de alimentos o recursos para obtener descripción y unidad de medida
                oAlimentosAD = new adAlimentos(con);
                p_afDatosElaboracion.setDescProducto(oAlimentosAD.recuperarDescripcionAlimento(p_afDatosElaboracion.getIdProducto(), p_oUsuario));
                p_afDatosElaboracion.setUnidadMedida(oAlimentosAD.recuperarUnidadMedidaPorAlimento(p_afDatosElaboracion.getIdProducto(), p_oUsuario));
            } else {
                oRecursoAD = new adRecurso(con);
                p_afDatosElaboracion.setDescProducto(oRecursoAD.recuperarDescripcionRecurso(p_afDatosElaboracion.getIdProducto(), p_oUsuario));
                p_afDatosElaboracion.setUnidadMedida(oRecursoAD.recuperarUnidadMedidaPorRecurso(p_afDatosElaboracion.getIdProducto(), p_oUsuario));
            }

            p_afDatosElaboracion.setRequisitos(alRequisitos);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void registroElaboracion(datosElaboracion p_oDatosElaboracion, DatosElaboracionActForm p_afDatosElaboracion, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".registroElaboracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElaboracionAlimentos oElaboracionAD;

        int iElaboracionId;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oElaboracionAD = new adElaboracionAlimentos(con);
            p_oDatosElaboracion.setIdEdificio(oElaboracionAD.recuperarEdificioIdElaboracion(p_afDatosElaboracion.getIdProducto(), p_oAbadia));
            //buscamos posibles productos en elaboración o pendientes del mismo productos
            iElaboracionId = oElaboracionAD.recuperarIdElaboracion(p_oDatosElaboracion.getIdEdificio(), p_afDatosElaboracion.getIdProducto(), String.valueOf(Constantes.ESTADO_ELABORACION_PENDIENTE).concat(",").concat(String.valueOf(Constantes.ESTADO_ELABORACION_ELABORANDO)));
            //si el producto no existe..
            if (iElaboracionId == 0) {
                p_oDatosElaboracion.setIdProducto(p_afDatosElaboracion.getIdProducto());
                p_oDatosElaboracion.setCantidad(p_afDatosElaboracion.getCantidad());
                oElaboracionAD.insertarProductoElaboracion(p_oDatosElaboracion);
            } else {
                oElaboracionAD.sumarCantidadElaboracion(iElaboracionId, p_afDatosElaboracion.getCantidad());
            }


        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward eliminarElaboracion(int p_iClave, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarElaboracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElaboracionAlimentos oElaboracionAD;

        int iEdificioId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oElaboracionAD = new adElaboracionAlimentos(con);
            iEdificioId = oElaboracionAD.recuperarEdificioIdPorElaboracionId(p_iClave, p_oAbadia);
            //si el edificio devuelto es = 0 significa que la elaboracion no es de nuestra abbatia (tal vez un tranposillo...)
            if (iEdificioId == 0) {
                throw new EdificioNotFoundException("No se ha encontrado el edificio asociado", log);
            } else {
                //eliminamos el registro de elaboración
                oElaboracionAD.eliminarElaboracion(p_iClave);
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                return new ActionForward("/mostrarEdificio.do?clave=" + iEdificioId + "&Tab=elaboracion");
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward pausarElaboracion(int p_iClave, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".pausarElaboracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElaboracionAlimentos oElaboracionAD;

        int iEdificioId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oElaboracionAD = new adElaboracionAlimentos(con);
            iEdificioId = oElaboracionAD.recuperarEdificioIdPorElaboracionId(p_iClave, p_oAbadia);
            //si el edificio devuelto es = 0 significa que la elaboracion no es de nuestra abbatia (tal vez un tranposillo...)
            if (iEdificioId == 0) {
                throw new EdificioNotFoundException("No se ha encontrado el edificio asociado", log);
            } else {
                oElaboracionAD.actualizarEstado(p_oAbadia, p_iClave, Constantes.ESTADO_ELABORACION_PAUSADO);
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                return new ActionForward("/mostrarEdificio.do?clave=" + iEdificioId + "&Tab=elaboracion");
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward reanudarElaboracion(int p_iClave, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".pausarElaboracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElaboracionAlimentos oElaboracionAD;

        int iEdificioId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oElaboracionAD = new adElaboracionAlimentos(con);
            iEdificioId = oElaboracionAD.recuperarEdificioIdPorElaboracionId(p_iClave, p_oAbadia);
            //si el edificio devuelto es = 0 significa que la elaboracion no es de nuestra abbatia (tal vez un tranposillo...)
            if (iEdificioId == 0) {
                throw new EdificioNotFoundException("No se ha encontrado el edificio asociado", log);
            } else {
                oElaboracionAD.actualizarEstado(p_oAbadia, p_iClave, Constantes.ESTADO_ELABORACION_ELABORANDO);
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                return new ActionForward("/mostrarEdificio.do?clave=" + iEdificioId + "&Tab=elaboracion");
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}
