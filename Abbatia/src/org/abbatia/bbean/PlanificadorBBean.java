/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 05-may-2009
 * Time: 0:09:25
 */
package org.abbatia.bbean;

import org.abbatia.process.prPlanificador;

import java.util.Timer;

public class PlanificadorBBean {

    private static PlanificadorBBean ourInstance = new PlanificadorBBean();
    private Timer tempo = null;
    private boolean bEstadoPlanificador;

    public static PlanificadorBBean getInstance() {
        return ourInstance;
    }

    private PlanificadorBBean() {
        tempo = new Timer();
    }

    public static void activarPlanificador() {
        getInstance().tempo.schedule(new prPlanificador(), 1, 5000);
        getInstance().setEstadoPlanificador(true);
    }

    public static Timer getTempo() {
        return getInstance().tempo;
    }

    public static void desactivarPlanificador() {
        getInstance().tempo.cancel();
        ourInstance = new PlanificadorBBean();
        getInstance().setEstadoPlanificador(false);
    }

    public static boolean getEstadoPlanificador() {
        return getInstance().bEstadoPlanificador;
    }

    private void setEstadoPlanificador(boolean bEstadoPlanificador) {
        this.bEstadoPlanificador = bEstadoPlanificador;
    }

    public static void errorPlanificador() {
        getInstance().setEstadoPlanificador(false);
    }

    public static void errorRecuperadoPlanificador() {
        getInstance().setEstadoPlanificador(true);
    }

}
