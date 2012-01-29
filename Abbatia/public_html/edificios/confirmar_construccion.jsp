<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="edificios.construccion.confirmar.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p align="right">
    <jsp:include page="/varios/mostrar_recursos.jsp"/>
</p>
<center>
    <logic:notEmpty name="Nombre">
        <p>
            <bean:message key="mercado.historico.abadia"/>:<br/>
            <b><bean:write name="Nombre"/></b></p>
    </logic:notEmpty>
    <html:form action="/crear_edificio">
        <logic:notEmpty name="Abadiaid_Obispado">
            <input type="hidden" name="abadiaid_obispado" value="<bean:write name="Abadiaid_Obispado" />">
        </logic:notEmpty>
        <html:hidden name="DatosEdificioForm" property="accion" value="aceptar"/>
        <html:hidden name="DatosEdificioForm" property="tipo_edificio"/>
        <html:hidden name="DatosEdificioForm" property="nombre_edificio"/>
        <html:hidden name="DatosEdificioForm" property="dias_costruccion"/>
        <html:hidden name="DatosEdificioForm" property="coste_oro"/>
        <html:hidden name="DatosEdificioForm" property="coste_madera"/>
        <html:hidden name="DatosEdificioForm" property="coste_piedra"/>
        <html:hidden name="DatosEdificioForm" property="coste_hierro"/>
        <html:hidden name="DatosEdificioForm" property="descripcion_edificio"/>
        <html:hidden name="DatosEdificioForm" property="coste_total"/>

        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="edificios.construccion.confirmar.cabecera"/>
                            <logic:notEmpty name="Nombre">
                                <br/><bean:message key="edificios.construccion.confirmar.cabecera2"/>&nbsp;<bean:write
                                    name="Nombre"/>
                            </logic:notEmpty>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td width="40%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="edificios.construccion.confirmar.tipo"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="60%" align="left">
                                <font size="2">
                                    <bean:write name="DatosEdificioForm" property="nombre_edificio"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="edificios.abadia.tiempocontruccion"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="60%" align="left">
                                <font size="2">
                                    <bean:write name="DatosEdificioForm" property="dias_costruccion"/> <bean:message
                                        key="edificios.abadia.dias"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="edificios.abadia.costeoro"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="60%" align="left">
                                <font size="2">
                                    <logic:empty name="Abadiaid_Obispado">
                                        <bean:write name="DatosEdificioForm" property="coste_oro"/>
                                    </logic:empty>
                                    <logic:notEmpty name="Abadiaid_Obispado">
                                        <bean:write name="DatosEdificioForm" property="coste_total"/>
                                    </logic:notEmpty>
                                </font>
                            </td>
                        </tr>
                        <logic:empty name="Abadiaid_Obispado">
                            <tr>
                                <td width="40%">
                                    <p align="right">
                                        <b>
                                            <font size="3">
                                                <bean:message key="edificios.abadia.costemadera"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                                <td width="60%" align="left">
                                    <font size="2">
                                        <bean:write name="DatosEdificioForm" property="coste_madera"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td width="40%">
                                    <p align="right">
                                        <b>
                                            <font size="3">
                                                <bean:message key="edificios.abadia.costepiedra"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                                <td width="60%" align="left">
                                    <font size="2">
                                        <bean:write name="DatosEdificioForm" property="coste_piedra"/>
                                    </font>
                                </td>
                            </tr>
                            <tr>
                                <td width="40%">
                                    <p align="right">
                                        <b>
                                            <font size="3">
                                                <bean:message key="edificios.abadia.costehierro"/>
                                            </font>
                                        </b>
                                    </p>
                                </td>
                                <td width="60%" align="left">
                                    <font size="2">
                                        <bean:write name="DatosEdificioForm" property="coste_hierro"/>
                                    </font>
                                </td>
                            </tr>
                        </logic:empty>
                        <tr>
                            <td colspan="2" align="center">
                                <b>
                                    <font size="3">
                                        <bean:message key="edificios.abadia.descripcion"/>
                                    </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <hr size="1" width="80%"/>
                                <font size="2">
                                    <bean:write name="DatosEdificioForm" property="descripcion_edificio"/>
                                </font>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <p align="center">
                        <input type="button" onclick="javascript:history.back();" value="Atrás"/>
                        <html:submit>
                            <bean:message key="mensajes.confirmacion.boton.aceptar"/>
                        </html:submit>
                    </p>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <logic:messagesPresent message="true">
                        <html:messages id="recursos" message="true">
                            <div class="success">
                                <STRONG>
                                    <FONT size="3">
                                        <bean:write name="recursos"/><br/>
                                    </FONT>
                                </STRONG>
                            </div>
                        </html:messages>
                    </logic:messagesPresent>
                </td>
            </tr>
        </table>
    </html:form>
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
</html>
