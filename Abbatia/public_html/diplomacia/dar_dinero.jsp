<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html>
<head>
    <link REL="STYLESHEET" HREF="theme/styles.css" TYPE="text/css"/>
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
                        <img border="0" src="images/iconos/16/monedas.jpg"
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
