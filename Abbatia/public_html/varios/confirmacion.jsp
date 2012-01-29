<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title>Pantalla de confirmación</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <p>&nbsp;</p>

    <table border="1" cellspacing="0" bordercolor="#000000" width="40%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height="20">
            <td align="center" bgcolor="#996633" colspan="2">
                <b>
                    <font color="#FFFFFF" size="2">
                        <bean:write name="DatosConfirmacion" property="titulo" scope="request"/>
                    </font>
                </b>
            </td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2" colspan="2" align="center">
                <font size=3>
                    <br/>
                    <b><bean:write name="DatosConfirmacion" property="textoConfirmacion" scope="request"/></b>
                    <br/>
                </font>
            </td>
        </tr>
        <tr>
            <td align="center">
                <a href='<bean:write name="DatosConfirmacion" property="accionSi" scope="request"/>'>
                    <bean:message key="mensajes.confirmacion.boton.aceptar"/>
                </a>
            </td>
            <td align="center">
                <a href='<bean:write name="DatosConfirmacion" property="accionNo" scope="request"/>'>
                    <bean:message key="mensajes.confirmacion.boton.cancelar"/>
                </a>
            </td>
        </tr>
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
</html>
