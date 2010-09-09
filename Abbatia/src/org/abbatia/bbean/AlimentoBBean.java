package org.abbatia.bbean;

import org.abbatia.adbean.adAlimentoLotes;
import org.abbatia.adbean.adEdificio;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.AlimentoLote;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AlimentoNoSalableException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class AlimentoBBean {
    private static Logger log = Logger.getLogger(AlimentoBBean.class.getName());

    public Edificio salarAlimento(Usuario p_oUsuario, Abadia p_oAbadia, int p_iAlimentoId, String p_szParameter) throws AbadiaException {
        String sTrace = this.getClass() + ".salarAlimento(" + p_oUsuario.getNick() + "," + p_oAbadia.getIdDeAbadia() + "," + p_iAlimentoId + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adAlimentoLotes oAlimentoLotesAD;

        Edificio oEdificio;
        ArrayList alAlimentos;
        Iterator itAlimentos;
        AlimentoLote oAlimentoLote;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos el edificio que alverga el alimento
            oEdificioAD = new adEdificio(con);
            oAlimentoLotesAD = new adAlimentoLotes(con);
            oEdificio = oEdificioAD.recuperarEdificioPorFamiliaAlimento(p_oAbadia, p_iAlimentoId, p_oUsuario);

            //si la opción es "salar" todo...
            if (p_szParameter.equals("todo")) {
                //recuperamos los lotes disponibles de ese mismo alimento
                alAlimentos = oAlimentoLotesAD.recuperarAlimentosPorID(p_iAlimentoId, oEdificio.getIdDeEdificio());

                itAlimentos = alAlimentos.iterator();
                while (itAlimentos.hasNext()) {
                    oAlimentoLote = (AlimentoLote) itAlimentos.next();
                    oAlimentoLotesAD.salarAlimento(oAlimentoLote, oAlimentoLote.getCantidad(), p_oAbadia.getIdDeAbadia());
                }
            } else if (p_szParameter.equals("lote")) {
                oAlimentoLote = oAlimentoLotesAD.recuperarAlimentoLote(p_iAlimentoId);
                //si el elimento no es salable...
                if (oAlimentoLote.getIdAlimentoSalado() == 0) {
                    throw new AlimentoNoSalableException(sTrace, log);
                }
                oAlimentoLotesAD.salarAlimento(oAlimentoLote, oAlimentoLote.getCantidad(), p_oAbadia.getIdDeAbadia());
            }

            return oEdificio;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


}