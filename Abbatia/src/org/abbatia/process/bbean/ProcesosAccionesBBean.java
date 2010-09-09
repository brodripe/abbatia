package org.abbatia.process.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bean.Solicitud;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Gestiona el proceso de aprobación/denegacion de solicitudes
 */
public class ProcesosAccionesBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosAccionesBBean.class.getName());

    public ProcesosAccionesBBean() throws AbadiaDBConnectionException {
        super();
    }


    public void accionViajarCopiarSi(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarCopiarSi()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adCampo oCampoAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            oCampoAD = new adCampo(con);

            // Marcar el monje con estado 3 para el viaje ( VIAJE )
            //adMonje monjeAD = new adMonje();
            oMonjeAD.actualizarEstado(solicitud.getIdMonje(), Constantes.MONJE_VIAJE);
            //monjeAD.finalize();

            //miramos si se viaja a caballo
            oViajarAD.iniciarViajeMonje(solicitud.getIdMonje());

            //elimino los posibles registros existentes en la tabla campo_monje
            oCampoAD.eliminarRegistroCampoMonje(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void accionViajarCopiarNo(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarCopiarNo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adAnimal oAnimalAD;
        adLibros oLibrosAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);
            oLibrosAD = new adLibros(con);

            oViajarAD.devolverCosteViaje(solicitud);
            //actualizamos el estado de los animales que estaban de viaje a 0
            ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(solicitud.getIdMonje());

            for (int idAnimale : idAnimales) {
                oAnimalAD.actualizarEstado(idAnimale, Constantes.ESTADO_ANIMAL_VIVO);
            }

            //se elimina el registro de viaje
            oViajarAD.eliminarViajePorIdMonje(solicitud.getIdMonje());

            //eliminar los resgistros temporales de actividades del monje
            oLibrosAD.eliminarTareasCopiaPorMonje(solicitud.getIdMonje());

            //eliminar bloqueos sobre actividades de la abbatia destino relativas al libro
            //que se solicito copiar.
            oMonjeAD.desbloquarActividadesCopia(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void accionViajarCopiarEliminar(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarCopiarEliminar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adAnimal oAnimalAD;
        adLibros oLibrosAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);
            oLibrosAD = new adLibros(con);

            oViajarAD.devolverCosteViaje(solicitud);
            //actualizamos el estado de los animales que estaban de viaje a 0
            ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(solicitud.getIdMonje());

            for (int idAnimale : idAnimales) {
                oAnimalAD.actualizarEstado(idAnimale, Constantes.ESTADO_ANIMAL_VIVO);
            }
            //eliminar el registro de viaje
            oViajarAD.eliminarViajePorIdMonje(solicitud.getIdMonje());

            //eliminar los resgistros temporales de actividades del monje
            oLibrosAD.eliminarTareasCopiaPorMonje(solicitud.getIdMonje());

            //eliminar bloqueos sobre actividades de la abbatia destino relativas al libro en la tabla: libro_tarea
            //que se solicito copiar.
            oMonjeAD.desbloquarActividadesCopia(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    public void accionViajarSi(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarSi()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adCampo oCampoAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            oCampoAD = new adCampo(con);

            oMonjeAD.actualizarEstado(solicitud.getIdMonje(), Constantes.MONJE_VIAJE);
            //monjeAD.finalize();

            //elimino los posibles registros existentes en la tabla campo_monje
            oCampoAD.eliminarRegistroCampoMonje(solicitud.getIdMonje());

            oViajarAD.iniciarViajeMonje(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void accionViajarNo(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarNo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adViajar oViajarAD;
        adAnimal oAnimalAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);

            //se devuelve el importe del viaje
            oViajarAD.devolverCosteViaje(solicitud);
            //adViajar viajarAD = new adViajar();
            //actualizamos el estado de los animales que estaban de viaje a 0
            ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(solicitud.getIdMonje());
            //viajarAD.finalize();

            //adAnimal animalAD = new adAnimal();
            for (int idAnimale : idAnimales) {
                oAnimalAD.actualizarEstado(idAnimale, Constantes.ESTADO_ANIMAL_VIVO);
            }
            //animalAD.finalize();

            //se elimina el registro de viaje
            oViajarAD.eliminarViajePorIdMonje(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void accionViajarEliminar(Solicitud solicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".accionViajarEliminar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adViajar oViajarAD;
        adAnimal oAnimalAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);

            oViajarAD.devolverCosteViaje(solicitud);
            //actualizamos el estado de los animales que estaban de viaje a 0
            ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(solicitud.getIdMonje());

            for (int idAnimale : idAnimales) {
                oAnimalAD.actualizarEstado(idAnimale, Constantes.ESTADO_ANIMAL_VIVO);
            }

            //eliminar el registro de viaje
            oViajarAD.eliminarViajePorIdMonje(solicitud.getIdMonje());

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

}