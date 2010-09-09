<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<html:html>

    <STYLE>
        div {
            position: absolute;
        }
    </STYLE>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <meta http-equiv="Content-Language" content="en-us">
        <html:base target="_self"/>
        <title></title>
    </head>

    <script language="JavaScript" src="theme/crossbrowser.js"></script>
    <script language="JavaScript" src="theme/outlook.js"></script>

    <body topmargin="0" leftmargin="0" bgcolor="#E4CFA2">

    <SCRIPT>

        // ---------------------------------------------------------------------------
        // Example of howto: use OutlookBar
        // ---------------------------------------------------------------------------


        //create OutlookBar
        var o = new createOutlookBar('Bar', 0, 0, screenSize.width, screenSize.height, '#E4CFA2', 'black')//'#000099') // OutlookBar
        var p

        //create first panel
        p = new createPanel('al', '<bean:message key="outlook.menu.correo"/>');
        p.addButton('images/iconos/mensaje_nuevo.gif', '<bean:message key="outlook.menu.correo.nuevo"/>', 'parent.principal.location="mensaje_nuevo.do"');
        p.addButton('images/iconos/leer_mensajes.gif', '<bean:message key="outlook.menu.correo.recibidos"/>', 'parent.principal.location="mensajes_recibidos.do"');
        p.addButton('images/iconos/mensajes_enviados.gif', '<bean:message key="outlook.menu.correo.enviados"/>', 'parent.principal.location="mensajes_enviados.do"');
        p.addButton('images/iconos/solicitudes.gif', '<bean:message key="outlook.menu.correo.solicitudes"/>', 'parent.principal.location="lista_solicitudes.do"');
        o.addPanel(p);

        p = new createPanel('p', '<bean:message key="outlook.menu.mercados"/>');
        p.addButton('images/iconos/mercado_compra.gif', '<bean:message key="outlook.menu.mercados.compra"/>', 'parent.principal.location="mercado_compra_agrupado.do"');
        p.addButton('images/iconos/mercado_venta.gif', '<bean:message key="outlook.menu.mercados.venta"/>', 'parent.principal.location="mercado_venta.do?accion=inicio"');
        p.addButton('images/iconos/mercado_historico.gif', '<bean:message key="outlook.menu.mercados.historico"/>', 'parent.principal.location="mercado_historico.do"');
        o.addPanel(p);

        p = new createPanel('l', '<bean:message key="outlook.menu.monjes"/>');
        o.addPanel(p);
        
        p.addButton('images/iconos/monje_2_${sessionScope.abadia.idDeOrden}.png', '<bean:message key="outlook.menu.monjes.muestra"/>', 'parent.principal.location="listarMonjes.do"');
        p.addButton('images/iconos/labrador.gif', '<bean:message key="outlook.menu.monjes.trabajos"/>', 'parent.principal.location="listarTrabajos.do"');
        p.addButton('images/iconos/monje_jerarquia.gif', '<bean:message key="outlook.menu.monjes.jerarquia"/>', 'parent.principal.location="mostrarDiplomacia.do"');
        p.addButton('images/iconos/monje_reclutar.gif', '<bean:message key="outlook.menu.monjes.recluta"/>', 'parent.principal.location="reclutarMonje.do"');
        p.addButton('images/iconos/monje_viajar.gif', '<bean:message key="diplomacia.jerarquia.viajar"/>', 'parent.principal.location="buscar_abadia.do?opcion=2&region=0"');

        p = new createPanel('l2', '<bean:message key="outlook.menu.edificios"/>');
        o.addPanel(p);
        p.addButton('images/iconos/lista_edificios.gif', '<bean:message key="outlook.menu.edificios.muestra"/>', 'parent.principal.location="listarEdificios.do"');
        p.addButton('images/iconos/construir_edificios.gif', '<bean:message key="outlook.menu.edificios.construye"/>', 'parent.principal.location="crear_edificio.do"');
        p.addButton('images/iconos/reparar.gif', '<bean:message key="outlook.menu.edificios.reparar"/>', 'parent.principal.location="mantenimientoEdificio.do"');
        p.addButton('images/iconos/mapa.gif', '<bean:message key="diplomacia.jerarquia.mapa"/>', 'parent.principal.location="mapaGeneral.do"');

        p = new createPanel('l0', '<bean:message key="outlook.menu.usuario"/>');
        o.addPanel(p);
        p.addButton('images/iconos/inicio.gif', '<bean:message key="outlook.menu.usuario.inicio"/>', 'parent.principal.location="main.do"');
        p.addButton('images/iconos/info.gif', '<bean:message key="outlook.menu.usuario.info"/>', 'parent.principal.location="getUserInfo.do"');
        p.addButton('images/iconos/puntuacion.gif', '<bean:message key="outlook.menu.usuario.puntuacion"/>', 'parent.principal.location="mostrarPuntuaciones.do"');
        p.addButton('images/foros.gif', '<bean:message key="outlook.menu.usuario.foros"/>', 'parent.principal.location="http://foros.abbatia.net"');
        p.addButton('images/iconos/desconectar.gif', '<bean:message key="outlook.menu.usuario.desconecta"/>', 'parent.location="desconectar.do"');


        //p.addButton('images/iconos/borrar_abadia.gif','<bean:message key="outlook.menu.usuario.elimina"/>','parent.principal.location="eliminar_abadia.do"');

    </Script>
    <logic:present name="usuario" scope="session">
        <logic:equal property="administrador" name="usuario" scope="session" value="1">
            <Script>
                p = new createPanel('l5', '** Administración **');
                o.addPanel(p);
                p.addButton('images/iconos/monje.gif', 'Admin Abbatia', 'parent.principal.location="AdministracionConfirmar.do"');
                p.addButton('images/iconos/monje.gif', 'Admin Mercado', 'parent.principal.location="lista_mercado_admin.do"');
                p.addButton('images/iconos/monje.gif', 'Procesos', 'parent.principal.location="AdminProcess.do"');
                p.addButton('images/iconos/monje.gif', 'Bloqueos', 'parent.principal.location="DesbloquearUsuario.do"');
                p.addButton('images/iconos/monje.gif', 'Control Altas', 'parent.principal.location="controlAltas.do"');
                p.addButton('images/iconos/monje.gif', 'Solicitud Bloqueo', 'parent.principal.location="ListarPeticionesBloqueo.do"');
                p.addButton('images/iconos/monje.gif', 'Traducciones', 'parent.principal.location="AdminLiterales.do"');
                /*                p.addButton('images/iconos/monje.gif', 'Tramposos', 'parent.principal.location="AdminTramposos.do"');*/
                p.addButton('images/libros/cubiculi_1.gif', 'Propagar Libros', 'parent.principal.location="propagarLibros.do"');
            </Script>
        </logic:equal>
        <logic:equal property="administrador" name="usuario" scope="session" value="2">
            <Script>
                p = new createPanel('l5', '** Colaborador **');
                o.addPanel(p);
                p.addButton('images/iconos/foros.gif', 'Bugs', 'parent.principal.location="http://bugs.abbatia.net"');
                p.addButton('images/iconos/monje.gif', 'Procesos', 'parent.principal.location="AdminProcess.do"');
                p.addButton('images/iconos/monje.gif', 'Traducciones', 'parent.principal.location="AdminLiterales.do"');
            </Script>
        </logic:equal>
        <logic:equal property="administrador" name="usuario" scope="session" value="3">
            <Script>
                p = new createPanel('l5', '** Colaborador + **');
                o.addPanel(p);
                p.addButton('images/iconos/monje.gif', 'Solicitud Bloqueo', 'parent.principal.location="ListarPeticionesBloqueo.do"');
                p.addButton('images/iconos/monje.gif', 'Procesos', 'parent.principal.location="AdminProcess.do"');
                p.addButton('images/iconos/monje.gif', 'Traducciones', 'parent.principal.location="AdminLiterales.do"');
                p.addButton('images/iconos/monje.gif', 'Bloqueos', 'parent.principal.location="DesbloquearUsuario.do"');
                p.addButton('images/iconos/monje.gif', 'Control Altas', 'parent.principal.location="controlAltas.do"');
                p.addButton('images/libros/cubiculi_1.gif', 'Propagar Libros', 'parent.principal.location="propagarLibros.do"');
            </Script>
        </logic:equal>
    </logic:present>
    <SCRIPT>

        o.draw();         //draw the OutlookBar

        //-----------------------------------------------------------------------------
        //functions to manage window resize
        //-----------------------------------------------------------------------------
        //resize OP5 (test screenSize every 100ms)
        function resize_op5() {
            if (bt.op5) {
                o.showPanel(o.aktPanel);
                var s = new createPageSize();
                if ((screenSize.width != s.width) || (screenSize.height != s.height)) {
                    screenSize = new createPageSize();
                    //need setTimeout or resize on window-maximize will not work correct!
                    //benötige das setTimeout oder das Maximieren funktioniert nicht richtig
                    setTimeout("o.resize(0,0,screenSize.width,screenSize.height)", 100);
                }
                setTimeout("resize_op5()", 100);
            }
        }

        //resize IE & NS (onResize event!)
        function myOnResize() {
            if (bt.ie4 || bt.ie5 || bt.ns5) {
                var s = new createPageSize();
                o.resize(0, 0, s.width, s.height);
            }
            else
                if (bt.ns4) location.reload();
        }

    </SCRIPT>
    <!--Inicio Script para google-analytics-->
    <script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
    </script>
    <script type="text/javascript">
        _uacct = "UA-302272-1";
        urchinTracker();
    </script>
    <!--Fin Script para google-analytics-->

    </body>
    <!-- need an onResize event to redraw outlookbar after pagesize changes! -->
    <!-- OP5 does not support onResize event! use setTimeout every 100ms -->
    <body onLoad="resize_op5();" onResize="myOnResize();">
    <!--Inicio Script para google-analytics-->
    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost +
                                "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
        var pageTracker = _gat._getTracker("UA-302272-1");
        pageTracker._initData();
        pageTracker._trackPageview();
    </script>
    <!--Fin Script para google-analytics-->

    </body>

</html:html>
