package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosEdificiosBBean;

import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRDesgasteEdificios extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosEdificiosBBean oEdificiosBBean;
        oEdificiosBBean = new ProcesosEdificiosBBean();
        oEdificiosBBean.gestionDesgasteEdificios();
        HashMap hmEdificios = oEdificiosBBean.gestionGastoMantenimientoEdificios();
        oEdificiosBBean.gestionaReparacionEdificios(hmEdificios);

    }
}
