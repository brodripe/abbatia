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
