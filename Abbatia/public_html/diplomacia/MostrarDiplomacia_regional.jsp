<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<%
    int n = 0;
%>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<br/>

<div align="center">
<table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
       bordercolordark="#000000">
<tr>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=0">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="diplomacia.abadia.titulo"/>
            </font>
            </b>
        </html:link>
    </td>
    <td align="center" style="border-bottom: none thin white;border-right: none thin white;" bgcolor="#E4CFA2">
        <b><bean:message key="diplomacia.jerarquia.titulo"/></b>
    </td>
    <td align="center" bgcolor="#780A00">
        <html:link action="/mostrarDiplomacia?tab=2">
            <b><font color="#FFFFFF" size="2">
                <bean:message key="diplomacia.jerarquia.abbatia.titulo"/>
            </font>
            </b>
        </html:link>
    </td>
</tr>
<tr>
<td bgcolor="#E4CFA2" colspan=3 style="border-top: none thin white;">
<!-- Papa -->
<table border="0" width="100%" cellspacing="4">
    <tr>
    </tr>
    <tr>
        <td colspan=2 bgcolor="#F0AF1C">
            <b><bean:message key="diplomacia.jerarquia.papa"/>:</b>
        </td>
    </tr>
    <td align="center" valign="top">
        <logic:present name="Papa">
            <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000"
                   bordercolorlight="#000000" bordercolordark="#000000">
                <tr>
                    <td align="center" bgcolor="#F0AF1C" colspan="2">
                        <font color="#000000" size="1">
                            <bean:write name="Papa" property="nombre"/>&nbsp;<bean:message
                                key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Papa" property="primerApellido"/>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <img src="/images/iconos/monje_papa.png">
                    </td>
                    <td bgcolor="#E4CFA2" align="center">
                        <table width="100%" border="0">
                            <tr>
                                <td align="right"><font color="#000000" size="1"><bean:message
                                        key="monjes.abadia.edad"/></font></td>
                                <td><font color="#000000" size="1"><bean:write name="Papa" property="edad"/></font></td>
                            </tr>
                            <tr>
                                <td align="right"><font color="#000000" size="1"><bean:message
                                        key="monjes.abadia.salud"/></font></td>
                                <td><bean:write name="Papa" property="barra_salud" filter="false"/></td>
                            </tr>
                            <tr>
                                <td align="right"><font color="#000000" size="1"><bean:message
                                        key="monjes.abadia.fe"/></font></td>
                                <td><bean:write name="Papa" property="barra_fe" filter="false"/></td>
                            </tr>
                            <tr>
                                <td align="right"><font color="#000000" size="1"><bean:message
                                        key="monjes.abadia.popularidad"/></font></td>
                                <td><bean:write name="Papa" property="barra_popularidad" filter="false"/></td>
                            </tr>
                        </table>
                    </td>
                    <logic:equal value="0" name="havotadoPapa">
                <tr>
                    <td bgcolor="#E4CFA2" colspan="2">
                        <table width="80%" border="0" align="center">
                            <tr>
                                <td colspan=2>
                                    <font color="#000000" size="1"><bean:message key="diplomacia.satisfaccion"/></font>
                                </td>
                            </tr>
                            <tr>
                                <td align="left">
                                    <html:link action="/popularidadMonje?voto=1" paramId="clave" paramName="Papa"
                                               paramProperty="idDeMonje">
                                        <html:img border="0" src="images/iconos/16/marca.gif"
                                                  altKey="diplomacia.satisfaccion.si"/>
                                    </html:link>
                                </td>
                                <td align="right">
                                    <html:link action="/popularidadMonje?voto=0" paramId="clave" paramName="Papa"
                                               paramProperty="idDeMonje">
                                        <html:img border="0" src="images/iconos/16/marca_roja.gif"
                                                  altKey="diplomacia.satisfaccion.no"/>
                                    </html:link>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                </logic:equal>
                <tr>
                    <td bgcolor="#E4CFA2" align="center" colspan="2">
                        <font color="#000000" size="1">
                            <bean:write name="Papa" property="abadia"/>
                            <br>
                            <bean:write name="Papa" property="region"/>
                        </font>
                    </td>
                </tr>
            </table>
        </logic:present>
        <logic:notPresent name="Papa">
            <b><bean:message key="diplomacia.jerarquia.sinpapa"/></b>
        </logic:notPresent>
    </td>
    <td>
        <!-- Opciones -->
        <logic:present name="Papa">
            <table width="100%" border=0>
                <tr>
                    <td width="" align="center">
                    </td>
                </tr>
            </table>
        </logic:present>
    </td>
    </tr>
</table>
<br/>

<!-- Cardenales -->
<table border="0" width="100%" cellspacing="4">
    <tr>
        <td bgcolor="#F0AF1C" colspan=5>
            <b><bean:message key="diplomacia.jerarquia.cardenales"/>:</b>
        </td>
    </tr>
    <tr>
        <%
            n = 0;
        %>
        <logic:iterate id="Monje" name="Cardenales">
            <td>

                <table border="1" width="130" align="center" cellspacing="0" bordercolor="#000000"
                       bordercolorlight="#000000" bordercolordark="#000000">
                    <tr>
                        <td align="center" bgcolor="#F0AF1C">
                            <font color="#000000" size="1">
                                <bean:write name="Monje" property="nombre"/>&nbsp;<bean:message
                                    key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="Monje"
                                                                                     property="primerApellido"/>
                            </font>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#E4CFA2" align="center">
                            <font color="#000000" size="1">
                                <bean:message key="monjes.abadia.edad"/>
                                <bean:write name="Monje" property="edad"/>
                                <br>
                                <bean:message key="monjes.abadia.salud"/>
                                <bean:write name="Monje" property="barra_salud" filter="false"/>
                                <bean:message key="monjes.abadia.fe"/>
                                <bean:write name="Monje" property="barra_fe" filter="false"/>
                                <br>
                            </font>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#E4CFA2" align="center">
                            <font color="#000000" size="1">
                                <bean:write name="Monje" property="abadia"/>
                                <br>
                                <bean:write name="Monje" property="region"/>
                            </font>
                        </td>
                    </tr>
                </table>

            </td>
            <%
                if (n > 3) {
                    out.println("<tr></tr>");
                    n = 0;
                } else n++;
            %>
        </logic:iterate>

    </tr>
</table>
<br/>


<!-- Obispos de tú region -->
<table border="0" width="100%" cellspacing="4">
    <tr>
    </tr>
    <tr>
        <td bgcolor="#F0AF1C" colspan=2>
            <b><bean:message key="diplomacia.jerarquia.obisposde"/>:</b>
        </td>
    </tr>
    <tr>
        <td align="center" width="250" valign="top">
            <logic:present name="ObispoRegion">
                <table border="1" width="180" align="center" cellspacing="0" bordercolor="#000000"
                       bordercolorlight="#000000" bordercolordark="#000000">
                    <tr>
                        <td align="center" bgcolor="#F0AF1C" colspan="2">
                            <font color="#000000" size="1">
                                <bean:write name="ObispoRegion" property="nombre"/>&nbsp;<bean:message
                                    key="monjes.abadia.nomciudad"/>&nbsp;<bean:write name="ObispoRegion"
                                                                                     property="primerApellido"/>
                            </font>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <img src="/images/iconos/monje_obispo.gif">
                        </td>
                        <td bgcolor="#E4CFA2" align="center">
                            <table width="100%" border="0">
                                <tr>
                                    <td align="right"><font color="#000000" size="1"><bean:message
                                            key="monjes.abadia.edad"/></font></td>
                                    <td><font color="#000000" size="1"><bean:write name="ObispoRegion"
                                                                                   property="edad"/></font></td>
                                </tr>
                                <tr>
                                    <td align="right"><font color="#000000" size="1"><bean:message
                                            key="monjes.abadia.salud"/></font></td>
                                    <td><bean:write name="ObispoRegion" property="barra_salud" filter="false"/></td>
                                </tr>
                                <tr>
                                    <td align="right"><font color="#000000" size="1"><bean:message
                                            key="monjes.abadia.fe"/></font></td>
                                    <td><bean:write name="ObispoRegion" property="barra_fe" filter="false"/></td>
                                </tr>
                                <tr>
                                    <td align="right"><font color="#000000" size="1"><bean:message
                                            key="monjes.abadia.popularidad"/></font></td>
                                    <td><bean:write name="ObispoRegion" property="barra_popularidad"
                                                    filter="false"/></td>
                                </tr>
                            </table>
                        </td>
                        <logic:equal value="0" name="havotadoObispo">
                    <tr>
                        <td bgcolor="#E4CFA2" colspan="2">
                            <table width="80%" border="0" align="center">
                                <tr>
                                    <td align="left">
                                        <html:link action="/popularidadMonje?voto=1" paramId="clave"
                                                   paramName="ObispoRegion" paramProperty="idDeMonje">
                                            <html:img border="0" src="images/iconos/16/marca.gif"
                                                      altKey="diplomacia.satisfaccion.si"/>
                                        </html:link>
                                    </td>
                                    <td align="right">
                                        <html:link action="/popularidadMonje?voto=0" paramId="clave"
                                                   paramName="ObispoRegion" paramProperty="idDeMonje">
                                            <html:img border="0" src="images/iconos/16/marca_roja.gif"
                                                      altKey="diplomacia.satisfaccion.no"/>
                                        </html:link>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    </logic:equal>
                    <tr>
                        <td bgcolor="#E4CFA2" align="center" colspan="2">
                            <font color="#000000" size="1">
                                <bean:write name="ObispoRegion" property="abadia"/>
                                <br>
                                <bean:write name="ObispoRegion" property="region"/>
                            </font>
                        </td>
                    </tr>
                </table>
            </logic:present>
        </td>
        <td align="center">
            <!-- Opciones -->
            <logic:present name="ObispoRegion">
                <table width="80%" border=1 cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
                       bordercolordark="#000000">
                    <tr>
                        <td align="center" colspan=6>
                            <b><bean:message key="diplomacia.jerarquia.comision.titulo"/></b>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.obispado"/></b>
                        </td>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.cancelacion"/></b>
                        </td>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.venta5"/></b>
                        </td>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.venta10"/></b>
                        </td>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.venta15"/></b>
                        </td>
                        <td align="center">
                            <b><bean:message key="diplomacia.jerarquia.comision.transito"/></b>
                        </td>

                    </tr>
                    <tr>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="obispado"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.obispado.help"/>
                        </td>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="cancelacion"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.cancelacion.help"/>
                        </td>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="venta5"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.venta5.help"/>
                        </td>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="venta10"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.venta10.help"/>
                        </td>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="venta15"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.venta15.help"/>
                        </td>
                        <td align="right">
                            <bean:write name="ComisionRegion" property="transito"/>&nbsp;%
                            <html:img src="images/iconos/16/ayuda.gif"
                                      altKey="diplomacia.jerarquia.comision.transito.help"/>
                        </td>

                    </tr>
                    <tr>
                        <td align="center" colspan="6">
                            <html:link action="/mostrarComisiones">
                                <bean:message key="obispado.mostrar.comisiones"/>
                                &nbsp;<bean:write name="ObispoRegion" property="abadia"/>
                            </html:link>
                        </td>
                    </tr>
                </table>
            </logic:present>
        </td>
    </tr>
</table>
<br/>

</td>
</tr>
</table>
</div>
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
</html>
