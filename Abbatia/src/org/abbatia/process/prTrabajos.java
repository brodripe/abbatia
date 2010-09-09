package org.abbatia.process;

import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosBBean;
import org.abbatia.process.bbean.ProcesosLibrosBBean;
import org.abbatia.process.bbean.ProcesosSolicitudesBBean;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;

import java.util.ArrayList;
import java.util.Hashtable;

public class prTrabajos extends ProcesosUtils {

    public static void trabajar_monje(int periodo) throws AbadiaException {

        Hashtable htStress;
        ArrayList alMonjes;

        ProcesosBBean oProcesosBBean;
        ProcesosSolicitudesBBean oSolicitudesBBean;
        ProcesosLibrosBBean oLibrosBBean;

        oProcesosBBean = new ProcesosBBean();
        oSolicitudesBBean = new ProcesosSolicitudesBBean();
        oLibrosBBean = new ProcesosLibrosBBean();
        // Al empezar a rezar por la mañana.... poner los estados a 0 y acumularlo a total
        if (periodo == Constantes.PERIODO_MAITINES) {
            // Actualizar las actividades y poner a 0 los acumulados y el realizado
            oProcesosBBean.resetActividades();

            // Establecer las cantidades de recogidas para no recoger varias veces el mismo animal por el mismo monje o por otro
            //procesos = new adProcesos();
            oProcesosBBean.eliminarMensajesActividadesAntiguos(-7);
            htStress = oProcesosBBean.cargarStressPorEdificio();

            oProcesosBBean.ganaderia_producirAlimentos(htStress);
            oProcesosBBean.ganaderia_producirRecursos(htStress);
        }

        //proceso de solicitudes
        oSolicitudesBBean.gestion_solicitudes();

        // Realizar los trabajos
        if ((periodo == Constantes.PERIODO_MAITINES) || (periodo == Constantes.PERIODO_LAUDES) || (periodo == Constantes.PERIODO_ANGELUS)) {
            oProcesosBBean.rezar(periodo);
        }
        if ((periodo == Constantes.PERIODO_PRIMA) || (periodo == Constantes.PERIODO_TERCIA) || (periodo == Constantes.PERIODO_NONA) || (periodo == Constantes.PERIODO_VISPERAS)) {

            oLibrosBBean.restaurarLibros(periodo);

            oProcesosBBean.teach(periodo);

            oLibrosBBean.copiar(periodo);

            oProcesosBBean.curar(periodo);

            alMonjes = oProcesosBBean.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_GANADERIA);
            oProcesosBBean.ganaderiaAlimentos(periodo, alMonjes);
            oProcesosBBean.ganaderiaRecursos(periodo, alMonjes);

            oProcesosBBean.rezar(periodo);

            oProcesosBBean.abad(periodo);

            oProcesosBBean.gestionAgricultura(periodo);

            oProcesosBBean.talar_arboles(periodo);

            oProcesosBBean.pescar(periodo);

            oProcesosBBean.moler(periodo);

            //elaboracion de alimentos
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELEBORAR_ALIMENTO, Constantes.EDIFICIO_COCINA, Constantes.MONJE_VIVO);
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELEBORAR_ALIMENTO, Constantes.EDIFICIO_COCINA, Constantes.MONJE_VISITA);

            oProcesosBBean.elaborarProductosEnReposo(Constantes.TAREA_ELEBORAR_ALIMENTO);

            //elaboracion de recursos
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_RECURSO, Constantes.EDIFICIO_ALMACEN, Constantes.MONJE_VIVO);
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_RECURSO, Constantes.EDIFICIO_ALMACEN, Constantes.MONJE_VISITA);

            oProcesosBBean.elaborarProductosEnReposo(Constantes.TAREA_ELABORAR_RECURSO);

            //elaboracion de costura
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_COSTURA, Constantes.EDIFICIO_TALLER_COSTURA, Constantes.MONJE_VIVO);
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_COSTURA, Constantes.EDIFICIO_TALLER_COSTURA, Constantes.MONJE_VISITA);

            oProcesosBBean.elaborarProductosEnReposo(Constantes.TAREA_ELABORAR_COSTURA);

            //elaboracion de artesania
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_ARTESANIA, Constantes.EDIFICIO_TALLER_ARTESANIA, Constantes.MONJE_VIVO);
            oProcesosBBean.elaborar(periodo, Constantes.TAREA_ELABORAR_ARTESANIA, Constantes.EDIFICIO_TALLER_ARTESANIA, Constantes.MONJE_VISITA);

            oProcesosBBean.elaborarProductosEnReposo(Constantes.TAREA_ELABORAR_ARTESANIA);

        }

        // Si hemos realizado todas las tareas... entonces aumentar las habilidades
        if (periodo == Constantes.PERIODO_ANGELUS) {
            oProcesosBBean.control_habilidades_fe(Constantes.MONJE_VIVO);
            oProcesosBBean.control_habilidades_fe(Constantes.MONJE_VISITA);
            oProcesosBBean.control_habilidades_resto();
        }

        // *************************************************************************************************************
        //Ejecuta la actualizacion para decrementar los consumos de proteina, lipidos... en función de las tareas
        //ajusta los valores maximos y minimos de las proteinas, lipidos...
        //marca el periodo como realizado...
        oProcesosBBean.resetearAlimentacionMonjes(periodo);

    }

}


