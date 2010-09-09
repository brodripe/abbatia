package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosEnfermedadesBBean;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 * To change this template use File | Settings | File Templates.
 */
public class PREnfermar extends ProcesoBase {
    private static Logger log = Logger.getLogger(PREnfermar.class.getName());

    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosEnfermedadesBBean procesosBBean = new ProcesosEnfermedadesBBean();
        procesosBBean.enfermar();
    }

}
