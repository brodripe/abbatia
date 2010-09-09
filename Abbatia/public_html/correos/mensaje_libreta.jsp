<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>


<html>
<head>
    <title>Pantalla mercado historico</title>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css">
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">

<center>
    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height=20>
            <td align="center" bgcolor="#996633"><b><font color="#FFFFFF" size="2">Abadias
                favoritas</font></b></td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                       bordercolordark="#808080">
                    <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Añadido
                        en</font></b></td>
                    <td bgcolor="#E1C08B" align="center"><font color="#FFFFFF"><b>Abadia</b></font></td>
                    <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Mensaje
                        enviados</font></b></td>
                    <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Mensajes
                        recibidos</font></b></td>
                    <tr>
                        <td align="right">
                            <p align="center">10/03/1104</td>
                        <td>Can Jova</td>
</center>
<td align="right">
    <p align="right">200 u</td>

<center>
    <td align="right">20,10</td>
    </tr>
    <tr>
        <td align="right">
            <p align="center">02/02/1102</td>
        <td>Jordi</td>
        <td align="right">1 u</td>
        <td align="right">1000</td>
    </tr>
    </table>
</center>
</td>
</tr>
</table>
<p align="center"><input type="button" value="Añadir más abadias" name="B3"></p>

<p><b>Nota:&nbsp; </b>Cuadro de cartón piedra ;-)</p>
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
