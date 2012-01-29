<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla de confirmación de envío</title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <p>&nbsp;</p>
    <html:form action="/mensaje_nuevo">

        <html:hidden name="MensajeForm" property="accion" value="aceptar"/>
        <html:hidden name="MensajeForm" property="destinatarioid"/>
        <html:hidden name="MensajeForm" property="msg"/>
        <html:hidden name="MensajeForm" property="coste"/>

        <table border="1" cellspacing="0" bordercolor="#000000" width="60%" bordercolorlight="#000000"
               bordercolordark="#000000">
            <tr height="20">
                <td align="center" bgcolor="#996633">
                    <b><font color="#FFFFFF" size="2">Confirmacion de envio</font></b>
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
                                            <bean:message key="mensajes.confirmacion.destinatarios"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="60%" align="left">
                                <font size="2">
                                    <bean:write name="MensajeForm" property="destinatario"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%">
                                <p align="right">
                                    <b>
                                        <font size="3">
                                            <bean:message key="mensajes.confirmacion.coste"/>
                                        </font>
                                    </b>
                                </p>
                            </td>
                            <td width="60%" align="left">
                                <font size="2">
                                    <bean:write name="MensajeForm" property="coste"/> <bean:message
                                        key="mensajes.confirmacion.coste2"/>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <b>
                                    <font size="3">
                                        <bean:message key="mensajes.confirmacion.texto"/>
                                    </font>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <hr size="1" width="80%"/>
                                <br>
                                <font size="2">
                                    <bean:write name="MensajeForm" property="msg"/>
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
                    <html:errors/>
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
