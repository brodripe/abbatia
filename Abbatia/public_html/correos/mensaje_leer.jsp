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

<p> &nbsp; </p>
<center>
    <table border="1" cellspacing="0" bordercolor="#000000" width="90%" bordercolorlight="#000000"
           bordercolordark="#000000">
        <tr height=20>
            <td align="center" bgcolor="#996633"><b><font color="#FFFFFF" size="2">Mensajes
                recibidos / enviados</font></b></td>
        </tr>
        <tr>
            <td bgcolor="#E4CFA2">
                <table border="1" width="100%" cellspacing="0" bordercolorlight="#808080" bordercolor="#808080"
                       bordercolordark="#808080">
                    <tr>
                        <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Tipo</font></b></td>
                        <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Abadia</font></b></td>
                        <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Fecha<br>
                            Abbatia</font></b></td>
                        <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Fecha<br>
                            Real</font></b></td>
                        <td bgcolor="#E1C08B" align="center"><font color="#FFFFFF"><b>Descripción</b></font></td>
                        <td bgcolor="#E1C08B" align="center"><b><font color="#FFFFFF">Fecha<br>
                            Leido</font></b></td>
                    </tr>
                    <tr>
                        <td><b><font color="#000080">Enviado</font></b></td>
                        <td><b>Can Jova ( johnny )</b></td>
                        <td align="right">
                            <p align="center">10/03/1104 10:20:20</td>
                        <td align="right">
                            <p align="center">02/04/2004 10:55:12</td>
                        <td><a href="mensaje_leer_1.jsp">Tienes el Santo grial para vender?</a></td>
</center>
<td align="center">
    <p align="center">02/04/2004 10:55:12</td>

<center>
    </tr>
    <tr>
        <td><b><font color="#008000">Recibido</font></b></td>
        <td><b>benjamin</b></td>
        <td align="right">
            <p align="center">02/02/1102 20:10:45</td>
        <td align="right">
            <p align="center">03/03/2004 14:25:40</td>
        <td><a href="mensaje_leer_1.jsp">Hola!</a></td>
        <td align="center">02/04/2004 10:55:12</td>
    </tr>
    </table>
</center>
</td>
</tr>
</table>
<p>&nbsp;</p>

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
