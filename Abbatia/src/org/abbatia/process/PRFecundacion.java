package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosAnimalesBBean;
import org.abbatia.process.bbean.ProcesosBBean;
import org.apache.log4j.Logger;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 * To change this template use File | Settings | File Templates.
 */
public class PRFecundacion extends ProcesoBase {
    private static Logger log = Logger.getLogger(PRFecundacion.class.getName());

    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosBBean procesos = new ProcesosBBean();
        Hashtable htStress = procesos.cargarStressPorEdificio();

        ProcesosAnimalesBBean animales = new ProcesosAnimalesBBean();
        animales.follar((short) 0, htStress);
        animales.follar((short) 1, htStress);
        animales.parir(htStress);
    }
}
