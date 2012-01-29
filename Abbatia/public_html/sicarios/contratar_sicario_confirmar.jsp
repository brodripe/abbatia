<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<p align="right">
    <logic:notEmpty name="DatosContents">
<table border="1" width="130" cellspacing="0" bordercolor="#000000" bordercolorlight="#000000"
       bordercolordark="#000000">
    <tr>
        <td align="center" bgcolor="#F0AF1C">
            <font color="#000000" size="1"><bean:message key="principal.recursos"/></font>
        </td>
    </tr>
    <tr>
        <td bgcolor="#F7FEEA" align="center">
            <table border="0" width="100%">
                <tr>
                    <td width="50%" align="center">
                        <img border="0" src="/images/iconos/16/monedas.jpg"
                             alt="<bean:message key="principal.hint.moneda"/>"/>
                    </td>
                    <td width="50%" align="right">
                        <bean:write name="DatosContents" property="recursoMonedas" filter="false"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</logic:notEmpty>
</p>
<center>
    <form action="/contratarSicario.do">
        <input type="hidden" name="confirmar" value="2"/>
        <input type="hidden" name="monjeid" value="<bean:write name="monjeid" />"/>
        <input type="hidden" name="sicarioid" value="<bean:write name="sicarioid" />"/>

        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b>
                        <font color="#FFFFFF" size="2">
                            <bean:message key="sicario.confirmar.titulo"/>
                        </font>
                    </b>
                </td>
            </tr>
            <tr>
                <td bgcolor="#E4CFA2">
                    <table border="0" width="100%">
                        <tr>
                            <td align="right">
                                <b><bean:message key="sicario.confirmar.nombre.sicario"/>:</b>
                            </td>
                            <td><bean:write name="Nom_Sicario"/></td>
                        </tr>
                        <tr>
                            <td algin="center" align="center" colspan="2">
                                <hr size=1 width="80%">
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <b><bean:message key="sicario.confirmar.nombre"/>:</b>
                            </td>
                            <td><bean:write name="Nom_Monje"/></td>
                        </tr>
                        <tr>
                            <td align="right">
                                <b><bean:message key="sicario.confirmar.abadia"/>:</b>
                            </td>
                            <td><bean:write name="Nom_Abadia"/> (<bean:write name="Nom_Region"/>)</td>
                        </tr>
                        <tr>
                            <td align="right">
                                <b><bean:message key="sicario.confirmar.viaje"/>:</b>
                            </td>
                            <td><bean:write name="Dias"/>&nbsp;<bean:message key="sicario.confirmar.viaje.dias"/></td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <hr size=1 width="80%">
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <b><bean:message key="sicario.confirmar.precio"/>:</b>
                            </td>
                            <td>
                                <b> <font color="#990000">
                                    <bean:write name="Precio"/>&nbsp;<bean:message
                                        key="sicario.confirmar.precio.monedas"/>
                                </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td algin="center" algin="center" colspan="2">
                                <hr size=1 width="80%">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <p align="center">
                        <input type="button" onclick="javascript:history.back();"
                               value="<bean:message key="general.atras"/>"/>
                        <input type="submit" value="<bean:message key="mensajes.confirmacion.boton.aceptar"/>"/>
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
    </form>
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
