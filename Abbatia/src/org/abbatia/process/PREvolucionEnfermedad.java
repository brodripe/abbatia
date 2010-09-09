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
public class PREvolucionEnfermedad extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosEnfermedadesBBean procesosBBean = new ProcesosEnfermedadesBBean();
        procesosBBean.evolucionEnfermedad();
    }

}
