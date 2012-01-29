<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <title></title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <meta name="GENERATOR" content="Microsoft FrontPage 4.0">
    <meta name="ProgId" content="FrontPage.Editor.Document">
    <title>New Page 1</title>
</head>
<body bgcolor="#E1C08B">

<center>
    <table cellSpacing="0" width="90%" bgColor="#996633" border="0">
        <tbody>
        <tr>
            <td width="20"></td>
            <td>
                <p align="center"><b><font color="#ffffff" size="2">Usuarios bloqueados<br/> </font></b></p>
            </td>
        </tr>
        </tbody>
    </table>
    <table style="BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #000000 1px solid; BORDER-LEFT: #000000 1px solid; BORDER-BOTTOM: #000000 1px solid"
           borderColor="#000000" cellSpacing="0" width="90%" border="1">
        <tbody>
        <tr>
            <td bgColor="#e4cfa2">
                <table border="1" width="100%">
                    <tr>
                        <td align="center">
                            <b>id Usuario</b>
                        </td>
                        <td align="center">
                            <b>Nick</b>
                        </td>
                        <td align="center">
                            <b>Nombre Abadia</b>
                        </td>
                        <td align="center">
                            <b>Fecha de Bloqueo</b>
                        </td>
                        <td align="center">
                            <b>Días bloqueo</b>
                        </td>
                        <td align="center">
                            <b>Motivo Bloqueo</b>
                        </td>
                        <td align="center">
                            <b>Opciones</b>
                        </td>
                    </tr>
                    <logic:iterate id="lista" name="bloqueados">
                        <tr>
                            <td align="center">
                                <bean:write property="idDeUsuario" name="lista"/>
                            </td>
                            <td align="center">
                                <bean:write property="nick" name="lista"/>
                            </td>
                            <td align="center">
                                <bean:write property="nombre" name="lista"/>
                            </td>
                            <td align="center">
                                <bean:write property="fecha_bloqueo" name="lista"/>
                            </td>
                            <td align="center">
                                <bean:write property="dias_bloqueo" name="lista"/>
                            </td>
                            <td align="left">
                                <bean:write property="motivo" name="lista"/>
                            </td>

                            <td align="center">
                                <html:link action="/DesbloquearUsuario" paramName="lista" paramProperty="idDeUsuario"
                                           paramId="clave">
                                    Desbloquear
                                </html:link>
                            </td>

                        </tr>
                    </logic:iterate>
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

</html>
