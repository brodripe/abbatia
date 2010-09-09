package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosBBean;
import org.abbatia.process.bbean.ProcesosEdificiosBBean;
import org.abbatia.process.bbean.ProcesosLibrosBBean;
import org.abbatia.process.bbean.ProcesosVariosBBean;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRVariosNoche extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosBBean oProcesosBBean = new ProcesosBBean();
        oProcesosBBean.gestionClimaAgricultura();

        ProcesosVariosBBean oVariosBBean = new ProcesosVariosBBean();
        oVariosBBean.forzarVueltaMonjesAbadiaVacia();
        oVariosBBean.cambiar_clima();
        oVariosBBean.obispado();
        oVariosBBean.crecimiento_pueblo();
        oVariosBBean.borrarAbadiasAntiguas();
        oVariosBBean.borrarAbadiasMarcadas();
        oVariosBBean.regularizarMonjesHuerfanos();
        oVariosBBean.regularizarMonjesPerdidos();
        oVariosBBean.borrarAbadiasSinUsuario();
        oVariosBBean.descongelarAbadias();
        oVariosBBean.desbloquearTareasCopiaMonjes();
        oVariosBBean.desbloquearTareasCopiaMonjesSinLibro();


        ProcesosEdificiosBBean oEdificiosBBean = new ProcesosEdificiosBBean();
        oEdificiosBBean.procesarDesgasteRecursosPorSaturacion();

        ProcesosLibrosBBean oLibrosBBean = new ProcesosLibrosBBean();
        oLibrosBBean.desgastarLibros();

        oVariosBBean.desbloquearLibrosIncompletos();
        oVariosBBean.gestionBloqueos();
        oVariosBBean.limpieza_bd();
    }
}
