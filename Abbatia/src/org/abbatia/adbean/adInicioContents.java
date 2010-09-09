package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class adInicioContents extends adbeans {

    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adInicioContents(Connection con) throws AbadiaException {
        super(con);
    }


    public InicioContents getInicioContents(Abadia abadiaDB, String todosMensajes, Usuario usuario, MessageResources resource) throws AbadiaException {
        //log.debug("Busco nombres para la región: " + region);
        InicioContents Contents = new InicioContents();
        adUtils Utils = null;
        adEnfermedad enfermedadAD = null;
        adMonje oMonjeAD;
        adAbadia abadiaAD;
        AbadiaPuntuacion abadiaPuntuacion;

        // Cargar los últimos mensajes de la abbatia
        //todo incluir idioma en el filtro.
        String sSQLMens = "SELECT MONJEID, MENSAJE, TIPO, FECHAABADIA FROM mensajes WHERE (abadiaID = ?) OR (abadiaid=-1 and monjeid=-1 and regionid=? and idiomaid = ?) " +
                " OR (abadiaid=-1 and monjeid=-1 and regionid=-1 and idiomaid = ?) ";

        String sSQLEdificios = "SELECT e.EDIFICIOID, l.LITERAL as NOMBRE, e.NIVEL " +
                " from edificio e, edificio_tipo et, literales l " +
                " where e.ABADIAID = ? AND et.TIPOEDIFICIOID = e.TIPOEDIFICIOID AND l.LITERALID = et.NOMBRE AND l.IDIOMAID = ? and et.TIPOEDIFICIOID <> " + Constantes.EDIFICIO_ABADIA +
                " order by NOMBRE";

        // Filtros
        if (todosMensajes.compareTo("0") == 0) sSQLMens = sSQLMens + "order by fechareal desc LIMIT 10";
        if (todosMensajes.compareTo("1") == 0) sSQLMens = sSQLMens + "order by fechareal desc";
        if (todosMensajes.compareTo("2") == 0) sSQLMens = sSQLMens + " AND Tipo=0 order by fechareal desc";
        if (todosMensajes.compareTo("3") == 0) sSQLMens = sSQLMens + " AND Tipo=1 order by fechareal desc";

        // varios
        String literalDe = "";
        String sHTML = "";
        String sTXT = "";
        int santo = 0;
        String sMensaje = "";
        MonjeInicio monje;

        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            // Cargar los mensajes
            ps = con.prepareStatement(sSQLMens);
            ps.setLong(1, abadiaDB.getIdDeAbadia());
            ps.setInt(2, abadiaDB.getIdDeRegion());
            ps.setInt(3, usuario.getIdDeIdioma());
            ps.setInt(4, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            ArrayList<Serializable> alTable = new ArrayList<Serializable>();
            Table tabla;
            Mensaje objMsg;

            oMonjeAD = new adMonje(con);
            Utils = new adUtils(con);

            while (rs.next()) {
                objMsg = new Mensaje();

                objMsg.setIdDeMonje(rs.getInt("MONJEID"));
                objMsg.setFechaAbadia(Utilidades.formatStringFromDB(rs.getString("FECHAABADIA")));
                objMsg.setTipo(rs.getInt("TIPO"));
                sMensaje = rs.getString("MENSAJE");
                if (objMsg.getIdDeMonje() != -1) {
                    sTXT = oMonjeAD.getDescJerarquiaMonje(objMsg.getIdDeMonje(), usuario.getIdDeIdioma()) + " (<a href='mostrarMonje.do?clave=" + objMsg.getIdDeMonje() + "'>";

                    santo = Utils.getSQL("SELECT SANTO FROM monje where MONJEID = " + objMsg.getIdDeMonje(), 0);
                    literalDe = Utils.getLiteral(5000, usuario.getIdDeIdioma());

                    if (santo == 1) sHTML = sHTML + "<font color='#FFFFFF'><b>";

                    sTXT = sTXT + oMonjeAD.getNomMonje(objMsg.getIdDeMonje(), literalDe);

                    if (santo == 1)
                        sHTML = sHTML + "</b></font>";
                    sTXT = sTXT + "</a>) ";
                    sTXT = sTXT + sMensaje;
                } else sTXT = sMensaje;
                if (objMsg.getTipo() == 1) {
                    sHTML = sHTML + "<font color='#800000'>" + sTXT + "</font>";
                } else sHTML = sHTML + sTXT;
                objMsg.setMensaje(sHTML);
                sHTML = "";
                alTable.add(objMsg);
            }
            rs.close();
            ps.close();

            if (alTable.isEmpty()) {
                sHTML = " <center>" + resource.getMessage("monjes.abadia.sinmensajes") + "</center>";
                alTable.add(new Mensaje(0, sHTML, 1, ""));
            }

            log.debug("getClaveValor. se devuelven los mensajes de la abbatia usuarios formato en ArrayList");
            Contents.setUltMensajes(alTable);

            // Cargar los monjes nuestros que se encuentran en nuestra abbatia
            ArrayList<MonjeInicio> alMonjes;
            Iterator<MonjeInicio> itMonjes;

            alMonjes = oMonjeAD.recuperarMonjesInicio(usuario, abadiaDB);

            itMonjes = alMonjes.iterator();

            alTable = new ArrayList<Serializable>();

            enfermedadAD = new adEnfermedad(con);
            while (itMonjes.hasNext()) {
                monje = itMonjes.next();
                sHTML = "<a href='mostrarMonje.do?clave=" + monje.getIdMonje() + "'>" + monje.getNombreMonje() + " (" + monje.getLiteral() + ") " + enfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()) + "</a>";
                tabla = new Table(monje.getIdMonje(), sHTML);
                alTable.add(tabla);
            }

            if (alTable.isEmpty()) {
                sHTML = " <center>" + resource.getMessage("monjes.abadia.sinmonjes") + " <a href='reclutarMonje.do'>" + resource.getMessage("monjes.abadia.recluirmonjes") + "</a></center>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }

            Contents.setUltMonjes(alTable);

            // Cargar los monjes que tenemos invitados
            ArrayList<MonjeInicio> alMonjesInvitados = null;
            Iterator<MonjeInicio> itMonjesInvitados = null;

            alMonjesInvitados = oMonjeAD.recuperarMonjesInvitados(usuario, abadiaDB);

            itMonjesInvitados = alMonjesInvitados.iterator();
            alTable = new ArrayList<Serializable>();

            while (itMonjesInvitados.hasNext()) {
                monje = itMonjesInvitados.next();

                sHTML = "<a href='mostrarMonje.do?clave=" + monje.getIdMonje() + "'>" + monje.getNombreMonje() + " (" + monje.getLiteral() + ") " + resource.getMessage("login.de") + " " + monje.getNombreAbadia() + " " + enfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()) + "</a>";
                tabla = new Table(monje.getIdMonje(), sHTML);
                alTable.add(tabla);
            }

            Contents.setUltMonjesInvitados(alTable);

            // Cargar los monjes que tenemos viajando
            ArrayList<MonjeInicio> alMonjesViajando = null;
            Iterator<MonjeInicio> itMonjesViajando = null;

            alMonjesViajando = oMonjeAD.recuperarMonjesViajando(usuario, abadiaDB);

            itMonjesViajando = alMonjesViajando.iterator();

            alTable = new ArrayList<Serializable>();

            while (itMonjesViajando.hasNext()) {
                monje = itMonjesViajando.next();
                if (monje.getFechaLlegadaOrigen() == null) {
                    sHTML = "<a href='mostrarMonje.do?clave=" + monje.getIdMonje() + "'>" + monje.getNombreMonje() + " (" + monje.getLiteral() + ") " + resource.getMessage("principal.portal.hacia") + " " + monje.getNombreAbadia() + " " + enfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()) + " </a>";
                } else {
                    sHTML = "<a href='mostrarMonje.do?clave=" + monje.getIdMonje() + "'>" + monje.getNombreMonje() + " (" + monje.getLiteral() + ") " + resource.getMessage("principal.portal.desde") + " " + monje.getNombreAbadia() + " " + enfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()) + " </a>";
                }
                tabla = new Table(monje.getIdMonje(), sHTML);
                alTable.add(tabla);
            }

            log.debug("getClaveValor. se devuelven los monjes de la abbatia formato en ArrayList");
            Contents.setUltMonjesViaje(alTable);

            // Cargar los monjes que tenemos visitando otra abbatia
            ArrayList<MonjeInicio> alMonjesVisita = null;
            Iterator<MonjeInicio> itMonjesVisita = null;

            alMonjesVisita = oMonjeAD.recuperarMonjesVisita(usuario, abadiaDB);

            itMonjesVisita = alMonjesVisita.iterator();
            alTable = new ArrayList<Serializable>();
            while (itMonjesVisita.hasNext()) {
                monje = itMonjesVisita.next();
                sHTML = "<a href='mostrarMonje.do?clave=" + monje.getIdMonje() + "'>" + monje.getNombreMonje() + " (" + monje.getLiteral() + ") " + resource.getMessage("principal.portal.en") + " " + monje.getNombreAbadia() + " " + enfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()) + " </a>";
                tabla = new Table(monje.getIdMonje(), sHTML);
                alTable.add(tabla);
            }

            Contents.setUltMonjesVisita(alTable);

            //Lista de edificios
            ps = con.prepareStatement(sSQLEdificios);
            ps.setLong(1, abadiaDB.getIdDeAbadia());
            ps.setInt(2, usuario.getIdDeIdioma());

            rs = ps.executeQuery();
            alTable = new ArrayList<Serializable>();
            while (rs.next()) {
                sHTML = "<a href='mostrarEdificio.do?clave=" + rs.getString("EDIFICIOID") + "'>" + rs.getString("NOMBRE") + " (" + rs.getInt("NIVEL") + ")</a>";
                tabla = new Table(rs.getInt("EDIFICIOID"), sHTML);
                alTable.add(tabla);
            }
            if (alTable.isEmpty()) {
                sHTML = " <center>" + resource.getMessage("monjes.abadia.sinedificios") + "</center>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }

            Contents.setEdificios(alTable);

            // Clasificacion
            sHTML = "";

            abadiaAD = new adAbadia(con);
            abadiaPuntuacion = abadiaAD.recuperarPuntuacionCompleta(abadiaDB.getIdDeAbadia());

            if (abadiaPuntuacion.getClasificacion() == 0) {
                sHTML = "<strong>" + resource.getMessage("principal.clasificacion_sinprocesar") + "</strong>";
            } else {
                long pagina = Math.round(Math.ceil(abadiaPuntuacion.getClasificacion() / 100));
                sHTML = Utilidades.redondear(abadiaPuntuacion.getTotal()) + " " + resource.getMessage("principal.clasificacion_puntos") + "<br>";
                sHTML = sHTML + "<strong>" + resource.getMessage("principal.clasificacion_puesto") + "</strong><br>";
                sHTML = sHTML + "<a href='/index_main.do?param=topusers&region=-1&pagina=" + pagina + "'>";
                sHTML = sHTML + abadiaPuntuacion.getClasificacion() + " - " + resource.getMessage("principal.clasificacion_general") + "</a><br>";
                sHTML = sHTML + "<a href='/index_main.do?param=topusers&region=" + abadiaDB.getIdDeRegion() + "'>";
                sHTML = sHTML + abadiaPuntuacion.getClasificacionRegional() + " - " + resource.getMessage("principal.clasificacion_regional") + "</a><br>";
            }

            Contents.setClasificacion(sHTML);

            // Clima de la region
            //PropValor.recuperarPropiedadValor()
            int regID = abadiaDB.getIdDeRegion();
            Contents.setTempRegion(Utils.getPropriedad(3, regID, "R"));
            String climaID = Utils.getPropriedad(1, regID, "R");
            sHTML = "<img alt='' src='images/Tiempo/" + climaID + "_lge.gif'/><br>";
            sHTML = sHTML + Utils.getTablaDescripcion("TIEMPO", climaID, "Calor_error");
            sHTML = sHTML + "<br>" + resource.getMessage("monjes.abadia.a") + " " + Contents.getTempRegion() + " °C";
            Contents.setClimaRegion(sHTML);
            // Nombre de la región
            sHTML = resource.getMessage("monjes.abadia.tiempo") + "<br>" + Utils.getSQL("SELECT descripcion FROM region WHERE regionid = " + regID, "errorBD");
            Contents.setNomRegion(sHTML);
            return Contents;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adInicioContents. getInicioContents. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Novedades> getNovedades(String param) throws AbadiaSQLException {
        String sSQLNovedades = "Select * from novedades where mostrar = 0 order by fecha desc ";
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<Novedades> novedades = new ArrayList<Novedades>();
        ArrayList<String> mensajes = new ArrayList<String>();
        Novedades novedad;
        String sFecha = "";
        String sFechaTmp = "";
        String sDescripcion = "";
        boolean esElUltimo = false;

        if (param.equals("novedad")) {
            sSQLNovedades = sSQLNovedades + " LIMIT 100";
        } else {
            sSQLNovedades = sSQLNovedades + " LIMIT 7";
        }

        try {
            ps = con.prepareStatement(sSQLNovedades);
            rs = ps.executeQuery();
            if (rs.next()) {
                sFecha = rs.getString("FECHA");
                sDescripcion = rs.getString("MENSAJE");
                mensajes.add(sDescripcion);
            }
            while (rs.next()) {
                sFechaTmp = rs.getString("FECHA");
                sDescripcion = rs.getString("MENSAJE");
                if (sFecha.equals(sFechaTmp)) {
                    mensajes.add(sDescripcion);
                    esElUltimo = true;
                } else {
                    novedad = new Novedades();
                    novedad.setFecha(Utilidades.formatStringFromDB(sFecha));
                    novedad.setMsg(mensajes);
                    novedades.add(novedad);
                    mensajes = new ArrayList<String>();
                    sFecha = sFechaTmp;
                    mensajes.add(sDescripcion);
                    esElUltimo = false;
                }
            }
            if (esElUltimo) {
                novedad = new Novedades();
                novedad.setFecha(Utilidades.formatStringFromDB(sFecha));
                novedad.setMsg(mensajes);
                novedades.add(novedad);
            }

            return novedades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adInicioContents. getNovedades. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public void getRecursos(Abadia abadiaDB, InicioContents Contents) throws AbadiaException {
        //Recursos
        adRecurso recurso = new adRecurso(con);
        String monedas = Utilidades.redondear(recurso.recuperarCantidadRecurso(0, abadiaDB.getIdDeAbadia()));
        String agua = Utilidades.redondear(recurso.recuperarCantidadRecurso(1, abadiaDB.getIdDeAbadia()));
        String madera = Utilidades.redondear(recurso.recuperarCantidadRecurso(2, abadiaDB.getIdDeAbadia()));
        String piedra = Utilidades.redondear(recurso.recuperarCantidadRecurso(3, abadiaDB.getIdDeAbadia()));
        String hierro = Utilidades.redondear(recurso.recuperarCantidadRecurso(4, abadiaDB.getIdDeAbadia()));

        Contents.setRecursoMonedas(monedas);
        Contents.setRecursoAgua(agua);
        Contents.setRecursoMadera(madera);
        Contents.setRecursoPiedra(piedra);
        Contents.setRecursoHierro(hierro);

    }

    public void getContadores(Abadia abadia, InicioContents Contents) throws AbadiaException {
        // Contadores
        adUtils Utils = new adUtils(con);
        Contents.setContadorMonjes(Utils.getSQL("Select Count(*) from monje where estado = 0 AND abadiaid = " + abadia.getIdDeAbadia(), "?"));
        Contents.setContadorGuardia(Utils.getSQL("Select cantidad from recurso where recursoid = 10 and abadiaid = " + abadia.getIdDeAbadia(), "2?p"));
        Contents.setContadorAldeanos(Utils.getSQL("Select cantidad from recurso where recursoid = 20 and abadiaid = " + abadia.getIdDeAbadia(), "53?"));
        Contents.setCorreos(Utils.getSQL("Select count(*) nuevos from correo_destinatario cd, correo c, abadia a where cd.leido=0 and cd.correoid = c.correoid and a.abadiaid = c.abadiaid and cd.abadiaid = " + abadia.getIdDeAbadia(), 0));
        Contents.setSolicitudes(Utils.getSQL("Select count(*) from solicitud s, solicitud_voto sv where s.solicitudid=sv.solicitudid and s.estado=0 and sv.abadiaid = " + abadia.getIdDeAbadia() + " and sv.voto = 0 ", 0));

    }

}

