<%@ page import="org.abbatia.utils.Constantes" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>untitled</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body bgcolor="#E1C08B">
<center>
    <html:form action="/mostrarMonjesCopia">
        <html:hidden property="accion" value="seleccionado"/>
        <html:hidden property="idLibro"/>
        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b><font color="#FFFFFF" size="2">
                        <bean:message key="edificios.abadia.biblioteca.seleccionmonje"/> <bean:write
                            name="datosCopiaLibro" property="nombreLibro"/>
                    </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2" style="border-top: none thin white;">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td bgcolor="#E1C08B" align="center" colspan="6">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificios.abadia.biblioteca.listamonjes"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#E1C08B" align="center"></td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.nombre"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.fe"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.destreza"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.talento"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificio.abadia.biblioteca.seleccionmonje.idioma"/>
                                </font>
                                </b>
                            </td>
                        </tr>

                        <logic:notEmpty name="datosCopiaLibro" property="monjes">
                            <logic:iterate id="lista" name="datosCopiaLibro" property="monjes">
                                <tr>
                                    <td align="center">
                                        <html:radio name="datosCopiaLibro" property="seleccion" idName="lista"
                                                    value="idDeMonje"/>
                                    </td>
                                    <td align="left">
                                        <bean:write name="lista" property="nombre"/> <bean:message
                                            key="monjes.abadia.de"/> <bean:write name="lista"
                                                                                 property="primerApellido"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_fe" filter="false"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_destreza" filter="false"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_talento" filter="false"/>
                                    </td>
                                    <td align="center">
                                        <bean:write name="lista" property="barra_idioma" filter="false"/>
                                    </td>
                                </tr>
                            </logic:iterate>
                        </logic:notEmpty>
                        <logic:empty name="datosCopiaLibro" property="monjes">
                            <tr>
                                <td align="center" colspan="6">
                                    <bean:message key="mensajes.aviso.notienesmonjescualificados"/>
                                </td>
                            </tr>
                        </logic:empty>
                    </table>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                           bordercolordark="#808080">
                        <tr>
                            <td bgcolor="#E1C08B" align="center" colspan="4">
                                <b><font color="#FFFFFF">
                                    <bean:message key="edificios.abadia.biblioteca.listaperiodos"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="monjes.trabajos.prima"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="monjes.trabajos.tercia"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="monjes.trabajos.nona"/>
                                </font>
                                </b>
                            </td>
                            <td bgcolor="#E1C08B" align="center">
                                <b><font color="#FFFFFF">
                                    <bean:message key="monjes.trabajos.visperas"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <logic:equal value="0" name="datosCopiaLibro" property="estadoPrima">
                                    <html:multibox name="datosCopiaLibro" property="periodo">
                                        <%=Constantes.PERIODO_PRIMA %>
                                    </html:multibox>
                                </logic:equal>
                                <logic:equal value="1" name="datosCopiaLibro" property="estadoPrima">
                                    <bean:message key="edificio.abadia.biblioteca.periodoocupado"/>
                                </logic:equal>
                            </td>
                            <td align="center">
                                <logic:equal value="0" name="datosCopiaLibro" property="estadoTercia">
                                    <html:multibox name="datosCopiaLibro" property="periodo">
                                        <%=Constantes.PERIODO_TERCIA %>
                                    </html:multibox>
                                </logic:equal>
                                <logic:equal value="1" name="datosCopiaLibro" property="estadoTercia">
                                    <bean:message key="edificio.abadia.biblioteca.periodoocupado"/>
                                </logic:equal>
                            </td>
                            <td align="center">
                                <logic:equal value="0" name="datosCopiaLibro" property="estadoNona">
                                    <html:multibox name="datosCopiaLibro" property="periodo">
                                        <%=Constantes.PERIODO_NONA %>
                                    </html:multibox>
                                </logic:equal>
                                <logic:equal value="1" name="datosCopiaLibro" property="estadoNona">
                                    <bean:message key="edificio.abadia.biblioteca.periodoocupado"/>
                                </logic:equal>
                            </td>
                            <td align="center">
                                <logic:equal value="0" name="datosCopiaLibro" property="estadoVisperas">
                                    <html:multibox name="datosCopiaLibro" property="periodo">
                                        <%=Constantes.PERIODO_VISPERAS %>
                                    </html:multibox>
                                </logic:equal>
                                <logic:equal value="1" name="datosCopiaLibro" property="estadoVisperas">
                                    <bean:message key="edificio.abadia.biblioteca.periodoocupado"/>
                                </logic:equal>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center" bgcolor="#E1C08B">
                    <input type="button" onclick="javascript:history.back();" value="Atrás"/>
                    <logic:notEmpty name="datosCopiaLibro" property="monjes">
                        <html:submit>
                            <bean:message key="edificios.abadia.biblioteca.siguiente"/>
                        </html:submit>
                    </logic:notEmpty>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <html:errors/>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <logic:messagesPresent message="true">
                        <html:messages id="msg" message="true">
                            <bean:write name="msg"/><br/>
                        </html:messages>
                    </logic:messagesPresent>
                </td>
            </tr>
        </table>
    </html:form>
</center>
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
</html>
