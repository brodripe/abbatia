package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.bbean.UtilsBBean;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosDesastresBBean;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRDesastre extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        // Los desastres se repiten por cada 500 abadias
        UtilsBBean oUtilsBBean = new UtilsBBean();
        ProcesosDesastresBBean oDesastresBBean = new ProcesosDesastresBBean();

        int max = oUtilsBBean.getSQL("SELECT Round( count(*) / 500) FROM `abadia` a, usuario u where a.usuarioid = u.usuarioid and u.abadia_congelada = 0", 2);
        for (int n = 0; n < max; n++) {
            oDesastresBBean.realizar_desastre(-1, false);  // Escoger una abbatia al aaazaaarrr :-D
        }

    }
}
