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
                <p align="center"><b><font color="#ffffff" size="2">Orden
                    de ejecución de los procesos<br/>Hora actual: <bean:write name="hora"/> - Hora abbatia: <bean:write
                            name="horaabbatia"/> </font></b></p>
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
                        <td>
                            <b>id</b>
                        </td>
                        <td>
                            <b>Activo</b>
                        </td>
                        <td>
                            <b>Descripción</b>
                        </td>
                        <td>
                            <b>Fecha ejecutar</b>
                        </td>
                        <td>
                            <b>Última - Empezo</b>
                        </td>
                        <td>
                            <b>Última - Acabo</b>
                        </td>
                        <td>
                            <b>Opciones</b>
                        </td>
                    </tr>
                    <logic:iterate id="lista" name="runs">
                        <font size="2">
                            <bean:write property="description" name="lista" filter="false"/>
                        </font>
                    </logic:iterate>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
    &nbsp;
    <table cellSpacing="0" width="90%" bgColor="#996633" border="0">
        <tbody>
        <tr>
            <td width="20"></td>
            <td>
                <p align="center"><b><font color="#ffffff" size="2">Últimos
                    mensajes de los procesos</font></b></p>
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
                        <td>
                            <b>Fecha</b>
                        </td>
                        <td>
                            <b>Descripción</b>
                        </td>
                    </tr>
                    <logic:iterate id="lista" name="logs">
                        <font size="2">
                            <bean:write property="description" name="lista" filter="false"/>
                        </font>
                    </logic:iterate>
                </table>
            </td>
        </tr>
        </tbody>
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
