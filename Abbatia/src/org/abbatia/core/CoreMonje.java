package org.abbatia.core;

import org.abbatia.actionform.AbadiaActForm;
import org.abbatia.adbean.adJerarquiaEclesiastica;
import org.abbatia.adbean.adUtils;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.AbadiaConfiguracionXML;
import org.abbatia.utils.Constantes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class CoreMonje extends adbeans {
    public static AbadiaConfiguracionXML configuracion;
    public static Random r = new Random(System.currentTimeMillis());

    public CoreMonje(Connection con) throws AbadiaException {
        super(con);
    }

    //este metodo creará monjes para una abbatia cuando esta sea creada,
    //las propiedades de los monjes que se creen deberan ser determinadas
    //por los parámetros que el usuario determine en la abbatia.
    //es importante destacar que la creación de estos monjes, no implicará
    //su alta en la base de datos, ya que este alta se producirá solo
    //cuando el usuario acepte la composición ofrecida.
    public ArrayList<Monje> generarMonjesParaAbadia(AbadiaActForm abadia, Usuario usuario) throws AbadiaException {
        //CoreTiempo tiempo = new CoreTiempo();

        configuracion = new AbadiaConfiguracionXML();

        //abrimos una conexión general
        //adbeans ad = new adbeans();

        adUtils util;
        adJerarquiaEclesiastica jerarad;

        Hashtable configuracionHT;
        ParametrosIniciales parametros;

        try {

            configuracionHT = configuracion.getParametrosIniciales(abadia.getActividad());
            parametros = (ParametrosIniciales) configuracionHT.get("abad");

            util = new adUtils(con);
            ArrayList<Table> nombres = util.getNombresPorRegion(abadia.getRegion());
            ArrayList<Table> nombres_ciudad = util.getCiudadesPorRegion(abadia.getRegion());

            ArrayList<Monje> monjes = new ArrayList<Monje>();
            Monje monje = new Monje();
            monje.setIdMonje(1);
            monje.setNombre(getNombreAleatorio(nombres));
            monje.setApellido1(getNombreAleatorio(nombres_ciudad));
            monje.setApellido2(getNombreAleatorio(nombres_ciudad));
            monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(50));
            monje.setFechaDeEntradaEnAbadia(CoreTiempo.getFechaNacimiento(30));
            monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
            monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
            monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
            monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
            monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
            monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
            monje.setIdDeJerarquia(Constantes.JERARQUIA_ABAD);

            jerarad = new adJerarquiaEclesiastica(con);
            monje.setJerarquia(jerarad.recuperarDescJerarquiaEclesiastica(monje.getIdDeJerarquia(), usuario.getIdDeIdioma()));

            monje.setSalud((short) 100);
            //monje.setHabilidades();
            monje.setComeFamiliaID1((short) 4);
            monje.setComeFamiliaID2((short) 1);
            monje.setComeFamiliaID3((short) 9);
            monje.setComeFamiliaID4((short) 5);
            monje.setComeFamiliaID5((short) 3);

            monje.setActMaitines((short) 1);
            monje.setActLaudes((short) 1);
            monje.setActAngelus((short) 1);
            monje.setActSexta((short) 12);

            monjes.add(monje);

            parametros = (ParametrosIniciales) configuracionHT.get("monje_1");

            monje = new Monje();
            monje.setIdMonje(2);
            monje.setNombre(getNombreAleatorio(nombres));
            monje.setApellido1(getNombreAleatorio(nombres_ciudad));
            monje.setApellido2(getNombreAleatorio(nombres_ciudad));
            monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(40));
            monje.setFechaDeEntradaEnAbadia(CoreTiempo.getFechaNacimiento(18));
            monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
            monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
            monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
            monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
            monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
            monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
            monje.setSalud((short) 100);
            monje.setIdDeJerarquia(Constantes.JERARQUIA_MONJE);

            monje.setJerarquia(jerarad.recuperarDescJerarquiaEclesiastica(monje.getIdDeJerarquia(), usuario.getIdDeIdioma()));

            monje.setComeFamiliaID1((short) 4);
            monje.setComeFamiliaID2((short) 1);
            monje.setComeFamiliaID3((short) 9);
            monje.setComeFamiliaID4((short) 5);
            monje.setComeFamiliaID5((short) 3);
            monje.setActMaitines((short) 1);
            monje.setActLaudes((short) 1);
            monje.setActAngelus((short) 1);
            monje.setActSexta((short) 12);


            monjes.add(monje);

            parametros = (ParametrosIniciales) configuracionHT.get("monje_2");

            monje = new Monje();
            monje.setIdMonje(3);
            monje.setNombre(getNombreAleatorio(nombres));
            monje.setApellido1(getNombreAleatorio(nombres_ciudad));
            monje.setApellido2(getNombreAleatorio(nombres_ciudad));
            monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(24));
            monje.setFechaDeEntradaEnAbadia(CoreTiempo.getFechaNacimiento(8));
            monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
            monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
            monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
            monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
            monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
            monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
            monje.setSalud((short) 100);
            monje.setIdDeJerarquia(Constantes.JERARQUIA_MONJE);

            monje.setJerarquia(jerarad.recuperarDescJerarquiaEclesiastica(monje.getIdDeJerarquia(), usuario.getIdDeIdioma()));

            monje.setComeFamiliaID1((short) 4);
            monje.setComeFamiliaID2((short) 1);
            monje.setComeFamiliaID3((short) 9);
            monje.setComeFamiliaID4((short) 5);
            monje.setComeFamiliaID5((short) 3);
            monje.setActMaitines((short) 1);
            monje.setActLaudes((short) 1);
            monje.setActAngelus((short) 1);
            monje.setActSexta((short) 12);


            monjes.add(monje);

            parametros = (ParametrosIniciales) configuracionHT.get("monje_3");
            monje = new Monje();
            monje.setIdMonje(4);
            monje.setNombre(getNombreAleatorio(nombres));
            monje.setApellido1(getNombreAleatorio(nombres_ciudad));
            monje.setApellido2(getNombreAleatorio(nombres_ciudad));
            monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(18));
            monje.setFechaDeEntradaEnAbadia(CoreTiempo.getFechaNacimiento(5));
            monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
            monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
            monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
            monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
            monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
            monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
            monje.setSalud((short) 100);
            monje.setIdDeJerarquia(Constantes.JERARQUIA_MONJE);

            monje.setJerarquia(jerarad.recuperarDescJerarquiaEclesiastica(monje.getIdDeJerarquia(), usuario.getIdDeIdioma()));

            monje.setComeFamiliaID1((short) 4);
            monje.setComeFamiliaID2((short) 1);
            monje.setComeFamiliaID3((short) 9);
            monje.setComeFamiliaID4((short) 5);
            monje.setComeFamiliaID5((short) 3);
            monje.setActMaitines((short) 1);
            monje.setActLaudes((short) 1);
            monje.setActAngelus((short) 1);
            monje.setActSexta((short) 12);


            monjes.add(monje);

            parametros = (ParametrosIniciales) configuracionHT.get("novicio");
            monje = new Monje();
            monje.setIdMonje(5);
            monje.setNombre(getNombreAleatorio(nombres));
            monje.setApellido1(getNombreAleatorio(nombres_ciudad));
            monje.setApellido2(getNombreAleatorio(nombres_ciudad));
            monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(16));
            monje.setFechaDeEntradaEnAbadia(CoreTiempo.getFechaNacimiento(1));
            monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
            monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
            monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
            monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
            monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
            monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
            monje.setSalud((short) 100);
            monje.setIdDeJerarquia(Constantes.JERARQUIA_NOVICIO);

            monje.setJerarquia(jerarad.recuperarDescJerarquiaEclesiastica(monje.getIdDeJerarquia(), usuario.getIdDeIdioma()));

            monje.setComeFamiliaID1((short) 4);
            monje.setComeFamiliaID2((short) 1);
            monje.setComeFamiliaID3((short) 9);
            monje.setComeFamiliaID4((short) 5);
            monje.setComeFamiliaID5((short) 3);
            monje.setActMaitines((short) 1);
            monje.setActLaudes((short) 1);
            monje.setActAngelus((short) 1);
            monje.setActSexta((short) 12);

            monjes.add(monje);

            //cerramos la conexión general
            //ad.finalize();

            return monjes;

        } catch (AbadiaException e) {
            throw e;
        }
    }


    public Monje reclutarNovicioParaAbadia(Abadia abadia) throws AbadiaException {
        //CoreTiempo tiempo = new CoreTiempo();

        configuracion = new AbadiaConfiguracionXML();

        Hashtable configuracionHT = configuracion.getParametrosIniciales(Constantes.ACTIVIDAD_AGRICULTURA);
        ParametrosIniciales parametros = (ParametrosIniciales) configuracionHT.get("novicio");
        adUtils util = new adUtils(con);
        ArrayList<Table> nombres = util.getNombresPorRegion(abadia.getIdDeRegion());
        ArrayList<Table> nombres_ciudad = util.getCiudadesPorRegion(abadia.getIdDeRegion());

        Monje monje;

        monje = new Monje();
        monje.setIdAbadia(abadia.getIdDeAbadia());
        monje.setNombre(getNombreAleatorio(nombres));
        monje.setApellido1(getNombreAleatorio(nombres_ciudad));
        monje.setApellido2(getNombreAleatorio(nombres_ciudad));
        monje.setFechaDeNacimiento(CoreTiempo.getFechaNacimiento(16));
        monje.setFechaDeEntradaEnAbadia(CoreTiempo.getTiempoAbadiaString());
        monje.setPropiedadesDeLosMonjesHT(parametros.getPropiedades());
        monje.setHabilidadesDeLosMonjesHT(parametros.getHabilidades());
        monje.setLipidos((short) Integer.valueOf((String) parametros.getPropiedades().get("lipidos")).intValue());
        monje.setProteinas((short) Integer.valueOf((String) parametros.getPropiedades().get("proteinas")).intValue());
        monje.setHidratosCarbono((short) Integer.valueOf((String) parametros.getPropiedades().get("hidratos")).intValue());
        monje.setVitaminas((short) Integer.valueOf((String) parametros.getPropiedades().get("vitaminas")).intValue());
        monje.setSalud((short) 100);
        monje.setComeFamiliaID1((short) 4);
        monje.setComeFamiliaID2((short) 1);
        monje.setComeFamiliaID3((short) 9);
        monje.setComeFamiliaID4((short) 5);
        monje.setComeFamiliaID5((short) 3);
        monje.setActMaitines((short) 1);
        monje.setActLaudes((short) 1);
        monje.setActAngelus((short) 1);
        monje.setActSexta((short) 12);


        return monje;
    }

    public static String getNombreAleatorio(ArrayList<Table> TablaNombres) {
        Table nombre = TablaNombres.get(r.nextInt(TablaNombres.size()));
        return nombre.getDescription();
    }


}
