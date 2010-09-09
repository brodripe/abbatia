package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosEnfermedadesBBean;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRGestionSaludEnfermedad extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosEnfermedadesBBean procesosBBean = new ProcesosEnfermedadesBBean();
        //restar salud de las enfermedades
        procesosBBean.restarSaludEnfermos();
        //Sumar salud a los sanos
        procesosBBean.subirSaludSanos();
        //regularizar salud ancianos.
        procesosBBean.restarSaludPorEdad();
    }

}
