package org.abbatia.process;

import org.abbatia.adbean.adUtils;
import org.abbatia.base.ProcesoBase;
import org.abbatia.bean.datosProceso;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.AbadiaClassForNameException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class prMain {
    private static Logger log = Logger.getLogger(prMain.class.getName());


    public void lanzar(datosProceso proceso, Connection con) throws AbadiaException {
        // averiguar el ID
        adUtils utils;
        adProcesos procesos;

        String fecharealstart = CoreTiempo.getTiempoRealStringConHoras();
        String fechalastrun = CoreTiempo.getTiempoAbadiaStringConHoras();
        //String fechanextrun   = "";
        utils = new adUtils(con);
        utils.execSQL("UPDATE `procesos` SET fecharealstart='" + fecharealstart + "', fecharealend=null WHERE procesoid = " + proceso.getIdProceso());

        procesos = new adProcesos(con);
        procesos.addLog("++ ARRANCANDO: " + proceso.getName(), 2);

        //aqui debemos recuperar la fecha del día en curso para garantizar que no se avance el día mientras no se ejecuten todos los procesos...
        String sFechaRun = utils.getPropiedad(99, "V", 1, CoreTiempo.getTiempoAbadiaString());
        //incremento el la fecha en función del intervalo programado en el proceso.
        sFechaRun = CoreTiempo.sumarDias(sFechaRun, proceso.getIntervalo());
        //necesitamos calcular la planficiacion en función del intervalo especificado en el proceso...
        sFechaRun = sFechaRun + " " + proceso.getHora();

        try {
            ProcesoBase pr = (ProcesoBase) Class.forName(proceso.getClase()).newInstance();
            pr.run(proceso.getParametro());
            utils.execSQL("UPDATE `procesos` SET fechalastrun='" + fechalastrun + "', fecharun='" + sFechaRun + "' , fecharealend='" + CoreTiempo.getTiempoRealStringConHoras() + "' WHERE procesoid = " + proceso.getIdProceso());
            procesos.addLog("-- FINALIZADO: " + proceso.getName(), 3);

        } catch (ClassNotFoundException e) {
            throw new AbadiaClassForNameException("prMain. lanzar. ClassNotFoundException", e, log);
        } catch (InstantiationException e) {
            throw new AbadiaClassForNameException("prMain. lanzar. InstantiationException", e, log);
        } catch (IllegalAccessException e) {
            throw new AbadiaClassForNameException("prMain. lanzar. IllegalAccessException", e, log);
        }

    }

}
