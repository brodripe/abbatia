<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html>
<head>
    <title>Pantalla muestra monje por actividad</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">

<p>

<center>

    <table borderColor="#000000" width="400" cellSpacing="0" borderColorDark="#000000" borderColorLight="#000000"
           border="1">
        <tr>
            <td bgColor="#e4cfa2" colSpan="3">
                <table width="100%" background="images/backgrounds/madera.jpg" cellSpacing="0" border="0">
                    <tr>
                        <td align="middle" width="5%">
                            <logic:notEqual name="ActividadesMonjeForm" property="anteriorID" value="0">
                                <html:link action="/mostrar_produccion" paramId="clave" paramName="ActividadesMonjeForm"
                                           paramProperty="anteriorID">
                                    <html:img border="0" page="/images/iconos/16/corner_left.gif"
                                              altKey="general.anterior"/>
                                </html:link>
                            </logic:notEqual>
                        </td>
                        <td align="middle" width="90%">
                            <p align="center"><b><font color="#ffffff" face="Tahoma" size="2"><bean:message
                                    key="monjes.abadia.datos"/></font></b></p>
                        </td>
                        <td align="middle" width="5%">
                            <logic:notEqual name="ActividadesMonjeForm" property="siguienteID" value="0">
                                <html:link action="/mostrar_produccion" paramId="clave" paramName="ActividadesMonjeForm"
                                           paramProperty="siguienteID">
                                    <html:img border="0" page="/images/iconos/16/corner_right.gif"
                                              altKey="general.siguiente"/>
                                </html:link>
                            </logic:notEqual>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgColor="#e4cfa2" colSpan="3">
                <table border="0">
                    <tr>
                        <td align="center" width="50" rowspan="5" valign="middle">
                            <p align="center"><img src="/images/iconos/monje_2_${sessionScope.abadia.idDeOrden}.png">
                        </td>
                        <td align="right"><b><bean:message key="monjes.abadia.nombre"/></b></td>
                        <td colspan="2">
                            <bean:write name="ActividadesMonjeForm" property="nombre"
                                        filter="false"/>&nbsp;<bean:message key="monjes.abadia.nomciudad"/>&nbsp;
                            <bean:write name="ActividadesMonjeForm" property="primerApellido" filter="false"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <p align="right"><b><bean:message key="monjes.abadia.fecha_nacimiento"/></b></p>
                        </td>
                        <td colspan="2">
                            <bean:write name="ActividadesMonjeForm" property="fechaDeNacimiento" filter="false"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><b><bean:message key="monjes.abadia.edad"/></b></td>
                        <td colspan="2"><bean:write name="ActividadesMonjeForm" property="edad"/>&nbsp;<bean:message
                                key="monjes.abadia.anyos"/></td>
                    </tr>
                    <tr>
                        <td align="right"><b><bean:message key="monjes.abadia.fecha_entrada"/></b></td>
                        <td colspan="2">
                            <bean:write name="ActividadesMonjeForm" property="fechaDeEntradaEnAbadia" filter="false"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</center>
</p>

<br>
<center>
    <table width="90%" size="90%">
        <tbody>
        <tr>
            <td align="middle" width="50%">
                <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                       borderColorLight="#000000" border="1">
                    <tbody>
                    <tr height="20">
                        <td align="middle" bgColor="#996633"><b><font color="#ffffff" size="2">&nbsp;Prima
                        </font><font color="#808080" size="2">(
                            7h 00m - 10h 00m ) </font></b></td>
                    </tr>
                    <tr>
                        <td bgColor="#e4cfa2">
                            <table width="100%" border="0">
                                <tbody>
                                <logic:iterate id="lista" name="ActividadesMonjeForm" property="listaActividades">
                                    <logic:equal name="lista" property="periodo" value="3">
                                        <tr>
                                            <td align="left">
                                                <p align="left"><b><bean:write name="lista" property="fecha"/></b></p>
                                            </td>
                                            <td><bean:write name="lista" property="tarea"/></td>
                                            <td><bean:write name="lista" property="mensaje"/></td>
                                        </tr>
                                    </logic:equal>
                                </logic:iterate>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td align="middle" width="50%">
                <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                       borderColorLight="#000000" border="1">
                    <tbody>
                    <tr height="20">
                        <td align="middle" bgColor="#996633"><b><font color="#ffffff" size="2">&nbsp;Tercia
                        </font><font color="#808080" size="2">(
                            10h 00m - 12h 00m ) </font></b></td>
                    </tr>
                    <tr>
                        <td bgColor="#e4cfa2">
                            <table width="100%" border="0">
                                <tbody>
                                <logic:iterate id="lista" name="ActividadesMonjeForm" property="listaActividades">
                                    <logic:equal name="lista" property="periodo" value="4">
                                        <tr>
                                            <td align="left">
                                                <p align="left"><b><bean:write name="lista" property="fecha"/></b></p>
                                            </td>
                                            <td><bean:write name="lista" property="tarea"/></td>
                                            <td><bean:write name="lista" property="mensaje"/></td>
                                        </tr>
                                    </logic:equal>
                                </logic:iterate>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td align="middle" width="50%">
                <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                       borderColorLight="#000000" border="1">
                    <tbody>
                    <tr height="20">
                        <td align="middle" bgColor="#996633"><b><font color="#ffffff" size="2">&nbsp;Nona
                        </font><font color="#808080" size="2">(
                            14h 00m - 16h 15m ) </font></b></td>
                    </tr>
                    <tr>
                        <td bgColor="#e4cfa2">
                            <table width="100%" border="0">
                                <tbody>
                                <logic:iterate id="lista" name="ActividadesMonjeForm" property="listaActividades">
                                    <logic:equal name="lista" property="periodo" value="6">
                                        <tr>
                                            <td align="left">
                                                <p align="left"><b><bean:write name="lista" property="fecha"/></b></p>
                                            </td>
                                            <td><bean:write name="lista" property="tarea"/></td>
                                            <td><bean:write name="lista" property="mensaje"/></td>
                                        </tr>
                                    </logic:equal>
                                </logic:iterate>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td align="middle" width="50%">
                <table borderColor="#000000" cellSpacing="0" borderColorDark="#000000" width="100%"
                       borderColorLight="#000000" border="1">
                    <tbody>
                    <tr height="20">
                        <td align="middle" bgColor="#996633"><b><font color="#ffffff" size="2">&nbsp;Visperas
                        </font></b><b><font color="#808080" size="2">(
                            16h 15m - 18h 45m ) </font></b></td>
                    </tr>
                    <tr>
                        <td bgColor="#e4cfa2">
                            <table width="100%" border="0">
                                <tbody>
                                <logic:iterate id="lista" name="ActividadesMonjeForm" property="listaActividades">
                                    <logic:equal name="lista" property="periodo" value="7">
                                        <tr>
                                            <td align="left">
                                                <p align="left"><b><bean:write name="lista" property="fecha"/></b></p>
                                            </td>
                                            <td><bean:write name="lista" property="tarea"/></td>
                                            <td><bean:write name="lista" property="mensaje"/></td>
                                        </tr>
                                    </logic:equal>
                                </logic:iterate>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>

</center>

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
<!--Fin Script para google-analytics-->

</body>
</html:html>

