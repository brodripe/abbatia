<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>
        <bean:message key="mensajes.nuevo.titulo"/>
    </title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<br/>
<center>
    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" bgcolor="#996633"><b><font color="#FFFFFF" size="2">
                <bean:message key="mensajes.nuevo.cabecera"/>
            </font>
            </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">&nbsp;
                <html:form action="/mensaje_nuevo">
                    <html:hidden name="MensajeForm" property="idAbadiaOrigen"/>
                    <html:hidden name="MensajeForm" property="destinatario"/>
                    <html:hidden name="MensajeForm" property="destinatarioid"/>
                    <html:hidden name="MensajeForm" property="desde"/>
                    <p>
                        <logic:notEmpty name="MensajeForm" property="desde">
                            <b><bean:message key="mensajes.nuevo.origen"/></b>
                            <br/>
                            <html:text property="desde" name="MensajeForm" size="20" disabled="true"></html:text>
                            <logic:equal name="MensajeForm" property="direccion" value="entrada">
                                <html:hidden name="MensajeForm" property="accion" value="contestar"/>
                                <html:submit>
                                    <bean:message key="mensajes.nuevo.contestar"/>
                                </html:submit>
                            </logic:equal>
                            <br/>
                        </logic:notEmpty>
                        <b>
                            <bean:message key="mensajes.nuevo.destinatario"/>
                        </b>
                        <br/>
                        <html:text property="destinatario" name="MensajeForm" size="50" disabled="true"></html:text>
                        <logic:notEqual property="tipo" name="MensajeForm" value="consulta">
                            <html:link action="/buscar_abadias">
                                <bean:message key="mensajes.nuevo.buscarabadia"/>
                            </html:link>
                        </logic:notEqual>
                        <br/>
                        <br/><b>
                        <bean:message key="mensajes.nuevo.mensaje"/>
                    </b>
                        <br/>
                        <html:textarea rows="10" cols="70" name="MensajeForm" property="msg"></html:textarea>
                    </p>

                    <p align="center">
                        <logic:notEqual property="tipo" name="MensajeForm" value="consulta">
                            <html:hidden name="MensajeForm" property="accion" value="validar"/>
                            <html:submit>
                                <bean:message key="mensajes.nuevo.submit"/>
                            </html:submit>
                        </logic:notEqual>
                        <logic:equal property="tipo" name="MensajeForm" value="consulta">
                            <input type="button" onClick="javascript:history.back();" value="Atrás"/>&nbsp;
                        </logic:equal>
                    </p>
                </html:form>
            </td>
        </tr>
    </table>
    <p>&nbsp;</p>
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
<jsp:include page="/abadias/congelado.jsp"/>
</html>
