package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.bbean.CultivoBBean;
import org.abbatia.bbean.UtilsBBean;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bbean.singleton.CargasInicialesFiltroLibrosBBean;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.base.AbadiaException;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRPasarDia extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {

        //actualizar el día en curso
        UtilsBBean oUtilsBBean;
        oUtilsBBean = new UtilsBBean();
        oUtilsBBean.setPropiedad(99, 1, "V", CoreTiempo.getTiempoAbadiaString());

        CultivoBBean oCultivoBBean;
        oCultivoBBean = new CultivoBBean();
        oCultivoBBean.gestionarPuntosCultivo();
        oCultivoBBean.resetAcumuladoCultivos();

        //añadir llamada a método de aviso de campos de cultivo en los que no hay monjes asignados
        oCultivoBBean.gestionCamposDesatendidos();

        //recargar las tablas de filtros iniciales
        CargasInicialesFiltroLibrosBBean.reload();
        CargasInicialesDietasBBean.reload();

    }
}
