<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <meta http-equiv="Content-Language" content="en-us"/>
    <meta name="GENERATOR" content="Microsoft FrontPage 4.0"/>
    <meta name="ProgId" content="FrontPage.Editor.Document"/>
    <title>Abbatia: Panel general</title>
    <link rel="stylesheet" href="theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="theme/style-revised.css" type="text/css"/>
    <base target="_self"/>
    <script language="javascript" type="text/javascript">
        var fecha
        var CronoID = null
        var browserName = navigator.appName;

        function InicializarCrono() {

            if (fecha == null) {
                fecha = new Date();
                fecha.setYear(<bean:write name="Tiempo" property="year" />);
                fecha.setMonth(<bean:write name="Tiempo" property="mes" />);
                fecha.setDate(<bean:write name="Tiempo" property="dia" />);
                fecha.setHours(<bean:write name="Tiempo" property="hora" />);
                fecha.setMinutes(<bean:write name="Tiempo" property="minuto" />);
                fecha.setSeconds(0);
            }
            MostrarCrono();
        }

        function MostrarCrono() {
            //configura la salida
            var anyo = fecha.getYear();
            var mes = fecha.getMonth() + 1;
            var dia = fecha.getDate();

            var hora = fecha.getHours();
            var minutos = fecha.getMinutes();
            if (browserName == "Microsoft Internet Explorer") {
                anyo = fecha.getYear();
            } else {
                anyo = <bean:write name="Tiempo" property="year" />;
            }


            if (hora < 10) {
                hora = "0" + hora;
            }
            if (minutos < 10) {
                minutos = "0" + minutos;
            }
            if (mes < 10) {
                mes = "0" + mes;
            }
            if (dia < 10) {
                dia = " " + dia;
            }


            document.getElementById("reloj").innerHTML = dia + "-" + mes + "-" + anyo + " " + hora + ":" + minutos;

            fecha.setMinutes(fecha.getMinutes() + 1);

            CronoID = setTimeout("InicializarCrono ()", 5000);
        }

    </script>
</head>
<body onLoad="InicializarCrono();">
<div id="container">
<h3><bean:write property="nombre" name="abadia"/> (<bean:write property="ipActual" name="usuario"
                                                               scope="session"/>)
    <bean:message key="edificios.abadia.nivel"/> <bean:write name="abadia" property="nivelJerarquico"/>
</h3>

<div id="events">
    <h4><bean:message key="principal.portal.mensajesalertas"/></h4>
    <dl>
        <logic:iterate id="ultamens" name="DatosContents" property="ultMensajes">
            <dt><bean:write property="fechaAbadia" name="ultamens"/></dt>
            <dd><bean:write property="mensaje" name="ultamens" filter="false"/></dd>
        </logic:iterate>
    </dl>
    <div id="opcionesPrincipal">
        <div class="floatLeft">
            <ul>
                <li><strong><bean:message key="principal.ver"/></strong></li>
                <li><a href="main.do?listar_mensajes=0"><bean:message key="principal.ultimos"/></a></li>
                <li><a href="main.do?listar_mensajes=1"><bean:message key="principal.todos"/></a></li>
                <li><a href="main.do?listar_mensajes=2"><bean:message key="principal.informativos"/></a></li>
                <li><a href="main.do?listar_mensajes=3"><bean:message key="principal.alertas"/></a></li>
            </ul>
        </div>
        <div class="floatRight">
            <ul>
                <li><strong><bean:message key="principal.borrar"/></strong></li>
                <li><a href="main.do?borrar_mensajes=1"><bean:message key="principal.todos"/></a></li>
                <li><a href="main.do?borrar_mensajes=2"><bean:message key="principal.informativos"/></a></li>
            </ul>
        </div>
        <div class="break"></div>
    </div>
</div>
<div class="break">&nbsp;</div>

<div class="floatLeft column">
    <h4><bean:write name="DatosContents" property="nomRegion" filter="false"/></h4>
    <!-- Clima -->
    <bean:write name="DatosContents" property="climaRegion" filter="false"/>
    <br/>
    <hr/>
    <!-- Fecha -->
    <h5><bean:message name="Tiempo" property="diaSemana"/></h5>

    <div id="reloj"><bean:message key="principal.portal.calculando"/></div>
    <hr/>

    <!-- Idioma en tu región -->
    <h5><bean:message key="principal.portal.idiomaregion"/></h5>

    <p><bean:write name="abadia" property="descIdioma" scope="session"/></p>

</div>


<!-- Monjes que requieren atenci&oacute;n -->
<div class="floatLeft column">
    <h4><bean:message key="principal.poblacion"/></h4>
    <h5>
            <span class="monk">
                <a href="listarMonjes.do">
                    <span style="text-decoration: underline;"><bean:write name="DatosContents" property="contadorMonjes"
                                                                          filter="false"/> <bean:message
                            key="principal.monjes"/></span>
                </a>
            </span>
    </h5>
    <ul>
        <logic:iterate id="monjes" name="DatosContents" property="ultMonjes">
            <li><bean:write property="description" name="monjes" filter="false"/></li>
        </logic:iterate>
    </ul>
    <hr/>
    <!-- Monjes invitados-->
    <h5><bean:message key="principal.monjesinvitados"/></h5>
    <ul>
        <logic:iterate id="monjes_invitados" name="DatosContents" property="ultMonjesInvitados">
            <li><bean:write property="description" name="monjes_invitados" filter="false"/></li>
        </logic:iterate>
    </ul>
    <hr/>
    <!-- Monjes viajando-->
    <h5><bean:message key="principal.monjesviajando"/></h5>
    <ul>
        <logic:iterate id="monjes_viaje" name="DatosContents" property="ultMonjesViaje">
            <li><bean:write property="description" name="monjes_viaje" filter="false"/></li>
        </logic:iterate>
    </ul>
    <hr/>
    <!-- Monjes de visita-->
    <h5><bean:message key="principal.monjesvisita"/></h5>
    <ul>
        <logic:iterate id="monjes_visita" name="DatosContents" property="ultMonjesVisita">
            <li><bean:write property="description" name="monjes_visita" filter="false"/></li>
        </logic:iterate>
    </ul>
    <hr/>
    <h5><span class="guards"><bean:message key="principal.guardias"/></span></h5>

    <p>
        <html:link action="/gestionGuardias?accion=inicio">
            <bean:write name="DatosContents" property="contadorGuardia" filter="false"/> <bean:message
                key="principal.guardias"/>
        </html:link>
    </p>
    <hr/>
    <h5><span class="people"><bean:message key="principal.aldeanos"/></span></h5>

    <p>
        <html:link action="/poblacion">
            <bean:write name="DatosContents" property="contadorAldeanos" filter="false"/> <bean:message
                key="principal.aldeanos"/>
        </html:link>
    </p>
</div>
<!-- Monjes que requieren atenci&oacute;n -->

<div class="floatLeft column">
    <h4><bean:message key="principal.edificios"/></h4>
    <ul>
        <logic:iterate id="edificio" name="DatosContents" property="edificios">
            <li><bean:write property="description" name="edificio" filter="false"/></li>
        </logic:iterate>
    </ul>
</div>

<!--Correo-->
<div class="floatLeft column">
    <h4><bean:message key="principal.correo"/></h4>
    <h5>
        <bean:message key="principal.pendiente"/><br/>
        <html:link action="/mensajes_recibidos">
            <strong><bean:write name="DatosContents" property="correos"/></strong>
        </html:link>
    </h5>
    <h5>
        <bean:message key="principal.solicitudes"/><br/>
        <html:link action="/lista_solicitudes">
            <strong><bean:write name="DatosContents" property="solicitudes"/></strong>
        </html:link>
    </h5>
    <h5>
        <bean:message key="principal.solicitudesin"/><br/>
        <html:link action="/lista_solicitudes">
            <strong><bean:write name="DatosContents" property="solicitudes"/></strong>
        </html:link>
    </h5>
</div>


<!-- Conclave -->
<logic:notEqual name="Conclave" value="0">
<div class="floatLeft column">
    <h4><bean:message key="principal.elecciones.votarPapa"/></h4>
    <h5>
        <logic:equal name="Conclave" value="1">
            <img src="images/iconos/sanpedro.gif" border="0"/> <br>
            <bean:message key="principal.elecciones.votarPapa.estado1"/>
        </logic:equal>
        <logic:equal name="Conclave" value="2">
            <logic:equal name="Fumata" value="0">
                <img src="images/iconos/sanpedro.gif" border="0"/> <br>
            </logic:equal>
            <logic:equal name="Fumata" value="1">
                <img src="images/iconos/sanpedro_fumatablanca.gif" border="0"/> <br>
            </logic:equal>
            <logic:equal name="Fumata" value="2">
                <img src="images/iconos/sanpedro_fumatanegro.gif" border="0"/> <br>
            </logic:equal>
            <bean:message key="principal.elecciones.votarPapa.estado2"/>
        </logic:equal>
    </h5>
</div>
</logic:notEqual>


<!-- Recursos -->
<div id="resources" class="floatLeft column">
    <jsp:include page="/varios/mostrar_recursos.jsp"/>
</div>


<div class="floatLeft column">
    <h4><bean:message key="principal.clasificacion"/></h4>
    <bean:write name="DatosContents" property="clasificacion" filter="false"/>
</div>


<!-- Elecciones -->
<logic:greaterThan value="-1" name="Elecciones">
<div class="floatLeft column">
    <h4><bean:message key="principal.elecciones.arzobispo"/></h4>
    <h5>
        <html:link action="/votar_obispado">
            <strong><bean:message key="principal.elecciones.votar"/></strong>
        </html:link>
    </h5>
    <h5>
        <bean:message key="principal.elecciones.faltan"/>&nbsp;
        <bean:write name="Elecciones" filter="false"/>&nbsp;
        <bean:message key="principal.elecciones.dias"/>
    </h5>

</div>
</logic:greaterThan>

<div class=”break”>&nbsp;</div>


<%--Modulo de estadisticas de google--%>
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-302272-1']);
    _gaq.push(['_setDomainName', 'abbatia.net']);
    _gaq.push(['_trackPageview']);
    (function() {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>


</body>
<jsp:include page="/abadias/congelado.jsp"/>
</html>
